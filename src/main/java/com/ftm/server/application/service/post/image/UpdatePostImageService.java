package com.ftm.server.application.service.post.image;

import com.ftm.server.application.port.out.persistence.post.DeletePostImagePort;
import com.ftm.server.application.port.out.persistence.post.LoadPostImagePort;
import com.ftm.server.application.port.out.persistence.post.SavePostImagePort;
import com.ftm.server.application.port.out.s3.S3ImageDeletePort;
import com.ftm.server.application.port.out.s3.S3PostImageUploadPort;
import com.ftm.server.application.port.out.transcation.AfterCommitExecutorPort;
import com.ftm.server.application.port.out.transcation.AfterRollbackExecutorPort;
import com.ftm.server.application.query.FindByPostIdQuery;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.Post;
import com.ftm.server.domain.entity.PostImage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UpdatePostImageService {

    private final LoadPostImagePort loadPostImagePort;
    private final DeletePostImagePort deletePostImagePort;
    private final SavePostImagePort savePostImagePort;

    private final S3PostImageUploadPort s3PostImageUploadPort;
    private final S3ImageDeletePort s3ImageDeletePort;

    private final AfterRollbackExecutorPort afterRollbackExecutorPort;
    private final AfterCommitExecutorPort afterCommitExecutorPort;

    public void execute(
            Post post, List<Long> deletePostImageIds, List<MultipartFile> addPostImageFiles) {
        // 삭제, 추가할 이미지가 없으면 return
        if (deletePostImageIds.isEmpty() && addPostImageFiles.isEmpty()) return;

        // 게시글 이미지 업로드
        List<String> uploadedPostImageObjectKeys =
                s3PostImageUploadPort.uploadImages(addPostImageFiles);
        registerRollbackHook(uploadedPostImageObjectKeys);

        // 현재 게시글 이미지 목록
        List<PostImage> currentImages =
                loadPostImagePort.loadPostImagesByPostId(FindByPostIdQuery.of(post.getId()));

        // 기존 이미지가 기본 이미지이고, 새로 추가한 이미지가 존재할 경우
        // 기본 이미지 삭제
        if (currentImages.size() == 1
                && currentImages.get(0).isDefaultImage()
                && !uploadedPostImageObjectKeys.isEmpty()) {
            deletePostImagePort.deletePostImages(List.of(currentImages.get(0)));
        }

        // 삭제할 이미지가 있는 경우
        int remainImageCount = -1; // 삭제 이후 남은 이미지 수
        if (!deletePostImageIds.isEmpty()) {
            remainImageCount = deletePostImages(deletePostImageIds, currentImages);
        }

        // 모든 이미지가 삭제되고, 새로 추가한 이미지가 없으면 기본 이미지 저장
        if (remainImageCount == 0 && uploadedPostImageObjectKeys.isEmpty()) {
            savePostImagePort.savePostImages(List.of(PostImage.createDefault(post.getId())));
            return;
        }

        // 새로 추가한 이미지가 있을 경우
        if (!uploadedPostImageObjectKeys.isEmpty()) {
            savePostImages(post, uploadedPostImageObjectKeys);
        }
    }

    private int deletePostImages(List<Long> deletePostImageIds, List<PostImage> currentImages) {
        Set<Long> deletePostImageIdsSet = new HashSet<>(deletePostImageIds);

        List<PostImage> imagesToDelete =
                currentImages.stream()
                        .filter(image -> deletePostImageIdsSet.contains(image.getId()))
                        .toList();

        if (imagesToDelete.size() != deletePostImageIds.size())
            throw new CustomException(ErrorResponseCode.UNAUTHORIZED_POST_IMAGE_ACCESS);

        // 삭제할 게시글 이미지 검증
        for (PostImage postImage : imagesToDelete) {
            postImage.validateDefaultImage();
        }

        // 삭제할 게시글 이미지 파일 objectKey 목록
        List<String> deletePostImageObjectKeys =
                imagesToDelete.stream().map(PostImage::getObjectKey).toList();
        registerCommitHook(deletePostImageObjectKeys); // 커밋 이후, S3 이미지 삭제 예약

        deletePostImagePort.deletePostImages(imagesToDelete);

        return currentImages.size() - imagesToDelete.size();
    }

    private void savePostImages(Post post, List<String> uploadedPostImageObjectKeys) {
        List<PostImage> imagesToSave =
                uploadedPostImageObjectKeys.stream()
                        .map(objectKey -> PostImage.create(post.getId(), objectKey))
                        .toList();

        savePostImagePort.savePostImages(imagesToSave);
    }

    private void registerRollbackHook(List<String> uploadedPostImageObjectKeys) {
        afterRollbackExecutorPort.doAfterRollback(
                () -> s3ImageDeletePort.deleteImages(uploadedPostImageObjectKeys));
    }

    private void registerCommitHook(List<String> deletePostImageObjectKeys) {
        afterCommitExecutorPort.doAfterCommit(
                () -> s3ImageDeletePort.deleteImages(deletePostImageObjectKeys));
    }
}
