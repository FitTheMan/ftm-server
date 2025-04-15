package com.ftm.server.adapter.out.s3;

import com.ftm.server.application.port.out.s3.S3ImageUploadPort;
import com.ftm.server.application.port.out.s3.S3UserImageUploadPort;
import com.ftm.server.common.annotation.Adapter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@Adapter
@RequiredArgsConstructor
public class S3UserImageUploadAdapter implements S3UserImageUploadPort {

    @Value("${aws.s3.path.user}")
    private String path;

    private final S3ImageUploadPort s3ImageUploadPort;

    @Override
    public String uploadImage(MultipartFile imageFile) {
        return s3ImageUploadPort.updateImage(imageFile, path);
    }
}
