package com.ftm.server.application.query;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FindUserImagesByIdsQuery {
    List<Long> userIds;

    public static FindUserImagesByIdsQuery of(List<Long> userIds) {
        return new FindUserImagesByIdsQuery(userIds);
    }
}
