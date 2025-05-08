package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.PostImage;
import java.util.List;

@Port
public interface LoadPostImageUserDomainPort {

    List<PostImage> loadRepresentativeImagesByPostIds(FindByIdsQuery query);
}
