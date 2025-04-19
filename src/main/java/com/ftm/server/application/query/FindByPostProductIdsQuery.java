package com.ftm.server.application.query;

import java.util.List;
import lombok.Getter;

@Getter
public class FindByPostProductIdsQuery {

    private final List<Long> postProductIds;

    private FindByPostProductIdsQuery(List<Long> postProductIds) {
        this.postProductIds = postProductIds;
    }

    public static FindByPostProductIdsQuery of(List<Long> postProductIds) {
        return new FindByPostProductIdsQuery(postProductIds);
    }
}
