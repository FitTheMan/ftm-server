package com.ftm.server.adapter.in.web.post.dto.response;

import com.ftm.server.application.vo.post.TrendingUserVo;
import lombok.Data;

@Data
public class LoadTrendingManResponse {
    private final Integer ranking;
    private final Long userId;
    private final String userName;
    private final String userImageUrl;

    public static LoadTrendingManResponse from(TrendingUserVo vo) {
        return new LoadTrendingManResponse(
                vo.getRank(), vo.getUserId(), vo.getUserName(), vo.getUserImageUrl());
    }
}
