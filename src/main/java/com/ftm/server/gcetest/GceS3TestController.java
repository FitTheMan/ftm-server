package com.ftm.server.gcetest;

import com.ftm.server.common.response.ApiResponse;
import com.ftm.server.common.response.enums.SuccessResponseCode;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/s3")
@RequiredArgsConstructor
public class GceS3TestController {

    private final GceS3TestService gceS3TestService;

    @GetMapping("/ping")
    public ResponseEntity<ApiResponse<Map<String, Object>>> head() {
        Map<String, Object> result = gceS3TestService.ping();
        return ResponseEntity.ok(ApiResponse.success(SuccessResponseCode.OK, result));
    }

    @PostMapping("/put")
    public ResponseEntity<ApiResponse<Map<String, Object>>> put() {
        Map<String, Object> result = gceS3TestService.put();
        return ResponseEntity.ok(ApiResponse.success(SuccessResponseCode.OK, result));
    }

    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<Map<String, Object>>> delete(@RequestParam String key) {
        Map<String, Object> result = gceS3TestService.delete(key);
        return ResponseEntity.ok(ApiResponse.success(SuccessResponseCode.OK, result));
    }
}
