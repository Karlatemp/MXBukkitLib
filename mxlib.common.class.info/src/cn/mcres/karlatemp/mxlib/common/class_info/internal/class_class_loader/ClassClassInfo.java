/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: ClassClassInfo.java@author: karlatemp@vip.qq.com: 2020/1/13 下午9:25@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info.internal.class_class_loader;

import cn.mcres.karlatemp.mxlib.common.class_info.ClassInfo;
import cn.mcres.karlatemp.mxlib.common.class_info.ClassLoaderPool;
import cn.mcres.karlatemp.mxlib.common.class_info.ClassPool;
import cn.mcres.karlatemp.mxlib.common.class_info.MethodInfo;
import cn.mcres.karlatemp.mxlib.common.class_info.internal.BaseClassInfo;
import cn.mcres.karlatemp.mxlib.common.class_info.internal.BaseFieldInfo;
import cn.mcres.karlatemp.mxlib.common.class_info.internal.BaseMethodInfo;
import cn.mcres.karlatemp.mxlib.common.class_info.internal.OverrideMethodInfo;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassClassInfo extends BaseClassInfo {
    public final Class<?> target;
    private static final Throwable A = new Throwable();

    protected void initialize0(ClassPool loader) {
        modifier = target.getModifiers();
        if (loader == null || isPrimitive()) return;
        try {
            String name = target.getName();
            int a = name.indexOf('/');// Lambda
            if (a != -1) {
                name = name.substring(0, a);
            }
            a = name.indexOf('.');
            if (a != -1) {
                name = name.substring(a + 1);
            }
            var resource = target.getResourceAsStream(name + ".class");
            if (resource == null) {
                throw A;
            } else {
                ClassReader reader;
                try (resource) {
                    reader = new ClassReader(resource);
                }
                ClassNode node = new ClassNode();
                reader.accept(node, ClassReader.EXPAND_FRAMES);
                if (!node.name.equals("java/lang/Object")) {
                    this.parent = loader.getClass(node.superName);
                    if (node.methods != null) {
                        for (var met : node.methods) {
                            ClassInfo Return = loader.getClass(Type.getReturnType(met.desc).getInternalName());
                            var params = Stream.of(
                                    Type.getArgumentTypes(met.desc)
                            ).map(Type::getInternalName)
                                    .map(loader::getClass)
                                    .collect(Collectors.toList());
                            var mname = met.name;
                            if ((node.access & Opcodes.ACC_STATIC) == 0) {
                                var mtt = parent.getMethod(mname, Return, params);
                                if (mtt != null) {
                                    if ((mtt.modifier() & Opcodes.ACC_PRIVATE) == 0) {
                                        methods.add(new OverrideMethodInfo(
                                                this, mtt, node.access, false
                                        ));
                                        continue;
                                    }
                                }
                            }
                            methods.add(new BaseMethodInfo(name, this, Return, params, node.access, false));
                        }
                    }
                } else {
                    for (var method : node.methods) {
                        this.methods.add(
                                new BaseMethodInfo(method.name, this,
                                        loader.getClass(Type.getReturnType(method.desc).getInternalName()),
                                        Stream.of(
                                                Type.getArgumentTypes(method.desc)
                                        ).map(Type::getInternalName)
                                                .map(loader::getClass)
                                                .collect(Collectors.toList()),
                                        method.access, false
                                )
                        );
                    }
                }
                if (node.fields != null)
                    for (var field : node.fields) {
                        fields.add(new BaseFieldInfo(field.name, loader.getClass(Type.getType(field.desc).getInternalName()), this, field.access, false));
                    }
            }
        } catch (Throwable throwable) {
            final Class<?> superC = target.getSuperclass();
            if (superC != null) {
                this.parent = loader.getClass(superC.getName());
            }
            for (Method method : target.getDeclaredMethods()) {
                String name = method.getName();
                ClassInfo ret = loader.getClass(method.getReturnType().getName());
                var param = Stream.of(method.getParameterTypes()).map(c -> loader.getClass(c.getName())).collect(Collectors.toList());
                if (!Modifier.isStatic(method.getModifiers()))
                    if (parent != null) {
                        var sup = parent.getMethod(name, ret, param);
                        if (sup != null) {
                            methods.add(new OverrideMethodInfo(this, sup, method.getModifiers(), false));
                            continue;
                        }
                    }
                methods.add(new BaseMethodInfo(name, this, ret, param, method.getModifiers(), false));
            }
        }
    }

    public ClassClassInfo(Class<?> target) {
        this.target = target;
    }

    @Override
    public String getJavaName() {
        return target.getName();
    }

    @Override
    public String getInternalName() {
        return target.getName().replace('.', '/');
    }

    @Override
    public String getRenamedJavaName() {
        return getJavaName();
    }

    @Override
    public String getRenamedInternalName() {
        return getInternalName();
    }

    @Override
    public boolean isSupportRename() {
        return false;
    }

    @Override
    public boolean rename(String internalName) {
        return false;
    }
}
