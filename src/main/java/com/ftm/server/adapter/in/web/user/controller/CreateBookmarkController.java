package com.ftm.server.adapter.in.web.user.controller;

import com.ftm.server.adapter.in.web.user.dto.request.CreateBookmarkRequest;
import com.ftm.server.application.command.user.CreateBookmarkCommand;
import com.ftm.server.application.port.in.user.CreateBookmarkUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.infrastructure.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateBookmarkController {

    private final CreateBookmarkUseCase createBookmarkUseCase;

    @PostMapping("/api/users/bookmarks")
    public ResponseEntity<ApiResponse> createBookMark(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody CreateBookmarkRequest request) {
        Boolean isCreated =
                createBookmarkUseCase.execute(
                        CreateBookmarkCommand.of(user.getId(), request.getPostId()));
        HttpStatus httpStatus = isCreated ? HttpStatus.CREATED : HttpStatus.OK;
        SuccessResponseCode successResponseCode =
                isCreated ? SuccessResponseCode.CREATED : SuccessResponseCode.OK;
        return ResponseEntity.status(httpStatus).body(ApiResponse.success(successResponseCode));
    }
}
