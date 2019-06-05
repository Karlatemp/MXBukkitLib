package cn.mcres.gyhhy.MXLib.impl;

import cn.mcres.gyhhy.MXLib.ThrowHelper;
import static cn.mcres.gyhhy.MXLib.event.EventHelper.*;
import cn.mcres.gyhhy.MXLib.event.*;
import cn.mcres.gyhhy.MXLib.ext.java.util.ComparatorSet;
import cn.mcres.gyhhy.MXLib.log.BasicLogger;
import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.List;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;

public class SSMapping {

    /**
     * No Error Filter
     */
    protected static class NError implements Filter {

        final Filter par;
        private final Set<String> c;
        private final boolean noThr;

        NError(Filter par, java.util.Collection<String> dts, boolean noThr) {
            this.par = par;
            this.c = new cn.mcres.gyhhy.MXLib.ext.java.util.ComparatorSet<>();
            if (dts != null) {
                dts.forEach(x -> {
                    c.add(x.toLowerCase());
                });
            }
//            System.out.println(c);
            this.noThr = noThr;
        }

        @Override
        public boolean isLoggable(LogRecord record) {
            return isLoggable(record, true);
        }

        public boolean isLoggable(LogRecord record, boolean z) {
            if (z && noThr) {
                Throwable thr = record.getThrown();
                if (thr instanceof OutOfMemoryError) {
                } else if (thr != null) {
                    return false;
                }
            }
            String lw = record.getLevel().getName().toLowerCase();
//            System.out.println(lw);
            if (c.contains(lw)) {
                return false;
            }
            if (par == null) {
                return true;
            }
            return par.isLoggable(record);
        }
    }

    static Server s() {
        return Bukkit.getServer();
    }

    public static void main(String[] args) throws Throwable {
        String msg = "[AWA]";

        if (msg.charAt(0) == '[') {
            int ind = msg.indexOf(']');
            if (ind != -1) {
                String name = msg.substring(1, ind);
                String text = msg.substring(ind + 1, msg.length());
                System.out.println(name);
                System.out.println(text);
                System.out.println(ind);
                System.out.println(msg.length());
            }
        }
    }

