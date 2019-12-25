/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DependChecker.java@author: karlatemp@vip.qq.com: 19-9-27 下午12:29@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.annotations.Depend;
import javassist.bytecode.annotation.*;
import org.jetbrains.annotations.Contract;

import java.util.Optional;

/**
 * Use in Depend System.
 *
 * @see Depend
 * @since 2.2
 */
public interface DependChecker {
    @Contract(value = "null -> false", pure = true)
    boolean isLoaded(Depend depend);

    @Contract(value = "null -> null; !null -> !null", pure = true)
    static Depend fromAnnotation(Annotation a) {
        if (a == null) return null;
        class VV implements MemberValueVisitor {
            @Override
            public void visitAnnotationMemberValue(AnnotationMemberValue node) {
            }

            @Override
            public void visitArrayMemberValue(ArrayMemberValue node) {
            }

            @Override
            public void visitBooleanMemberValue(BooleanMemberValue node) {
            }

            @Override
            public void visitByteMemberValue(ByteMemberValue node) {
            }

            @Override
            public void visitCharMemberValue(CharMemberValue node) {
            }

            @Override
            public void visitDoubleMemberValue(DoubleMemberValue node) {
            }

            @Override
            public void visitEnumMemberValue(EnumMemberValue node) {
            }

            @Override
            public void visitFloatMemberValue(FloatMemberValue node) {
            }

            @Override
            public void visitIntegerMemberValue(IntegerMemberValue node) {
            }

            @Override
            public void visitLongMemberValue(LongMemberValue node) {
            }

            @Override
            public void visitShortMemberValue(ShortMemberValue node) {
            }

            @Override
            public void visitStringMemberValue(StringMemberValue node) {
            }

            @Override
            public void visitClassMemberValue(ClassMemberValue node) {
            }
        }
        Pointer<String> value = new Pointer<>(), version = new Pointer<>("");
        Pointer<Integer> compare = new Pointer<>(0);
        {
            Optional.ofNullable(a.getMemberValue("value")).ifPresent(x -> x.accept(new VV() {
                @Override
                public void visitStringMemberValue(StringMemberValue node) {
                    value.value(node.getValue());
                }
            }));
            Optional.ofNullable(a.getMemberValue("version")).ifPresent(x -> x.accept(new VV() {
                @Override
                public void visitStringMemberValue(StringMemberValue node) {
                    version.value(node.getValue());
                }
            }));
            Optional.ofNullable(a.getMemberValue("compare")).ifPresent(x -> x.accept(new VV() {
                @Override
                public void visitIntegerMemberValue(IntegerMemberValue node) {
                    compare.value(node.getValue());
                }
            }));
        }
        return new Depend() {

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return Depend.class;
            }

            @Override
            public String value() {
                return value.value();
            }

            @Override
            public String version() {
                return version.value();
            }

            @Override
            public int compare() {
                return compare.value();
            }

            @Override
            public String toString() {
                return Depend.class.getName() + "(value=" + value() + ", version=" + version() + ", compare=" + compare() + ")";
            }
        };
    }
}
