package com.ftm.server.application.service.post.product;

import com.ftm.server.application.port.in.post.CreateProductLikeUseCase;
import com.ftm.server.application.port.out.persistence.post.DeleteProductLikePort;
import com.ftm.server.application.port.out.persistence.post.LoadProductLikePort;
import com.ftm.server.application.port.out.persistence.post.SaveProductLikePort;
import com.ftm.server.domain.entity.ProductLike;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateProductLikeService implements CreateProductLikeUseCase {

    private final LoadProductLikePort loadProductLikePort;
    private final SaveProductLikePort saveProductLikePort;
    private final DeleteProductLikePort deleteProductLikePort;

    @Transactional
    public Boolean execute(Long userId, Long productId) {

        // 이미 등록된 좋아요 있는지 확인
        Optional<Long> optionalProductLikeId =
                loadProductLikePort.findOneByUserAndProduct(userId, productId);

        // 없으면 좋아요 생성
        if (optionalProductLikeId.isEmpty()) {
            ProductLike productLike = ProductLike.create(productId, userId);
            saveProductLikePort.saveProductLike(productLike);
            return true;
        }

        // 있으면 삭제
        else {
            deleteProductLikePort.deleteProductLike(optionalProductLikeId.get());
            return false;
        }
    }
}
