package com.ftm.server.adapter.in.web.user.controller;

import com.ftm.server.adapter.in.web.user.dto.request.UpdateUserInfoRequest;
import com.ftm.server.adapter.in.web.user.dto.response.UpdateUserInfoResponse;
import com.ftm.server.application.command.user.UpdateUserCommand;
import com.ftm.server.application.port.in.user.UpdateUserInfoUseCase;
import com.ftm.server.application.vo.user.UserWithImageVo;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UpdateUserInfoController {

    private final UpdateUserInfoUseCase updateUserInfoUseCase;

    @PatchMapping("api/users/info")
    public ResponseEntity<ApiResponse<UpdateUserInfoResponse>> updateUserInfo(
            @RequestPart(value = "data") UpdateUserInfoRequest request,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        UpdateUserCommand updateUserCommand =
                UpdateUserCommand.from(userPrincipal.getId(), request, imageFile);
        UserWithImageVo userWithImageVo = updateUserInfoUseCase.execute(updateUserCommand);
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.success(
                                SuccessResponseCode.OK,
                                UpdateUserInfoResponse.from(userWithImageVo)));
    }
}
