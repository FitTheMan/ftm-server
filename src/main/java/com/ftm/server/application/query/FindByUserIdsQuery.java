package com.ftm.server.application.query;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FindByUserIdsQuery {

    private final List<Long> userIds;

    public static FindByUserIdsQuery of(List<Long> userIds) {
        return new FindByUserIdsQuery(userIds);
    }
}
