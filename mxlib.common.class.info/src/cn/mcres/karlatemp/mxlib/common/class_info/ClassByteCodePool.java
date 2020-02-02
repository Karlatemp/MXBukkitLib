/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: ClassByteCodePool.java@author: karlatemp@vip.qq.com: 2020/1/18 下午9:00@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info;

import cn.mcres.karlatemp.mxlib.common.class_info.internal.*;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipFile;

public class ClassByteCodePool extends SimpleClassPool {
    public static ClassByteCodePool of(ClassPool parent, Object o) {
        if (o instanceof ClassByteCodePool) return (ClassByteCodePool) o;
        if (o instanceof ClassLoader) {
            var cl = (ClassLoader) o;
            return new ClassByteCodePool(parent, cn -> {
                var res = cl.getResourceAsStream(cn + ".class");
                if (res == null) {
                    return null;
                }
                try (res) {
                    var bos = new ByteArrayOutputStream();
                    Toolkit.IO.writeTo(res, bos);
                    return bos.toByteArray();
                } catch (IOException ignore) {
                }
                return null;
            });
        }
        if (o instanceof ZipFile) {
            var zip = (ZipFile) o;
            return new ClassByteCodePool(parent, cn -> {
                var entry = zip.getEntry(cn + ".class");
                if (entry != null) {
                    try (var stream = zip.getInputStream(entry)) {
                        var bos = new ByteArrayOutputStream();
                        Toolkit.IO.writeTo(stream, bos);
                        return bos.toByteArray();
                    } catch (IOException ignore) {
                    }
                }
                return null;
            });
        }
        throw new UnsupportedOperationException();
    }

    private final Function<String, byte[]> codeLoader;

    public ClassByteCodePool(ClassPool parent, Function<String, byte[]> codeLoader) {
        super(parent);
        this.codeLoader = codeLoader;
    }

    public ClassByteCodePool(Function<String, byte[]> codeLoader) {
        this(null, codeLoader);
    }

    @Override
    protected ClassInfo findClass(String name) {
        return findInternalClass(name.replace('.', '/'));
    }

    @Override
    protected ClassInfo findInternalClass(String name) {
        final byte[] code = codeLoader.apply(name);
        if (code == null) {
            return defineFakeClass(name);
        }
        ClassReader cr = new ClassReader(code);
        if (!cr.getClassName().equals(name)) {
            return defineFakeClass(name);
        }
        ClassNode node = new ClassNode();
        cr.accept(node, 0);
        return defineClass(new SimpleRenameableClassInfo(null, name, node.access) {
            @Override
            protected void initialize0(ClassPool pool) {
                var p = parent = getInternalClass(node.superName);
                for (var met : node.methods) {
                    ClassInfo ret = getInternalClass(Type.getReturnType(met.desc).getInternalName());
                    var par = Stream.of(Type.getArgumentTypes(met.desc)).map(Type::getInternalName).map(ClassByteCodePool.this::getInternalClass)
                            .collect(Collectors.toList());
                    var ov = p.getMethod(met.name, ret, par);
                    if (ov != null) {
                        methods.add(new OverrideMethodInfo(this, ov, met.access, true));
                    } else {
                        methods.add(new RenameableMethodInfo(
                                met.name, this, ret, par, met.access, true
                        ));
                    }
                }
                for (var fie : node.fields) {
                    fields.add(new RenameableFieldInfo(fie.name, getInternalClass(Type.getType(fie.desc).getInternalName()), this, fie.access, true));
                }
            }
        });
    }

    private ClassInfo defineFakeClass(String name) {
        return defineClass(new SimpleClassInfo(null, name, 0));
    }
}
