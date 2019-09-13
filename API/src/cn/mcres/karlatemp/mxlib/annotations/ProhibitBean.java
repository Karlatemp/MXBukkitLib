package cn.mcres.karlatemp.mxlib.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface ProhibitBean {
    ProhibitType value() default ProhibitType.ALL_WITH_SUBCLASS;
}
