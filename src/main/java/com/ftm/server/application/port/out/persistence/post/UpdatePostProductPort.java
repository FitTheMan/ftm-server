package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.PostProduct;
import java.util.List;

@Port
public interface UpdatePostProductPort {

    void updatePostProducts(List<PostProduct> postProducts);

    void updatePostProduct(PostProduct postProducts);
}
