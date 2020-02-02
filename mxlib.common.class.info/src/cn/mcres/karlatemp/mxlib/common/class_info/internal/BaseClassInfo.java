/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: BaseClassInfo.java@author: karlatemp@vip.qq.com: 2020/1/13 下午9:37@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info.internal;

import cn.mcres.karlatemp.mxlib.common.class_info.ClassInfo;
import cn.mcres.karlatemp.mxlib.common.class_info.ClassPool;
import cn.mcres.karlatemp.mxlib.common.class_info.FieldInfo;
import cn.mcres.karlatemp.mxlib.common.class_info.MethodInfo;
import cn.mcres.karlatemp.mxlib.exceptions.MessageDump;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public abstract class BaseClassInfo extends BaseModifiable implements ClassInfo {
    protected ClassInfo arrayType;
    protected Deque<MethodInfo> methods = new ConcurrentLinkedDeque<>();
    protected Deque<FieldInfo> fields = new ConcurrentLinkedDeque<>();
    protected ClassInfo parent;

    @Override
    public Collection<FieldInfo> getFields() {
        return fields;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public Collection<MethodInfo> getMethods() {
        return methods;
    }

    @Override
    public MethodInfo getMethod(String name, ClassInfo returnType, Collection<ClassInfo> arguments) {
        root:
        for (MethodInfo method : getMethods()) {
            if (method.getName().equals(name)) {
                if (method.getReturnType().equals(returnType)) {
                    final List<ClassInfo> parameterTypes = method.getParameterTypes();
                    try {
                        if (parameterTypes.size() == arguments.size()) {
                            if (arguments.isEmpty()) return method;
                            var iterator = parameterTypes.iterator();
                            for (ClassInfo argument : arguments) {
                                if (!iterator.next().equals(argument)) {
                                    continue root;
                                }
                            }
                            return method;
                        }
                    } catch (Throwable a) {
                        a.addSuppressed(MessageDump.create("-> name:" + name));
                        a.addSuppressed(MessageDump.create("-> scan pt:" + parameterTypes));
                        a.addSuppressed(MessageDump.create("-> arguments:" + arguments));
                        a.addSuppressed(MessageDump.create("-> " + method));
                        throw a;
                    }
                }
            }
        }
        if (parent != null) return parent.getMethod(name, returnType, arguments);
        return null;
    }

    @Override
    public MethodInfo getMethod(String name, ClassInfo returnType, ClassInfo... arguments) {
        root:
        for (MethodInfo method : getMethods()) {
            if (method.getName().equals(name)) {
                if (method.getReturnType().equals(returnType)) {
                    final List<ClassInfo> parameterTypes = method.getParameterTypes();
                    if (parameterTypes.size() == arguments.length) {
                        if (arguments.length == 0) return method;
                        var iterator = parameterTypes.iterator();
                        for (ClassInfo argument : arguments) {
                            if (!iterator.next().equals(argument)) {
                                continue root;
                            }
                        }
                        return method;
                    }
                }
            }
        }
        if (parent != null) return parent.getMethod(name, returnType, arguments);
        return null;
    }

    @Override
    public FieldInfo getField(String name, ClassInfo type) {
        for (var field : getFields()) {
            if (field.getName().equals(name)) {
                if (field.getType().equals(type)) {
                    return field;
                }
            }
        }
        if (parent != null) return parent.getField(name, type);
        return null;
    }

    @Override
    public @NotNull ClassInfo array() {
        if (arrayType == null) {
            return arrayType = new ArrayClassInfo(this);
        }
        return arrayType;
    }

    @Override
    public ClassInfo parent() {
        return parent;
    }

    @Override
    public ClassInfo component() {
        return null;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        int md = this.modifier();
        if ((md & Opcodes.ACC_PUBLIC) != 0)
            sb.append("public ");
        if ((md & Opcodes.ACC_ABSTRACT) != 0)
            sb.append("abstract ");
        if ((md & Opcodes.ACC_ANNOTATION) != 0)
            sb.append("@interface ");
        else if ((md & Opcodes.ACC_INTERFACE) != 0)
            sb.append("interface ");
        else if ((md & Opcodes.ACC_ENUM) != 0)
            sb.append("enum ");
        else sb.append("class ");
        sb.append(getJavaName());
        return sb.toString();
    }

    protected boolean initialized = false;

    public synchronized void initialize(ClassPool pool) {
        if (initialized) throw new IllegalAccessError();
        initialize0(pool);
        initialized = true;
    }

    protected void initialize0(ClassPool pool) {
    }
}
