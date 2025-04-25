package com.ftm.server.adapter.in.web.post.dto.request;

import com.ftm.server.domain.enums.GroomingCategory;
import com.ftm.server.domain.enums.HashTag;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdatePostRequest {

    private final String title;
    private final GroomingCategory groomingCategory;
    private final List<HashTag> hashTags;
    private final String content;
    private final List<Long> deletePostImageIds; // 삭제할 게시글 이미지 ID 목록

    private final List<Long> deleteProductIds; // 삭제할 상품 ID 목록
    private final List<SavePostProductRequest> addProducts; // 새로 추가할 상품 목록
    private final List<UpdatePostProductRequest> updateProducts; // 수정할 상품 목록

    public static UpdatePostRequest of(
            String title,
            GroomingCategory groomingCategory,
            List<HashTag> hashTags,
            String content,
            List<Long> deletePostImageIds,
            List<Long> deleteProductIds,
            List<SavePostProductRequest> addProducts,
            List<UpdatePostProductRequest> updateProducts) {
        return UpdatePostRequest.builder()
                .title(title)
                .groomingCategory(groomingCategory)
                .hashTags(hashTags)
                .content(content)
                .deletePostImageIds(deletePostImageIds)
                .deleteProductIds(deleteProductIds)
                .addProducts(addProducts)
                .updateProducts(updateProducts)
                .build();
    }
}
