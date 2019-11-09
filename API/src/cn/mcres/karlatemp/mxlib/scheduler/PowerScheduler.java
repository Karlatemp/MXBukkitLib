/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PowerScheduler.java@author: karlatemp@vip.qq.com: 19-11-9 下午5:25@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.scheduler;

import cn.mcres.karlatemp.mxlib.logging.ILogger;
import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.concurrent.*;

public class PowerScheduler implements MXScheduler {
    private final ExecutorService asy;
    private final ExecutorService sync;
    private final ConcurrentLinkedQueue<PowerTask> syncTasks = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<PowerTask> asyTasks = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Worker> aliveAsys = new ConcurrentLinkedQueue<>();

    private static class TW extends Worker implements Callable<Void> {
        private ConcurrentLinkedQueue<PowerTask> tasks;

        TW() {
        }

        TW(ConcurrentLinkedQueue<PowerTask> ps) {
            tasks = ps;
        }

        @Override
        public Void call() throws Exception {
            while (true) {
                rnuTask(tasks);
                Thread.sleep(10);
            }
        }
    }

    private static class Worker {
        private final ConcurrentLinkedQueue<PowerTask> buffed = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Worker> alives;

        synchronized void rnuTask(ConcurrentLinkedQueue<PowerTask> tasks) {
            if (alives != null)
                alives.remove(this);
            if (tasks.isEmpty()) return;
            long current = System.currentTimeMillis();
            buffed.clear();
            final Iterator<PowerTask> iterator = tasks.iterator();
            while (iterator.hasNext()) {
                final PowerTask next = iterator.next();
                iterator.remove();
                if (next != null) {
                    if (current > next.runOn) {
                        next.run();
                        if (!next.isDone()) {
                            buffed.add(next);
                        }
                    } else {
                        buffed.add(next);
                    }
                }
            }
            tasks.addAll(buffed);
            if (alives != null)
                alives.add(this);
        }
    }

    private class PowerTask<T> implements MXTask<T>, Callable<T>, Future<T> {
        private boolean sync;
        private boolean cancelled;
        private long runOn;
        private long loop_time;
        private boolean errorHidden;
        private Callable<T> task;
        private volatile MXTaskState state = MXTaskState.WAITING;
        private final Object runLock = new Object(), notify = new Object();
        private Thread runningThread;
        private Throwable err;
        private T refval;

        @Override
        public MXTaskState getState() {
            return state;
        }

        @Override
        public boolean isSync() {
            return sync;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            cancel();
            if (mayInterruptIfRunning) {
                if (runningThread != null) {
                    runningThread.interrupt();
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
            return state == MXTaskState.FINISH;
        }

        @Override
        public T get() throws InterruptedException, ExecutionException {
            if (Thread.currentThread() == runningThread) {
                throw new RuntimeException("Cannot get self task's return");
            }
            if (!isDone()) {
                synchronized (notify) {
                    notify.wait();
                }
            }
            if (err != null) {
                throw new ExecutionException(err);
            }
            return refval;
        }

        @Override
        public T get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            if (Thread.currentThread() == runningThread) {
                throw new RuntimeException("Cannot get self task's return");
            }
            if (!isDone()) {
                synchronized (notify) {
                    unit.timedWait(notify, timeout);
                    if (runningThread != null) throw new TimeoutException();
                }
            }
            if (err != null) {
                throw new ExecutionException(err);
            }
            return refval;
        }

        @Override
        public void cancel() {
            cancelled = true;
            if (sync) {
                syncTasks.remove(this);
            } else {
                asyTasks.remove(this);
            }
            synchronized (notify) {
                state = MXTaskState.FINISH;
                notify.notifyAll();
            }
        }

        @Override
        public void hiddenExceptions() {
            errorHidden = true;
        }

        @Override
        public Future<T> getFuture() {
            return this;
        }

        void run() {
            if (cancelled) {
                loop_time = 0;
                runOn = 0;
                return;
            }
            if (state == MXTaskState.WAITING) {
                synchronized (runLock) {
                    if (state == MXTaskState.WAITING) {
                        state = MXTaskState.RUNNING;
                        runningThread = Thread.currentThread();
                        try {
                            err = null;
                            refval = call();
                        } catch (Throwable thr) {
                            err = thr;
                        } finally {
                            if (loop_time > 0) {
                                this.runOn += loop_time;
                                state = MXTaskState.WAITING;
                            } else {
                                state = MXTaskState.FINISH;
                            }
                            runningThread = null;
                            synchronized (notify) {
                                notify.notifyAll();
                            }
                        }
                    }
                }
            } else if (state == MXTaskState.RUNNING) {
                throw new RuntimeException("This task was running.");
            } else {
                throw new RuntimeException("The task finished.");
            }
        }

        @Override
        public T call() {
            try {
                return task.call();
            } catch (Throwable e) {
                if (errorHidden)
                    return ThrowHelper.thrown(e);
                dumpException(this, e);
                return ThrowHelper.thrown(e);
            }
        }
    }

    private ILogger logger;

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
        sync.submit(new TW(syncTasks));
        asy.submit((Callable<Void>) () -> {
            while (true) {
                if (!asyTasks.isEmpty()) {
                    if (aliveAsys.isEmpty()) {
                        TW a = new TW();
                        a.tasks = asyTasks;
                        a.alives = aliveAsys;
                        aliveAsys.add(a);
                        asy.submit(a);
                    }
                }
                Thread.sleep(1000L);
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
        if ((sync ? this.sync : asy).isShutdown()) {
            return null;
        }
        PowerTask<T> ref = new PowerTask<>();
        ref.task = task;
        ref.sync = sync;
        ref.runOn = System.currentTimeMillis() + unit.toMillis(delay);
        ref.loop_time = unit.toMillis(loop);
        (sync ? syncTasks : asyTasks).offer(ref);
        return ref;
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
