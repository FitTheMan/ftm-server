package com.ftm.server.adapter.in.web.post.dto.request;

import com.ftm.server.domain.enums.HashTag;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdatePostProductRequest {

    private final Long id;
    private final String name;
    private final String brand;
    private final List<HashTag> hashTags;
    private final Long deleteProductImageId;
    private final int imageIndex;

    public static UpdatePostProductRequest of(
            Long id,
            String name,
            String brand,
            List<HashTag> hashTags,
            Long deleteProductImageId,
            int imageIndex) {
        return UpdatePostProductRequest.builder()
                .id(id)
                .name(name)
                .brand(brand)
                .hashTags(hashTags)
                .deleteProductImageId(deleteProductImageId)
                .imageIndex(imageIndex)
                .build();
    }
}
