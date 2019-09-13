package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.tools.IMemberScanner;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

public class SharedMemberScanner implements IMemberScanner {
    private <T> Collection<T> col() {
        return new ArrayList<>();
    }

    private <T extends AccessibleObject> Collection<T> open(Collection<T> mem) {
        mem.forEach(m -> m.setAccessible(true));
        return mem;
    }

    private Collection<Method> filter(Collection<Method> f) {
        class $ {
            String met;
            Class[] param;
            int pc;
            Class ret;
            Method mtt;
            boolean st;
        }
        Collection<$> m = col();
        aw:
        for (Method mx : f) {
            Class[] pm = mx.getParameterTypes();
            $ nw = new $();
            nw.met = mx.getName();
            nw.param = pm;
            nw.ret = mx.getReturnType();
            nw.pc = mx.getParameterCount();
            nw.mtt = mx;
            nw.st = false;
            if (Modifier.isStatic(mx.getModifiers()) || Modifier.isPrivate(mx.getModifiers())) {
                nw.st = true;
                m.add(nw);
                continue;
            }
            for ($ x : m) {
                if (!x.st)
                    if (x.pc == mx.getParameterCount()) {
                        if (x.met.equals(mx.getName())) {
                            if (Arrays.equals(x.param, pm)) {
                                if (x.ret.isAssignableFrom(mx.getReturnType())) {
                                    x.mtt = mx;
                                }
                                continue aw;
                            }
                        }
                    }
            }
            m.add(nw);
        }
        return m.stream().map($ -> $.mtt).collect(Collectors.toList());
    }


    @NotNull
    @Override
    public Collection<Method> getAllMethod(@NotNull Class c) {
        Collection<Method> ref = col();
        if (c.isArray()) {
            return ref;
        }
        while (c != null) {
            ref.addAll(Arrays.asList(c.getDeclaredMethods()));
            c = c.getSuperclass();
        }
        return open(filter(ref));
    }

    @NotNull
    @Override
    public Collection<Method> getMethodByAnnotation(@NotNull Class c, @NotNull Class<? extends Annotation> ann) {
        Collection<Method> ref = getAllMethod(c);
        ref.removeIf(m -> m.getAnnotation(ann) == null);
        return ref;
    }

    @NotNull
    @Override
    public Collection<Field> getAllField(@NotNull Class c) {
        Collection<Field> ref = col();
        if (c.isArray()) {
            return ref;
        }
        while (c != null) {
            ref.addAll(Arrays.asList(c.getDeclaredFields()));
            c = c.getSuperclass();
        }
        return open(ref);
    }

    @NotNull
    @Override
    public Collection<Field> getFieldByAnnotation(@NotNull Class c, @NotNull Class<? extends Annotation> ann) {
        Collection<Field> ref = getAllField(c);
        ref.removeIf(m -> m.getAnnotation(ann) == null);
        return ref;
    }
}
