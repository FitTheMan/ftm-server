package com.ftm.server.application.query;

import java.util.List;
import lombok.Getter;

@Getter
public class FindByIdsQuery {

    private final List<Long> ids;

    private FindByIdsQuery(List<Long> ids) {
        this.ids = ids;
    }

    public static FindByIdsQuery from(List<Long> ids) {
        return new FindByIdsQuery(ids);
    }
}
