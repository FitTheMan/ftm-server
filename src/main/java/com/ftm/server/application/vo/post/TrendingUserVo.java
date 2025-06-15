package com.ftm.server.application.vo.post;

import com.ftm.server.common.consts.PropertiesHolder;
import lombok.Data;

@Data
public class TrendingUserVo {
    private final Integer rank;
    private final Long userId;
    private final String userName;
    private final String userImageUrl;

    public static TrendingUserVo of(
            Integer rank, Long userId, String userName, String userImageUrl) {
        return new TrendingUserVo(
                rank, userId, userName, PropertiesHolder.CDN_PATH + "/" + userImageUrl);
    }
}
