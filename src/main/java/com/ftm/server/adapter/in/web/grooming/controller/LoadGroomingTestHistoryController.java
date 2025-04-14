package com.ftm.server.adapter.in.web.grooming.controller;

import com.ftm.server.adapter.in.web.grooming.dto.response.GroomingTestHistoryResponse;
import com.ftm.server.application.port.in.grooming.LoadGroomingTestHistoryUseCase;
import com.ftm.server.application.query.FindByUserIdQuery;
import com.ftm.server.application.vo.grooming.GroomingTestHistoryVo;
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
public class LoadGroomingTestHistoryController {

    private final LoadGroomingTestHistoryUseCase loadGroomingTestsHistoryUseCase;

    @GetMapping("/api/grooming/tests/histories")
    public ResponseEntity<ApiResponse<GroomingTestHistoryResponse>> loadHistories(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        GroomingTestHistoryVo vo =
                loadGroomingTestsHistoryUseCase.execute(
                        FindByUserIdQuery.of(userPrincipal.getId()));

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.success(
                                SuccessResponseCode.OK, GroomingTestHistoryResponse.of(vo)));
    }
}
