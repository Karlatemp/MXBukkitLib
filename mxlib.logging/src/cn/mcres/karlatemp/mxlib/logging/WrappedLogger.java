/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: WrappedLogger.java@author: karlatemp@vip.qq.com: 2020/2/14 下午7:35@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.logging;

// import cn.mcres.karlatemp.mxlib.tools.EmptyStream;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.management.ThreadInfo;
import java.util.Locale;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Wrapped Logger.
 *
 * @since 2.8
 */
public class WrappedLogger implements ILogger {
    private static final Runnable a = () -> {
    };
    public static final Runnable EMPTY_RUNNABLE = a;
    protected ILogger parent;
    protected Runnable before;
    protected Runnable after;

    class PW extends PrintStream {
        private final boolean a;

        PrintStream getImpl() {
            return a ? parent.getPrintStream() : parent.getErrorStream();
        }

        @Override
        public void flush() {
            try {
                before.run();
                getImpl().flush();
            } finally {
                after.run();
            }
        }

        @Override
        public void close() {
            try {
                before.run();
                getImpl().close();
            } finally {
                after.run();
            }
        }

        @Override
        public boolean checkError() {
            try {
                before.run();
                return getImpl().checkError();
            } finally {
                after.run();
            }
        }

        @Override
        public void write(int b) {
            try {
                before.run();
                getImpl().write(b);
            } finally {
                after.run();
            }
        }

        @Override
        public void write(@NotNull byte[] buf, int off, int len) {
            try {
                before.run();
                getImpl().write(buf, off, len);
            } finally {
                after.run();
            }
        }

        @Override
        public void print(boolean b) {
            try {
                before.run();
                getImpl().print(b);
            } finally {
                after.run();
            }
        }

        @Override
        public void print(char c) {
            try {
                before.run();
                getImpl().print(c);
            } finally {
                after.run();
            }
        }

        @Override
        public void print(int i) {
            try {
                before.run();
                getImpl().print(i);
            } finally {
                after.run();
            }
        }

        @Override
        public void print(long l) {
            try {
                before.run();
                getImpl().print(l);
            } finally {
                after.run();
            }
        }

        @Override
        public void print(float f) {
            try {
                before.run();
                getImpl().print(f);
            } finally {
                after.run();
            }
        }

        @Override
        public void print(double d) {
            try {
                before.run();
                getImpl().print(d);
            } finally {
                after.run();
            }
        }

        @Override
        public void print(@NotNull char[] s) {
            try {
                before.run();
                getImpl().print(s);
            } finally {
                after.run();
            }
        }

        @Override
        public void print(String s) {
            try {
                before.run();
                getImpl().print(s);
            } finally {
                after.run();
            }
        }

        @Override
        public void print(Object obj) {
            try {
                before.run();
                getImpl().print(obj);
            } finally {
                after.run();
            }
        }

        @Override
        public void println() {
            try {
                before.run();
                getImpl().println();
            } finally {
                after.run();
            }
        }

        @Override
        public void println(boolean x) {
            try {
                before.run();
                getImpl().println(x);
            } finally {
                after.run();
            }
        }

        @Override
        public void println(char x) {
            try {
                before.run();
                getImpl().println(x);
            } finally {
                after.run();
            }
        }

        @Override
        public void println(int x) {
            try {
                before.run();
                getImpl().println(x);
            } finally {
                after.run();
            }
        }

        @Override
        public void println(long x) {
            try {
                before.run();
                getImpl().println(x);
            } finally {
                after.run();
            }
        }

        @Override
        public void println(float x) {
            try {
                before.run();
                getImpl().println(x);
            } finally {
                after.run();
            }
        }

        @Override
        public void println(double x) {
            try {
                before.run();
                getImpl().println(x);
            } finally {
                after.run();
            }
        }

        @Override
        public void println(@NotNull char[] x) {
            try {
                before.run();
                getImpl().println(x);
            } finally {
                after.run();
            }
        }

        @Override
        public void println(String x) {
            try {
                before.run();
                getImpl().println(x);
            } finally {
                after.run();
            }
        }

        @Override
        public void println(Object x) {
            try {
                before.run();
                getImpl().println(x);
            } finally {
                after.run();
            }
        }

        @Override
        public PrintStream printf(@NotNull String format, Object... args) {
            try {
                before.run();
                return getImpl().printf(format, args);
            } finally {
                after.run();
            }
        }

        @Override
        public PrintStream printf(Locale l, @NotNull String format, Object... args) {
            try {
                before.run();
                return getImpl().printf(l, format, args);
            } finally {
                after.run();
            }
        }

        @Override
        public PrintStream format(@NotNull String format, Object... args) {
            try {
                before.run();
                return getImpl().format(format, args);
            } finally {
                after.run();
            }
        }

