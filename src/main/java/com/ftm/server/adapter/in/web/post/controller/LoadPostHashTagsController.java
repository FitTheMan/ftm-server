package com.ftm.server.adapter.in.web.post.controller;

import com.ftm.server.adapter.in.web.post.dto.response.LoadPostHashTagsResponse;
import com.ftm.server.application.port.in.post.LoadPostHashTagsUseCase;
import com.ftm.server.application.vo.post.PostHashTagDetailVo;
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
public class LoadPostHashTagsController {

    private final LoadPostHashTagsUseCase loadPostHashTagsUseCase;

    @GetMapping("/api/posts/hashtags")
    public ResponseEntity<ApiResponse<LoadPostHashTagsResponse>> loadPostHashTags() {
        List<PostHashTagDetailVo> vos = loadPostHashTagsUseCase.execute();

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.success(
                                SuccessResponseCode.OK, LoadPostHashTagsResponse.of(vos)));
    }
}
