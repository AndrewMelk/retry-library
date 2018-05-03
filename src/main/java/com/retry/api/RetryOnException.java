package com.retry.api;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RetryOnException {

    int attempts() default 3;
    long delay() default 50L;

    Class<? extends Throwable>[] types() default {Throwable.class};

}
