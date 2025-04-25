package com.ftm.server.application.command.post;

import com.ftm.server.adapter.in.web.post.dto.request.UpdatePostRequest;
import com.ftm.server.common.utils.CollectionUtils;
import com.ftm.server.domain.enums.GroomingCategory;
import com.ftm.server.domain.enums.HashTag;
import java.util.List;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UpdatePostCommand {

    private final Long id;
    private final Long userId;
    private final String title;
    private final GroomingCategory groomingCategory;
    private final HashTag[] hashTags;
    private final String content;
    private final List<Long> deletePostImageIds;
    private final List<Long> deleteProductIds;
    private final List<SavePostProductCommand> addProducts;
    private final List<UpdatePostProductCommand> updateProducts;
    private final List<MultipartFile> postImageFiles;
    private final List<MultipartFile> productImageFiles;

    private UpdatePostCommand(
            Long id,
            Long userId,
            UpdatePostRequest request,
            List<MultipartFile> postImageFiles,
            List<MultipartFile> productImageFiles) {
        this.id = id;
        this.userId = userId;
        this.title = request.getTitle();
        this.groomingCategory = request.getGroomingCategory();
        this.hashTags = CollectionUtils.listToArrayOrNull(request.getHashTags(), HashTag[]::new);
        this.content = request.getContent();
        this.deletePostImageIds = CollectionUtils.safeList(request.getDeletePostImageIds());
        this.deleteProductIds = CollectionUtils.safeList(request.getDeleteProductIds());
        this.addProducts =
                CollectionUtils.mapOrEmpty(request.getAddProducts(), SavePostProductCommand::from);
        this.updateProducts =
                CollectionUtils.mapOrEmpty(
                        request.getUpdateProducts(), UpdatePostProductCommand::from);
        this.postImageFiles = CollectionUtils.safeList(postImageFiles);
        this.productImageFiles = CollectionUtils.safeList(productImageFiles);
    }

    public static UpdatePostCommand from(
            Long id,
            Long userId,
            UpdatePostRequest request,
            List<MultipartFile> postImageFiles,
            List<MultipartFile> productImageFiles) {
        return new UpdatePostCommand(id, userId, request, postImageFiles, productImageFiles);
    }
}
