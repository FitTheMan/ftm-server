package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.PostImage;
import java.util.List;

@Port
public interface SavePostImagePort {

    List<PostImage> savePostImages(List<PostImage> postImages);
}
