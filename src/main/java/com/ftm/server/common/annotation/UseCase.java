package com.ftm.server.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** UseCase 어노테이션 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseCase {}
