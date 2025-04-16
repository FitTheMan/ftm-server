package com.ftm.server.application.port.out.s3;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface S3ImageUploadPort {

    String uploadImage(MultipartFile imageFile, String dirName);

    List<String> uploadImages(List<MultipartFile> imageFiles, String dirName);
}
