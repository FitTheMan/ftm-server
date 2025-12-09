package com.ftm.server.adapter.in.web.post.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ftm.server.application.vo.post.PostDetailVo;
import com.ftm.server.domain.enums.PostHashtag;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public class LoadPostDetailResponse {

    private final Long postId;
    private final String title;
    private final String content;
    private final List<String> hashtags;
    private final Integer viewCount;
    private final Integer likeCount;
    private final Boolean userLikeYn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private final LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private final LocalDateTime updatedAt;

    private final List<PostImageResponse> postImages;
    private final PostWriterResponse writer;
    private final List<PostProductResponse> postProducts;

    private LoadPostDetailResponse(PostDetailVo postDetailVo) {
        this.postId = postDetailVo.getPostId();
        this.title = postDetailVo.getTitle();
        this.content = postDetailVo.getContent();
        this.hashtags = Arrays.stream(postDetailVo.getHashtags()).map(PostHashtag::getTag).toList();
        this.viewCount = postDetailVo.getViewCount();
        this.likeCount = postDetailVo.getLikeCount();
        this.createdAt = postDetailVo.getCreatedAt();
        this.updatedAt = postDetailVo.getUpdatedAt();
        this.postImages =
                postDetailVo.getPostImages().stream().map(PostImageResponse::from).toList();
        this.writer = PostWriterResponse.from(postDetailVo.getUser(), postDetailVo.getUserImage());
        this.postProducts =
                postDetailVo.getProducts().stream().map(PostProductResponse::from).toList();
        this.userLikeYn = postDetailVo.getUserLikeYn();
    }

    public static LoadPostDetailResponse from(PostDetailVo postDetailVo) {
        return new LoadPostDetailResponse(postDetailVo);
    }
}
