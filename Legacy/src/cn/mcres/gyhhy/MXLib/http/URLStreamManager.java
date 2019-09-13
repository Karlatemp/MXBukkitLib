package cn.mcres.gyhhy.MXLib.http;

import cn.mcres.gyhhy.MXLib.RefUtil;
import static cn.mcres.gyhhy.MXLib.RefUtilEx.invoke;
import cn.mcres.gyhhy.MXLib.encode.Base64Actuator;
import cn.mcres.gyhhy.MXLib.ThrowHelper;
import cn.mcres.gyhhy.MXLib.ext.lookup.Looker;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("UseOfObsoleteCollectionType")
public class URLStreamManager {

    private static final Looker lk = new Looker(Looker.openLookup(URL.class, ~0));
    private static final MethodHandle factory$getter = lk.findStaticGetter(URL.class, "factory", URLStreamHandlerFactory.class);
    private static final MethodHandle factory$setter = lk.findStaticSetter(URL.class, "factory", URLStreamHandlerFactory.class);
    private static final MethodHandle handlers$getter;
    private static final MethodHandle getURLStreamHandler = lk.findStatic(URL.class, "getURLStreamHandler", MethodType.methodType(URLStreamHandler.class, String.class));
    private static final Object streamHandlerLock = invoke(lk.findStaticGetter(URL.class, "streamHandlerLock", Object.class));

    static {
        MethodHandle mm;
        try {
            mm = lk.unreflectGetter(URL.class.getDeclaredField("handlers"));
        } catch (NoSuchFieldException | SecurityException thr) {
            mm = lk.findStaticGetter(URL.class, "handlers", java.util.Hashtable.class);
        }
        handlers$getter = mm;
    }

    public static URLStreamHandlerFactory getURLStreamHandlerFactory() {
        return invoke(factory$getter);
    }

    @Deprecated
    public static void setURLStreamHandlerFactory(URLStreamHandlerFactory ff) {
        synchronized (streamHandlerLock) {
            invoke(factory$setter, null);
            URL.setURLStreamHandlerFactory(ff);
            inits();
        }
    }

    public static URLStreamHandler getURLStreamHandler(String protocol) {
        try {
            return (URLStreamHandler) getURLStreamHandler.invoke(protocol);
        } catch (Throwable ex) {
            return ThrowHelper.getInstance().thr(ex);
        }
    }

    public static boolean registerHandler(String protocol, URLStreamHandler handler) {
        protocol = protocol.toLowerCase();
        synchronized (streamHandlerLock) {
            Map<String, URLStreamHandler> handlers = getHandlers();
            URLStreamHandler ush = handlers.get(protocol);
            if (ush == null) {
                handlers.put(protocol, handler);
                return true;
            } else if (ush == handler) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public static Map<String, URLStreamHandler> getHandlers() {
        return invoke(handlers$getter);
    }

    public static void main(String[] args) throws Exception {
        Base64Actuator ua = Base64Actuator.getInstance();
        String ww = ua.encodeToString("AABWIHXPOOWOJ82uiD(N@JWskjxoiauriorhae97y58qtyrhcn9n w4ywy9ur30oqu");
        System.out.println(ww);
        URL u = new URL("base64://wjix/ww/wjdxw/wxwxws");
        System.out.println(u);
        System.out.println(u.getAuthority());
        System.out.println(u.getPath());
        System.out.println(u.getHost());
        System.out.println(u.getFile());
    }

    private static final Map<String, URLStreamHandler> lochandlers = new HashMap<>();

    private static void inits() {
        registerLocHandler("base64");
        registerLocHandler("data");
        registerLocHandler("rcon");
    }

    static {
        inits();
    }

    private static void registerLocHandler(String p) {
        synchronized (streamHandlerLock) {
            String cname = URLStreamManager.class.getPackage().getName() + ".handlers." + p + ".Handler";
            URLStreamHandler ush = lochandlers.get(cname);
            if (ush != null) {
                registerHandler(p, ush);
                return;
            }
            Class<?> c = RefUtil.safeLoadClass(cname);
            if (c == null) {
                throw new NoClassDefFoundError(cname);
            }
            try {
                ush = c.asSubclass(URLStreamHandler.class).newInstance();
                lochandlers.put(cname, ush);
                registerHandler(p, ush);
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void load() {
        streamHandlerLock.getClass();
    }
}
