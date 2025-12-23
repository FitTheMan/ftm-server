package com.ftm.server.gcetest;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class GceS3TestService {

    private final S3Client s3Client;

    private static final String PREFIX = "healthcheck/";

    @Value("${aws.s3.bucket-name}")
    private String bucket;

    public Map<String, Object> ping() {
        try {
            s3Client.headObject(builder -> builder.bucket(bucket));
        } catch (Exception ex) {
            log.warn("S3 headObject 예외: {}", ex.getMessage(), ex);
        }

        return Map.of("ok", true, "bucket", bucket, "checkedAt", OffsetDateTime.now().toString());
    }

    public Map<String, Object> put() {
        String objectKey = PREFIX + UUID.randomUUID() + ".txt";

        PutObjectRequest req =
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(objectKey)
                        .contentType("text/plain")
                        .build();

        PutObjectResponse res = s3Client.putObject(req, RequestBody.empty());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("ok", true);
        result.put("bucket", bucket);
        result.put("key", objectKey);
        result.put("etag", res.eTag());
        return result;
    }

    public Map<String, Object> delete(String key) {
        String objectKey = PREFIX + key;
        s3Client.deleteObject(builder -> builder.bucket(bucket).key(objectKey));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("ok", true);
        result.put("bucket", bucket);
        result.put("key", objectKey);
        result.put("deleted", true);
        return result;
    }
}
