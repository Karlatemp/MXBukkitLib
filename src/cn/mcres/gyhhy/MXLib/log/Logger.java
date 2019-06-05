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
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Server;

/**
 * a logger with color typography
 *
 * @author 32798
 */
public class Logger extends BasicLogger {

    /**
     * org.bukkit.Bukkit.getServer();
     */
    public static Server server() {
        return Bukkit.getServer();
    }

    /**
     * Print a line
     */
    public Logger println(String line) {
        super.println(line);
        return this;
    }

    /**
     * Print a line
     */
    public Logger printf(Object data) {
        super.printf(data);
        return this;
    }

    /**
     * Print a line with
     * {@link cn.mcres.gyhhy.MXLib.StringHelper#variable(java.lang.String,java.lang.Object[])}
     */
    public Logger printf(String line, Object... argc) {
        super.printf(line, argc);
        return this;
    }

    /**
     * Print a line with
     * {@link cn.mcres.gyhhy.MXLib.StringHelper#variable(java.lang.String,java.util.Map)}
     */
    public Logger printf(String line, Map<String, Object> argc) {
        super.printf(line, argc);
        return this;
    }

    /**
     * Print a line
     */
    public Logger printf(String line) {
        super.printf(line);
        return this;
    }

    /**
     * Print a line, use error prefix
     */
    public Logger error(String line) {
        super.error(line);
        return this;
    }

    /**
     * And {@link #printf(java.lang.String, java.lang.Object...)} different is
     * that she is using the error prefix
     */
    public Logger error(String line, Object... argc) {
        super.error(line, argc);
        return this;
    }

    /**
     * And {@link #printf(java.lang.String, java.util.Map) } different is that
     * she is using the error prefix
     */
    public Logger error(String line, Map<String, Object> argc) {
        super.error(line, argc);
        return this;
    }

    /**
     * And {@link #format(java.lang.String, java.lang.Object...) } different is
     * that she is using the error prefix
     */
    public Logger errorformat(String format, Object... argc) {
        super.errorformat(format, argc);
        return this;
    }

    /**
     * Print a line with
     *  {@link java.lang.String#format(java.lang.String, java.lang.Object...) }
     */
    public Logger format(String format, Object... argc) {
        super.format(format, argc);
        return this;
    }

    public void write(String line) {
        write(this, line);
    }

    /**
     * This method will write a line to console.
     */
    public static void write(BasicLogger logger, String line) {
        Server server = server();
        if (server == null) {
            System.out.println(Ascii.ec(line));
            return;
        }
        server.getConsoleSender().sendMessage(line);
    }
    private static final Object lock = new Object();
    private static Plugin[] px = new Plugin[0];
    private static Logger[] lger = new Logger[0];

    private static void checkup(Logger thiz, Plugin pl) {
        synchronized (lock) {
            for (int i = 0; i < lger.length; i++) {
                if (lger[i] == thiz) {
                    throw new java.lang.RuntimeException("This logger is store in catch.");
                }
            }
            for (int i = 0; i < px.length; i++) {
                if (px[i] == pl) {
                    throw new java.lang.RuntimeException("This plugin is registered a logger!");
                }
            }
        }
    }

    /**
     * Get the plugin to which the current Logger
     */
    public final Plugin getPlugin() {
        return plugin;
    }
    private final Plugin plugin;
    private final int index;

    /**
     * Get the current logger's id
     */
    public final int getId() {
        return index;
    }

    private static int index(Logger logger) {
        for (int i = 0; i < lger.length; i++) {
            if (lger[i] == logger) {
                return i;
            }
        }
        return -1;
    }

    private static void register(Logger logger) {
        checkup(logger, logger.plugin);
        synchronized (lock) {
            int size = lger.length + 1;
            int now = lger.length;
            Logger[] arr = new Logger[size];
            Plugin[] pxw = new Plugin[size];
            System.arraycopy(lger, 0, arr, 0, now);
            System.arraycopy(px, 0, pxw, 0, now);
            arr[now] = logger;
            pxw[now] = logger.plugin;
            px = pxw;
            lger = arr;
        }
    }

    public Logger(Plugin plugin, String format, String errformat) {
        super(format, errformat, plugin.getName());
        this.plugin = plugin;
//        checkup(this, plugin);
        register(this);
        index = index(this);
    }

    public PrintStream getPrintStream() {
        return super.getPrintStream();
    }

    public PrintStream getErrorPrintStream() {
        return super.getErrorPrintStream();
    }

    /**
     * Get logger based on id
     */
    public static Logger getLogger(int id) {
        synchronized (lock) {
            if (id > -1 && id < lger.length) {
                return lger[id];
            }
        }
        return null;
    }

