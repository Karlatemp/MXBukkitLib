/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/13 11:18:42
 *
 * MXLib/mxlib.logging/AsyncLogger.java
 */

package cn.mcres.karlatemp.mxlib.logging;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.lang.management.ThreadInfo;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class AsyncLogger implements ILogger {
    private final ILogger wrapper;
    private final Executor executor;

    @Override
    public String getStackTraceElementMessage(StackTraceElement track) {
        return wrapper.getStackTraceElementMessage(track);
    }

    @Override
    @NotNull
    public ILogger println(String line) {
        executor.execute(() -> wrapper.println(line));
        return this;
    }

    @Override
    @NotNull
    public ILogger printf(Object data) {
        executor.execute(() -> wrapper.printf(data));
        return this;
    }

    @Override
    @NotNull
    public ILogger printf(boolean err, String ln) {
        executor.execute(() -> wrapper.printf(err, ln));
        return this;
    }

    @Override
    @NotNull
    public ILogger printf(String line) {
        executor.execute(() -> wrapper.printf(line));
        return this;
    }

    @Override
    @NotNull
    public ILogger error(String line) {
        executor.execute(() -> wrapper.error(line));
        return this;
    }

    @Override
    @NotNull
    public ILogger error(Object line) {
        executor.execute(() -> wrapper.error(line));
        return this;
    }

    @Override
    @NotNull
    public ILogger format(String format, Object... args) {
        executor.execute(() -> wrapper.format(format, args));
        return this;
    }

    @Override
    @NotNull
    public ILogger format(Locale locale, String format, Object... args) {
        executor.execute(() -> wrapper.format(locale, format, args));
        return this;
    }

    @Override
    @NotNull
    public ILogger errformat(String format, Object... args) {
        executor.execute(() -> wrapper.errformat(format, args));
        return this;
    }

    @Override
    @NotNull
    public ILogger errformat(Locale locale, String format, Object... args) {
        executor.execute(() -> wrapper.errformat(locale, format, args));
        return this;
    }

    @Override
    public @NotNull PrintStream getPrintStream() {
        return wrapper.getPrintStream();
    }

    @Override
    public @NotNull PrintStream getErrorStream() {
        return wrapper.getErrorStream();
    }

    @Override
    @NotNull
    public ILogger printThreadInfo(@NotNull ThreadInfo info, boolean fullFrames, boolean emptyPrefix) {
        executor.execute(() -> wrapper.printThreadInfo(info, fullFrames, emptyPrefix));
        return this;
    }

    @Override
    @NotNull
    public ILogger printThreadInfo(@NotNull Thread thread, boolean fullFrames, boolean emptyPrefix) {
        executor.execute(() -> wrapper.printThreadInfo(thread, fullFrames, emptyPrefix));
        return this;
    }

    @Override
    @NotNull
    public ILogger printStackTrace(@NotNull Throwable thr) {
        executor.execute(() -> wrapper.printStackTrace(thr));
        return this;
    }

    @Override
    public boolean isError(Level level) {
        return wrapper.isError(level);
    }

    @Override
    @NotNull
    public ILogger printStackTrace(@NotNull Throwable thr, boolean printStacks, boolean isError) {
        return wrapper.printStackTrace(thr, printStacks, isError);
    }

    @Override
    @NotNull
    public ILogger publish(LogRecord record, Handler handler) {
        return wrapper.publish(record, handler);
    }

    public AsyncLogger(ILogger wrapper, Executor executor) {
        this.wrapper = wrapper;
        this.executor = executor;
    }
}
