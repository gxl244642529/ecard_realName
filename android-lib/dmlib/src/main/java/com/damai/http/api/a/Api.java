package com.damai.http.api.a;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.citywithincity.models.cache.CachePolicy;
import com.damai.core.Crypt;

/**
 * api的指示器
 * @author Randy
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Api {

    public static final int Array = 0;
    public static final int Page = 1;
    public static final int Object = 2;

    /**
     * api名称
     * @return
     */
    String name();

    /**
     * 接口加密方式
     * @return
     */
    int crypt() default Crypt.NONE;

    /**
     * 服务器
     * @return
     */
    int server() default 0;

    /**
     * 实体类
     * @return
     */
    Class<?> entity();

    /**
     * 类型
     * @return
     */
    int type() default Array;

    /**
     * 缓存策略
     * @return
     */
    CachePolicy cachePolicy() default CachePolicy.CachePolity_NoCache;

    /**
     * 参数名称列表
     * 如果type==Page，第一个参数必须为position
     *
     *
     * @return
     */
    String[] params() default {};

}
