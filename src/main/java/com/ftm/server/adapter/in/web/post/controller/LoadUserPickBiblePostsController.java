package com.ftm.server.adapter.in.web.post.controller;

import com.ftm.server.adapter.in.web.post.dto.response.LoadUserPickBiblePostsResponse;
import com.ftm.server.application.port.in.post.LoadUserPickBiblePostsUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LoadUserPickBiblePostsController {

    private final LoadUserPickBiblePostsUseCase userPickBiblePostsUseCase;

    @GetMapping("/api/posts/userpick/bible")
    public ResponseEntity<ApiResponse> loadUserPickGroomingBiblePosts() {
        List<LoadUserPickBiblePostsResponse> result =
                userPickBiblePostsUseCase.execute().stream()
                        .map(LoadUserPickBiblePostsResponse::from)
                        .toList();
        return ResponseEntity.ok(ApiResponse.success(SuccessResponseCode.OK, result));
    }
}
