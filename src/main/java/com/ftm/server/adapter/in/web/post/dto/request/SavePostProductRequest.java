package com.ftm.server.adapter.in.web.post.dto.request;

import com.ftm.server.domain.enums.HashTag;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SavePostProductRequest {

    private int imageIndex;
    @NotEmpty private String name;
    private String brand;
    private List<HashTag> hashtags;
}
