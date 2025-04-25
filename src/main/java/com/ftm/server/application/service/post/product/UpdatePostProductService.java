package com.ftm.server.application.service.post.product;

import com.ftm.server.application.command.post.HasImageIndex;
import com.ftm.server.application.command.post.SavePostProductCommand;
import com.ftm.server.application.command.post.UpdatePostCommand;
import com.ftm.server.application.command.post.UpdatePostProductCommand;
import com.ftm.server.application.port.out.persistence.post.*;
import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.service.post.image.UpdatePostProductImageService;
import com.ftm.server.application.validator.PostProductValidator;
import com.ftm.server.application.vo.post.PostProductSaveWithImageVo;
import com.ftm.server.application.vo.post.PostProductUpdateWithImageVo;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.PostProduct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UpdatePostProductService {

    private final LoadPostProductPort loadPostProductPort;
    private final UpdatePostProductPort updatePostProductPort;
    private final SavePostProductPort savePostProductPort;
    private final DeletePostProductPort deletePostProductPort;

    private final UpdatePostProductImageService updatePostProductImageService;

    public void execute(Post post, UpdatePostCommand command) {

        // 업데이트, 저장, 삭제할 상품 목록
        List<UpdatePostProductCommand> updateProducts = command.getUpdateProducts();
        List<SavePostProductCommand> saveProducts = command.getAddProducts();
        List<Long> deleteProductIds = command.getDeleteProductIds();

        // 상품 : 상품 이미지 검증
        validateProductsWithImage(updateProducts, saveProducts, command.getProductImageFiles());

        // 삭제, 업데이트, 저장할 상품이 없으면 return
        if (deleteProductIds.isEmpty() && updateProducts.isEmpty() && saveProducts.isEmpty())
            return;

        List<PostProductUpdateWithImageVo> productImageUpdateContexts = List.of();
        List<PostProductSaveWithImageVo> productImageSaveContexts = new ArrayList<>();

        // 업데이트할 상품이 있을 경우
        if (!updateProducts.isEmpty()) {
            productImageUpdateContexts = updateProducts(post, updateProducts);
        }

        // 새로 추가할 상품이 있을 경우
        if (!saveProducts.isEmpty()) {
            List<PostProduct> saved = saveProducts(post, saveProducts);

            for (int i = 0; i < saved.size(); i++) {
                PostProduct postProduct = saved.get(i);
                SavePostProductCommand cmd = saveProducts.get(i);

                productImageSaveContexts.add(
                        PostProductSaveWithImageVo.of(postProduct.getId(), cmd.getImageIndex()));
            }
        }

        // 상품 이미지 삭제를 먼저하기 위해 상품 이미지 업데이트 먼저 수행
        updatePostProductImageService.execute(
                command.getProductImageFiles(),
                deleteProductIds,
                productImageUpdateContexts,
                productImageSaveContexts);

        // 삭제할 상품이 있는 경우
        if (!deleteProductIds.isEmpty()) {
            deleteProducts(post, deleteProductIds);
        }
    }

    private void validateProductsWithImage(
            List<UpdatePostProductCommand> updateProducts,
            List<SavePostProductCommand> saveProducts,
            List<MultipartFile> productImageFiles) {
        List<? extends HasImageIndex> mergedProducts =
                Stream.concat(updateProducts.stream(), saveProducts.stream()).toList();

        PostProductValidator.validateImageIndexRange(mergedProducts, productImageFiles);
        PostProductValidator.validateImageIndexDuplication(mergedProducts);
        PostProductValidator.validateOneToOneImageProductMapping(mergedProducts, productImageFiles);
    }

    // 상품 업데이트
    private List<PostProductUpdateWithImageVo> updateProducts(
            Post post, List<UpdatePostProductCommand> updateProducts) {
        List<PostProductUpdateWithImageVo> productImageUpdateContexts = new ArrayList<>();
        List<Long> updateProductIds =
                updateProducts.stream().map(UpdatePostProductCommand::getId).toList();

        // 업데이트할 상품 Map
        Map<Long, PostProduct> updateProductMap =
                loadPostProductPort
                        .loadPostProductsByIds(FindByIdsQuery.from(updateProductIds))
                        .stream()
                        .collect(Collectors.toMap(PostProduct::getId, Function.identity()));

        // 업데이트할 상품 검증
        validateProducts(post, updateProductMap.values().stream().toList());

        List<PostProduct> productsToUpdate = new ArrayList<>();
        for (UpdatePostProductCommand cmd : updateProducts) {
            PostProduct postProduct = updateProductMap.get(cmd.getId());
            postProduct.update(cmd);
            productsToUpdate.add(postProduct);

            // 이미지 변경이 없는 경우
            if (cmd.getDeleteProductImageId() == null && cmd.getImageIndex() == -1) continue;

            productImageUpdateContexts.add(
                    PostProductUpdateWithImageVo.of(
                            postProduct.getId(),
                            cmd.getDeleteProductImageId(),
                            cmd.getImageIndex()));
        }

        updatePostProductPort.updatePostProducts(productsToUpdate);

        return productImageUpdateContexts;
    }

    // 상품 추가
    private List<PostProduct> saveProducts(Post post, List<SavePostProductCommand> saveProducts) {
        List<PostProduct> productsToSave = new ArrayList<>();
        for (SavePostProductCommand cmd : saveProducts) {
            PostProduct postProduct = PostProduct.create(cmd.withPostId(post.getId()));
            productsToSave.add(postProduct);
        }

        return savePostProductPort.savePostProducts(productsToSave);
    }

    // 상품 삭제
    private void deleteProducts(Post post, List<Long> deleteProductIds) {
        // 삭제할 상품 목록
        List<PostProduct> deleteProducts =
                loadPostProductPort.loadPostProductsByIds(FindByIdsQuery.from(deleteProductIds));

        // 삭제할 상품이 게시글에 포함된 상품인지 검증
        validateProducts(post, deleteProducts);

        // 상품 삭제
        deletePostProductPort.deletePostProducts(deleteProducts);
    }

    private void validateProducts(Post post, List<PostProduct> products) {
        if (products.isEmpty())
            throw new CustomException(ErrorResponseCode.UNAUTHORIZED_POST_PRODUCT_ACCESS);

        for (PostProduct postProduct : products) {
            postProduct.validatePost(post.getId());
        }
    }
}
