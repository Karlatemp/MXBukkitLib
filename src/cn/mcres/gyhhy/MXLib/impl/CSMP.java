/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.impl;

import cn.mcres.gyhhy.MXLib.ThrowHelper;
import cn.mcres.gyhhy.MXLib.ext.lookup.Looker;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.logging.ConsoleHandler;
import java.util.logging.ErrorManager;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class CSMP extends ConsoleHandler {

    private final ConsoleHandler sup;
    private static final MethodHandle reportError, setOutputStream;
//            SetSup;
    private static volatile transient ConsoleHandler mapp;

    static {
        Looker lk = new Looker(Looker.openLookup(CSMP.class, ~0));
        reportError = lk.findVirtual(ConsoleHandler.class, "reportError", MethodType.methodType(
                void.class, String.class, Exception.class, int.class
        ));
        setOutputStream = lk.findVirtual(ConsoleHandler.class, "setOutputStream", MethodType.methodType(
                void.class, OutputStream.class
        ));
//        SetSup = lk.findSetter(CSMP.class, "sup", ConsoleHandler.class);
    }

    public CSMP(ConsoleHandler ch) {
//        super();
        this.sup = ch;
    }

    @Override
    public synchronized void setEncoding(String encoding) throws SecurityException, UnsupportedEncodingException {
        if (sup != null) {
            sup.setEncoding(encoding);
        }
    }

    @Override
    protected synchronized void setOutputStream(OutputStream out) throws SecurityException {
        try {
            if (sup != null) {
                setOutputStream.invoke(sup, out); //To change body of generated methods, choose Tools | Templates.
            }
        } catch (SecurityException se) {
            throw se;
        } catch (Throwable ex) {
            ThrowHelper.getInstance().thr(ex);
        }
    }

    @Override
    public synchronized void setLevel(Level newLevel) throws SecurityException {
        if (sup != null) {
            sup.setLevel(newLevel); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    public synchronized void setFormatter(Formatter newFormatter) throws SecurityException {
        if (sup != null) {
            sup.setFormatter(newFormatter); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    public synchronized void setFilter(Filter newFilter) throws SecurityException {
        if (sup != null) {
            sup.setFilter(newFilter); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    public synchronized void setErrorManager(ErrorManager em) {
        if (sup != null) {
            sup.setErrorManager(em); //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    protected void reportError(String msg, Exception ex, int code) {
        try {
            if (sup != null) {
                reportError.invoke(reportError, msg, ex, code);
            }
        } catch (Throwable ex1) {
            ThrowHelper.getInstance().thr(ex1);
        }
    }

    @Override
    public boolean isLoggable(LogRecord record) {
        if (sup != null) {
            return sup.isLoggable(record); //To change body of generated methods, choose Tools | Templates.
        }
        return super.isLoggable(record);
    }

    @Override
    public int hashCode() {
        if (sup != null) {
            return sup.hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (sup == null) {
            return obj == null;
        }
        if (obj == this) {
            return true;
        }
        if (sup == obj) {
            return true;
        }
        if (sup.equals(obj)) {
            return true;
        }
        if (obj instanceof CSMP) {
            return sup.equals(((CSMP) obj).sup);
        }
        return false;
    }

    @Override
    public Level getLevel() {
        if (sup != null) {
            return sup.getLevel();
        }
        return super.getLevel();
    }

    @Override
    public Formatter getFormatter() {
        if (sup != null) {
            return sup.getFormatter();
        }
        return super.getFormatter();
    }

    @Override
    public Filter getFilter() {
        if (sup != null) {
            return sup.getFilter();
        }
        return super.getFilter();
    }

    @Override
    public ErrorManager getErrorManager() {
        if (sup != null) {
            return sup.getErrorManager();
        }
        return super.getErrorManager();
    }

    @Override
    public String getEncoding() {
        if (sup != null) {
            return sup.getEncoding();
        }
        return super.getEncoding();
    }

    @Override
    public void publish(LogRecord record) {
        if (sup == null) {
            return;
        }
        Filter ft = this.getFilter();
        if (ft != null && !ft.isLoggable(record)) {
            return;
        }
        sup.publish(record);
    }

    @Override
    public synchronized void flush() {
        if (sup != null) {
            sup.flush();
        }
    }

    @Override
    public void close() {
        if (sup != null) {
            sup.close();
        }
    }

}
