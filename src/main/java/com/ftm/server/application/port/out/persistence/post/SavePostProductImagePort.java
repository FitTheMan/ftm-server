package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.PostProductImage;
import java.util.List;

@Port
public interface SavePostProductImagePort {

    List<PostProductImage> savePostProductImages(List<PostProductImage> productImages);
}
