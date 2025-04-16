package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.PostProduct;
import java.util.List;

@Port
public interface SavePostProductPort {

    List<PostProduct> savePostProducts(List<PostProduct> postProducts);
}
