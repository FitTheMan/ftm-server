package com.ftm.server.application.query;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class FindPostByDeleteOptionQuery {

    private final Boolean isDeleted;
    private final LocalDate deletedAt;

    private FindPostByDeleteOptionQuery(Boolean isDeleted, LocalDate deletedAt) {
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
    }

    public static FindPostByDeleteOptionQuery of(Boolean isDeleted, LocalDate deletedAt) {
        return new FindPostByDeleteOptionQuery(isDeleted, deletedAt);
    }
}
