package com.ftm.server.adapter.in.web.post.controller;

import com.ftm.server.adapter.in.web.post.dto.request.UpdatePostRequest;
import com.ftm.server.application.command.post.UpdatePostCommand;
import com.ftm.server.application.port.in.post.UpdatePostUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.infrastructure.security.UserPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UpdatePostController {

    private final UpdatePostUseCase updatePostUseCase;

    @PatchMapping("/api/posts/{postId}")
    public ResponseEntity<ApiResponse<?>> updatePost(
            @PathVariable Long postId,
            @RequestPart(value = "data") UpdatePostRequest request,
            @RequestPart(value = "postImageFiles", required = false)
                    List<MultipartFile> postImageFiles,
            @RequestPart(value = "productImageFiles", required = false)
                    List<MultipartFile> productImageFiles,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        updatePostUseCase.execute(
                UpdatePostCommand.from(
                        postId, userPrincipal.getId(), request, postImageFiles, productImageFiles));

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessResponseCode.OK));
    }
}
