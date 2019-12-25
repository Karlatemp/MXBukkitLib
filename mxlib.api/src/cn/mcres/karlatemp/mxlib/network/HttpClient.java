/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: HttpClient.java@author: karlatemp@vip.qq.com: 19-12-21 下午6:23@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.network;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * Fast Http client util. Link BungeeCord.
 *
 * @since 2.9
 */
public class HttpClient {
    public static final int TIMEOUT = 5000;
    private static final Cache<String, InetAddress> addressCache = CacheBuilder.newBuilder().expireAfterWrite(1L, TimeUnit.MINUTES).build();

    public static void main(String[] args) {
        System.setProperty("log4j2.loggerContextFactory", "org.apache.logging.log4j.simple.SimpleLoggerContextFactory");
        get(
                "http://ci.md-5.net/job/BungeeCord",
                new NioEventLoopGroup(1),
                5000, 5000, TimeUnit.MILLISECONDS,
                new HttpCallback() {
                    @Override
                    public HttpHandlerAction preview(URI uri, HttpResponse response) {
                        System.out.println("Connected " + uri + " with code " + response.status());
                        return null;
                    }

                    @Override
                    public void done(URI uri, HttpResponse responseCode, ByteBuf buf, Throwable err) {
                        if (err != null) {
                            err.printStackTrace();
                            System.exit(1);
                            return;
                        }
                        byte[] buf0 = new byte[1024];
                        while (buf.isReadable()) {
                            int len;
                            buf.readBytes(buf0, 0, len = Math.min(buf0.length, buf.readableBytes()));
                            System.out.write(buf0, 0, len);
                        }
                        System.exit(0);
                    }
                });
    }

    public static void get(
            @NotNull String uri, @NotNull EventLoopGroup group,
            int connect_timeout, long read_timeout,
            @NotNull TimeUnit readTimeoutUnit,
            @NotNull HttpCallback callback) {
        get(uri, group, connect_timeout, read_timeout, readTimeoutUnit, callback, 0, 5);
    }

    public static void get(
            @NotNull String uri, @NotNull EventLoopGroup group,
            int connect_timeout, long read_timeout,
            @NotNull TimeUnit readTimeoutUnit,
            @NotNull HttpCallback callback,
            int depth, int maxDepth) {
        URI url = URI.create(uri);
        if (depth > maxDepth) {
            callback.done(url, null, null, new IllegalAccessException("Too many redirects."));
            return;
        }
        Preconditions.checkNotNull(url.getScheme(), "scheme");
        Preconditions.checkNotNull(url.getHost(), "host");
        boolean ssl = url.getScheme().equals("https");
        int port = url.getPort();
        if (port == -1) {
            switch (url.getScheme()) {
                case "http":
                    port = 80;
                    break;
                case "https":
                    port = 443;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown scheme " + url.getScheme());
            }
        }
        InetAddress inetHost = addressCache.getIfPresent(url.getHost());
        if (inetHost == null) {
            try {
                inetHost = InetAddress.getByName(url.getHost());
            } catch (UnknownHostException ex) {
                callback.done(url, null, null, ex);
                return;
            }
            addressCache.put(url.getHost(), inetHost);
        }
        new Bootstrap().channel(PipelineUtils.getChannel())
                .group(group)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connect_timeout)
                .handler(new HttpInitializer(callback, url, ssl, port, connect_timeout, read_timeout, readTimeoutUnit, depth, maxDepth))
                .remoteAddress(inetHost, port)
                .connect().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {

                if (future.isSuccess()) {

                    String path = url.getRawPath() + ((url.getRawQuery() == null) ? "" : ("?" + url.getRawQuery()));

                    DefaultHttpRequest defaultHttpRequest = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, path);
                    defaultHttpRequest.headers().set("Host", url.getHost());
                    future.channel().writeAndFlush(defaultHttpRequest)
                            .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
                            .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                } else {
                    addressCache.invalidate(url.getHost());
                    callback.done(url, null, null, future.cause());
                }
            }
        });
    }
}
