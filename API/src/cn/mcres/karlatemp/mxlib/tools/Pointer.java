package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;

import java.lang.ref.Reference;
import java.util.function.Supplier;
import java.util.function.Consumer;
import java.util.function.Function;

@ProhibitBean
public class Pointer<T> implements Supplier<T>, Consumer<T>, Function<T, T> {
    private T value;

    public T value() {
        return value;
    }

    public T value(T val) {
        value = val;
        return val;
    }

    public Pointer() {
        this(null);
    }

    public Pointer(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void accept(T t) {
        value = t;
    }

    @Override
    public T apply(T t) {
        T old = value;
        value = t;
        return old;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
