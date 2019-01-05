/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.log;

import cn.mcres.gyhhy.MXLib.StringHelper;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Map;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Server;

/**
 *
 * @author 32798
 */
public final class Logger {

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
            Logger.this.println(s);
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
    public class ErrorPrintStream extends DefaultPrintStream{
        private ErrorPrintStream(){}
        public void print(String x){
            Logger.this.error(x);
        }
    }
    
    public static Server server() {
        return Bukkit.getServer();
    }

    public Logger println(String line) {
        write(prefix + line);
        return this;
    }
    public Logger printf(Object data){
        return printf(String.valueOf(data));
    }
    public Logger printf(String line, Object... argc) {
        write(prefix + StringHelper.variable(line, argc));
        return this;
    }

    public Logger printf(String line, Map<String, Object> argc) {
        write(prefix + StringHelper.variable(line, argc));
        return this;
    }

    public Logger printf(String line) {
        write(prefix + line);
        return this;
    }

    public Logger error(String line) {
        write(errprefix + line);
        return this;
    }

    public Logger error(String line, Object... argc) {
        write(errprefix + StringHelper.variable(line, argc));
        return this;
    }

    public Logger error(String line, Map<String, Object> argc) {
        write(errprefix + StringHelper.variable(line, argc));
        return this;
    }

    public Logger errorformat(String format, Object... argc) {
        write(errprefix + String.format(format, argc));
        return this;
    }

    public Logger format(String format, Object... argc) {
        write(prefix + String.format(format, argc));
        return this;
    }

    public static void write(String line) {
        server().getConsoleSender().sendMessage(line);
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

    public final Plugin getPlugin() {
        return plugin;
    }
    private final Plugin plugin;
    private final int index;
    private final String prefix;
    private final String errprefix;

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
    private DefaultPrintStream out,err;
    private Logger(Plugin plugin, String format, String errformat) {
        if (plugin == null) {
            throw new java.lang.IllegalArgumentException("No plugin found.");
        }
        if (format == null) {
            format = "\u00a7f[\u00a7b%s\u00a7f] \u00a7e";
        }
        if (errformat == null) {
            errformat = "\u00a7f[\u00a7b%s\u00a7f] \u00a7c";
        }
        prefix = String.format(format, plugin.getName());
        errprefix = String.format(errformat, plugin.getName());
        this.plugin = plugin;
        checkup(this, plugin);
        register(this);
        index = index(this);
        out = new DefaultPrintStream();
        err = new ErrorPrintStream();
    }
    public PrintStream getPrintStream(){
        return out;
    }
    public PrintStream getErrorPrintStream(){return err;}

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

    public static Logger getOrCreateLogger(Plugin plugin) {
        return getOrCreateLogger(plugin, null, null);
    }

    public static Logger getOrCreateLogger(Plugin plugin, String format) {
        String err = null;
        if (format != null) {
            err = format + "\u00a7c";
        }
        return getOrCreateLogger(plugin, format, err);
    }

    public static Logger getOrCreateLogger(Plugin plugin, String format, String errorFormat) {
        Logger logger = getLogger(plugin);
        if (logger == null) {
            logger = createLogger(plugin, format, errorFormat);
        }
        return logger;
    }

    public static Logger createLogger(Plugin plugin) {
        return createLogger(plugin, null, null);
    }

    public static Logger createLogger(Plugin plugin, String format) {
        String err = null;
        if (format != null) {
            err = format + "\u00a7c";
        }
        return createLogger(plugin, format, err);
    }

    public static Logger createLogger(Plugin plugin, String format, String errorFormat) {
        return new Logger(plugin, format, errorFormat);
    }

}
