package com.ftm.server.adapter.out.s3;

import com.ftm.server.application.port.out.s3.S3PostProductImageUploadPort;
import com.ftm.server.common.annotation.Adapter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Adapter
@RequiredArgsConstructor
public class S3PostProductImageUploadAdapter implements S3PostProductImageUploadPort {

    @Value("${aws.s3.path.product}")
    private String path;

    private final S3ImageUploadAdapter s3ImageUploadAdapter;

    @Override
    public List<String> uploadImages(List<MultipartFile> imageFiles) {
        return s3ImageUploadAdapter.uploadImages(imageFiles, path);
    }
}
