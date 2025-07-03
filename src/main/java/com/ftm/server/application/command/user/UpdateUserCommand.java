package com.ftm.server.application.command.user;

import com.ftm.server.adapter.in.web.user.dto.request.UpdateUserInfoRequest;
import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashtagCategory;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserCommand {
    private final Long userId;
    private final String nickname;
    private final AgeGroup ageGroup;
    private final HashtagCategory[] hashtags;
    private final String imageAction;
    private final MultipartFile profileImage;

    public static UpdateUserCommand from(
            Long userId, UpdateUserInfoRequest request, MultipartFile imageFile) {
        return new UpdateUserCommand(
                userId,
                request.getNickname(),
                request.getAge(),
                request.getHashtags(),
                request.getImageAction(),
                imageFile);
    }
}
