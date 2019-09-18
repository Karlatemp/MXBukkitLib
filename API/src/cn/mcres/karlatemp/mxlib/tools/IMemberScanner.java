package cn.mcres.karlatemp.mxlib.tools;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 类成员搜索器, 在BeanManager获取
 */
public interface IMemberScanner {
    @NotNull
    Collection<Method> getAllMethod(@NotNull Class c);

    @NotNull
    Collection<Method> getMethodByAnnotation(@NotNull Class c, @NotNull Class<? extends Annotation> ann);

    @NotNull
    Collection<Field> getAllField(@NotNull Class c);

    @NotNull
    Collection<Field> getFieldByAnnotation(@NotNull Class c, @NotNull Class<? extends Annotation> ann);
}
