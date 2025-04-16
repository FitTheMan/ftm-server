package com.ftm.server.adapter.out.s3;

import com.ftm.server.application.port.out.s3.S3ImageUploadPort;
import com.ftm.server.application.port.out.tika.TikaFileTypeDetectionPort;
import com.ftm.server.common.annotation.Adapter;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RequiredArgsConstructor
@Adapter
@Slf4j
public class S3ImageUploadAdapter implements S3ImageUploadPort {

    private final S3Client s3Client;

    private final TikaFileTypeDetectionPort tikaFileTypeDetectionPort;

    @Value("${aws.s3.bucket-name}")
    private String bucket;

    @Override
    public String uploadImage(MultipartFile imageFile, String dirName) {

        // 사전 이미지 검증
        validateImageFile(imageFile);

        String originalFileName = imageFile.getOriginalFilename();
        String fileName = dirName + "/" + UUID.randomUUID() + "_" + originalFileName;

        // 이미지 업로드 + 재시도
        uploadImageWithRetry(fileName, imageFile);

        return fileName;
    }

    @Override
    public List<String> uploadImages(List<MultipartFile> imageFiles, String dirName) {
        List<String> uploadedFileNames = new ArrayList<>();

        // 사전 이미지 검증
        for (MultipartFile imageFile : imageFiles) {
            validateImageFile(imageFile);
        }

        // 이미지 업로드
        for (MultipartFile imageFile : imageFiles) {
            String originalFileName = imageFile.getOriginalFilename();
            String fileName = dirName + "/" + UUID.randomUUID() + "_" + originalFileName;

            // 이미지 업로드 + 재시도
            uploadImageWithRetry(fileName, imageFile);
            uploadedFileNames.add(fileName);
        }

        return uploadedFileNames;
    }

    // 이미지 업로드 + 재시도
    private void uploadImageWithRetry(String fileName, MultipartFile imageFile) {
        // S3 이미지 요청 객체
        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(fileName)
                        .contentType(imageFile.getContentType())
                        .build();

        int maxRetries = 3; // 최대 재시도 횟수
        int attempt = 0;

        // 이미지 업로드 요청 실패 시 최대 3회까지 재시도
        while (true) {
            try {
                s3Client.putObject(
                        putObjectRequest,
                        RequestBody.fromInputStream(
                                imageFile.getInputStream(), imageFile.getSize()));

                return;
            } catch (AwsServiceException | SdkClientException | IOException e) {
                attempt++;
                log.warn(
                        "[warn] Failed to upload image attempt {}/{}. Error: {}",
                        attempt,
                        maxRetries,
                        e.getMessage());

                if (attempt >= maxRetries) {
                    // 재시도 끝까지 실패하면 예외 던지기
                    throw new CustomException(ErrorResponseCode.FAIL_TO_UPLOAD_IMAGE);
                }

                try {
                    Thread.sleep(1000L * attempt); // 1초, 2초, 3초 점진적 딜레이
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // 인터럽트 발생 시 즉시 종료
                    throw new CustomException(ErrorResponseCode.FAIL_TO_UPLOAD_IMAGE);
                }
            }
        }
    }

    // 이미지 검증 : 사이즈, 타입, empty 여부
    private void validateImageFile(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new CustomException(ErrorResponseCode.INVALID_IMAGE_FORMAT);
        }

        String type;
        try {
            type = tikaFileTypeDetectionPort.detectFileType(imageFile); // 이미지 타입 추출
        } catch (IOException e) {
            throw new CustomException(ErrorResponseCode.INVALID_IMAGE_FORMAT);
        }

        if ((float) imageFile.getSize() / (1024.0 * 1024.0) > (float) 10
                || !type.startsWith("image/")) {
            throw new CustomException(ErrorResponseCode.INVALID_IMAGE_FORMAT);
        }
    }
}
