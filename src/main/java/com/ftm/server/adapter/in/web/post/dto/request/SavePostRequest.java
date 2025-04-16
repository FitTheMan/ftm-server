package com.ftm.server.adapter.in.web.post.dto.request;

import com.ftm.server.domain.enums.GroomingCategory;
import com.ftm.server.domain.enums.HashTag;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SavePostRequest {

    @NotEmpty private String title;
    private GroomingCategory groomingCategory;
    private List<HashTag> hashtags;
    @NotEmpty private String content;
    private List<SavePostProductRequest> products;
}
