package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.Bookmark;

@Port
public interface SaveBookmarkPort {
    Boolean saveBookmark(Bookmark bookmark);
}
