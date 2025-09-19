package com.ftm.server.application.vo.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookmarkYnWrapperVo<T> {
    Boolean bookmarkYn;
    T data;
}
