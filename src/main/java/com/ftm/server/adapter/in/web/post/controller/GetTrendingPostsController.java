package com.ftm.server.adapter.in.web.post.controller;

import com.ftm.server.adapter.in.web.post.dto.response.GetTrendingPostsResponse;
import com.ftm.server.application.port.in.post.LoadTrendingPostsUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetTrendingPostsController {

    private final LoadTrendingPostsUseCase loadTrendingPostsUseCase;

    @GetMapping("/api/posts/trend")
    public ResponseEntity<ApiResponse> getTrendingPosts() {
        List<GetTrendingPostsResponse> result =
                loadTrendingPostsUseCase.execute().stream()
                        .map(GetTrendingPostsResponse::from)
                        .toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessResponseCode.OK, result));
    }
}
