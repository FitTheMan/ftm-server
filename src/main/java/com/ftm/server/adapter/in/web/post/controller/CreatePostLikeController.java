package com.ftm.server.adapter.in.web.post.controller;

import com.ftm.server.adapter.in.web.post.dto.response.CreatePostLikeResponse;
import com.ftm.server.application.port.in.post.CreatePostLikeUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CreatePostLikeController {

    private final CreatePostLikeUseCase createPostLikeUseCase;

    @PostMapping("/api/posts/{postId}/like")
    public ResponseEntity createProductLike(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable(name = "postId") Long postId) {
        Boolean isCreated = createPostLikeUseCase.execute(userPrincipal.getId(), postId);
        return ResponseEntity.ok(
                ApiResponse.success(SuccessResponseCode.OK, new CreatePostLikeResponse(isCreated)));
    }
}
