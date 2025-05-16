package com.ftm.server.adapter.in.web.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteBookmarkRequest {
    @NotNull private final Long postId;
}