    protected static void NetEventListen() {
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            final ProxySelector df = ProxySelector.getDefault();
            class PS extends ProxySelector {

                @Override
                public List<Proxy> select(URI uri) {
                    NetURIConnectEvent ev = new NetURIConnectEvent(uri);
                    if (plon(ev).isCancelled()) {
                        IOException thr = ev.getCancelThrow();
                        if (thr == null) {
                            thr = new IOException("Network connect cancelled");
                        }
                        ThrowHelper.getInstance().thr(thr);
                    }
                    return df.select(uri);
                }

                @Override
                public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                    df.connectFailed(uri, sa, ioe);
                    plon(new NetURIConnectFailedEvent(uri, sa, ioe));
                }
            }
            PS.setDefault(new PS());
            return null;
        });
    }

    protected static void LogEventListen() {
        java.util.logging.Logger root = root();
        java.util.logging.Handler h;
        root.addHandler(h = new java.util.logging.Handler() {
            {
                ConsoleHandler rc = rc();
                setFormatter(rc.getFormatter());
                setLevel(rc.getLevel());
                setErrorManager(rc.getErrorManager());
            }

            @Override
            public void publish(LogRecord record) {
                plon(new LoggerPublishEvent(root, this, record));
            }

            @Override
            public void flush() {
                plon(new LoggerFlushEvent(root, this));
            }

            @Override
            public void close() {
                plon(new LoggerCloseEvent(root, this));
            }
        });
        onDIS.add(() -> root.removeHandler(h));
    }

    static java.util.logging.Logger root() {
        java.util.logging.Logger root = java.util.logging.Logger.getGlobal(),
                temp = root;
        while (temp != null) {
            root = temp;
            temp = temp.getParent();
        }
        return root;
    }

    static ConsoleHandler rc() {
        java.util.logging.Logger r = root();
        Handler[] hc = r.getHandlers();
        for (Handler h : hc) {
            if (h instanceof ConsoleHandler) {
                return (ConsoleHandler) h;
            }
        }
        return null;
    }
    static List<Runnable> onDIS = new ArrayList<>();

    public static void onPlDSB(Plugin pl) {
        onDIS.forEach(Runnable::run);
    }

    protected static void CloseDefaultConsoleHandlerErrorShow(List<String> deny, boolean noThr) {
        final ConsoleHandler h = rc();
        Logger root = root();

        final ConsoleHandler sx = new CSMP(h);
        root.addHandler(sx);
        root.removeHandler(h);
        Filter old;
        sx.setFilter(new NError(old = h.getFilter(), deny, noThr));
        onDIS.add(0, () -> {
            root.removeHandler(sx);
            root.addHandler(h);
            h.setFilter(old);
        });
//        return ne;
    }

    @java.lang.SuppressWarnings({"unchecked", "rawtypes"})
    protected static void UseLibsLogger() {
        final ConsoleHandler hd = rc();
        Logger root = root();
        root.removeHandler(hd);
        class KV<K, V> {

            K k;
            V v;

            @Override
            public String toString() {
                return String.valueOf(k);
            }
        }
        Comparator p = (a, b) -> {
            return String.valueOf(a).compareTo(String.valueOf(b));
        };
        final ComparatorSet<KV<String, BasicLogger>> st = new ComparatorSet<>(p);
        final ComparatorSet<KV<String, BasicLogger>> st2 = new ComparatorSet<>(p);
//        final ComparatorSet<KV<String, KV<BasicLogger, BasicLogger>>> nmap = new ComparatorSet<>(p);
        Handler h;
        java.util.logging.Logger r = root();
        final Pattern crp = Pattern.compile("\\u00a7[0-9a-f]", Pattern.CASE_INSENSITIVE);
        r.addHandler(h = new Handler() {
            @Override
            public boolean isLoggable(LogRecord record) {
                return super.isLoggable(record);
            }

            @Override
            public void publish(LogRecord record) {
                if (!this.isLoggable(record)) {
                    return;
                }
                String msg = record.getMessage();
                String ne = record.getLoggerName();
                String ky = ne;
                String p = null, ep = null;
                if (!msg.isEmpty()) {
                    if (msg.charAt(0) == '[') {
                        int ind = msg.indexOf(']');
                        if (ind != -1) {
                            String name = msg.substring(1, ind);
                            int s = ind + 1, e = msg.length();
                            //p = BasicLogger.getDefaultFormat().trim();
                            //ep = BasicLogger.getDefaultErrorFormat().trim();
                            String text;
                            if (s < e) {
                                if (msg.charAt(s) == ' ') {
                                    s++;
                                }
                            }
                            if (s < e) {
                                text = msg.substring(s, msg.length());
                            } else {
                                text = "";
                            }
                            record.setMessage(text);
                            ne = name;
                        }
                    }
                }
                if (ep == null) {
                    ep = BasicLogger.getDefaultErrorFormat();
                }
                if (p == null) {
                    p = BasicLogger.getDefaultFormat();
                }
                run:
                do {
                    switch (ky) {
                        case "Minecraft": {
                            ep = crp.matcher(ep).replaceFirst("\u00a79");
                            p = crp.matcher(p).replaceFirst("\u00a79");
                            break run;
                        }
                        case "global": {
                            ep = crp.matcher(ep).replaceFirst("\u00a76");
                            p = crp.matcher(p).replaceFirst("\u00a76");
                            break run;
                        }
                    }
                    if (ky.startsWith("com.earth2me.essentials")) {
                        ep = crp.matcher(ep).replaceFirst("\u00a75");
                        p = crp.matcher(p).replaceFirst("\u00a75");
                        break;
                    }
                } while (false);
                KV<String, BasicLogger> k = st.search(ky);
                if (k == null) {
                    k = new KV<>();
                    k.k = ky;
                    k.v = FD(st2, p, ep, ne);
                    st.add(k);
                }
                k.v.publish(record, this);
            }

            @Override

            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }

            private BasicLogger FD(ComparatorSet<KV<String, BasicLogger>> st, String p, String ep, String ne) {
                KV<String, BasicLogger> k = st.search(ne);
                if (k == null) {
                    k = new KV<>();
                    k.k = ne;
                    k.v = BasicLogger.createRawLogger(p, ep, ne);
                    st.add(k);
                }
                return k.v;
            }
        }
        );
        h.setFormatter(hd.getFormatter());
        h.setLevel(hd.getLevel());
        h.setErrorManager(hd.getErrorManager());
        onDIS.add(0, () -> {
            root.removeHandler(h);
            root.addHandler(hd);
        });
    }

    public static void onPlEnb(Plugin pl) {
        ConsoleHandler ch = rc();
        /*System.out.println("SystemConsoleHandler: " + ch);
        System.out.println("                     " + ch.getClass());
        System.out.println("SystemFormatter: " + ch.getFormatter());*/
//        System.out.println(ch.getFormatter());
        FileConfiguration cf = pl.getConfig();
        if (cf.getBoolean("events.NetEvent", true)) {
            NetEventListen();
        }
        if (cf.getBoolean("events.LogEvent", true)) {
            LogEventListen();
        }
        {
            List<String> list = cf.getStringList("logging.SystemNoLogging");
            boolean nt = cf.getBoolean("logging.removeSystemErrorLogging", false);
            if ((!list.isEmpty()) || nt) {
                CloseDefaultConsoleHandlerErrorShow(list, nt);
            }
        }
        if (cf.getBoolean("logging.useLibsLogger", false)) {
            UseLibsLogger();
        }
    }
}
