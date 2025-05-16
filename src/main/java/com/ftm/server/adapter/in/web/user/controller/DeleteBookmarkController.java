package com.ftm.server.adapter.in.web.user.controller;

import com.ftm.server.adapter.in.web.user.dto.request.DeleteBookmarkRequest;
import com.ftm.server.application.command.user.DeleteBookmarkCommand;
import com.ftm.server.application.port.in.user.DeleteBookmarkUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.infrastructure.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DeleteBookmarkController {

    private final DeleteBookmarkUseCase deleteBookmarkUseCase;

    @DeleteMapping("api/users/bookmarks")
    public ResponseEntity<ApiResponse> deleteBookmark(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody DeleteBookmarkRequest request) {
        deleteBookmarkUseCase.execute(
                DeleteBookmarkCommand.of(userPrincipal.getId(), request.getPostId()));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
