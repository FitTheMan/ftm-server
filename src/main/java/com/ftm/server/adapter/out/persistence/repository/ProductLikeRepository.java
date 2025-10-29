package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.ProductLikeJpaEntity;
import com.ftm.server.application.vo.post.LoadProductAndUserLikeVo;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLikeRepository extends JpaRepository<ProductLikeJpaEntity, Long> {

    @Query(
            """
    SELECT new com.ftm.server.application.vo.post.LoadProductAndUserLikeVo(
        p.id,
        :userId,
        CASE WHEN pl.id IS NOT NULL THEN true ELSE false END
    )
    FROM PostProductJpaEntity p
    LEFT JOIN ProductLikeJpaEntity pl
        ON pl.postProduct.id = p.id
       AND pl.user.id = :userId
    WHERE p.id in (:postProductIds)
    """)
    List<LoadProductAndUserLikeVo> findProductLikeByUser(
            @Param("userId") Long userId, @Param("postProductIds") List<Long> postProductIds);
}
