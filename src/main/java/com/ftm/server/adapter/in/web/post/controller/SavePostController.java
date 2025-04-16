package com.ftm.server.adapter.in.web.post.controller;

import com.ftm.server.adapter.in.web.post.dto.request.SavePostRequest;
import com.ftm.server.application.command.post.SavePostCommand;
import com.ftm.server.application.port.in.post.SavePostUseCase;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.infrastructure.security.UserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class SavePostController {

    private final SavePostUseCase savePostUseCase;

    @PostMapping("/api/posts")
    public ResponseEntity<ApiResponse<Void>> savePost(
            @RequestPart(value = "data") @Valid SavePostRequest request,
            @RequestPart(value = "postImageFiles", required = false)
                    List<MultipartFile> postImageFiles,
            @RequestPart(value = "productImageFiles", required = false)
                    List<MultipartFile> productImageFiles,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        savePostUseCase.execute(
                SavePostCommand.from(
                        userPrincipal.getId(), request, postImageFiles, productImageFiles));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessResponseCode.CREATED));
    }
}
