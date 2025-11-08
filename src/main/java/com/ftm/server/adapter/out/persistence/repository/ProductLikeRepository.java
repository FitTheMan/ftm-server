package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.ProductLikeJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLikeRepository extends JpaRepository<ProductLikeJpaEntity, Long> {

    @Query(
            "select p.id from ProductLikeJpaEntity p where p.user.id =:userId and p.postProduct.id =:productId")
    Optional<Long> findByUserAndAndPostProduct(
            @Param("userId") Long userId, @Param("productId") Long productId);
}
