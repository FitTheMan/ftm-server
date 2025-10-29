package com.ftm.server.adapter.out.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostProductCustomRepositoryImpl implements PostProductCustomRepository {

    //    private final JPAQueryFactory queryFactory;
    //
    //    @Override
    //    public List<PostProductJpaEntity> findByHashtags(List<ProductHashtag> postHashtagList) {
    //
    //        List<String> targetHashtags =
    // List.of(ProductHashtag.values()).stream().map(a->a.name()).toList();
    //
    //        List<PostProductJpaEntity> result = queryFactory
    //                .selectFrom(postProductJpaEntity)
    //                .where(
    //                        Expressions.booleanTemplate(
    //                                "{0} @> cast({1} as product_hashtag[])",
    //                                postProductJpaEntity.hashtags,
    //                                targetHashtags.toArray(new String[0])
    //                        )
    //                )
    //                .orderBy(postProductJpaEntity.createdAt.desc())
    //                .fetch();
    //
    //        return result;
    //
    //    }
}
