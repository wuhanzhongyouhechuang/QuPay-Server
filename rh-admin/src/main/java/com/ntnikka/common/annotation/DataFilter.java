
package com.ntnikka.common.annotation;

import java.lang.annotation.*;

/**
 * 数据过滤
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataFilter {
    /**
     * 表的别名
     */
    String tableAlias() default "";

    /**
     * true：没有本部门数据权限，也能查询本人数据
     */
    boolean user() default true;

    /**
     * true：拥有子部门数据权限
     */
    boolean subDept() default false;
}
