package com.ftm.server.common.utils;

import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;

public class CollectionUtils {

    public static <T> List<T> safeList(List<T> list) {
        return list != null ? list : List.of();
    }

    public static <T, R> List<R> mapOrEmpty(List<T> list, Function<T, R> mapper) {
        return list != null ? list.stream().map(mapper).toList() : List.of();
    }

    public static <T> T[] listToArrayOrNull(List<T> list, IntFunction<T[]> arrayConstructor) {
        return list != null ? list.toArray(arrayConstructor.apply(0)) : null;
    }
}
