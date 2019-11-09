/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MXScheduler.java@author: karlatemp@vip.qq.com: 19-11-9 下午4:56@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.scheduler;

import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public interface MXScheduler {
    default MXTask<Void> scheduleSycnDelayedTask(@NotNull Runnable task, long delay, TimeUnit unit) {
        return scheduleSycnDelayedTask(Toolkit.toCallable(task), delay, unit);
    }

    <T> MXTask<T> scheduleSycnDelayedTask(@NotNull Callable<T> task, long delay, TimeUnit unit);

    default MXTask<Void> scheduleAsyncDelayedTask(@NotNull Runnable task, long delay, TimeUnit unit) {
        return scheduleAsyncDelayedTask(
                Toolkit.toCallable(task), delay, unit
        );
    }

    <T> MXTask<T> scheduleAsyncDelayedTask(@NotNull Callable<T> task, long delay, TimeUnit unit);

    <T> MXTask<T> scheduleSyncMethod(@NotNull Callable<T> task);

    default void cancelTask(MXTask task) {
        task.cancel();
    }

    <T> MXTask<T> runTaskLater(@NotNull Callable<T> task, long delay, TimeUnit unit);

    default MXTask<Void> runTaskLater(@NotNull Runnable task, long delay, TimeUnit unit) {
        return runTaskLater(Toolkit.toCallable(task), delay, unit);
    }

    <T> MXTask<T> runTaskTimer(@NotNull Callable<T> task, long delay, long loop, TimeUnit unit);

    default MXTask<Void> runTaskTimer(@NotNull Runnable task, long delay, long loop, TimeUnit unit) {
        return runTaskTimer(Toolkit.toCallable(task), delay, loop, unit);
    }

    <T> MXTask<T> runTaskAsyncTimer(@NotNull Callable<T> task, long delay, long loop, TimeUnit unit);

    default MXTask<Void> runTaskAsyncTimer(@NotNull Runnable task, long delay, long loop, TimeUnit unit) {
        return runTaskAsyncTimer(Toolkit.toCallable(task), delay, loop, unit);
    }

    default void shutdown() {
    }

    default boolean isShutdown() {
        return false;
    }
}
