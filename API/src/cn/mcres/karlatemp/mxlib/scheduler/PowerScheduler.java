/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PowerScheduler.java@author: karlatemp@vip.qq.com: 19-11-9 下午5:25@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.scheduler;

import cn.mcres.karlatemp.mxlib.logging.ILogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

/**
 * Default implements for MXScheduler.
 * <p>
 * Rebuild at 2.8.1
 */
public class PowerScheduler implements MXScheduler {
    private final ExecutorService asy;
    private final ExecutorService sync;
    private final ConcurrentLinkedQueue<PowerTask> syncTasks = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<PowerTask> asyTasks = new ConcurrentLinkedQueue<>();
    //    private final ConcurrentLinkedQueue<Worker> aliveAsys = new ConcurrentLinkedQueue<>();
    private final Object notify_sync = new Object(), notify_asy = new Object();
    // private final NotifyWaiter nw_sync, nw_asy;

    private static class NotifyWaiter implements Callable<Void> {
        //final Object output_notify = new Object();
        //boolean notInWaiting = true;
        final Object notify;
        final ConcurrentLinkedQueue<PowerTask> tasks;

        NotifyWaiter(Object notify, ConcurrentLinkedQueue<PowerTask> tasks) {
            this.notify = notify;
            this.tasks = tasks;
        }

        void invoke(List<PowerTask> tasks) {
            for (PowerTask t : tasks) {
                t.post();
            }
        }

        void wait_(long t) throws InterruptedException {
            synchronized (notify) {
                /*synchronized (this) {
                    notInWaiting = false;
                    synchronized (output_notify) {
                        output_notify.notifyAll();
                    }
                }
                try {*/
                if (t == 0 && !tasks.isEmpty()) return;
                // System.out.println("S: " + t + ", " + toString());
                notify.wait(t);
                /*} finally {
                    synchronized (this) {
                        notInWaiting = true;
                    }
                }*/
            }
        }

        void startup() throws InterruptedException {
            root:
            while (true) {
                // System.out.println("LP: " + toString());
                if (tasks.isEmpty()) {
                    wait_(0);
                } else {
                    List<PowerTask> invokes = new ArrayList<>();
                    final Iterator<PowerTask> iterator = tasks.iterator();
                    long time = System.currentTimeMillis();
                    while (iterator.hasNext()) {
                        PowerTask next = iterator.next();
                        if (next == null) iterator.remove();
                        else {
                            if (next.beginTimeline <= time) {
                                invokes.add(next);
                                if (!next.doLoop) iterator.remove();
                            }
                        }
                    }
                    invoke(invokes);
                    for (PowerTask task : invokes) {
                        if (task.doLoop) {
                            task.beginTimeline += task.loopTimes;
                        }
                    }
                    long current = System.currentTimeMillis();
                    if (tasks.isEmpty()) continue;
                    long nearest = Long.MAX_VALUE;
                    for (PowerTask task : tasks) {
                        if (task.beginTimeline < nearest) {
                            nearest = task.beginTimeline;
                            if (nearest <= current)
                                continue root;
                        }
                    }
                    wait_(nearest - current);
                }

            }
        }

        @Override
        public Void call() throws Exception {
            startup();
            return null;
        }
    }

    private class PowerTask<T> implements MXTask<T>, Future<T> {
        Callable<T> cb;
        boolean sync;
        boolean cancelled;
        long beginTimeline;
        long loopTimes;
        boolean doLoop;
        boolean hiddenE;
        Throwable error;
        Thread invoking;
        T result;
        final Object locker = new Object();
        MXTaskState stat = MXTaskState.WAITING;
        boolean interrupted;

        @Override
        public boolean isSync() {
            return sync;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            doLoop = false;
            cancelled = true;
            stat = MXTaskState.FINISH;
            if (mayInterruptIfRunning) {
                interrupted = true;
                if (invoking != null) {
                    invoking.interrupt();
                }
            } else {
                if (invoking == null) {
                    synchronized (locker) {
                        locker.notifyAll();
                    }
                }
            }
            return true;
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public boolean isDone() {
            return stat == MXTaskState.FINISH;
        }

        @Override
        public T get() throws InterruptedException, ExecutionException {
            if (doLoop)
                throw new ExecutionException(new UnsupportedOperationException("Cannot got result from loop invoking."));
            while (true) {
                if (isDone()) return result();
                synchronized (locker) {
                    locker.wait();
                }
                if (interrupted) throw new InterruptedException();
            }
        }

        private T result() throws ExecutionException {
            if (error != null) throw new ExecutionException(error);
            return result;
        }

        @Override
        public T get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            if (doLoop)
                throw new ExecutionException(new UnsupportedOperationException("Cannot got result from loop invoking."));
            if (isDone()) return result();
            synchronized (locker) {
                unit.timedWait(locker, timeout);
            }
            if (interrupted) throw new InterruptedException();
            if (isDone()) return result();
            throw new TimeoutException();
        }

