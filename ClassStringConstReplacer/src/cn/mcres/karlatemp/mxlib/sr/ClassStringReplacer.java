/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ClassStringReplacer.java@author: karlatemp@vip.qq.com: 19-12-20 下午11:34@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.sr;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public class ClassStringReplacer extends ClassVisitor {
    private final StringReplacer replacer;
    private String className;

    public ClassStringReplacer(int api, StringReplacer replacer) {
        super(api);
        this.replacer = replacer;
    }

    public ClassStringReplacer(int api, ClassVisitor classVisitor, StringReplacer replacer) {
        super(api, classVisitor);
        this.replacer = replacer;
    }

    @NotNull
    private Object w(String a, Object b) {
        if (a == null) return b;
        return a;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (value instanceof String) {
            return super.visitField(access, name, descriptor, signature,
                    w(replacer.replaceField(className, name, (String) value), value));
        }
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return new MethodVisitor(api, super.visitMethod(access, name, descriptor, signature, exceptions)) {
            private int index;

            @Override
            public void visitLdcInsn(Object value) {
                if (value instanceof String) {
                    value = w(replacer.replaceMethod(className, name, descriptor, (String) value, index++), value);
                }
                super.visitLdcInsn(value);
            }
        };
    }
}
