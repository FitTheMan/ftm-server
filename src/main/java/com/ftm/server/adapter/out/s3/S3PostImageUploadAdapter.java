package com.ftm.server.adapter.out.s3;

import com.ftm.server.application.port.out.s3.S3PostImageUploadPort;
import com.ftm.server.common.annotation.Adapter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@Adapter
@RequiredArgsConstructor
public class S3PostImageUploadAdapter implements S3PostImageUploadPort {

    @Value("${aws.s3.path.post}")
    private String path;

    private final S3ImageUploadAdapter s3ImageUploadAdapter;

    @Override
    public List<String> uploadImages(List<MultipartFile> imageFiles) {
        return s3ImageUploadAdapter.uploadImages(imageFiles, path);
    }
}
