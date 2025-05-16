package com.ftm.server.application.port.out.persistence.user;

import com.ftm.server.application.query.FindBookmarksByPagingQuery;
import com.ftm.server.common.annotation.Port;
import com.ftm.server.domain.entity.Bookmark;
import org.springframework.data.domain.Slice;

@Port
public interface LoadBookmarkPort {

    Slice<Bookmark> loadBookmarksByUserIdWithPaging(FindBookmarksByPagingQuery query);
}
