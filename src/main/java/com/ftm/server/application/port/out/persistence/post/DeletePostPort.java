package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.common.annotation.Port;
import java.util.List;

@Port
public interface DeletePostPort {

    void deletePostsByIds(List<Long> postIds);
}
