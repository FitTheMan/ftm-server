package com.ftm.server.adapter.in.web.post.dto.request;

import com.ftm.server.domain.enums.ProductHashtag;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoadProductsByHashTagRequest {
    private final List<ProductHashtag> hashTagList;
}
