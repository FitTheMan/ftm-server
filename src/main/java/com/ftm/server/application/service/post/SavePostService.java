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
import com.ftm.server.application.validator.PostProductValidator;
import com.ftm.server.application.vo.post.PostInfoVo;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.PostImage;
import com.ftm.server.domain.entity.PostProduct;
import com.ftm.server.domain.entity.PostProductImage;
import java.util.ArrayList;
import java.util.List;
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
    public PostInfoVo execute(SavePostCommand command) {

        // 상품, 상품이미지 검증
        validateProductsWithImages(command.getProducts(), command.getProductImages());

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

        return PostInfoVo.from(post);
    }

    private void validateProductsWithImages(
            List<SavePostProductCommand> products, List<MultipartFile> productImages) {
        PostProductValidator.validateImageIndexRange(products, productImages);
        PostProductValidator.validateImageIndexDuplication(products);
        PostProductValidator.validateOneToOneImageProductMapping(products, productImages);
    }
}
