package com.ftm.server.application.query;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindByPostIdsAndUserQuery {
    List<Long> postIds;
    Long userId;

    public static FindByPostIdsAndUserQuery of(List<Long> postIds, Long userId) {
        return new FindByPostIdsAndUserQuery(postIds, userId);
    }
}
