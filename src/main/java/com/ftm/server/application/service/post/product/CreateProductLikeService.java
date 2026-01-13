package com.ftm.server.application.service.post.product;

import com.ftm.server.application.port.in.post.CreateProductLikeUseCase;
import com.ftm.server.application.port.out.persistence.post.*;
import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.PostProduct;
import com.ftm.server.domain.entity.ProductLike;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateProductLikeService implements CreateProductLikeUseCase {

    private final LoadProductLikePort loadProductLikePort;
    private final LoadPostProductPort loadPostProductPort;
    private final SaveProductLikePort saveProductLikePort;
    private final DeleteProductLikePort deleteProductLikePort;
    private final UpdatePostProductPort updatePostProductPort;

    @Transactional
    public Boolean execute(Long userId, Long productId) {

        PostProduct postProduct =
                loadPostProductPort
                        .loadPostProductsByIds(FindByIdsQuery.from(List.of(productId)))
                        .stream()
                        .findFirst()
                        .orElseThrow(
                                () ->
                                        new CustomException(
                                                ErrorResponseCode.POST_PRODUCT_NOT_FOUND));

        // 이미 등록된 좋아요 있는지 확인
        Optional<Long> optionalProductLikeId =
                loadProductLikePort.findOneByUserAndProduct(userId, productId);

        // 없으면 좋아요 생성
        if (optionalProductLikeId.isEmpty()) {
            ProductLike productLike = ProductLike.create(productId, userId);
            saveProductLikePort.saveProductLike(productLike);

            postProduct.plusRecommendedCountByOne();
            updatePostProductPort.updatePostProduct(postProduct);
            return true;
        }

        // 있으면 삭제
        else {
            deleteProductLikePort.deleteProductLike(optionalProductLikeId.get());

            postProduct.minusRecommendedCountByOne();
            updatePostProductPort.updatePostProduct(postProduct);
            return false;
        }
    }
}
