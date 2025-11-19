package com.ftm.server.application.query;

import com.ftm.server.domain.enums.ProductHashtag;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FindByProductHashTagsQuery {
    List<ProductHashtag> productHashtagList;
}
