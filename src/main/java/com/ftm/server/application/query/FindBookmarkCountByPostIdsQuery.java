package com.ftm.server.application.query;

import java.util.List;
import lombok.Data;

@Data
public class FindBookmarkCountByPostIdsQuery {
    private final List<Long> postIds;

    public static FindBookmarkCountByPostIdsQuery of(List<Long> postIds) {
        return new FindBookmarkCountByPostIdsQuery(postIds);
    }
}
