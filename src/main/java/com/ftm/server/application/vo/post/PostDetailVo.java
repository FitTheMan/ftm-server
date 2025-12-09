package com.ftm.server.application.vo.post;

import com.ftm.server.domain.entity.*;
import com.ftm.server.domain.enums.PostHashtag;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class PostDetailVo {

    private final Long postId;
    private final String title;
    private final String content;
    private final PostHashtag[] hashtags;
    private final Integer viewCount;
    private final Integer likeCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final User user;
    private final UserImage userImage;
    private final List<PostImage> postImages;
    private final List<PostProductDetailVo> products;
    private final Boolean userLikeYn;

    private PostDetailVo(
            Post post,
            User user,
            UserImage userImage,
            List<PostImage> postImages,
            List<PostProductDetailVo> products,
            Boolean userLikeYn) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.hashtags = post.getHashtags();
        this.viewCount = post.getViewCount();
        this.likeCount = post.getLikeCount();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.user = user;
        this.userImage = userImage;
        this.postImages = postImages;
        this.products = products;
        this.userLikeYn = userLikeYn;
    }

    public static PostDetailVo from(
            Post post,
            User user,
            UserImage userImage,
            List<PostImage> postImages,
            List<PostProduct> postProducts,
            Map<Long, PostProductImage> postProductImageMap,
            Boolean userLikeYn) {
        return new PostDetailVo(
                post,
                user,
                userImage,
                postImages,
                PostProductDetailVo.listFrom(postProducts, postProductImageMap),
                userLikeYn);
    }
}
