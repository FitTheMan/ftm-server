package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.PostImageJpaEntity;
import com.ftm.server.adapter.out.persistence.model.PostJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImageJpaEntity, Long> {

    List<PostImageJpaEntity> findAllByPost(PostJpaEntity post);
}
