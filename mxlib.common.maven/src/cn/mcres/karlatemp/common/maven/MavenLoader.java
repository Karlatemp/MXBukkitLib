/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: MavenLoader.java@author: karlatemp@vip.qq.com: 2020/1/31 下午4:14@version: 2.0
 */

package cn.mcres.karlatemp.common.maven;

import cn.mcres.karlatemp.mxlib.reflect.MagicAccessorMarkerLoader;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MavenLoader {
    private final Consumer<URL> URLRegisterer;
    private static final BiConsumer<Object, Object> URLClassLoaderRegisterer;
    public Logger logger;

    static {
        MagicAccessorMarkerLoader.load();
        ClassWriter writer = new ClassWriter(0);
        writer.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "cn/mcres/karlatemp/common/maven/" + UUID.randomUUID(),
                null, "cn/mcres/karlatemp/mxlib/reflect/MagicAccessorMarker", new String[]{
                        "java/util/function/BiConsumer"
                });
        var met = writer.visitMethod(Opcodes.ACC_PUBLIC, "accept", "(Ljava/lang/Object;Ljava/lang/Object;)V", null, null);
        met.visitVarInsn(Opcodes.ALOAD, 1);
        met.visitTypeInsn(Opcodes.CHECKCAST, "java/net/URLClassLoader");
        met.visitVarInsn(Opcodes.ALOAD, 2);
        met.visitTypeInsn(Opcodes.CHECKCAST, "java/net/URL");
        met.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/net/URLClassLoader", "addURL", "(Ljava/net/URL;)V", false);
        met.visitInsn(Opcodes.RETURN);
        met.visitMaxs(2, 3);
        URLClassLoaderRegisterer = Toolkit.Reflection.allocObject(Toolkit.Reflection.defineClass(MavenLoader.class.getClassLoader(), writer, null).asSubclass(BiConsumer.class));
    }

    public static Consumer<URL> URLClassLoaderAddURL(@NotNull URLClassLoader loader) {
        return url -> URLClassLoaderRegisterer.accept(loader, url);
    }

    private ConcurrentLinkedQueue<MavenRepo> repos = new ConcurrentLinkedQueue<>();

    public MavenLoader(@NotNull Consumer<URL> registerer) {
        this.URLRegisterer = registerer;
    }

    public synchronized void append(MavenRepo repo) {
        if (repo == null) return;
        if (repos.contains(repo)) {
            return;
        }
        repos.add(repo);
        if (logger != null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "Loading " + repo.inline());
            }
        }
        if (repo.location != null)
            URLRegisterer.accept(repo.location);
        if (repo.depends != null)
            for (var dep : repo.depends) {
                append(dep);
            }
    }
}
