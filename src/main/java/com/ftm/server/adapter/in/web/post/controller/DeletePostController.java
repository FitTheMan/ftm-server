package com.ftm.server.adapter.in.web.post.controller;

import com.ftm.server.application.command.post.DeletePostCommand;
import com.ftm.server.application.port.in.post.DeletePostUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeletePostController {

    private final DeletePostUseCase deletePostUseCase;

    @DeleteMapping("/api/posts/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long postId, @AuthenticationPrincipal UserPrincipal userPrincipal) {

        deletePostUseCase.execute(DeletePostCommand.of(postId, userPrincipal.getId()));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessResponseCode.OK));
    }
}
