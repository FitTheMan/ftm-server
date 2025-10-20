package com.ftm.server.adapter.in.web.post.controller;

import com.ftm.server.adapter.in.web.post.dto.response.GetUserPickPostsLatestResponse;
import com.ftm.server.adapter.in.web.post.dto.response.GetUserPickPostsPopularCursorResponse;
import com.ftm.server.application.port.in.post.GetUserPickPostsUseCase;
import com.ftm.server.application.query.FindUserPickLatestPostsByCursorQuery;
import com.ftm.server.application.query.FindUserPickPopularPostsByCursorQuery;
import com.ftm.server.application.vo.post.GetUserPickAllPostsPopularWithCursorVo;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.infrastructure.security.UserPrincipal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetUserPickPostsController {

    private final GetUserPickPostsUseCase getUserPickPostsUseCase;

    @GetMapping("/api/posts/userpick/all/latest")
    public ResponseEntity<ApiResponse> getLatestPosts(
            @RequestParam(required = false, name = "lastCursor")
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime nextCursorCreatedAt,
            @RequestParam(required = false, defaultValue = "20") int limit,
            @AuthenticationPrincipal UserPrincipal user) {

        GetUserPickPostsLatestResponse result =
                GetUserPickPostsLatestResponse.from(
                        getUserPickPostsUseCase.executeLatest(
                                FindUserPickLatestPostsByCursorQuery.of(
                                        limit,
                                        nextCursorCreatedAt,
                                        user == null ? null : user.getId())));

        return ResponseEntity.ok(ApiResponse.success(SuccessResponseCode.OK, result));
    }

    @GetMapping("/api/posts/userpick/all/popular")
    public ResponseEntity<ApiResponse> getUserPickPopularPosts(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(name = "limit") Integer size,
            @RequestParam(name = "lastScore", required = false) Double lastScore,
            @RequestParam(name = "lastPostId", required = false) Long lastId) {

        GetUserPickAllPostsPopularWithCursorVo items =
                getUserPickPostsUseCase.executePopular(
                        FindUserPickPopularPostsByCursorQuery.of(
                                size, lastScore, lastId, user == null ? null : user.getId()));

        GetUserPickPostsPopularCursorResponse response =
                GetUserPickPostsPopularCursorResponse.from(items);

        return ResponseEntity.ok(ApiResponse.success(SuccessResponseCode.OK, response));
    }
}
