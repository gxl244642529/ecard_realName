package com.damai.http.api.a;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JobError {
    /**
     * 指定哪个api执行成功了
     * @return
     */
    String[] value();
}
