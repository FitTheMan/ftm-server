package com.ftm.server.application.port.out.s3;

import org.springframework.web.multipart.MultipartFile;

public interface S3UserImageUploadPort {

    public String uploadImage(MultipartFile imageFile);
}
