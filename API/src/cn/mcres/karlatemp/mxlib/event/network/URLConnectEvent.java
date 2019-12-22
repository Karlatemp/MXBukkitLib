/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: URLConnectEvent.java@author: karlatemp@vip.qq.com: 19-12-21 下午5:34@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.event.network;

import cn.mcres.karlatemp.mxlib.event.Cancellable;
import cn.mcres.karlatemp.mxlib.event.Event;
import cn.mcres.karlatemp.mxlib.event.HandlerList;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;

public class URLConnectEvent extends Event implements Cancellable {
    private final URL url;
    private final Class<?> caller;
    private final Proxy proxy;
    private boolean c;
    private IOException cancel;

    public Proxy getProxy() {
        return proxy;
    }

    public IOException getCancel() {
        if (c)
            return cancel;
        return null;
    }

    public void setCancel(IOException cancel) {
        c = true;
        this.cancel = cancel;
    }

    public static final HandlerList<URLConnectEvent> handlers = new HandlerList<>();

    public URLConnectEvent(URL url, Proxy p, Class<?> caller) {
        this.url = url;
        this.caller = caller;
        this.proxy=p;
    }

    @Override
    public boolean isCancelled() {
        return c;
    }

    public Class<?> getCaller() {
        return caller;
    }

    public URL getURL() {
        return url;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        c = isCancelled;
    }

    @Override
    public HandlerList getHandlerList() {
        return handlers;
    }
}
