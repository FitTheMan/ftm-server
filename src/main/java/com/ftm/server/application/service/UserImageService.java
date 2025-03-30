package com.ftm.server.application.service;

import com.ftm.server.application.dto.query.FindByUserIdQuery;
import com.ftm.server.application.port.repository.UserImageRepository;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserImageService {

    private final UserImageRepository userImageRepository;

    public UserImage queryUserImageByUserId(FindByUserIdQuery query) {
        return userImageRepository
                .findByUserId(query.getUserId())
                .orElseThrow(() -> new CustomException(ErrorResponseCode.USER_IMAGE_NOT_FOUND));
    }

    public UserImage saveUserDefaultImage(User user) {
        UserImage userImage = UserImage.createUserImage(user);
        userImageRepository.save(userImage);
        return userImage;
    }
}
