package com.ftm.server.adapter.in.web.post.controller;

import com.ftm.server.adapter.in.web.post.dto.response.CreateProductLikeResponse;
import com.ftm.server.application.port.in.post.CreateProductLikeUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateProductLikeController {

    private final CreateProductLikeUseCase createProductLikeUseCase;

    @PostMapping("/api/products/{productId}/like")
    public ResponseEntity createProductLike(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable(name = "productId") Long productId) {
        Boolean isCreated = createProductLikeUseCase.execute(userPrincipal.getId(), productId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        SuccessResponseCode.OK, new CreateProductLikeResponse(isCreated)));
    }
}