    /**
     * Get the logger based on the plugin
     */
    public static Logger getLogger(Plugin plugin) {
        if (plugin == null) {
            return null;
        }
        synchronized (lock) {
            for (int i = 0; i < px.length; i++) {
                if (px[i] == plugin) {
                    return lger[i];
                }
            }
        }
        return null;
    }

    /**
     * Get/create a logger
     */
    public static Logger getOrCreateLogger(Plugin plugin) {
        return getOrCreateLogger(plugin, null, null);
    }

    /**
     * Get/create a logger
     */
    public static Logger getOrCreateLogger(Plugin plugin, String format) {
        String err = null;
        if (format != null) {
            err = format + "\u00a7c";
        }
        return getOrCreateLogger(plugin, format, err);
    }

    /**
     * Get/create a logger
     */
    public static Logger getOrCreateLogger(Plugin plugin, String format, String errorFormat) {
        Logger logger = getLogger(plugin);
        if (logger == null) {
            logger = createLogger(plugin, format, errorFormat);
        }
        return logger;
    }

    /**
     * Create a logger<br>
     * If the logger has been created before, it will throw a
     * {@code java.lang.RuntimeException} exception.
     */
    public static Logger createLogger(Plugin plugin) {
        return createLogger(plugin, null, null);
    }

    /**
     * Create a logger<br>
     * If the logger has been created before, it will throw a
     * {@code java.lang.RuntimeException} exception.
     */
    public static Logger createLogger(Plugin plugin, String format) {
        String err = null;
        if (format != null) {
            err = format + "\u00a7c";
        }
        return createLogger(plugin, format, err);
    }

    /**
     * Create a logger<br>
     * If the logger has been created before, it will throw a
     * {@code java.lang.RuntimeException} exception.
     */
    public static Logger createLogger(Plugin plugin, String format, String errorFormat) {
        return new Logger(plugin, format, errorFormat);
    }
    private static final Class<? extends ClassLoader> PluginClassLoaderClass;
    private static final Field PluginClassLoader$plugin$field;

    static {
        Field fie = null;
        Class<? extends ClassLoader> cz = null;
        try {
            cz = Class.forName("org.bukkit.plugin.java.PluginClassLoader").asSubclass(ClassLoader.class);
            fie = cz.getDeclaredField("plugin");
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        PluginClassLoaderClass = cz;
        PluginClassLoader$plugin$field = fie;
    }

    private static Plugin getPluginClassLoaderPlugin(ClassLoader loader) {
        if (!PluginClassLoaderClass.isInstance(loader)) {
            throw new java.lang.ClassCastException("Cannot cast this class loader to org.bukkit.plugin.java.PluginClassLoader");
        }
        try {
            PluginClassLoader$plugin$field.setAccessible(true);
            return (Plugin) PluginClassLoader$plugin$field.get(loader);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Error err) {
            throw err;
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        }
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
            ClassLoader loader = c.getClassLoader();
            if (PluginClassLoaderClass.isInstance(loader)) {
                version = getPluginClassLoaderPlugin(loader).getDescription().getVersion();
            } else {
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
        } catch (ClassNotFoundException | NoClassDefFoundError ex) {
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return Logger.getStackTraceElementMessage$return(stack, clazz, zip, version);
    }

    protected void printStackTraceElement(StackTraceElement stack, boolean err) {
        super.printStackTraceElement(stack, err);
    }

    protected void printStackTraceElement(String prefix, StackTraceElement stack, boolean err) {
        super.printStackTraceElement(prefix, stack, err);
    }

    protected void printEnclosedStackTrace(Throwable thiz,
            StackTraceElement[] enclosingTrace,
            String caption,
            String prefix,
            Set<Throwable> dejaVu, boolean err) {
        super.printEnclosedStackTrace(thiz, enclosingTrace, caption, prefix, dejaVu, err);
    }

    public String toString(Throwable thr) {
        return super.toString(thr);
    }

    public Logger printStackTrace(Throwable thr, boolean printStacks) {
        return printStackTrace(thr, printStacks, true);
    }

    @Override
    public Logger printStackTrace(Throwable thr, boolean printStacks, boolean err) {
        super.printStackTrace(thr, printStacks, err); //To change body of generated methods, choose Tools | Templates.
        return this;
    }

    public Logger printStackTrace(Throwable thr) {
        super.printStackTrace(thr);
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
    public Logger(String format, String errformat, String pname) {
        super(format, errformat, pname);
        this.plugin = null;
//        checkup(this, plugin);
//        register(this);
        index = -1;
    }

    public static Logger createRawLogger(String format, String errformat, String plugin_name) {
        return new Logger(format, errformat, plugin_name);
    }

    public static void main(String[] args) {
        Logger logger = new Logger((String) null, null, "Test");
        logger.printStackTrace(
                new Error("Test test and more test",
                        new RuntimeException("And deep dark more",
                                new OutOfMemoryError("TEST"))));
    }
}
