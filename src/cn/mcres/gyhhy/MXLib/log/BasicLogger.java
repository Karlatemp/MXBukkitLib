/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.log;

import cn.mcres.gyhhy.MXLib.StringHelper;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Scanner;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

/**
 * a logger with color typography
 *
 * @author 32798
 */
public class BasicLogger {

    /**
     * Logger's PrintStream
     */
    public class DefaultPrintStream extends cn.mcres.gyhhy.MXLib.io.PrintStream {

        private DefaultPrintStream() {
            super(cn.mcres.gyhhy.MXLib.io.EmptyStream.stream.asOutputStream());
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
    protected PrefixFormatter pf;

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

    /**
     * Print a line with
     * {@link cn.mcres.gyhhy.MXLib.StringHelper#variable(java.lang.String,java.lang.Object[])}
     */
    public BasicLogger printf(String line, Object... argc) {
        printf(StringHelper.variable(line, argc));
        return this;
    }

    /**
     * Print a line with
     * {@link cn.mcres.gyhhy.MXLib.StringHelper#variable(java.lang.String,java.util.Map)}
     */
    public BasicLogger printf(String line, Map<String, Object> argc) {
        printf(StringHelper.variable(line, argc));
        return this;
    }
    private static final Pattern linespl = Pattern.compile("\\n");

    /**
     * Print a line
     */
    public BasicLogger printf(String line) {
        write(getPrefix(line), line);
        return this;
    }

    protected static String excpre(String pre) {
        StringBuilder b = new StringBuilder();
        int lg = pre.length();
        // \u4E00-\u9FFF
        char[] cs = (pre.toCharArray());
        fe:
        for (int i = 0; i < cs.length; i++) {
            char c = cs[i];
            switch (c) {
                case '\u00a7': {
                    b.append('\u00a7').append(cs[++i]);
                    lg -= 2;
                    continue fe;
                }
            }
            if (c >= 0x4E00 && c <= 0x9FFF) {
                lg += 1;
            }
        }
        return StringHelper.fill(' ', lg) + b;
    }

    /**
     * Print a line, use error prefix
     */
    public BasicLogger error(String line) {
        write(getErrorPrefix(line), line);
        return this;
    }

    /**
     * And {@link #printf(java.lang.String, java.lang.Object...)} different is
     * that she is using the error prefix
     */
    public BasicLogger error(String line, Object... argc) {
        error(StringHelper.variable(line, argc));
        return this;
    }

    /**
     * And {@link #printf(java.lang.String, java.util.Map) } different is that
     * she is using the error prefix
     */
    public BasicLogger error(String line, Map<String, Object> argc) {
        error(StringHelper.variable(line, argc));
        return this;
    }

    /**
     * And {@link #format(java.lang.String, java.lang.Object...) } different is
     * that she is using the error prefix
     */
    public BasicLogger errorformat(String format, Object... argc) {
        error(String.format(format, argc));
        return this;
    }

    /**
     * Print a line with
     *  {@link java.lang.String#format(java.lang.String, java.lang.Object...) }
     */
    public BasicLogger format(String format, Object... argc) {
        printf(String.format(format, argc));
        return this;
    }

    /**
     * This method will write a line to console.
     */
    protected void write(String line) {
        write(this, line);
    }

    protected void write(String prefix, String line) {
        if (line.indexOf('\n') == -1) {
            write(prefix + line);
        } else {
            String[] ls = linespl.split(line);
            write(prefix + ls[0]);
            String f = excpre(prefix);
            for (int i = 1; i < ls.length; i++) {
                write(f + ls[i]);
            }
        }
    }

    /**
     * This method will write a line to console.
     */
    public static void write(BasicLogger bl, String line) {
        System.out.println(Ascii.ec(line) + Ascii.RESET);
    }

    protected String getPrefix(String msg) {
        return this.getPrefixFormatter().format(prefix, msg, false);
    }

    protected String getErrorPrefix(String msg) {
        return this.getPrefixFormatter().format(prefix, msg, true);
    }
    /**
     * Use {@link BasicLogger#getPrefix()}
     */
    @Deprecated
    protected final String prefix;
    /**
     * Use {@link BasicLogger#getErrorPrefix()}
     */
    @Deprecated
    protected final String errprefix;

    private final DefaultPrintStream out, err;

    public PrintStream getPrintStream() {
        return out;
    }

    public PrintStream getErrorPrintStream() {
        return err;
    }

    private static final Method getOurStackTrace$met;

    static {
        Method met = null;
        try {
            met = Throwable.class.getDeclaredMethod("getOurStackTrace");
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        getOurStackTrace$met = met;
    }

    protected static StackTraceElement[] getOurStackTrace(Throwable thr) {
        if (thr == null) {
            return null;
        }
        try {
            getOurStackTrace$met.setAccessible(true);
            return (StackTraceElement[]) getOurStackTrace$met.invoke(thr);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return thr.getStackTrace();
    }

    public static String getStackTraceElementMessage(StackTraceElement stack) {
        String zip = null;
        String version = null;
        String clazz = stack.getClassName();
        try {
            Class<?> c = Class.forName(clazz);
            URL url = c.getResource(c.getSimpleName() + ".class");
            if (url != null) {
                String toStr = url.toString();
                if (toStr.startsWith("jar:file:")) {
                    zip = toStr.replace("jar:file:", "").replaceFirst("!.*$", "").replaceFirst("^.*/", "");
                } else if (toStr.startsWith("file:")) {
                    zip = toStr.replace("file:", "").replaceFirst("." + clazz.replaceAll("\\$", ".") + ".class$", "").replaceFirst("^.*/", "");
                }
            }
            {
                InputStream stream = c.getResourceAsStream("/META-INF/MANIFEST.MF");
                if (stream != null) {
                    try (Scanner scanner = new Scanner(stream)) {
                        while (scanner.hasNextLine()) {
                            String next = scanner.nextLine();
                            if (next.startsWith("Implementation-Version:")) {
                                version = next.replace("Implementation-Version:", "").trim();
                                break;
                            }
                        }
                    }
                    stream.close();
                }
            }
        } catch (ClassNotFoundException | NoClassDefFoundError ex) {
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return getStackTraceElementMessage$return(stack, clazz, zip, version);
    }

    protected static String getStackTraceElementMessage$return(StackTraceElement stack, String clazz, String zip, String version) {
        return String.format("\t\u00a76at \u00a7c%s\u00a7f.\u00a7e%s\u00a7r(%s\u00a7r) [\u00a7b%s\u00a76:\u00a7d%s\u00a7r]",
                clazz, stack.getMethodName(),
                stack.isNativeMethod() ? "\u00a7dNative Method"
                : (stack.getFileName() == null
                ? "\u00a77Unknown Source"
                : (stack.getLineNumber() > -1
                ? "\u00a72" + stack.getFileName() + "\u00a7f:\u00a76" + stack.getLineNumber()
                : "\u00a72" + stack.getFileName())),
                zip == null ? "?" : zip,
                version == null ? "?" : version);
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
            write(err, getStackTraceElementMessage(stack));
        } else {
            write(err, prefix + getStackTraceElementMessage(stack));
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
     *
     * @param thiz throwable
     * @param enclosingTrace
     * @param caption
     * @param prefix
     * @param dejaVu
     * @param printStacks
     */
    protected void printEnclosedStackTrace(Throwable thiz,
            StackTraceElement[] enclosingTrace,
            String caption,
            String prefix,
            Set<Throwable> dejaVu,
            boolean printStacks, boolean err) {
//        assert Thread.holdsLock(s.lock());
        if (dejaVu.contains(thiz)) {
            write(err, "\t\u00a76[CIRCULAR REFERENCE:" + toString(thiz) + "\u00a76]");
        } else {
            dejaVu.add(thiz);
            // Compute number of frames in common between this and enclosing trace
            StackTraceElement[] trace = getOurStackTrace(thiz);
            int m = trace.length - 1;
            int n = enclosingTrace.length - 1;
            while (m >= 0 && n >= 0 && trace[m].equals(enclosingTrace[n])) {
                m--;
                n--;
            }
            int framesInCommon = trace.length - 1 - m;

            // Print our stack trace
            write(err, prefix + caption + toString(thiz));
            if (printStacks) {
                for (int i = 0; i <= m; i++) {
                    this.printStackTraceElement(prefix, trace[i], err);
                }
                if (framesInCommon != 0) {
                    write(err, prefix + "\t... " + framesInCommon + " more");
                }
            }

            // Print suppressed exceptions, if any
            for (Throwable se : thiz.getSuppressed()) {
                this.printEnclosedStackTrace(se, trace, SUPPRESSED_CAPTION, prefix + "\t", dejaVu, printStacks, err);
            }
//                se.printEnclosedStackTrace(s, trace, SUPPRESSED_CAPTION,
//                                           prefix +"\t", dejaVu);

            // Print cause, if any
            Throwable ourCause = thiz.getCause();
            if (ourCause != null) {
                this.printEnclosedStackTrace(ourCause, trace, CAUSE_CAPTION, prefix, dejaVu, printStacks, err);
            }
//                ourCause.printEnclosedStackTrace(s, trace, CAUSE_CAPTION, prefix, dejaVu);
        }
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

    public String toString(Throwable thr) {
        String s = thr.getClass().getName();
        String message = thr.getLocalizedMessage();
        //"\u00a7c%s\u00a7b: \u00a7e%s"
        return (message != null) ? ("\u00a7c" + s + "\u00a7b: \u00a7e" + message) : "\u00a7c" + s;
    }

    public BasicLogger printStackTrace(Throwable thr) {
        return printStackTrace(thr, true);
    }

    public BasicLogger log(Level lv, String mg) {
        if (lv.intValue() <= Level.INFO.intValue()) {
            return printf(mg);
        }
        return error(mg);
    }

    public BasicLogger publish(LogRecord record, java.util.logging.Handler h) {
//            publish(record,this);
        if (!h.isLoggable(record)) {
            return this;
        }
        Filter filter = h.getFilter();
        if (filter != null && !filter.isLoggable(record)) {
            return this;
        }
        Level lv = record.getLevel();
        String msg = record.getMessage();
        Throwable thr = record.getThrown();
        if (msg != null) {
            Formatter fe = h.getFormatter();
            record.setThrown(null);
            String mg = fe.formatMessage(record);
            record.setThrown(thr);
            log(lv, mg);
        }
        if (thr != null) {
            printStackTrace(lv, thr);
        }
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

    public BasicLogger printStackTrace(Throwable thr, boolean printStacks, boolean err) {
        // Guard against malicious overrides of Throwable.equals by
        // using a Set with identity equality semantics.
        Set<Throwable> dejaVu
                = Collections.newSetFromMap(new IdentityHashMap<>());
        dejaVu.add(thr);

//        synchronized (s.lock()) {
        // Print our stack trace
        write(err, toString(thr));
        StackTraceElement[] trace = getOurStackTrace(thr);
        if (printStacks) {
            for (StackTraceElement traceElement : trace) {
                this.printStackTraceElement(traceElement, err);
//            println("\tat " + traceElement);
            }
        }

        // Print suppressed exceptions, if any
        for (Throwable se : thr.getSuppressed()) //                se.printEnclosedStackTrace(s, trace, SUPPRESSED_CAPTION, "\t", dejaVu);
        {
            this.printEnclosedStackTrace(se, trace, SUPPRESSED_CAPTION, "\t", dejaVu, printStacks, err);
        }

        // Print cause, if any
        Throwable ourCause = thr.getCause();
        if (ourCause != null) {
            this.printEnclosedStackTrace(ourCause, trace, CAUSE_CAPTION, "", dejaVu, printStacks, err);
        }
//                ourCause.printEnclosedStackTrace(s, trace, CAUSE_CAPTION, "", dejaVu);
//        }
        return this;
    }

    /*public Logger printStackTrace(Throwable thr) {
        if (thr == null) {
            return this;
        }
        String clazz = thr.getClass().getName();
        String msg = thr.getMessage();
        StackTraceElement[] stackTrace = getOurStackTrace(thr);
        synchronized (this) {
            errorformat("\u00a7c%s\u00a7b: \u00a7e%s", clazz, msg);
            for (StackTraceElement el : stackTrace) {
                printStackTraceElement(el);
            }
            Throwable cause = thr.getCause();
            if (cause != null) {
                StackTraceElement[] trace = getOurStackTrace(cause);
                int m = trace.length - 1;
                int n = stackTrace.length - 1;
                while (m >= 0 && n >= 0 && trace[m].equals(stackTrace[n])) {
                    m--;
                    n--;
                }
                int framesInCommon = trace.length - 1 - m;
                clazz = cause.getClass().getName();
                msg = cause.getMessage();
                errorformat("\u00a73Cause by: \u00a7c%s\u00a7b: \u00a7e%s", clazz, msg);
                for (int i = 0; i <= m; i++) {
                    this.printStackTraceElement(trace[i]);
                }
                if (framesInCommon != 0) {
                    errorformat("\u00a73\t...\u00a76%s\u00a7bmore", framesInCommon);
                }

            }
        }
        return this;
    }*/
    @SuppressWarnings("AssignmentToMethodParameter")
    public BasicLogger(String format, String errformat, String pname) {
        if (format == null) {
            format = defaultFormat;
        }
        if (errformat == null) {
            errformat = defaultErrFormat;
        }
        prefix = String.format(format, pname);
        errprefix = String.format(errformat, pname);
//        checkup(this, plugin);
//        register(this);
        out = new DefaultPrintStream();
        err = new ErrorPrintStream();
    }

    @SuppressWarnings("AssignmentToMethodParameter")
    public BasicLogger(String prefix, String errprefix) {
        if (prefix == null) {
            prefix = defaultFormat;
        }
        if (errprefix == null) {
            errprefix = defaultErrFormat;
        }
        this.prefix = prefix;
        this.errprefix = errprefix;
//        checkup(this, plugin);
//        register(this);
        out = new DefaultPrintStream();
        err = new ErrorPrintStream();
    }
    private static String defaultFormat = "\u00a7r[\u00a7b%s\u00a7r]\u00a7e ",
            defaultErrFormat = "\u00a7r[\u00a7b%s\u00a7r]\u00a7c ";

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
}
