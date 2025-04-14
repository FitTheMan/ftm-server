package com.ftm.server.adapter.in.web.grooming.controller;

import com.ftm.server.adapter.in.web.grooming.dto.response.GroomingTestHistoryDetailResponse;
import com.ftm.server.application.port.in.grooming.LoadGroomingTestHistoryDetailUseCase;
import com.ftm.server.application.query.FindGroomingTestResultByUserIdAndTestedAtQuery;
import com.ftm.server.application.vo.grooming.GroomingTestHistoryDetailVo;
import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import com.ftm.server.infrastructure.security.UserPrincipal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadGroomingTestHistoryDetailController {

    private final LoadGroomingTestHistoryDetailUseCase loadGroomingTestHistoryDetailUseCase;

    @GetMapping("/api/grooming/tests/histories/detail")
    public ResponseEntity<ApiResponse<GroomingTestHistoryDetailResponse>> loadHistoryDetail(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate date,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        GroomingTestHistoryDetailVo vo =
                loadGroomingTestHistoryDetailUseCase.execute(
                        FindGroomingTestResultByUserIdAndTestedAtQuery.of(
                                userPrincipal.getId(), date));
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.success(
                                SuccessResponseCode.OK,
                                GroomingTestHistoryDetailResponse.from(vo)));
    }
}
