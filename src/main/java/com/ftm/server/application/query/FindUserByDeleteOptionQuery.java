package com.ftm.server.application.query;

import java.time.LocalDate;
import lombok.Data;

@Data
public class FindUserByDeleteOptionQuery {
    private final Boolean isDeleted;
    private final LocalDate deletedAt;

    public static FindUserByDeleteOptionQuery of(Boolean isDeleted, LocalDate deletedAt) {
        return new FindUserByDeleteOptionQuery(isDeleted, deletedAt);
    }
}
