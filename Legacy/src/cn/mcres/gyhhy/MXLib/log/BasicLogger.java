/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BasicLogger.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.log;

import cn.mcres.gyhhy.MXLib.StringHelper;
import cn.mcres.gyhhy.MXLib.ext.lookup.Looker;
import cn.mcres.gyhhy.MXLib.io.LinePrintStream;
import cn.mcres.gyhhy.MXLib.io.LinePrintWriter;
import cn.mcres.gyhhy.MXLib.io.LineWritable;
import cn.mcres.karlatemp.mxlib.logging.AbstractBaseLogger;
import cn.mcres.karlatemp.mxlib.logging.BukkitMessageFactory;
import cn.mcres.karlatemp.mxlib.logging.IMessageFactory;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Scanner;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

/**
 * a logger with color typography
 * {@link cn.mcres.karlatemp.mxlib.logging.ILogger}
 *
 * @author karlatemp
 */
@Deprecated
public class BasicLogger extends AbstractBaseLogger {
    static IMessageFactory factory = new BukkitMessageFactory();

    public static IMessageFactory getMessageFactory() {
        return factory;
    }

    public static void setMessageFactory(IMessageFactory f) {
        factory = f;
    }

    /**
     * Message for trying to suppress a null exception.
     */
    public static final String NULL_CAUSE_MESSAGE = "Cannot suppress a null exception.";
    /**
     * Message for trying to suppress oneself.
     */
    public static final String SELF_SUPPRESSION_MESSAGE = "Self-suppression not permitted";
    /**
     * Caption for labeling causative exception stack traces
     */
    public static final String CAUSE_CAPTION = "Caused by: ";
    /**
     * Caption for labeling suppressed exception stack traces
     */
    public static final String SUPPRESSED_CAPTION = "Suppressed: ";
    private static String defaultFormat = "\u00a7r[\u00a7b%s\u00a7r]\u00a7e ";
    private static String defaultErrFormat = "\u00a7r[\u00a7b%s\u00a7r]\u00a7c ";

    private static final PrintStream defaultOutput = System.out;
    private static final LineWritable defaultLW = new LinePrintStream(defaultOutput);

    public static String toPrintingMessage(String line, PrintingType type) {
        if (type == null) {
            return line;
        }
        switch (type) {
            case COLORED: {
                return Ascii.ec(line) + Ascii.RESET;
            }
            case SKIP_COLOR: {
                return Ascii.skipColor(line);
            }
        }
        return line;
    }

    /**
     * This method will write a line to console.
     */
    public static void write(BasicLogger bl, String line) {
        bl.getWritingOutput().println(toPrintingMessage(line, bl.pt));
    }

    public static void setDefaultFormat(String f) {
        defaultFormat = f;
    }

    public static void setDefaultErrorFormat(String f) {
        defaultErrFormat = f;
    }

    public static String getDefaultFormat() {
        return defaultFormat;
    }

    public static String getDefaultErrorFormat() {
        return defaultErrFormat;
    }

    public static BasicLogger createRawLogger(String prefix, String errprefix) {
        return new BasicLogger(prefix, errprefix);
    }

    public static BasicLogger createRawLogger(String format, String errformat, String plugin_name) {
        return new BasicLogger(format, errformat, plugin_name);
    }

    /**
     * Set to null only when you don't need to use this field.
     */
    @Deprecated
    protected LineWritable outputStream = defaultLW;
    protected PrintingType pt = PrintingType.COLORED;
    protected PrefixFormatter pf;
    @Deprecated
    protected final String prefix;
    @Deprecated
    protected final String errprefix;

    @SuppressWarnings("AssignmentToMethodParameter")
    public BasicLogger(String format, String errformat, String pname) {
        this(factory, format, errformat, pname);
    }

    @SuppressWarnings("AssignmentToMethodParameter")
    public BasicLogger(String prefix, String errprefix) {
        this(factory, prefix, errprefix);
    }

    @SuppressWarnings("AssignmentToMethodParameter")
    public BasicLogger(IMessageFactory factory, String format, String errformat, String pname) {
        super(factory);
        if (format == null) {
            format = defaultFormat;
        }
        if (errformat == null) {
            errformat = defaultErrFormat;
        }
        prefix = String.format(format, pname);
        errprefix = String.format(errformat, pname);
    }

