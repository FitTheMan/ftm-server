package com.ftm.server.application.command.post;

import com.ftm.server.adapter.in.web.post.dto.request.SavePostRequest;
import com.ftm.server.domain.enums.GroomingCategory;
import com.ftm.server.domain.enums.HashTag;
import java.util.List;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class SavePostCommand {

    private final Long userId;
    private final String title;
    private final GroomingCategory groomingCategory;
    private final HashTag[] hashTags;
    private final String content;
    private final List<MultipartFile> postImages;
    private final List<SavePostProductCommand> products;
    private final List<MultipartFile> productImages;

    private SavePostCommand(
            Long userId,
            SavePostRequest request,
            List<MultipartFile> postImages,
            List<SavePostProductCommand> products,
            List<MultipartFile> productImages) {
        this.userId = userId;
        this.title = request.getTitle();
        this.groomingCategory = request.getGroomingCategory();
        this.hashTags = request.getHashtags().toArray(new HashTag[0]);
        this.content = request.getContent();
        this.postImages = postImages;
        this.products = products;
        this.productImages = productImages;
    }

    public static SavePostCommand from(
            Long userId,
            SavePostRequest request,
            List<MultipartFile> postImages,
            List<MultipartFile> productImages) {
        if (postImages == null) postImages = List.of();
        if (productImages == null) productImages = List.of();
        List<SavePostProductCommand> products =
                request.getProducts().stream().map(SavePostProductCommand::from).toList();
        return new SavePostCommand(userId, request, postImages, products, productImages);
    }
}