        @Override
        public void cancel() {
            cancel(true);
        }

        @Override
        public void hiddenExceptions() {
            hiddenE = true;
        }

        @Override
        public Future<T> getFuture() {
            return this;
        }

        @Override
        public MXTaskState getState() {
            return stat;
        }

        void post() {
            synchronized (locker) {
                if (stat == MXTaskState.WAITING) {
                    stat = MXTaskState.RUNNING;
                    invoking = Thread.currentThread();
                    try {
                        result = cb.call();
                    } catch (Throwable thr) {
                        error = thr;
                        if (!hiddenE) {
                            dumpException(this, thr);
                        }
                    } finally {
                        invoking = null;
                        if (doLoop) {
                            stat = MXTaskState.WAITING;
                        } else {
                            stat = MXTaskState.FINISH;
                            locker.notifyAll();
                        }
                    }
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        }
    }

    private final ILogger logger;

    protected void dumpException(MXTask task, Throwable error) {
        if (logger == null) error.printStackTrace();
        else {
            synchronized (logger) {
                logger.printf("Error on running task " + task);
                logger.printStackTrace(error);
            }
        }
    }

    public PowerScheduler(@NotNull ExecutorService sync, @NotNull ExecutorService asy, @Nullable ILogger logger) {
        this.logger = logger;
        this.sync = sync;
        this.asy = asy;
        sync.submit(new NotifyWaiter(notify_sync, syncTasks) {
            @Override
            public String toString() {
                return "Sync";
            }
        });
        asy.submit(new NotifyWaiter(notify_asy, asyTasks) {
            @Override
            void invoke(List<PowerTask> tasks) {
                for (PowerTask task : tasks) {
                    asy.submit(task::post, null);
                }
            }

            @Override
            public String toString() {
                return "Asy";
            }
        });
    }

    @Override
    public void shutdown() {
        sync.shutdown();
        asy.shutdown();
        syncTasks.clear();
        asyTasks.clear();
    }

    @Override
    public boolean isShutdown() {
        return sync.isShutdown() || asy.isShutdown();
    }

    private <T> PowerTask<T> createTask(Callable<T> task, boolean sync, long delay, long loop, TimeUnit unit) {
        ConcurrentLinkedQueue<PowerTask> tasks = sync ? syncTasks : asyTasks;
        PowerTask<T> taskx = new PowerTask<>();
        taskx.doLoop = loop > 0;
        if (unit == null) unit = TimeUnit.MILLISECONDS;
        taskx.loopTimes = unit.toMillis(loop);
        taskx.sync = sync;
        taskx.beginTimeline = System.currentTimeMillis() + unit.toMillis(delay);
        taskx.cb = task;
        tasks.add(taskx);
        {
            final Object notify = sync ? notify_sync : notify_asy;
            synchronized (notify) {
                notify.notifyAll();
            }
        }
        return taskx;
    }

    @Override
    public <T> MXTask<T> scheduleSycnDelayedTask(@NotNull Callable<T> task, long delay, TimeUnit unit) {
        return createTask(
                task, true, delay, 0, unit
        );
    }

    @Override
    public <T> MXTask<T> scheduleAsyncDelayedTask(@NotNull Callable<T> task, long delay, TimeUnit unit) {
        return createTask(
                task, false, delay, 0, unit
        );
    }

    @Override
    public <T> MXTask<T> scheduleSyncMethod(@NotNull Callable<T> task) {
        return createTask(
                task, true, 1, 0, TimeUnit.MILLISECONDS
        );
    }

    @Override
    public <T> MXTask<T> runTaskLater(@NotNull Callable<T> task, long delay, TimeUnit unit) {
        return createTask(
                task, true, delay, 0, unit
        );
    }

    @Override
    public <T> MXTask<T> runTaskTimer(@NotNull Callable<T> task, long delay, long loop, TimeUnit unit) {
        return createTask(
                task, true, delay, loop, unit
        );
    }

    @Override
    public <T> MXTask<T> runTaskAsyncTimer(@NotNull Callable<T> task, long delay, long loop, TimeUnit unit) {
        return createTask(
                task, false, delay, loop, unit
        );
    }
}
