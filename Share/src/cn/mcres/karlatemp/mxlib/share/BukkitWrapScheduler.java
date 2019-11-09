/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BukkitWrapScheduler.java@author: karlatemp@vip.qq.com: 19-11-9 下午7:13@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.scheduler.MXScheduler;
import cn.mcres.karlatemp.mxlib.scheduler.MXTask;
import cn.mcres.karlatemp.mxlib.scheduler.MXTaskState;
import cn.mcres.karlatemp.mxlib.tools.Pointer;
import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.*;

public class BukkitWrapScheduler implements MXScheduler {
    private static final BukkitScheduler bs = Bukkit.getScheduler();

    public static int getTicks(long delay, TimeUnit unit) {
        long mills = unit.toMillis(delay);
        return (int) (mills / (1000 / 20));
    }

    private static class BukkitWrapTask<T> implements MXTask<T>, Future<T>, Runnable, Callable<T> {
        BukkitTask task;
        Callable<T> cb;
        private final Object notifyLock = new Object();
        private MXTaskState st = MXTaskState.WAITING;
        T ref;
        Throwable err;
        private boolean hid;

        BukkitWrapTask(Callable<T> cb) {
            this.cb = cb;
        }

        @Override
        public T call() throws Exception {
            st = MXTaskState.RUNNING;
            try {
                return ref = cb.call();
            } catch (Throwable e) {
                err = e;
                if (!hid) return ThrowHelper.thrown(e);
                return null;
            } finally {
                st = MXTaskState.FINISH;
                synchronized (notifyLock) {
                    notifyLock.notifyAll();
                }
            }
        }

        @Override
        public void run() {
            st = MXTaskState.RUNNING;
            try {
                ref = cb.call();
            } catch (Throwable e) {
                err = e;
                if (!hid) ThrowHelper.thrown(e);
            } finally {
                st = MXTaskState.FINISH;
                synchronized (notifyLock) {
                    notifyLock.notifyAll();
                }
            }
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isSync() {
            return task.isSync();
        }

        @Override
        public boolean isDone() {
            return st == MXTaskState.FINISH;
        }

        @Override
        public void cancel() {
            task.cancel();
            st = MXTaskState.FINISH;
        }

        @Override
        public void hiddenExceptions() {
            this.hid = true;
        }

        @Override
        public Future<T> getFuture() {
            return this;
        }

        @Override
        public MXTaskState getState() {
            return st;
        }

        @Override
        public T get() throws InterruptedException, ExecutionException {
            if (st != MXTaskState.FINISH) {
                synchronized (notifyLock) {
                    notifyLock.wait();
                }
            }
            if (err != null) {
                throw new ExecutionException(err);
            }
            return ref;
        }

        @Override
        public T get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            if (st != MXTaskState.FINISH) {
                synchronized (notifyLock) {
                    unit.timedWait(notifyLock, timeout);
                    if (st != MXTaskState.FINISH)
                        throw new TimeoutException();
                }
            }
            if (err != null) {
                throw new ExecutionException(err);
            }
            return ref;
        }

        BukkitWrapTask<T> pow(BukkitTask bt) {
            task = bt;
            return this;
        }
    }

    @Override
    public <T> MXTask<T> scheduleSycnDelayedTask(@NotNull Callable<T> task, long delay, TimeUnit unit) {
        return Optional.of(new BukkitWrapTask<>(task)).map(w -> w.pow(
                bs.runTaskLater(BukkitToolkit.getCallerPlugin(), w, getTicks(delay, unit))
        )).get();
    }

    @Override
    public <T> MXTask<T> scheduleAsyncDelayedTask(@NotNull Callable<T> task, long delay, TimeUnit unit) {
        return Optional.of(new BukkitWrapTask<>(task)).map(w -> w.pow(
                bs.runTaskLaterAsynchronously(BukkitToolkit.getCallerPlugin(), w, getTicks(delay, unit))
        )).get();
    }

    @Override
    public <T> MXTask<T> scheduleSyncMethod(@NotNull Callable<T> task) {
        return Optional.of(new BukkitWrapTask<>(task)).map(w -> w.pow(
                bs.runTask(BukkitToolkit.getCallerPlugin(), w)
        )).get();
    }

    @Override
    public <T> MXTask<T> runTaskLater(@NotNull Callable<T> task, long delay, TimeUnit unit) {
        return scheduleSycnDelayedTask(task, delay, unit);
    }

    @Override
    public <T> MXTask<T> runTaskTimer(@NotNull Callable<T> task, long delay, long loop, TimeUnit unit) {
        return Optional.of(new BukkitWrapTask<>(task)).map(w -> w.pow(
                bs.runTaskTimer(BukkitToolkit.getCallerPlugin(), w, getTicks(delay, unit), getTicks(loop, unit))
        )).get();
    }

    @Override
    public <T> MXTask<T> runTaskAsyncTimer(@NotNull Callable<T> task, long delay, long loop, TimeUnit unit) {
        return Optional.of(new BukkitWrapTask<>(task)).map(w -> w.pow(
                bs.runTaskTimerAsynchronously(BukkitToolkit.getCallerPlugin(), w, getTicks(delay, unit), getTicks(loop, unit))
        )).get();
    }
}
