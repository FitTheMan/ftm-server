package com.ftm.server.adapter.in.web.post.controller;

import com.ftm.server.adapter.in.web.post.dto.response.LoadUserPickTopBookmarkPostsResponse;
import com.ftm.server.application.port.in.post.LoadUserPickTopBookmarkPostsUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadUserPickTopBookmarkPostsController {

    private final LoadUserPickTopBookmarkPostsUseCase useCase;

    @GetMapping("/api/posts/userpick/top-bookmarks")
    public ResponseEntity<ApiResponse> loadTopBookmarkPosts() {
        List<LoadUserPickTopBookmarkPostsResponse> result =
                useCase.execute().stream().map(LoadUserPickTopBookmarkPostsResponse::from).toList();
        return ResponseEntity.ok(ApiResponse.success(SuccessResponseCode.OK, result));
    }
}
