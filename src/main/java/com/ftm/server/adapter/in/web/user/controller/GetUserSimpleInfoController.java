package com.ftm.server.adapter.in.web.user.controller;

import com.ftm.server.adapter.in.web.user.dto.response.GetUserSimpleInfoResponse;
import com.ftm.server.application.port.in.user.GetUserSimpleInfoUseCase;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.vo.user.UserWithImageVo;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetUserSimpleInfoController {
    private final GetUserSimpleInfoUseCase getUserSimpleInfoUseCase;

    @GetMapping("/api/users/info/simple")
    public ResponseEntity<ApiResponse<GetUserSimpleInfoResponse>> getUserInfo(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserWithImageVo userWithImageVo =
                getUserSimpleInfoUseCase.execute(FindByUserIdQuery.of(userPrincipal.getId()));
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.success(
                                SuccessResponseCode.OK,
                                GetUserSimpleInfoResponse.from(userWithImageVo)));
    }
}
