package com.ftm.server.domain.service;

import com.ftm.server.adapter.gateway.repository.UserImageRepository;
import com.ftm.server.domain.dto.query.FindByUserIdQuery;
import com.ftm.server.entity.entities.User;
import com.ftm.server.entity.entities.UserImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserImageService {

    private final UserImageRepository userImageRepository;

    public UserImage queryUserImageByUserId(FindByUserIdQuery query) {
        return userImageRepository
                .findByUserId(query.getUserId())
                .orElseThrow(() -> new RuntimeException(""));
    }

    public void saveUserDefaultImage(User user) {
        UserImage userImage = UserImage.createUserImage(user);
        userImageRepository.save(userImage);
    }
}
