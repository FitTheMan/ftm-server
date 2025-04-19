package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.PostJpaEntity;
import com.ftm.server.adapter.out.persistence.model.PostProductJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostProductRepository extends JpaRepository<PostProductJpaEntity, Long> {

    List<PostProductJpaEntity> findAllByPost(PostJpaEntity post);
}
