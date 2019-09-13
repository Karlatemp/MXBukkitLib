package cn.mcres.karlatemp.mxlib.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface Resource {
    /**
     * 设置需要的Bean
     */
    Class value() default Object.class;
}
