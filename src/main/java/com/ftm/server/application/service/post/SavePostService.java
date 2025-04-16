package com.ftm.server.application.service.post;

import com.ftm.server.application.command.post.SavePostCommand;
import com.ftm.server.application.command.post.SavePostProductCommand;
import com.ftm.server.application.port.in.post.SavePostUseCase;
import com.ftm.server.application.port.out.persistence.post.SavePostImagePort;
import com.ftm.server.application.port.out.persistence.post.SavePostPort;
import com.ftm.server.application.port.out.persistence.post.SavePostProductImagePort;
import com.ftm.server.application.port.out.persistence.post.SavePostProductPort;
import com.ftm.server.application.port.out.s3.S3ImageDeletePort;
import com.ftm.server.application.port.out.s3.S3PostImageUploadPort;
import com.ftm.server.application.port.out.s3.S3PostProductImageUploadPort;
import com.ftm.server.application.port.out.transcation.AfterRollbackExecutorPort;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.PostImage;
import com.ftm.server.domain.entity.PostProduct;
import com.ftm.server.domain.entity.PostProductImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavePostService implements SavePostUseCase {

    private final SavePostPort savePostPort;
    private final S3PostImageUploadPort s3PostImageUploadPort;
    private final SavePostImagePort savePostImagePort;
    private final SavePostProductPort savePostProductPort;
    private final S3PostProductImageUploadPort s3PostProductImageUploadPort;
    private final SavePostProductImagePort savePostProductImagePort;
    private final S3ImageDeletePort s3ImageDeletePort;

    private final AfterRollbackExecutorPort afterRollbackExecutorPort;

    @Override
    @Transactional
    public void execute(SavePostCommand command) {

        // 상품, 상품이미지 검증
        validateProductImages(command.getProducts(), command.getProductImages());

        // 이미지 업로드 로직 먼저 수행
        List<String> uploadedPostImageKeys =
                s3PostImageUploadPort.uploadImages(command.getPostImages());
        List<String> uploadedPostProductImageKeys =
                s3PostProductImageUploadPort.uploadImages(command.getProductImages());

        // 롤백 시 업로드된 이미지 삭제 저장
        afterRollbackExecutorPort.doAfterRollback(
                () -> {
                    s3ImageDeletePort.deleteImages(uploadedPostImageKeys);
                    s3ImageDeletePort.deleteImages(uploadedPostProductImageKeys);
                });

        // 게시글 저장
        Post post = savePostPort.savePost(Post.create(command));

        // 게시글 이미지 목록 저장
        List<PostImage> postImages =
                uploadedPostImageKeys.isEmpty()
                        ? List.of(PostImage.createDefault(post.getId()))
                        : uploadedPostImageKeys.stream()
                                .map(key -> PostImage.create(post.getId(), key))
                                .toList();
        savePostImagePort.savePostImages(postImages);

        // 게시글 상품 목록 저장
        List<SavePostProductCommand> savePostProductCommands = command.getProducts();
        List<PostProduct> postProducts =
                savePostProductCommands.stream()
                        .map(product -> PostProduct.create(product.withPostId(post.getId())))
                        .toList();
        List<PostProduct> savedPostProducts = savePostProductPort.savePostProducts(postProducts);

        // 게시글 상품 이미지 목록 저장
        List<PostProductImage> postProductImages = new ArrayList<>();
        for (int i = 0; i < savePostProductCommands.size(); i++) {
            PostProduct postProduct = savedPostProducts.get(i);
            int imageIndex = savePostProductCommands.get(i).getImageIndex();
            if (imageIndex > 0) {
                postProductImages.add(
                        PostProductImage.create(
                                postProduct.getId(),
                                uploadedPostProductImageKeys.get(imageIndex - 1)));
                continue;
            }

            postProductImages.add(PostProductImage.createDefault(postProduct.getId()));
        }
        savePostProductImagePort.savePostProductImages(postProductImages);
    }

    private void validateProductImages(
            List<SavePostProductCommand> products, List<MultipartFile> productImages) {

        List<Integer> imageIndexes =
                products.stream()
                        .map(SavePostProductCommand::getImageIndex)
                        .filter(index -> index > 0)
                        .toList();

        // 중복된 이미지 인덱스 검증
        Set<Integer> seen = new HashSet<>();
        for (int index : imageIndexes) {
            if (!seen.add(index)) {
                log.warn("중복된 imageIndex : index={}", index);
                throw new CustomException(ErrorResponseCode.INVALID_POST_PRODUCT_IMAGE_MAPPING);
            }
        }

        // 이미지와 매핑된 상품 정보와 요청한 상품 이미지의 개수가 다를 경우
        // 중복된 imageIndex, 업로드할 이미지가 이미지와 매핑된 상품 정보 개수보다 많을 경우, 업도드할 이미지가 이미지와 매핑된 상품 정보 개수보다 적을 경우
        if (imageIndexes.size() != productImages.size()) {
            log.warn(
                    "상품과 업로드할 상품 이미지 1:1 매핑 실패 : 이미지와 매핑된 상품 개수={}, 업로드할 이미지 개수={}",
                    imageIndexes.size(),
                    productImages.size());
            throw new CustomException(ErrorResponseCode.INVALID_POST_PRODUCT_IMAGE_MAPPING);
        }
    }
}
