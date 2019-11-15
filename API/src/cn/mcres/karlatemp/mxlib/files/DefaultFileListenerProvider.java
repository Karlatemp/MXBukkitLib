/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: FileListenerProvider.java@author: karlatemp@vip.qq.com: 19-11-15 下午5:37@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.files;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.util.MapKeySet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link FileListenerProvider}'s default provider
 */
public class DefaultFileListenerProvider implements FileListenerProvider {
    public static final Supplier<Map<Path, ?>> DEFAULT_MAP_CONSTRUCTOR = ConcurrentHashMap::new;
    public static final Supplier<Set<?>> DEFAULT_SET_CONSTRUCTOR = () -> new MapKeySet<>(new ConcurrentHashMap<>());

    private final Map<Path, Set<FileListener>> listeners;
    private final Supplier<Set<?>> ss;
    private final Map<Path, FileTime> exists;
    private final Map<Path, Set<Path>> subModifys;
    private final Map<Path, Long> sizes;
    private final Consumer<Throwable> errorCatch;
    private final Object removeToken;

    public DefaultFileListenerProvider(
            @NotNull Supplier<Map<Path, ?>> mapSupplier,
            @NotNull Supplier<Set<?>> setSupplier,
            @Nullable Consumer<Throwable> errorCatch) {
        this(mapSupplier, setSupplier, errorCatch, null);
    }

    @SuppressWarnings("unchecked")
    public DefaultFileListenerProvider(
            @NotNull Supplier<Map<Path, ?>> mapSupplier,
            @NotNull Supplier<Set<?>> setSupplier,
            @Nullable Consumer<Throwable> errorCatch,
            Object removeToken) {
        this.removeToken = removeToken;
        listeners = (Map<Path, Set<FileListener>>) mapSupplier.get();
        exists = (Map<Path, FileTime>) mapSupplier.get();
        sizes = (Map<Path, Long>) mapSupplier.get();
        subModifys = (Map<Path, Set<Path>>) mapSupplier.get();
        //noinspection RedundantCast
        if ((Object) listeners == (Object) exists || (Object) exists == (Object) sizes ||
                (Object) subModifys == (Object) listeners) {
            throw new IllegalArgumentException("mapSupplier not a constructor.");
        }
        this.ss = setSupplier;
        if (errorCatch == null)
            this.errorCatch = e -> MXBukkitLib.getLogger().printStackTrace(e);
        else
            this.errorCatch = errorCatch;
    }

    public DefaultFileListenerProvider() {
        this(DEFAULT_MAP_CONSTRUCTOR, null);
    }

    public DefaultFileListenerProvider(Consumer<Throwable> errorCatch) {
        this(ConcurrentHashMap::new, errorCatch);
    }

    public DefaultFileListenerProvider(@NotNull Supplier<Map<Path, ?>> mapSupplier) {
        this(mapSupplier, null);
    }

    public DefaultFileListenerProvider(
            @NotNull Supplier<Map<Path, ?>> mapSupplier,
            @Nullable Consumer<Throwable> errorCatch) {
        this(mapSupplier, DEFAULT_SET_CONSTRUCTOR, errorCatch);
    }

    @SuppressWarnings({"Duplicates", "unchecked"})
    public synchronized void register(@NotNull Path path, @NotNull FileListener listener) {
        if (listeners.containsKey(path)) {
            listeners.get(path).add(listener);
        } else {
            if (Files.exists(path)) {
                try {
                    exists.put(path, Files.getLastModifiedTime(path));
                } catch (Throwable e) {
                    errorCatch.accept(e);
                }
                if (!Files.isDirectory(path)) {
                    try {
                        sizes.put(path, Files.size(path));
                    } catch (Throwable e) {
                        errorCatch.accept(e);
                    }
                }
                try {
                    Set<Path> pps = (Set<Path>) ss.get();
                    subModifys.put(path, pps);
                    Files.walk(path, 1).forEach(pps::add);
                } catch (Throwable e) {
                    errorCatch.accept(e);
                }
            }
            Set<FileListener> st = (Set<FileListener>) ss.get();
            st.add(listener);
            listeners.put(path, st);
        }
    }

    private synchronized void remove(@NotNull Path path, boolean doListeners) {
        if (doListeners) {
            listeners.remove(path);
        }
        exists.remove(path);
        subModifys.remove(path);
    }

