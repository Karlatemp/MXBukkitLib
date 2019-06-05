/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.log;

import java.util.Map;
import java.util.logging.Level;
import org.bukkit.plugin.Plugin;

/**
 * Add in version 0.8
 *
 * @author 32798
 */
public class MXLogger extends Logger {

    private java.util.logging.Logger out;

    public java.util.logging.Logger getLogger() {
        return out;
    }

    public MXLogger setLogger(java.util.logging.Logger log) {
        out = log;
        return this;
    }

    public MXLogger log(Level lv, String ln) {
        return log(lv, lv == Level.INFO ? prefix : errprefix, ln);
    }

    public MXLogger log(Level lv, String prefix, String ln) {
        if (out != null) {
            out.log(Level.INFO, Ascii.ec(prefix, ln, Ascii.RESET));
            return this;
        }
        super.printf(ln);
        return this;
    }

    @Override
    public MXLogger printf(String line) {
        if (out != null) {
            out.log(Level.INFO, Ascii.ec(prefix, line, Ascii.RESET));
            return this;
        }
        super.printf(line); //To change body of generated methods, choose Tools | Templates.
        return this;
    }

    @Override
    public void write(String line) {
        printf(line);
    }

    @Override
    public MXLogger error(String line) {
        if (out != null) {
            out.log(Level.SEVERE, Ascii.ec(errprefix, line, Ascii.RESET));
            return this;
        }
        super.error(line); //To change body of generated methods, choose Tools | Templates.
        return this;
    }

    @Override
    public MXLogger error(String line, Map<String, Object> argc) {
        super.error(line, argc); //To change body of generated methods, choose Tools | Templates.
        return this;
    }

    @Override
    public MXLogger error(String line, Object... argc) {
        super.error(line, argc); //To change body of generated methods, choose Tools | Templates.
        return this;
    }

    @Override
    public MXLogger errorformat(String format, Object... argc) {
        super.errorformat(format, argc); //To change body of generated methods, choose Tools | Templates.
        return this;
    }

    @Override
    public MXLogger format(String format, Object... argc) {
        super.format(format, argc); //To change body of generated methods, choose Tools | Templates.
        return this;
    }

    @Override
    public MXLogger printStackTrace(Throwable thr) {
        super.printStackTrace(thr); //To change body of generated methods, choose Tools | Templates.
        return this;
    }

    @Override
    public MXLogger println(String line) {
        super.println(line); //To change body of generated methods, choose Tools | Templates.
        return this;
    }

    @Override
    public MXLogger printf(Object data) {
        super.printf(data); //To change body of generated methods, choose Tools | Templates.
        return this;
    }

    @Override
    public MXLogger printf(String line, Map<String, Object> argc) {
        super.printf(line, argc); //To change body of generated methods, choose Tools | Templates.
        return this;
    }

    @Override
    public MXLogger printf(String line, Object... argc) {
        super.printf(line, argc); //To change body of generated methods, choose Tools | Templates.
        return this;
    }

    MXLogger(Plugin plugin, String format, String errformat) {
        super(plugin, format, errformat);
        this.setLogger(java.util.logging.Logger.getGlobal());

    }

    MXLogger(String format, String errformat, String pname) {
        super(format, errformat, pname);
        this.setLogger(java.util.logging.Logger.getGlobal());
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
    public static MXLogger createLogger(Plugin plugin) {
        return createLogger(plugin, null, null);
    }

    /**
     * Create a logger<br>
     * If the logger has been created before, it will throw a
     * {@code java.lang.RuntimeException} exception.
     */
    public static MXLogger createLogger(Plugin plugin, String format) {
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
    public static MXLogger createLogger(Plugin plugin, String format, String errorFormat) {
        return new MXLogger(plugin, format, errorFormat);
    }

    public static MXLogger createRawLogger(String format, String errformat, String plugin_name) {
        return new MXLogger(format, errformat, plugin_name);
    }

    public static void main(String[] test) {
        MXLogger log = createRawLogger(null, null, "FA");
        log.setLogger(java.util.logging.Logger.getAnonymousLogger());
        log.printf("FAQ");
    }
}
