package com.ftm.server.adapter.in.web.post.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ftm.server.application.vo.post.PostInfoVo;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SavePostResponse {

    private final Long postId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private final LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private final LocalDateTime updatedAt;

    private SavePostResponse(PostInfoVo postInfoVo) {
        this.postId = postInfoVo.getId();
        this.createdAt = postInfoVo.getCreatedAt();
        this.updatedAt = postInfoVo.getUpdatedAt();
    }

    public static SavePostResponse from(PostInfoVo postInfoVo) {
        return new SavePostResponse(postInfoVo);
    }
}
