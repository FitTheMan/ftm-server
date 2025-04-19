package com.ftm.server.adapter.in.web.post.controller;

import com.ftm.server.adapter.in.web.post.dto.response.LoadPostDetailResponse;
import com.ftm.server.application.port.in.post.LoadPostDetailUseCase;
import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.application.vo.post.PostDetailVo;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadPostDetailController {

    private final LoadPostDetailUseCase loadPostDetailUseCase;

    @GetMapping("/api/posts/{postId}")
    public ResponseEntity<ApiResponse<?>> loadPostDetail(@PathVariable Long postId) {
        PostDetailVo vo = loadPostDetailUseCase.execute(FindByIdQuery.of(postId));

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessResponseCode.OK, LoadPostDetailResponse.from(vo)));
    }
}
