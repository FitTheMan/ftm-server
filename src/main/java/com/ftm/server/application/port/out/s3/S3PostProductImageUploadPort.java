package com.ftm.server.application.port.out.s3;

import com.ftm.server.common.annotation.Port;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Port
public interface S3PostProductImageUploadPort {

    List<String> uploadImages(List<MultipartFile> imageFiles);
}