    @SuppressWarnings("AssignmentToMethodParameter")
    public BasicLogger(IMessageFactory factory, String prefix, String errprefix) {
        super(factory);
        if (prefix == null) {
            prefix = defaultFormat;
        }
        if (errprefix == null) {
            errprefix = defaultErrFormat;
        }
        this.prefix = prefix;
        this.errprefix = errprefix;
    }

    /**
     * This method is call by writer
     */
    public LineWritable getWritingOutput() {
        return outputStream;
    }

    public BasicLogger setWritingOutput(LineWritable out) {
        this.outputStream = Objects.requireNonNull(out, "Null print stream");
        return this;
    }

    public BasicLogger setWritingOutput(PrintStream out) {
        return setWritingOutput((LineWritable) new LinePrintStream(out));
    }

    public BasicLogger setWritingOutput(PrintWriter pw) {
        return setWritingOutput((LineWritable) new LinePrintWriter(pw));
    }

    public PrintingType getPrintingType() {
        return pt;
    }

    public BasicLogger setPrintingType(PrintingType pt) {
        this.pt = pt;
        return this;
    }

    public String getStackTraceElementMsg(StackTraceElement stack) {
        return getStackTraceElementMessage(stack);
    }

    public PrefixFormatter getPrefixFormatter() {
        if (pf == null) {
            pf = NonFormatter.getInstance();
        }
        return pf;
    }

    public BasicLogger setPrefixFormatter(PrefixFormatter pf) {
        this.pf = pf;
        return this;
    }

    /**
     * Lambda
     */
    public BasicLogger setPrefixFormatter(PrefixFormatter.PrefixFormatterNoBool pf) {
        return setPrefixFormatter((PrefixFormatter) pf);
    }

    /**
     * Lambda
     */
    public BasicLogger setPrefixFormatter(PrefixFormatter.PrefixFormatterWithBool pf) {
        return setPrefixFormatter((PrefixFormatter) pf);
    }

    /**
     * Print a line
     */
    public BasicLogger println(String line) {
        printf(line);
        return this;
    }

    /**
     * Print a line
     */
    public BasicLogger printf(Object data) {
        return printf(String.valueOf(data));
    }

    public BasicLogger printf(String line, Object... argc) {
        printf(StringHelper.variable(line, argc));
        return this;
    }

    public BasicLogger printf(String line, Map<String, Object> argc) {
        printf(StringHelper.variable(line, argc));
        return this;
    }

    /**
     * Print a line
     */
    public BasicLogger printf(String line) {
        write(getPrefix(line), line);
        return this;
    }

    /**
     * Print a line, use error prefix
     */
    public BasicLogger error(String line) {
        write(getErrorPrefix(line), line);
        return this;
    }

    public BasicLogger error(String line, Object... argc) {
        error(StringHelper.variable(line, argc));
        return this;
    }

    public BasicLogger error(String line, Map<String, Object> argc) {
        error(StringHelper.variable(line, argc));
        return this;
    }

    public BasicLogger errorformat(String format, Object... argc) {
        error(String.format(format, argc));
        return this;
    }

    public BasicLogger format(String format, Object... argc) {
        printf(String.format(format, argc));
        return this;
    }

    /**
     * This method will write a line to console.
     */
    public void write(String line) {
        write(this, line);
    }

    public void write(String prefix, String line) {
        write(prefix, line, false);
    }

    public void write(String prefix, String line, boolean fullPrefix) {
        this.write(!fullPrefix, prefix, false, line);
    }

    protected String getPrefix(String msg) {
        return this.getPrefixFormatter().format(prefix, msg, false);
    }

    protected String getErrorPrefix(String msg) {
        return this.getPrefixFormatter().format(errprefix, msg, true);
    }

    public PrintStream getErrorPrintStream() {
        return getErrorStream();
    }

    public String toString(LockInfo lockInfo) {
        return String.format("\u00a72%s\u00a76@\u00a7b%s", lockInfo.getClassName(), lockInfo.getIdentityHashCode());
    }

    public String colored(Thread.State st) {
        return st.name();
    }

    @NotNull
    public BasicLogger printThreadInfo(@NotNull Thread thread, boolean fullFrames, boolean emptyPrefix) {
        super.printThreadInfo(thread, fullFrames, emptyPrefix);
        return this;
    }

    @NotNull
    public BasicLogger printThreadInfo(@NotNull ThreadInfo ti, boolean fullFrames, boolean emptyPrefix) {
        super.printThreadInfo(ti, fullFrames, emptyPrefix);
        return this;
    }

