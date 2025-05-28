package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.query.FindByPostIdQuery;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.PostImage;
import java.util.List;

@Port
public interface LoadPostImagePort {

    List<PostImage> loadPostImagesByPostId(FindByPostIdQuery query);

    List<PostImage> loadPostImagesByPostIds(FindByIdsQuery query);

    List<PostImage> loadRepresentativeImagesByPostIds(FindByIdsQuery query);
}
