package com.ftm.server.application.service.user;

import com.ftm.server.application.port.in.user.GetUserSimpleInfoUseCase;
import com.ftm.server.application.port.out.persistence.user.LoadUserImagePort;
import com.ftm.server.application.port.out.persistence.user.LoadUserPort;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.vo.user.UserWithImageVo;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserSimpleInfoService implements GetUserSimpleInfoUseCase {

    private final LoadUserPort loadUserPort;
    private final LoadUserImagePort loadUserImagePort;

    @Override
    public UserWithImageVo execute(FindByUserIdQuery query) {

        User user = loadUserPort.loadUserById(query);
        UserImage userImage = loadUserImagePort.loadUserImageByUserId(query);

        return UserWithImageVo.of(user, userImage);
    }
}
