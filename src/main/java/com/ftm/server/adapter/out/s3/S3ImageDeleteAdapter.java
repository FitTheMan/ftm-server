package com.ftm.server.adapter.out.s3;

import com.ftm.server.application.port.out.s3.S3ImageDeletePort;
import com.ftm.server.common.annotation.Adapter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

@Adapter
@RequiredArgsConstructor
public class S3ImageDeleteAdapter implements S3ImageDeletePort {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucket;

    @Override
    public void deleteImage(String objectKey) {
        if (objectKey == null) return;
        DeleteObjectRequest deleteRequest =
                DeleteObjectRequest.builder().bucket(bucket).key(objectKey).build();

        s3Client.deleteObject(deleteRequest);
    }
}
