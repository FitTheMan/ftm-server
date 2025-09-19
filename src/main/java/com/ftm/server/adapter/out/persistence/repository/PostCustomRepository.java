package com.ftm.server.adapter.out.persistence.repository;

import com.ftm.server.adapter.out.persistence.model.PostJpaEntity;
import com.ftm.server.application.query.*;
import com.ftm.server.application.vo.post.BookmarkYnWrapperVo;
import com.ftm.server.application.vo.post.PostIdAndBookmarkYnVo;
import com.querydsl.core.Tuple;
import java.util.List;
import org.springframework.data.domain.Slice;

public interface PostCustomRepository {

    List<PostJpaEntity> findAllByDeletedBefore(FindPostByDeleteOptionQuery query);

    Slice<PostJpaEntity> findAllByUserIdWithPaging(FindPostsByPagingQuery query);

    List<Tuple> findAllByCreatedDateInOneWeekAndUserGrouping(FindPostsByCreatedDateQuery query);

    List<BookmarkYnWrapperVo> findPostsByLatestCursor(FindUserPickLatestPostsByCursorQuery query);

    List<PostIdAndBookmarkYnVo> findPostIdWithBookmarkYn(FindByPostIdsAndUserQuery query);
}
