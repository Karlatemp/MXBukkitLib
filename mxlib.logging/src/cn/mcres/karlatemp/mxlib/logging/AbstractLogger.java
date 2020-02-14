/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: AbstractLogger.java@author: karlatemp@vip.qq.com: 2020/2/14 下午7:23@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.logging;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.lang.management.ThreadInfo;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * 一个基本的日志类
 */
public abstract class AbstractLogger implements ILogger {
    protected final IMessageFactory factory;
    protected final Object lock;

    public AbstractLogger(IMessageFactory factory) {
        this(null, factory);
    }

    public AbstractLogger(Object lock, IMessageFactory factory) {
        if (lock == null) lock = this;
        this.lock = lock;
        if (factory == null) {
            if (!(this instanceof IMessageFactory)) {
                throw new NullPointerException();
            }
            factory = (IMessageFactory) this;
        }
        this.factory = factory;
    }

    private class PS extends InlinePrintStream {
        protected PS() {
        }

        @Override
        public void print(String s) {
            AbstractLogger.this.printf(s);
        }

        @Override
        public void println() {
            print("");
        }

        @Override
        public void println(String x) {
            print(x);
        }
    }

    private class PR extends PS {
        protected PR() {
        }

        @Override
        public void print(String s) {
            AbstractLogger.this.error(s);
        }
    }

    private final PrintStream out = new PS(), err = new PR();

    @Override
    public String getStackTraceElementMessage(StackTraceElement track) {
        synchronized (lock) {
            if (factory == this) throw new StackOverflowError("????? " + this);
            return factory.getStackTraceElementMessage(track);
        }
    }

    @NotNull
    @Override
    public ILogger printf(String line) {
        synchronized (lock) {
            write(false, false, line);
            return this;
        }
    }

    @NotNull
    @Override
    public ILogger error(String line) {
        synchronized (lock) {
            write(false, true, line);
            return this;
        }
    }

    protected abstract void write(boolean emp_prefix, boolean error, String message);

    @NotNull
    @Override
    public PrintStream getPrintStream() {
        return out;
    }

    @NotNull
    @Override
    public PrintStream getErrorStream() {
        return err;
    }

    @NotNull
    @Override
    public ILogger printThreadInfo(@NotNull ThreadInfo info, boolean fullFrames, boolean emptyPrefix) {
        synchronized (lock) {
            write(emptyPrefix, false, factory.dump(info, fullFrames));
            return this;
        }
    }

    protected static StackTraceElement[] getOutStackTrace(Throwable thr) {
        return RFToolkit.r.a(thr);
    }

    @NotNull
    @Override
    public ILogger printStackTrace(@NotNull Throwable thr, boolean printStacks, boolean isError) {
        synchronized (lock) {
            Set<Throwable> dejaVu
                    = Collections.newSetFromMap(new IdentityHashMap<>());
            dejaVu.add(thr);
            write(false, isError, factory.dump(thr));
            // Print our stack trace
            StackTraceElement[] trace = getOutStackTrace(thr);
            if (printStacks) {
                for (StackTraceElement element : trace)
                    printStackTraceElement(element, isError);
            }
            // Print suppressed exceptions, if any
            for (Throwable se : thr.getSuppressed()) {
                this.printEnclosedStackTrace(se, trace, SUPPRESSED_CAPTION, "\t", dejaVu, printStacks, isError);
            }
            // Print cause, if any
            Throwable ourCause = thr.getCause();
            if (ourCause != null) {
                this.printEnclosedStackTrace(ourCause, trace, CAUSE_CAPTION, "", dejaVu, printStacks, isError);
            }
            return this;
        }
    }

    protected void printEnclosedStackTrace(
            Throwable thiz,
            StackTraceElement[] enclosingTrace,
            String caption,
            String prefix,
            Set<Throwable> dejaVu,
            boolean printStacks, boolean err) {
        synchronized (lock) {
            if (dejaVu.contains(thiz)) {
                write(false, err, factory.CIRCULAR_REFERENCE(thiz));
            } else {
                dejaVu.add(thiz);
                // Compute number of frames in common between this and enclosing trace
                StackTraceElement[] trace = getOutStackTrace(thiz);
                write(false, err, prefix + caption + factory.dump(thiz));
                if (printStacks) {
                    int m = trace.length - 1;
                    int n = enclosingTrace.length - 1;
                    while (m >= 0 && n >= 0 && trace[m].equals(enclosingTrace[n])) {
                        m--;
                        n--;
                    }
                    int framesInCommon = trace.length - 1 - m;
                    for (int i = 0; i <= m; i++) {
                        this.printStackTraceElement(prefix, trace[i], err);
                    }
                    if (framesInCommon != 0) {
                        write(false, err, prefix + "\t... " + framesInCommon + " more");
                    }
                }
                // Print suppressed exceptions, if any
                for (Throwable se : thiz.getSuppressed())
                    this.printEnclosedStackTrace(se, trace, SUPPRESSED_CAPTION, prefix + "\t", dejaVu, printStacks, err);

                // Print cause, if any
                Throwable ourCause = thiz.getCause();
                if (ourCause != null) {
                    this.printEnclosedStackTrace(ourCause, trace, CAUSE_CAPTION, prefix, dejaVu, printStacks, err);
                }
            }
        }
    }

    protected void printStackTraceElement(StackTraceElement element, boolean err) {
        synchronized (lock) {
            printStackTraceElement(null, element, err);
        }
    }

    protected void printStackTraceElement(String prefix, StackTraceElement element, boolean err) {
        synchronized (lock) {
            if (prefix == null || prefix.isEmpty()) {
                write(false, err, getStackTraceElementMessage(element));
            } else {
                write(false, err, prefix + getStackTraceElementMessage(element));
            }
        }
    }
}