    public String toString(ThreadInfo inf, boolean fullFrames) {
        return factory.dump(inf, fullFrames);
    }

    protected final void printStackTraceElement(StackTraceElement stack) {
        printStackTraceElement(stack, true);
    }

    protected final void printStackTraceElement(String prefix, StackTraceElement stack) {
        printStackTraceElement(prefix, stack, true);
    }

    protected void printStackTraceElement(StackTraceElement stack, boolean err) {
        printStackTraceElement(null, stack, err);
    }

    protected BasicLogger write(boolean err, String msg) {
        if (err) {
            return error(msg);
        } else {
            return printf(msg);
        }
    }

    protected void printStackTraceElement(String prefix, StackTraceElement stack, boolean err) {
        if (prefix == null || prefix.isEmpty()) {
            write(err, getStackTraceElementMsg(stack));
        } else {
            write(err, prefix + getStackTraceElementMsg(stack));
        }
    }

    protected void printEnclosedStackTrace(Throwable thiz,
                                           StackTraceElement[] enclosingTrace,
                                           String caption,
                                           String prefix,
                                           Set<Throwable> dejaVu, boolean err) {
        printEnclosedStackTrace(thiz, enclosingTrace, caption, prefix, dejaVu, true, err);
    }

    /**
     * Print our stack trace as an enclosed exception for the specified stack
     * trace.
     */
    protected void printEnclosedStackTrace(Throwable thiz,
                                           StackTraceElement[] enclosingTrace,
                                           String caption,
                                           String prefix,
                                           Set<Throwable> dejaVu,
                                           boolean printStacks, boolean err) {
        super.printEnclosedStackTrace(thiz, enclosingTrace, caption, prefix, dejaVu, printStacks, err);
    }

    public String toString(Throwable thr) {
        return factory.dump(thr);
    }

    @NotNull
    public BasicLogger printStackTrace(@NotNull Throwable thr) {
        super.printStackTrace(thr);
        return this;
    }

    public BasicLogger log(Level lv, String mg) {
        if (lv.intValue() <= Level.INFO.intValue()) {
            return printf(mg);
        }
        return error(mg);
    }

    public BasicLogger publish(LogRecord record, java.util.logging.Handler h) {
        super.publish(record, h);
        return this;
    }

    public BasicLogger printStackTrace(Level lv, Throwable thr) {
        return printStackTrace(lv, thr, true);
    }

    public BasicLogger printStackTrace(Level lv, Throwable thr, boolean printStacks) {
        if (lv.intValue() <= Level.INFO.intValue()) {
            return printStackTrace(thr, printStacks, false);
        }
        return printStackTrace(thr, printStacks, true);
    }

    public BasicLogger printStackTrace(Throwable thr, boolean printStacks) {
        return printStackTrace(thr, printStacks, true);
    }

    @NotNull
    public BasicLogger printStackTrace(@NotNull Throwable thr, boolean printStacks, boolean err) {
        super.printStackTrace(thr, printStacks, err);
        return this;
    }

    @Override
    protected void writeLine(String pre, String message, boolean error) {
        write(this, pre + message);
    }

    @NotNull
    @Override
    protected String getPrefix(boolean error, String line, Level level, LogRecord lr) {
        return getPrefix(error, line);
    }

    protected String getPrefix(boolean error, String line) {
        return error ? errprefix : prefix;
    }

    public static enum PrintingType {
        RAW(0), COLORED(1), SKIP_COLOR(2);
        private final int type;

        public int type() {
            return type;
        }

        public String toString() {
            return name() + "[" + type + "]";
        }

        private PrintingType(int type) {
            this.type = type;
        }

        public static PrintingType valueOf(int code) {
            for (PrintingType t : values()) {
                if (code == t.type) {
                    return t;
                }
            }
            return null;
        }
    }

    /**
     * Logger's PrintStream
     */
    public class DefaultPrintStream extends cn.mcres.gyhhy.MXLib.io.PrintStream {

        private DefaultPrintStream() {
            super(cn.mcres.karlatemp.mxlib.tools.EmptyStream.stream.asOutputStream());
        }

        @Override
        public void print(String s) {
            BasicLogger.this.println(s);
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

    public class ErrorPrintStream extends DefaultPrintStream {

        private ErrorPrintStream() {
        }

        public void print(String x) {
            BasicLogger.this.error(x);
        }
    }
}