        @Override
        public PrintStream format(Locale l, @NotNull String format, Object... args) {
            try {
                before.run();
                return getImpl().format(l, format, args);
            } finally {
                after.run();
            }
        }

        @Override
        public PrintStream append(CharSequence csq) {
            try {
                before.run();
                return getImpl().append(csq);
            } finally {
                after.run();
            }
        }

        @Override
        public PrintStream append(CharSequence csq, int start, int end) {
            try {
                before.run();
                return getImpl().append(csq, start, end);
            } finally {
                after.run();
            }
        }

        @Override
        public PrintStream append(char c) {
            try {
                before.run();
                return getImpl().append(c);
            } finally {
                after.run();
            }
        }

        @Override
        public void write(@NotNull byte[] b) throws IOException {
            try {
                before.run();
                getImpl().write(b);
            } finally {
                after.run();
            }
        }

        PW(boolean a) {
            super(InlinePrintStream.EO);
            this.a = a;
        }

    }

    private final PW err = new PW(true), def = new PW(false);

    public WrappedLogger(ILogger parent, Runnable before, Runnable after) {
        this.parent = parent;
        if (before == null) before = a;
        if (after == null) after = a;
        this.before = before;
        this.after = after;
    }

    @Override
    public String getStackTraceElementMessage(StackTraceElement track) {
        try {
            before.run();
            return parent.getStackTraceElementMessage(track);
        } finally {
            after.run();
        }
    }

    @Override
    @NotNull
    public ILogger println(String line) {
        try {
            before.run();
            return parent.println(line);
        } finally {
            after.run();
        }
    }

    @Override
    @NotNull
    public ILogger printf(Object data) {
        try {
            before.run();
            return parent.printf(data);
        } finally {
            after.run();
        }
    }

    @Override
    @NotNull
    public ILogger printf(boolean err, String ln) {
        try {
            before.run();
            return parent.printf(err, ln);
        } finally {
            after.run();
        }
    }

    @Override
    @NotNull
    public ILogger printf(String line) {
        try {
            before.run();
            return parent.printf(line);
        } finally {
            after.run();
        }
    }

    @Override
    @NotNull
    public ILogger error(String line) {
        try {
            before.run();
            return parent.error(line);
        } finally {
            after.run();
        }
    }

    @Override
    @NotNull
    public ILogger error(Object line) {
        try {
            before.run();
            return parent.error(line);
        } finally {
            after.run();
        }
    }

    @Override
    @NotNull
    public ILogger format(String format, Object... args) {
        try {
            before.run();
            return parent.format(format, args);
        } finally {
            after.run();
        }
    }

    @Override
    @NotNull
    public ILogger format(Locale locale, String format, Object... args) {
        try {
            before.run();
            return parent.format(locale, format, args);
        } finally {
            after.run();
        }
    }

    @Override
    @NotNull
    public ILogger errformat(String format, Object... args) {
        try {
            before.run();
            return parent.errformat(format, args);
        } finally {
            after.run();
        }
    }

    @Override
    @NotNull
    public ILogger errformat(Locale locale, String format, Object... args) {
        try {
            before.run();
            return parent.errformat(locale, format, args);
        } finally {
            after.run();
        }
    }

    @Override
    @NotNull
    public PrintStream getPrintStream() {
        return def;
    }

    @Override
    @NotNull
    public PrintStream getErrorStream() {
        return err;
    }

    @Override
    @NotNull
    public ILogger printThreadInfo(@NotNull ThreadInfo info, boolean fullFrames, boolean emptyPrefix) {
        try {
            before.run();
            return parent.printThreadInfo(info, fullFrames, emptyPrefix);
        } finally {
            after.run();
        }
    }

    @Override
    @NotNull
    public ILogger printThreadInfo(@NotNull Thread thread, boolean fullFrames, boolean emptyPrefix) {
        try {
            before.run();
            return parent.printThreadInfo(thread, fullFrames, emptyPrefix);
        } finally {
            after.run();
        }
    }

    @Override
    @NotNull
    public ILogger printStackTrace(@NotNull Throwable thr) {
        try {
            before.run();
            return parent.printStackTrace(thr);
        } finally {
            after.run();
        }
    }

    @Override
    public boolean isError(Level level) {
        try {
            before.run();
            return parent.isError(level);
        } finally {
            after.run();
        }
    }

    @Override
    @NotNull
    public ILogger printStackTrace(@NotNull Throwable thr, boolean printStacks, boolean isError) {
        try {
            before.run();
            return parent.printStackTrace(thr, printStacks, isError);
        } finally {
            after.run();
        }
    }

    @Override
    @NotNull
    public ILogger publish(LogRecord record, Handler handler) {
        try {
            before.run();
            return parent.publish(record, handler);
        } finally {
            after.run();
        }
    }
}
