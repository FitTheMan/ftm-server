package com.ftm.server.application.port.out.s3;

import org.springframework.web.multipart.MultipartFile;

public interface S3ImageUploadPort {

    String updateImage(MultipartFile imageFile, String dirName);
}
