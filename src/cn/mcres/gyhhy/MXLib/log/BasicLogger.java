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

/**
 * a logger with color typography
 *
 * @author 32798
 */
public class BasicLogger {

    /**
     * Logger's PrintStream
     */
    public class DefaultPrintStream extends PrintStream {

        private DefaultPrintStream() {
            super(cn.mcres.gyhhy.MXLib.io.EmptyStream.stream.asOutputStream());
        }

        @Override
        public PrintStream format(String format, Object... args) {
            print(String.format(format, args));
            return this;
        }

        @Override
        public PrintStream format(Locale l, String format, Object... args) {
            print(String.format(l, format, args));
            return this;
        }

        @Override
        public void print(Object obj) {
            print(String.valueOf(obj));
        }

        @Override
        public void print(String s) {
            BasicLogger.this.println(s);
        }

        @Override
        public void print(boolean b) {
            print(String.valueOf(b));
        }

        @Override
        public void print(char c) {
            print(String.valueOf(c));
        }

        @Override
        public void print(char[] s) {
            print(String.valueOf(s));
        }

        @Override
        public void print(double d) {
            print(String.valueOf(d));
        }

        @Override
        public void print(float f) {
            print(String.valueOf(f));
        }

        @Override
        public void print(int i) {
            print(String.valueOf(i));
        }

        @Override
        public void print(long l) {
            print(String.valueOf(l));
        }

        @Override
        public PrintStream printf(String format, Object... args) {
            return format(format, args);
        }

        @Override
        public PrintStream printf(Locale l, String format, Object... args) {
            return format(l, format, args);
        }

        @Override
        public void println() {
            print("");
        }

        @Override
        public void println(Object x) {
            print(x);
        }

        @Override
        public void println(String x) {
            print(x);
        }

        @Override
        public void println(boolean x) {
            print(x);
        }

        @Override
        public void println(char x) {
            print(x);
        }

        @Override
        public void println(char[] x) {
            print(x);
        }

        @Override
        public void println(double x) {
            print(x);
        }

        @Override
        public void println(float x) {
            print(x);
        }

        @Override
        public void println(int x) {
            print(x);
        }

        @Override
        public void println(long x) {
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

    /**
     * Print a line
     */
    public BasicLogger printf(String line) {
        write(prefix + line);
        return this;
    }

    /**
     * Print a line, use error prefix
     */
    public BasicLogger error(String line) {
        write(errprefix + line);
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
    public static void write(String line) {
            System.out.println(Ascii.ec(line));
    }

    protected final String prefix;
    protected  final String errprefix;


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
                    Scanner scanner = new Scanner(stream);
                    while (scanner.hasNextLine()) {
                        String next = scanner.nextLine();
                        if (next.startsWith("Implementation-Version:")) {
                            version = next.replace("Implementation-Version:", "").trim();
                            break;
                        }
                    }
                    scanner.close();
                    stream.close();
                }
            }
        } catch (ClassNotFoundException ex) {
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return getStackTraceElementMessage$return(stack,clazz,zip,version);
    }
    protected static String getStackTraceElementMessage$return(StackTraceElement stack,String clazz,String zip,String version){
        return String.format("\t\u00a76at \u00a7c%s\u00a7f.\u00a7e%s\u00a7f(%s\u00a7f) [\u00a7b%s\u00a76:\u00a7d%s\u00a7f]",
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

    protected void printStackTraceElement(StackTraceElement stack) {
        printStackTraceElement(null, stack);
    }

    protected void printStackTraceElement(String prefix, StackTraceElement stack) {
        if (prefix == null || prefix.isEmpty()) {
            error(getStackTraceElementMessage(stack));
        } else {
            error(prefix + getStackTraceElementMessage(stack));
        }
    }

    /**
     * Print our stack trace as an enclosed exception for the specified stack
     * trace.
     */
    protected void printEnclosedStackTrace(Throwable thiz,
            StackTraceElement[] enclosingTrace,
            String caption,
            String prefix,
            Set<Throwable> dejaVu) {
//        assert Thread.holdsLock(s.lock());
        if (dejaVu.contains(thiz)) {
            error("\t\u00a76[CIRCULAR REFERENCE:" + toString(thiz) + "\u00a76]");
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
            error(prefix + caption + toString(thiz));
            for (int i = 0; i <= m; i++) {
                this.printStackTraceElement(prefix, trace[i]);
            }
            if (framesInCommon != 0) {
                error(prefix + "\t... " + framesInCommon + " more");
            }

            // Print suppressed exceptions, if any
            for (Throwable se : thiz.getSuppressed()) {
                this.printEnclosedStackTrace(se, trace, SUPPRESSED_CAPTION, prefix + "\t", dejaVu);
            }
//                se.printEnclosedStackTrace(s, trace, SUPPRESSED_CAPTION,
//                                           prefix +"\t", dejaVu);

            // Print cause, if any
            Throwable ourCause = thiz.getCause();
            if (ourCause != null) {
                this.printEnclosedStackTrace(ourCause, trace, CAUSE_CAPTION, prefix, dejaVu);
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
        // Guard against malicious overrides of Throwable.equals by
        // using a Set with identity equality semantics.
        Set<Throwable> dejaVu
                = Collections.newSetFromMap(new IdentityHashMap<Throwable, Boolean>());
        dejaVu.add(thr);

//        synchronized (s.lock()) {
        // Print our stack trace
        error(toString(thr));
        StackTraceElement[] trace = getOurStackTrace(thr);
        for (StackTraceElement traceElement : trace) {
            this.printStackTraceElement(traceElement);
//            println("\tat " + traceElement);
        }

        // Print suppressed exceptions, if any
        for (Throwable se : thr.getSuppressed()) //                se.printEnclosedStackTrace(s, trace, SUPPRESSED_CAPTION, "\t", dejaVu);
        {
            this.printEnclosedStackTrace(se, trace, SUPPRESSED_CAPTION, "\t", dejaVu);
        }

        // Print cause, if any
        Throwable ourCause = thr.getCause();
        if (ourCause != null) {
            this.printEnclosedStackTrace(ourCause, trace, CAUSE_CAPTION, "", dejaVu);
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
    public BasicLogger(String format, String errformat, String pname) {
        if (format == null) {
            format = "\u00a7f[\u00a7b%s\u00a7f] \u00a7e";
        }
        if (errformat == null) {
            errformat = "\u00a7f[\u00a7b%s\u00a7f] \u00a7c";
        }
        prefix = String.format(format, pname);
        errprefix = String.format(errformat, pname);
//        checkup(this, plugin);
//        register(this);
        out = new DefaultPrintStream();
        err = new ErrorPrintStream();
    }
    public static BasicLogger createRawLogger(String format, String errformat, String plugin_name){
        return new BasicLogger(format,errformat,plugin_name);
    }
}
