package com.ftm.server.application.query;

import com.ftm.server.domain.enums.UserRole;
import lombok.Data;

@Data
public class FindUserByRoleQuery {
    private final UserRole userRole;

    public static FindUserByRoleQuery of(UserRole userRole) {
        return new FindUserByRoleQuery(userRole);
    }
}
