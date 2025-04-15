package com.ftm.server.application.port.out.s3;

public interface S3ImageDeletePort {
    void deleteImage(String objectKey);
}
