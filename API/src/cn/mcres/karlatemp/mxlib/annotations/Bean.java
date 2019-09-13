package cn.mcres.karlatemp.mxlib.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Bean {
}
