package com.ftm.server.application.port.in.user;

import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.vo.user.UserWithImageVo;

public interface GetUserSimpleInfoUseCase {
    UserWithImageVo execute(FindByUserIdQuery query);
}
