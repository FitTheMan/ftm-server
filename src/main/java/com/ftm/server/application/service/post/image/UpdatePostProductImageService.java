package com.ftm.server.application.service.post.image;

import static com.ftm.server.common.consts.PropertiesHolder.PRODUCT_DEFAULT_IMAGE;

import com.ftm.server.application.port.out.persistence.post.*;
import com.ftm.server.application.port.out.s3.S3ImageDeletePort;
import com.ftm.server.application.port.out.s3.S3PostProductImageUploadPort;
import com.ftm.server.application.port.out.transcation.AfterCommitExecutorPort;
import com.ftm.server.application.port.out.transcation.AfterRollbackExecutorPort;
import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.vo.post.PostProductSaveWithImageVo;
import com.ftm.server.application.vo.post.PostProductUpdateWithImageVo;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.PostProductImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UpdatePostProductImageService {

    private final LoadPostProductImagePort loadPostProductImagePort;
    private final UpdatePostProductImagePort updatePostProductImagePort;
    private final SavePostProductImagePort savePostProductImagePort;
    private final DeletePostProductImagePort deletePostProductImagePort;

    private final S3PostProductImageUploadPort s3PostProductImageUploadPort;
    private final S3ImageDeletePort s3ImageDeletePort;

    private final AfterRollbackExecutorPort afterRollbackExecutorPort;
    private final AfterCommitExecutorPort afterCommitExecutorPort;

    private final List<String> deleteProductImageObjectKeys = new ArrayList<>();

    public void execute(
            List<MultipartFile> productImageFiles,
            List<Long> deleteProductIds,
            List<PostProductUpdateWithImageVo> productImageUpdateContexts,
            List<PostProductSaveWithImageVo> productImageSaveContexts) {

        // 상품 이미지 업로드
        List<String> uploadedProductImageObjectKeys =
                s3PostProductImageUploadPort.uploadImages(productImageFiles);
        registerRollbackHook(uploadedProductImageObjectKeys); // 롤백 시, 업로드된 S3 이미지 삭제 예약

        // 업데이트할 상품 이미지가 있을 경우
        if (!productImageUpdateContexts.isEmpty()) {
            updatePostProductImages(productImageUpdateContexts, uploadedProductImageObjectKeys);
        }

        // 저장할 상품 이미지가 있을 경우
        if (!productImageSaveContexts.isEmpty()) {
            savePostProductImages(productImageSaveContexts, uploadedProductImageObjectKeys);
        }

        // 삭제할 상품 이미지가 있을 경우
        if (!deleteProductIds.isEmpty()) {
            deletePostProductImages(deleteProductIds);
        }

        // 커밋 이후, S3 이미지 삭제 예약
        registerCommitHook();
    }

    // 상품 이미지 업데이트
    private void updatePostProductImages(
            List<PostProductUpdateWithImageVo> productImageUpdateContexts,
            List<String> uploadedProductImageObjectKeys) {
        List<Long> updatedProductIds =
                productImageUpdateContexts.stream()
                        .map(PostProductUpdateWithImageVo::getPostProductId)
                        .toList();

        // 상품 ID : 해당 상품 이미지 map
        Map<Long, PostProductImage> updateProductImageMap =
                loadPostProductImagePort
                        .loadPostProductImagesByPostProductIds(
                                FindByIdsQuery.from(updatedProductIds))
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        PostProductImage::getPostProductId, Function.identity()));

        List<PostProductImage> imagesToUpdate = new ArrayList<>();
        for (PostProductUpdateWithImageVo context : productImageUpdateContexts) {
            PostProductImage postProductImage =
                    updateProductImageMap.get(context.getPostProductId());

            // 상품 이미지 검증
            if (context.getDeletePostProductImageId() != null) {
                postProductImage.validateId(context.getDeletePostProductImageId());
            }

            String currentKey = postProductImage.getObjectKey();
            String objectKey = PRODUCT_DEFAULT_IMAGE;

            // 1. 기본 이미지인데 삭제 요청
            if (PRODUCT_DEFAULT_IMAGE.equals(currentKey)
                    && context.getDeletePostProductImageId() != null) {
                throw new CustomException(ErrorResponseCode.CANNOT_DELETE_DEFAULT_IMAGE);
            }

            // 2. 기존 이미지 존재 + 삭제 요청 없이 새 이미지 업로드
            if (!PRODUCT_DEFAULT_IMAGE.equals(currentKey)
                    && context.getDeletePostProductImageId() == null
                    && context.getImageIndex() > 0) {
                throw new CustomException(ErrorResponseCode.POST_PRODUCT_IMAGE_ALREADY_EXISTS);
            }

            // 3. 삭제할 이미지가 존재할 경우
            if (context.getDeletePostProductImageId() != null) {
                deleteProductImageObjectKeys.add(currentKey);

                // 4. 새로 추가할 이미지가 존재할 경우
                if (context.getImageIndex() > 0) {
                    objectKey = uploadedProductImageObjectKeys.get(context.getImageIndex() - 1);
                }
            }

            // 5. 기본 이미지 -> 새 이미지 업로드
            if (context.getDeletePostProductImageId() == null && context.getImageIndex() > 0) {
                objectKey = uploadedProductImageObjectKeys.get(context.getImageIndex() - 1);
            }

            postProductImage.updateObjectKey(objectKey);
            imagesToUpdate.add(postProductImage);
        }

        updatePostProductImagePort.updatePostProductImages(imagesToUpdate);
    }

    // 상품 이미지 저장
    private void savePostProductImages(
            List<PostProductSaveWithImageVo> productImageSaveContexts,
            List<String> uploadedProductImageObjectKeys) {
        List<PostProductImage> imagesToSave = new ArrayList<>();
        for (PostProductSaveWithImageVo context : productImageSaveContexts) {
            int imageIndex = context.getImageIndex();

            // 이미지가 없는 경우, 기본 이미지
            if (imageIndex == -1) {
                imagesToSave.add(PostProductImage.createDefault(context.getPostProductId()));
                continue;
            }

            imagesToSave.add(
                    PostProductImage.create(
                            context.getPostProductId(),
                            uploadedProductImageObjectKeys.get(imageIndex - 1)));
        }

        savePostProductImagePort.savePostProductImages(imagesToSave);
    }

    // 상품 이미지 삭제
    private void deletePostProductImages(List<Long> deleteProductIds) {
        List<PostProductImage> deleteProductImages =
                loadPostProductImagePort.loadPostProductImagesByPostProductIds(
                        FindByIdsQuery.from(deleteProductIds));

        for (PostProductImage postProductImage : deleteProductImages) {
            String objectKey = postProductImage.getObjectKey();
            if (PRODUCT_DEFAULT_IMAGE.equals(objectKey)) continue;

            deleteProductImageObjectKeys.add(objectKey);
        }

        // 상품 이미지 삭제
        deletePostProductImagePort.deletePostProductImages(deleteProductImages);
    }

    private void registerRollbackHook(List<String> uploadedProductImageObjectKeys) {
        afterRollbackExecutorPort.doAfterRollback(
                () -> s3ImageDeletePort.deleteImages(uploadedProductImageObjectKeys));
    }

    private void registerCommitHook() {
        afterCommitExecutorPort.doAfterCommit(
                () -> {
                    s3ImageDeletePort.deleteImages(this.deleteProductImageObjectKeys);
                });
    }
}
