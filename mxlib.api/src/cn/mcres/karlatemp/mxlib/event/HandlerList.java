/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: HandlerList.java@author: karlatemp@vip.qq.com: 19-9-27 下午1:26@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.event;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Event's handler list.
 *
 * @since 2.2
 */
public class HandlerList<E extends Event> {
    public HandlerList() {
        this(true);
    }

    private static final ConcurrentLinkedQueue<WeakReference<HandlerList<?>>> handlers = new ConcurrentLinkedQueue<>();

    public HandlerList(boolean register) {
        if (register) {
            handlers.add(new WeakReference<>(this));
        }
    }

    @SuppressWarnings("unchecked")
    public static void unregisterAll(EventHandler<?> handler) {
        invoke(HandlerList::unregister, handler);
    }

    @SuppressWarnings("rawtypes")
    private static <T> void invoke(BiConsumer<HandlerList, T> func, T param) {
        final Iterator<WeakReference<HandlerList<?>>> iterator = handlers.iterator();
        while (iterator.hasNext()) {
            var next = iterator.next();
            if (next.isEnqueued()) iterator.remove();
            else {
                final HandlerList<?> list = next.get();
                if (list == null) iterator.remove();
                else func.accept(list, param);
            }
        }
    }

    public interface ErrorCatch {
        void accept(@NotNull Event posing, @NotNull EventHandler invoking, @NotNull Throwable error);
    }

    private ErrorCatch errorCatch;
    public static final ErrorCatch THROW = (posing, eh, thr) -> ThrowHelper.thrown(thr),
            PRINT = (posing, invoking, error) -> {
                MXBukkitLib.getLogger().error("Error in posing event " + posing.getClass() + " when running handler " + invoking);
                MXBukkitLib.getLogger().printStackTrace(error);
            };
    private static ErrorCatch DEFAULT_ERROR_CATCH = PRINT;

    public static ErrorCatch getDefaultErrorCatch() {
        return DEFAULT_ERROR_CATCH;
    }

    public ErrorCatch getErrorCatch() {
        return errorCatch;
    }

    public static void setDefaultErrorCatch(ErrorCatch defaultErrorCatch) {
        DEFAULT_ERROR_CATCH = defaultErrorCatch;
    }

    public void setErrorCatch(ErrorCatch errorCatch) {
        this.errorCatch = errorCatch;
    }

    @NotNull
    public final ErrorCatch getErrorCatchRunnable() {
        final ErrorCatch consumer = getErrorCatch();
        if (consumer != null) return consumer;
        final ErrorCatch def = getDefaultErrorCatch();
        if (def != null) {
            return def;
        }
        return THROW;
    }

    private static class ND<E extends Event> {
        final ArrayList<EventHandler<E>> handlers = new ArrayList<>();
        ND<E> next, last;
        int pro;
    }

    ND<E> n;

    public void forEach(Consumer<EventHandler<E>> c) {
        if (n != null) {
            synchronized (this) {
                /*
                 * Remove handler support
                 */
                ArrayList<EventHandler<E>> handlers = new ArrayList<>();
                ND<E> cur = n;
                while (cur != null) {
                    handlers.clear();
                    handlers.addAll(cur.handlers);
                    handlers.forEach(c);
                    cur = cur.next;
                }
            }
        }
    }

    public synchronized void register(@NotNull EventHandler<E> handler) {
        int p = handler.priority();
        if (n == null) {
            ND<E> nx;
            nx = n = new ND<>();
            nx.pro = p;
            nx.handlers.add(handler);
        } else {
            ND<E> app = null, cur = n, lastest = null;
            while (cur != null) {
                lastest = cur;
                int pp = cur.pro;
                if (pp == p) {
                    app = cur;
                    break;
                } else {
                    if (pp > p) {
                        ND l = cur.last;
                        app = new ND<>();
                        app.pro = p;
                        if (l == null) {
                            app.next = cur;
                            cur.last = app;
                            n = app;
                        } else {
                            app.last = l;
                            app.next = cur;
                            cur.last = app;
                            l.next = app;
                        }
                        break;
                    }
                }
                cur = cur.next;
            }
            if (app == null) {
                app = new ND<>();
                lastest.next = app;
                app.last = lastest;
                app.pro = p;
            }
            app.handlers.add(handler);
        }
    }

    public synchronized void unregister(@NotNull EventHandler<E> handler) {
        if (n == null) return;
        ND<E> cur = n;
        int p = handler.priority();
        while (cur != null) {
            int x = cur.pro;
            if (p > x) return;
            if (p == x) {
                cur.handlers.remove(handler);
            }
            cur = cur.next;
        }
    }

    public void post(E event) {
        forEach(h -> {
            try {
                h.post(event);
            } catch (Throwable thr) {
                getErrorCatchRunnable().accept(event, h, thr);
            }
        });
    }

    public synchronized void clear() {
        n = null;
    }
}
