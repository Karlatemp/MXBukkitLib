/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ClassesInfoPreChecker.java@author: karlatemp@vip.qq.com: 19-12-22 下午12:36@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.sr;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.List;
import java.util.logging.Level;

public class ClassesInfoPreChecker extends ClassVisitor {
    private final ClassesInfo ci;
    private String className;

    public ClassesInfoPreChecker(int api, ClassesInfo ci) {
        super(api);
        this.ci = ci;
    }

    public ClassesInfoPreChecker(int api, ClassVisitor classVisitor, ClassesInfo ci) {
        super(api, classVisitor);
        this.ci = ci;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        final ClassesInfo.ClassInfo info = ci.classes.get(className);
        if (info != null) {
            if (info.fields.containsKey(name)) {
                if (!(value instanceof String)) {
                    Logging.log("Class Pre check", Level.WARNING, "class.field.type.changed", className, name, descriptor, Logging.parseAccess(access));
                }
            }
        }
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        final ClassesInfo.ClassInfo info = ci.classes.get(className);
        if (info != null) {
            final List<String> method = info.methods.get(name + descriptor);
            if (method != null) {
                return new MethodVisitor(api, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                    private int count;

                    @Override
                    public void visitLdcInsn(Object value) {
                        if (value instanceof String) {
                            count++;
                        }
                        super.visitLdcInsn(value);
                    }

                    @Override
                    public void visitEnd() {
                        if (count != method.size()) {
                            Logging.log("Class Pre check", Level.WARNING, "class.method.size.not.match", className, name, descriptor, count, method.size());
                            info.methods.remove(name + descriptor);
                        }
                        super.visitEnd();
                    }
                };
            }
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
