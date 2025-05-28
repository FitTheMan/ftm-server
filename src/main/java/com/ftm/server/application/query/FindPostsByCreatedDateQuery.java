package com.ftm.server.application.query;

import java.time.LocalDate;
import lombok.Data;

@Data
public class FindPostsByCreatedDateQuery {
    private final LocalDate date;

    public static FindPostsByCreatedDateQuery of(LocalDate date) {
        return new FindPostsByCreatedDateQuery(date);
    }
}
