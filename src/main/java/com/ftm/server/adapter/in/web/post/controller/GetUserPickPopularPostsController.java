package com.ftm.server.adapter.in.web.post.controller;

import com.ftm.server.adapter.in.web.post.dto.response.GetUserPickPopularPostsResponse;
import com.ftm.server.application.port.in.post.GetUserPickPopularPostsUseCase;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.infrastructure.security.UserPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class GetUserPickPopularPostsController {

    private final GetUserPickPopularPostsUseCase userPickPopularPostsUseCase;

    @GetMapping("/api/posts/userpick/popular")
    public ResponseEntity<ApiResponse> getUserPickPopularPosts(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<GetUserPickPopularPostsResponse> userPickPopularPostsResponses =
                userPickPopularPostsUseCase
                        .execute(
                                FindByUserIdQuery.of(
                                        userPrincipal == null ? null : userPrincipal.getId()))
                        .stream()
                        .map(GetUserPickPopularPostsResponse::from)
                        .toList();

        return ResponseEntity.ok(
                ApiResponse.success(SuccessResponseCode.OK, userPickPopularPostsResponses));
    }
}
