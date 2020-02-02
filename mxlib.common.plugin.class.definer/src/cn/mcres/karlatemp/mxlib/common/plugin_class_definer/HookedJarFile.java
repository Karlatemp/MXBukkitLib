/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: HookedJarFile.java@author: karlatemp@vip.qq.com: 2020/1/15 下午10:38@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.plugin_class_definer;

import cn.mcres.karlatemp.mxlib.event.Event;
import cn.mcres.karlatemp.mxlib.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class HookedJarFile extends JarFile {
    private static final Field ZipEntry$size;

    static {
        try {
            (ZipEntry$size = ZipEntry.class.getDeclaredField("size")).setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public interface InputStreamResolver {
        @NotNull InputStream resolve(@NotNull InputStreamSuppler stream) throws IOException;
    }

    public interface InputStreamSuppler {
        @NotNull InputStream get() throws IOException;
    }

    public static class JarStreamGetEvent extends Event {
        private final HandlerList<JarStreamGetEvent> handlers;
        private final ZipEntry path;
        private @NotNull InputStreamSuppler stream;

        public JarStreamGetEvent(
                @NotNull InputStreamSuppler stream,
                @NotNull HandlerList<JarStreamGetEvent> handlers,
                @NotNull ZipEntry path) {
            this.handlers = handlers;
            this.stream = stream;
            this.path = path;
        }

        public ZipEntry getPath() {
            return path;
        }

        public void stream(@NotNull InputStreamSuppler stream) {
            this.stream = stream;
        }

        public void resolve(@NotNull InputStreamResolver resolver) {
            var stream = stream();
            stream(() -> resolver.resolve(stream));
        }

        public @NotNull InputStreamSuppler stream() {
            return stream;
        }

        @Override
        public HandlerList<JarStreamGetEvent> getHandlerList() {
            return handlers;
        }

    }

    public HookedJarFile(String name) throws IOException {
        super(name);
    }

    public HookedJarFile(String name, boolean verify) throws IOException {
        super(name, verify);
    }

    public HookedJarFile(File file) throws IOException {
        super(file);
    }

    public HookedJarFile(File file, boolean verify) throws IOException {
        super(file, verify);
    }

    public HookedJarFile(File file, boolean verify, int mode) throws IOException {
        super(file, verify, mode);
    }

    public HookedJarFile(File file, boolean verify, int mode, Runtime.Version version) throws IOException {
        super(file, verify, mode, version);
    }

    private HandlerList<JarStreamGetEvent> handlers = new HandlerList<>();

    public HandlerList<JarStreamGetEvent> getJarStreamGetEventHandlers() {
        return handlers;
    }

    public void setJarStreamGetEventHandlers(HandlerList<JarStreamGetEvent> handlers) {
        this.handlers = handlers;
    }

    @Override
    public ZipEntry getEntry(String name) {
        var ent = super.getEntry(name);
        if (ent == null) return null;
        var cop = new JarEntry(ent);
        try {
            ZipEntry$size.setLong(cop, -1L);
        } catch (IllegalAccessException e) {
            return null;
        }
        return cop;
    }

    public synchronized InputStream getInputStream0(ZipEntry ze) throws IOException {
        return super.getInputStream(ze);
    }

    @Override
    public synchronized InputStream getInputStream(ZipEntry ze) throws IOException {
        return Event.post(new JarStreamGetEvent(() -> getInputStream0(ze), handlers, ze)).stream().get();
    }
}
