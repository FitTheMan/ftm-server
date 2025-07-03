package com.ftm.server.application.command.post;

import com.ftm.server.adapter.in.web.post.dto.request.UpdatePostProductRequest;
import com.ftm.server.common.utils.CollectionUtils;
import com.ftm.server.domain.enums.ProductHashtag;
import lombok.Getter;

@Getter
public class UpdatePostProductCommand implements HasImageIndex {

    private final Long id;
    private final String name;
    private final String brand;
    private final ProductHashtag[] hashtags;
    private final Long deleteProductImageId;
    private final int imageIndex;

    private UpdatePostProductCommand(UpdatePostProductRequest request) {
        this.id = request.getId();
        this.name = request.getName();
        this.brand = request.getBrand();
        this.hashtags =
                CollectionUtils.listToArrayOrNull(request.getHashtags(), ProductHashtag[]::new);
        this.deleteProductImageId = request.getDeleteProductImageId();
        this.imageIndex = request.getImageIndex();
    }

    public static UpdatePostProductCommand from(UpdatePostProductRequest request) {
        return new UpdatePostProductCommand(request);
    }
}
