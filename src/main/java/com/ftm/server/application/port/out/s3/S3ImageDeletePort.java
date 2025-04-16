package com.ftm.server.application.port.out.s3;

import java.util.List;

public interface S3ImageDeletePort {
    void deleteImage(String objectKey);

    void deleteImages(List<String> objectKeys);
}
