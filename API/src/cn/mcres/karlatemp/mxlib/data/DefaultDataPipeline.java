/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DefaultDataPipeline.java@author: karlatemp@vip.qq.com: 19-11-15 下午11:48@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiPredicate;

public class DefaultDataPipeline implements DataPipeline {
    protected final Map<String, DataHandler[]> processors;
    protected final List<String> process;

    public DefaultDataPipeline(@NotNull Map<String, DataHandler[]> map, @NotNull List<String> process) {
        processors = map;
        this.process = process;
    }

    public DefaultDataPipeline() {
        this(new ConcurrentHashMap<>(), new ArrayList<>());
    }

    private boolean register(String namespace, DataHandler[] handlers) {
        if (handlers.length == 0) return true;
        if (processors.containsKey(namespace)) {
            throw new IllegalArgumentException("The namespace " + namespace + " is already occupied.");
        }
        processors.put(namespace, Arrays.copyOf(handlers, handlers.length));
        return false;
    }

    public DataPipeline addFirst(@NotNull String namespace, @NotNull DataHandler... handlers) {
        if (register(namespace, handlers)) return this;
        process.add(0, namespace);
        return this;
    }

    public DataPipeline addLast(@NotNull String namespace, @NotNull DataHandler... handlers) {
        if (register(namespace, handlers)) return this;
        process.add(namespace);
        return this;
    }

    public DataPipeline addBefore(@NotNull String namespace, @NotNull String target, @NotNull DataHandler... handlers) {
        if (register(namespace, handlers)) return this;
        final int index = process.indexOf(target);
        if (index < 0) {
            process.add(namespace);
        } else {
            process.add(index, namespace);
        }
        return this;
    }

    public DataPipeline addAfter(@NotNull String namespace, @NotNull String target, @NotNull DataHandler... handlers) {
        if (register(namespace, handlers)) return this;
        final int index = process.indexOf(target);
        if (index < 0) {
            process.add(namespace);
        } else {
            process.add(index + 1, namespace);
        }
        return this;
    }

    public DataPipeline remove(@NotNull String namespace) {
        process.remove(namespace);
        processors.remove(namespace);
        return this;
    }

    private void remove(@NotNull BiPredicate<String, DataHandler> filter) {
        final Iterator<Map.Entry<String, DataHandler[]>> iterator = processors.entrySet().iterator();
        root:
        while (iterator.hasNext()) {
            final Map.Entry<String, DataHandler[]> entry = iterator.next();
            String namespace = entry.getKey();
            DataHandler[] handlers = entry.getValue();

            for (int i = 0; i < handlers.length; i++) {
                if (handlers[i] != null)
                    if (filter.test(namespace, handlers[i])) {
                        handlers[i] = null;
                    }
            }

            for (DataHandler dh : handlers) {
                if (dh != null) continue root;
            }
            iterator.remove();
        }
    }

    public DataPipeline remove(@NotNull DataHandler handler) {
        remove((n, dh) -> dh == handler);
        return this;
    }

    public <T extends DataHandler> DataPipeline remove(@NotNull Class<T> type) {
        remove((n, dn) -> dn.getClass() == type);
        return this;
    }

    public DataPipeline replace(@NotNull String namespace, @NotNull String target, @NotNull DataHandler... handlers) {
        if (namespace.equals(target)) {
            if (handlers.length == 0) {
                remove(namespace);
            } else {
                processors.put(namespace, handlers);
            }
            return this;
        }
        if (register(namespace, handlers)) return this;
        int at = process.indexOf(target);
        process.add(at, namespace);
        remove(target);
        return this;
    }

    protected List<DataHandler> getProcesses() {
        List<DataHandler> handlers = new ArrayList<>();
        for (String namespace : new ArrayList<>(this.process)) {
            final DataHandler[] registered = processors.get(namespace);
            if (registered != null)
                for (DataHandler dh : registered) {
                    if (dh != null) {
                        handlers.add(dh);
                    }
                }
        }
        return handlers;
    }

    public DataProcessContext createContext() {
        return new DefaultProcessContext(() -> getProcesses().iterator());
    }
}
