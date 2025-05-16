package com.ftm.server.adapter.in.web.user.controller;

import com.ftm.server.adapter.in.web.user.dto.response.LoadMyPostsResponse;
import com.ftm.server.application.port.in.user.LoadMyBookmarkPostUseCase;
import com.ftm.server.application.query.FindBookmarksByPagingQuery;
import com.ftm.server.application.vo.post.PostPagingVo;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadMyBookmarkPostController {

    private final LoadMyBookmarkPostUseCase loadMyBookmarkPostUseCase;

    @GetMapping("/api/users/me/bookmarks")
    public ResponseEntity<ApiResponse<LoadMyPostsResponse>> loadMyBookmarkPosts(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {
        // 요청 페이징 데이터 유효성 검증
        if (page < 0) throw new CustomException(ErrorResponseCode.BAD_REQUEST_PAGING_INDEX_RANGE);
        if (size < 1 || size > 10)
            throw new CustomException(ErrorResponseCode.BAD_REQUEST_PAGING_SIZE_RANGE);

        PostPagingVo vo =
                loadMyBookmarkPostUseCase.execute(
                        FindBookmarksByPagingQuery.of(userPrincipal.getId(), page, size));

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessResponseCode.OK, LoadMyPostsResponse.from(vo)));
    }
}
