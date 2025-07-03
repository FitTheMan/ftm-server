package com.ftm.server.adapter.in.web.post.controller;

import com.ftm.server.adapter.in.web.post.dto.response.LoadPostProductHashTagsResponse;
import com.ftm.server.application.port.in.post.LoadPostProductHashTagsUseCase;
import com.ftm.server.application.vo.post.PostProductHashTagDetailVo;
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
public class LoadPostProductHashTagsController {

    private final LoadPostProductHashTagsUseCase loadPostProductHashTagsUseCase;

    @GetMapping("/api/posts/products/hashtags")
    public ResponseEntity<ApiResponse<?>> loadProductHashTags() {
        List<PostProductHashTagDetailVo> vos = loadPostProductHashTagsUseCase.execute();

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.success(
                                SuccessResponseCode.OK, LoadPostProductHashTagsResponse.of(vos)));
    }
}
