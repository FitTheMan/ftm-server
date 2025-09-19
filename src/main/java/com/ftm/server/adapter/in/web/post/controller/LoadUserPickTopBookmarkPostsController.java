package com.ftm.server.adapter.in.web.post.controller;

import com.ftm.server.adapter.in.web.post.dto.response.LoadUserPickTopBookmarkPostsResponse;
import com.ftm.server.application.port.in.post.LoadUserPickTopBookmarkPostsUseCase;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.infrastructure.security.UserPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadUserPickTopBookmarkPostsController {

    private final LoadUserPickTopBookmarkPostsUseCase useCase;

    @GetMapping("/api/posts/userpick/top-bookmarks")
    public ResponseEntity<ApiResponse> loadTopBookmarkPosts(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<LoadUserPickTopBookmarkPostsResponse> result =
                useCase
                        .execute(
                                FindByUserIdQuery.of(
                                        userPrincipal == null ? null : userPrincipal.getId()))
                        .stream()
                        .map(LoadUserPickTopBookmarkPostsResponse::from)
                        .toList();
        return ResponseEntity.ok(ApiResponse.success(SuccessResponseCode.OK, result));
    }
}
