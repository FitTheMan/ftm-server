package com.ftm.server.application.vo.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserIdAndNameVo {
    Long userId;
    String authorName;
}
