package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.query.FindPostsByPagingQuery;
import com.ftm.server.domain.entity.Post;
import java.util.List;
import org.springframework.data.domain.Slice;

public interface LoadPostUserDomainPort {

    List<Post> loadPostListByUser(FindByUserIdQuery query);

    Slice<Post> loadPostsByUserIdWithPaging(FindPostsByPagingQuery query);

    List<Post> loadPostsByIds(FindByIdsQuery query);
}