    public synchronized boolean unregister(@Nullable Path path, @NotNull FileListener listener) {
        boolean removed = false;
        if (path == null) {
            for (Set<FileListener> fl : listeners.values()) {
                if (fl.remove(listener))
                    removed = true;
            }
            final Iterator<Map.Entry<Path, Set<FileListener>>> iterator = listeners.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Path, Set<FileListener>> entry = iterator.next();
                if (entry != null) {
                    final Path key = entry.getKey();
                    final Set<FileListener> value = entry.getValue();
                    if (value != null) {
                        if (value.isEmpty()) {
                            iterator.remove();
                        }
                        if (key != null) {
                            remove(key, false);
                        }
                    }
                }
            }
        } else {
            final Set<FileListener> set = this.listeners.get(path);
            if (set != null) {
                if (set.remove(listener)) {
                    if (set.isEmpty()) {
                        remove(path, true);
                    }
                    return true;
                }
            }
        }
        return removed;
    }

    @SuppressWarnings("unchecked")
    public void doTick() {
        final Iterator<Map.Entry<Path, Set<FileListener>>> iterator = listeners.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Path, Set<FileListener>> entry = iterator.next();
            Path p = entry.getKey();
            Set<FileListener> listeners = entry.getValue();
            if (p != null && listeners != null && !listeners.isEmpty()) {
                try {
                    if (!Files.exists(p)) {
                        if (exists.containsKey(p)) {
                            exists.remove(p);
                            subModifys.remove(p);
                            sizes.remove(p);
                            for (FileListener fl : listeners) {
                                try {
                                    fl.onDelete(p);
                                } catch (Throwable thr) {
                                    errorCatch.accept(thr);
                                }
                            }
                        }
                    } else {
                        final FileTime modifiedTime = Files.getLastModifiedTime(p);
                        final FileTime oldTime = exists.get(p);
                        final boolean isDir = Files.isDirectory(p);
                        if (oldTime == null) {
                            exists.put(p, Files.getLastModifiedTime(p));
                            sizes.put(p, 0L);
                            if (isDir)
                                for (FileListener fl : listeners) {
                                    try {
                                        fl.onDirCreate(p);
                                    } catch (Throwable thr) {
                                        errorCatch.accept(thr);
                                    }
                                }
                            else for (FileListener fl : listeners) {
                                try {
                                    fl.onFileCreate(p);
                                } catch (Throwable thr) {
                                    errorCatch.accept(thr);
                                }
                            }
                        } else if (!modifiedTime.equals(oldTime)) {
                            exists.put(p, modifiedTime);
                            if (!isDir) {
                                long osize = sizes.get(p);
                                long nsize = Files.size(p);
                                sizes.put(p, nsize);
                                for (FileListener fl : listeners) {
                                    try {
                                        fl.onFileChange(p, osize, nsize);
                                    } catch (Throwable thr) {
                                        errorCatch.accept(thr);
                                    }
                                }
                            }
                        }
                        if (isDir) {
                            final Set<Path> subExists;
                            if (!subModifys.containsKey(p)) {
                                subModifys.put(p, subExists = (Set<Path>) ss.get());
                            } else {
                                subExists = subModifys.get(p);
                            }
                            Files.walk(p, 1).filter(w -> !w.equals(p)).forEach(w -> {
                                if (!subExists.contains(w)) {
                                    subExists.add(w);
                                    boolean id = Files.isDirectory(w);
                                    if (id) {
                                        for (FileListener fl : listeners) {
                                            try {
                                                fl.onSubDirCreate(p, w);
                                            } catch (Throwable thr) {
                                                errorCatch.accept(thr);
                                            }
                                        }
                                    } else {
                                        for (FileListener fl : listeners) {
                                            try {
                                                fl.onSubFileCreate(p, w);
                                            } catch (Throwable thr) {
                                                errorCatch.accept(thr);
                                            }
                                        }
                                    }
                                }
                            });
                            final Iterator<Path> pathIterator = subExists.iterator();
                            while (pathIterator.hasNext()) {
                                final Path sub = pathIterator.next();
                                if (!Files.exists(sub)) {
                                    pathIterator.remove();
                                    for (FileListener fl : listeners) {
                                        try {
                                            fl.onSubFileDelete(p, sub);
                                        } catch (Throwable thr) {
                                            errorCatch.accept(thr);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Throwable e) {
                    errorCatch.accept(e);
                }
            } else {
                iterator.remove();
            }
        }
    }

    @Override
    public void unregisterAll(Object sudo_token) {
        if (removeToken == sudo_token) {
            listeners.clear();
            exists.clear();
            subModifys.clear();
            return;
        }
        throw new UnsupportedOperationException();
    }
}
