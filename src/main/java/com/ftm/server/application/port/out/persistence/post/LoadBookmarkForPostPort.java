package com.ftm.server.application.port.out.persistence.post;

import com.ftm.server.application.query.FindBookmarkByUserIdAndPostIdQuery;

public interface LoadBookmarkForPostPort {
    Boolean existsBookmarkByUserIdAndPostId(FindBookmarkByUserIdAndPostIdQuery query);
}
