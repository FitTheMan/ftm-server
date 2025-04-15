package com.ftm.server.adapter.in.web.user.dto.response;

import com.ftm.server.application.vo.user.UserWithImageVo;
import com.ftm.server.common.consts.PropertiesHolder;
import com.ftm.server.domain.entity.User;
import com.ftm.server.domain.entity.UserImage;
import com.ftm.server.domain.enums.AgeGroup;
import com.ftm.server.domain.enums.HashTag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;

@Data
public class UpdateUserInfoResponse {
    private final Long userId;
    private final String userNickname;
    private final String imageUrl;
    private final AgeInfo ageInfo;
    private final List<HashTagInfo> hashTagInfo;

    public static UpdateUserInfoResponse from(UserWithImageVo userWithImageVo) {
        User user = userWithImageVo.getUser();
        UserImage userImage = userWithImageVo.getUserImage();

        String imageUrl = PropertiesHolder.CDN_PATH + "/" + userImage.getObjectKey();
        HashTag[] userHashTagWithArray = user.getFavoriteHashtags();
        List<HashTag> userHashTag =
                userHashTagWithArray == null || userHashTagWithArray.length == 0
                        ? new ArrayList<>()
                        : Arrays.stream(user.getFavoriteHashtags()).toList();
        List<HashTagInfo> hashTagInfos = new ArrayList<>();
        for (HashTag hashTag : HashTag.values()) {
            if (userHashTag.contains(hashTag)) {
                hashTagInfos.add(HashTagInfo.from(hashTag, true));
            } else {
                hashTagInfos.add(HashTagInfo.from(hashTag, false));
            }
        }
        return new UpdateUserInfoResponse(
                user.getId(),
                user.getNickname(),
                imageUrl,
                AgeInfo.from(user.getAgeGroup()),
                hashTagInfos);
    }

    private record AgeInfo(String value, String description) {
        private static AgeInfo from(AgeGroup ageGroup) {
            return new AgeInfo(ageGroup.name(), ageGroup.getValue());
        }
    }

    private record HashTagInfo(String value, String description, Boolean isSelected) {
        private static HashTagInfo from(HashTag hashTag, Boolean isSelected) {
            return new HashTagInfo(hashTag.name(), hashTag.getValue(), isSelected);
        }
    }
}
