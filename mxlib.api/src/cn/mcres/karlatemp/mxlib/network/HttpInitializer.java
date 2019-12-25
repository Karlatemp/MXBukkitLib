/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: HttpInitializer.java@author: karlatemp@vip.qq.com: 19-12-21 下午7:21@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLEngine;
import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * Create at 2019/12/21 19:21
 * Copyright Karlatemp
 * MXBukkitLibRebuild $ cn.mcres.karlatemp.mxlib.network
 */
public class HttpInitializer extends ChannelInitializer<Channel> {
    private final boolean ssl;
    private final long rt;
    private final TimeUnit ru;
    private final URI url;
    private final int port;
    private final HttpCallback callback;
    private final int depth;
    private final int maxDepth;
    private final int ct;

    public HttpInitializer(@NotNull HttpCallback callback,
                           @NotNull URI url,
                           boolean ssl, int port, int connect_timeout, long read_timeout,
                           @NotNull TimeUnit readTimeoutUnit,
                           int depth, int maxDepth) {
        this.ssl = ssl;
        this.ct = connect_timeout;
        this.rt = read_timeout;
        this.ru = readTimeoutUnit;
        this.url = url;
        this.port = port;
        this.callback = callback;
        this.depth = depth;
        this.maxDepth = maxDepth;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        if (rt > 0)
            ch.pipeline().addLast("timeout", new ReadTimeoutHandler(rt, ru));
        if (this.ssl) {

            SSLEngine engine = SslContextBuilder.forClient().build().newEngine(ch.alloc(), this.url.getHost(), this.port);

            ch.pipeline().addLast("ssl", new SslHandler(engine));
        }
        ch.pipeline().addLast("http", new HttpClientCodec());
        ch.pipeline().addLast("handler", new HttpHandler(this.callback, depth, maxDepth, ct, rt, ru, url));
    }
}
