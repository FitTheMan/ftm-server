package com.ftm.server.adapter.in.web.post.controller;

import com.ftm.server.adapter.in.web.post.dto.response.LoadTrendingManResponse;
import com.ftm.server.application.port.in.post.LoadTrendingManUseCase;
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
public class GetTrendingManController {

    private final LoadTrendingManUseCase loadTrendingManUseCase;

    @GetMapping("/api/posts/trend/users")
    public ResponseEntity<ApiResponse> getTrendingMan() {
        List<LoadTrendingManResponse> result =
                loadTrendingManUseCase.execute().stream()
                        .map(LoadTrendingManResponse::from)
                        .toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessResponseCode.OK, result));
    }
}
