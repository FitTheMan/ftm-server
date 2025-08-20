package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.application.query.FindByIdsQuery;
import com.ftm.server.application.vo.post.UserIdAndNameVo;
import com.ftm.server.common.annotation.Port;
import java.util.List;

@Port
public interface LoadUserForPostDomainPort {

    List<UserIdAndNameVo> loadPostAndAuthorName(FindByIdsQuery query);
}
