package com.ftm.server.adapter.in.web.post.controller;

import com.ftm.server.adapter.in.web.post.dto.request.LoadProductsByHashTagRequest;
import com.ftm.server.adapter.in.web.post.dto.response.LoadProductsByHashTagResponse;
import com.ftm.server.application.port.in.post.LoadProductsByHashTagUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.infrastructure.security.UserPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadProductsByHashTagController {

    private final LoadProductsByHashTagUseCase useCase;

    @PostMapping("/api/posts/products")
    public ResponseEntity loadProductsByHashTag(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody(required = false) LoadProductsByHashTagRequest request) {

        List<LoadProductsByHashTagResponse> result =
                useCase
                        .execute(
                                userPrincipal == null ? null : userPrincipal.getId(),
                                request == null ? null : request.getHashTagList())
                        .stream()
                        .map(LoadProductsByHashTagResponse::from)
                        .toList();

        return ResponseEntity.ok().body(ApiResponse.success(SuccessResponseCode.OK, result));
    }
}
