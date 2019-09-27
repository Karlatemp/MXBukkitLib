/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NListener.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.legacy;

import cn.mcres.gyhhy.MXLib.event.NetURIConnectEvent;
import cn.mcres.gyhhy.MXLib.http.WebHelper;
import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.network.NetWorkManager;
import cn.mcres.karlatemp.mxlib.share.BukkitToolkit;
import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.net.*;
import java.util.Objects;

import static cn.mcres.gyhhy.MXLib.event.EventHelper.plon;

public class NListener implements NetWorkManager.NetWorkListener {
    static class HandlerMapping extends URLStreamHandler {
        private final URLStreamHandler stream;
        private final int port;

        HandlerMapping(URLStreamHandler stream, int port) {
            this.stream = stream;
            this.port = port;
        }

        @Override
        protected void parseURL(URL u, String spec, int start, int limit) {
            try {
                URL nw = new URL(null, spec, stream);
                setURL(u, nw.getProtocol(), nw.getHost(), nw.getPort(), nw.getAuthority(), nw.getUserInfo(), nw.getPath(), nw.getQuery(), nw.getRef());
            } catch (MalformedURLException e) {
                ThrowHelper.thrown(e);
            }
        }

        private Plugin getPlugin() {
            Class[] context = Toolkit.StackTrace.getClassContext();
            for (int i = 3; i < context.length; i++) {
                Class c = context[i];
                if (WebHelper.class.isAssignableFrom(c)) { // WebHelper not the real caller
                    continue;
                }
                Plugin p = BukkitToolkit.getPluginByClass(c);
                if (p != null) return p;// Return the first plugin
            }
            return null;
        }

        protected void post(URL url) throws IOException {
            NetURIConnectEvent ev;
            try {
                ev = new NetURIConnectEvent(url.toURI(), getPlugin());
            } catch (URISyntaxException e) {
                throw new IOException(e);
            }
            if (plon(ev, false).isCancelled()) {
                IOException thr = ev.getCancelThrow();
                if (thr == null) {
                    thr = new IOException("Network connect cancelled");
                }
                throw thr;
            }
        }

        @Override
        protected URLConnection openConnection(URL u) throws IOException {
            post(u);
            return new URL(null, u.toExternalForm(), stream).openConnection();
        }

        @Override
        protected URLConnection openConnection(URL u, Proxy p) throws IOException {
            post(u);
            return new URL(null, u.toExternalForm(), stream).openConnection(p);
        }

        @Override
        protected synchronized InetAddress getHostAddress(URL u) {
            return super.getHostAddress(u);
        }

        @Override
        protected int getDefaultPort() {
            return port;
        }
    }

    @Override
    public URLStreamHandler getURLStreamHandler(URLStreamHandler handler, String key) {
        switch (key.toLowerCase()) {
            case "http":
                return new HandlerMapping(handler, 80);
            case "https":
                return new HandlerMapping(handler, 443);
        }
        return handler;
    }

    @Override
    public URLStreamHandler changeURLStreamHandler(URLStreamHandler handler, String key) {
        return handler;
    }

    @Override
    public boolean doClear(boolean run, Void unused) {
        return run;
    }
}
