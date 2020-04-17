/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/MinecraftProtocolHelper.java
 */

package cn.mcres.karlatemp.mxlib.network.minecraft;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.network.IPAddress;
import cn.mcres.karlatemp.mxlib.network.NettyHelper;
import cn.mcres.karlatemp.mxlib.network.PipelineUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.jetbrains.annotations.NotNull;

import javax.naming.Context;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Fast query MinecraftServer status
 *
 * @since 2.9
 */
public class MinecraftProtocolHelper {
    /**
     * @param host The host need resolve
     * @return SRV address
     * @since 2.12
     */
    public static IPAddress getMinecraftSRVAddress(String host) {
        try {
            // Only for load this class
            //noinspection Java9ReflectionClassVisibility
            Class.forName("com.sun.jndi.dns.DnsContextFactory");

            Hashtable<String, String> var2 = new Hashtable<>();
            var2.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
            var2.put(Context.PROVIDER_URL, "dns:");
            var2.put("com.sun.jndi.dns.timeout.retries", "1");
            DirContext var3 = new InitialDirContext(var2);
            Attributes var4 = var3.getAttributes("_minecraft._tcp." + host, new String[]{"SRV"});
            String[] var5 = var4.get("srv").get().toString().split(" ", 4);
            int port = 25565;
            try {
                port = Integer.parseInt(var5[2]);
            } catch (Throwable ignore) {
            }
            return new IPAddress(var5[3], port, host, 25565);
        } catch (Throwable throwable) {
            return new IPAddress(host, 25565);
        }
    }

    /**
     * @param address          The connect address
     * @param listPingCallback The callback
     * @param ms               Need check connect delay
     * @since 2.12
     */
    public static void ping(
            IPAddress address,
            @NotNull ListPingCallback listPingCallback,
            boolean ms) {
        final EventLoopGroup group = PipelineUtils.newEventLoopGroup();
        Channel c;
        try {
            c = new Bootstrap()
                    .channel(PipelineUtils.getChannel())
                    .group(group)
                    .remoteAddress(address.getHost(), address.getPort())
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                    .handler(new ChannelInboundHandlerAdapter()).connect().sync().channel().pipeline()
                    .addLast(new ReadTimeoutHandler(10000L, TimeUnit.MILLISECONDS))
                    .channel();
        } catch (Throwable any) {
            listPingCallback.done(null, 0, any);
            return;
        }
        ping(c, address.getSourceHost(), address.getPort(), listPingCallback.before(ListPingCallback.shutdown(group)), ms);
    }

    /**
     * @param group    The Netty Event Group using
     * @param address  The connect address
     * @param callback The callback
     * @param ms       Need check delay
     * @since 2.12
     */
    public static void ping(EventLoopGroup group, IPAddress address, @NotNull ListPingCallback callback, boolean ms) {
        Channel c;
        try {
            c = new Bootstrap()
                    .channel(PipelineUtils.getChannel())
                    .group(group)
                    .remoteAddress(address.getHost(), address.getPort())
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                    .handler(new ChannelInboundHandlerAdapter()).connect().sync().channel().pipeline()
                    .addLast(new ReadTimeoutHandler(10000L, TimeUnit.MILLISECONDS))
                    .channel();
        } catch (Throwable any) {
            callback.done(null, 0, any);
            return;
        }
        ping(c, address.getSourceHost(), address.getPort(), callback, ms);

    }

    /**
     * @param host             The host in channel write
     * @param port             The port in channel write
     * @param srvHost          The connect host
     * @param srcPort          The connect port
     * @param listPingCallback The callback
     * @param ms               Does check delay?
     * @since 2.12
     */
    public static void ping(
            String host, int port,
            String srvHost, int srcPort,
            @NotNull ListPingCallback listPingCallback,
            boolean ms) {
        final EventLoopGroup group = PipelineUtils.newEventLoopGroup();
        Channel c;
        try {
            c = new Bootstrap()
                    .channel(PipelineUtils.getChannel())
                    .group(group)
                    .remoteAddress(srvHost, srcPort)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                    .handler(new ChannelInboundHandlerAdapter()).connect().sync().channel().pipeline()
                    .addLast(new ReadTimeoutHandler(10000L, TimeUnit.MILLISECONDS))
                    .channel();
        } catch (Throwable any) {
            group.shutdownNow();
            listPingCallback.done(null, 0, any);
            return;
        }
        ping(c, host, port, listPingCallback.before(ListPingCallback.shutdown(group)), ms);
    }

    /**
     * Parse input to real address
     *
     * @param host The input source
     * @return The real address
     */
    @NotNull
    public static IPAddress parseMinecraftServerAddress(@NotNull String host) {
        String[] var1 = host.split(":");

        if (host.startsWith("[")) {
            int var2 = host.indexOf("]");
            if (var2 > 0) {
                String var3 = host.substring(1, var2);
                String var4 = host.substring(var2 + 1).trim();
                if (var4.startsWith(":")) {
                    var4 = var4.substring(1);
                    var1 = new String[2];
                    var1[0] = var3;
                    var1[1] = var4;
                } else {
                    var1 = new String[1];
                    var1[0] = var3;
                }
            }
        }

        if (var1.length > 2) {
            var1 = new String[1];
            var1[0] = host;
        }

        String var5 = var1[0];
        int var6 = (var1.length > 1) ? parseInt(var1[1], 25565) : 25565;

        if (var6 == 25565) {
            return getMinecraftSRVAddress(var5);
        }

        return new IPAddress(var5, var6);
    }

    private static int parseInt(String var0, int var1) {
        try {
            return Integer.parseInt(var0.trim());
        } catch (Exception exception) {
            return var1;
        }
    }

    /**
     * List Ping Callback.
     *
     * @since 2.9
     */
    public interface ListPingCallback {
        /**
         * Call when connection finish.
         *
         * @param result The MOTD for server. result.toString(StandardCharsets.UTF_8);
         * @param ms     Ms
         * @param err    The error in connecting.
         */
        void done(ByteBuf result, long ms, Throwable err);

        /**
         * @param group The target group
         * @return A callback run event group shutdown.
         * @since 1.12
         */
        static ListPingCallback shutdown(@NotNull EventLoopGroup group) {
            return run(group::shutdownGracefully);
        }

        /**
         * Build a Runnable Callback
         *
         * @param runnable The runnable
         * @return The build-in callback
         * @since 2.12
         */
        static ListPingCallback run(Runnable runnable) {
            return (result, ms, err) -> runnable.run();
        }

        /**
         * Run other callback before this
         *
         * @param before The target
         * @return The build-in callback.
         * @since 2.12
         */
        default ListPingCallback before(@NotNull ListPingCallback before) {
            return before.then(this);
        }

        /**
         * Run other callback after this.
         *
         * @param then The target
         * @return The build-in callback
         * @since 2.12
         */
        default ListPingCallback then(@NotNull ListPingCallback then) {
            return (result, ms, err) -> {
                done(result, ms, err);
                then.done(result, ms, err);
            };
        }
    }

    public interface QueryCallback {
        boolean hasChallengeInteger();

        int getChallengeInteger();

        void setChallengeInteger(int ci);

        void done(String motd, String gametype, String map, String numplayers, String maxplayers, int hostport, String hostip);

        void error(Throwable throwable);
    }

    public abstract static class SimpleQueryCallback implements QueryCallback {
        private boolean hci;
        private int ci;

        @Override
        public boolean hasChallengeInteger() {
            return hci;
        }

        @Override
        public int getChallengeInteger() {
            return ci;
        }

        @Override
        public void setChallengeInteger(int ci) {
            hci = true;
            this.ci = ci;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Channel c = new Bootstrap()
                .channel(PipelineUtils.getChannel())
                .group(PipelineUtils.newEventLoopGroup())
                .remoteAddress("localhost", 25565)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .handler(new ChannelInboundHandlerAdapter()).connect().sync().channel().pipeline()
                .addLast(new ReadTimeoutHandler(10000L, TimeUnit.MILLISECONDS))
                .channel();
        ping(c, "localhost", 25565, new ListPingCallback() {
            @Override
            public void done(ByteBuf result, long ms, Throwable err) {
                if (result != null) {
                    try {
                        result.readBytes(System.out, result.readableBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (err != null) {
                    err.printStackTrace();
                }
                System.exit(0);
            }
        }, false);
    }

    /**
     * Fast ping server.
     *
     * @param channel  The channel using. Must TPC protocol.
     * @param address  The remote address
     * @param port     The ping port.
     * @param callback The callback for connection.
     * @param ms       Need check delay?
     */
    public static void ping(
            @NotNull Channel channel, @NotNull String address, int port,
            @NotNull ListPingCallback callback, boolean ms) {
        if (channel instanceof DatagramChannel) {
            callback.done(null, 0, new IllegalAccessException("Only support TCP channel."));
            return;
        }
        ping(channel, address, port, callback, ms, 498);
    }

    /**
     * Fast ping server.
     *
     * @param channel  The channel using. Must TPC protocol.
     * @param address  The remote address
     * @param port     The ping port.
     * @param callback The callback for connection.
     * @param ms       Need check delay?
     * @param protocol Protocol using.
     */
    public static void ping(
            @NotNull Channel channel, @NotNull String address, int port,
            @NotNull ListPingCallback callback, boolean ms, int protocol) {
        if (channel instanceof DatagramChannel) {
            callback.done(null, 0, new IllegalAccessException("Only support TCP channel."));
            return;
        }
        channel.pipeline()
                .addLast(new MinecraftPacketMessageEncoder())
                .addLast(new MinecraftPacketMessageDecoder())
                .addLast(NettyHelper.createHooker(channel));
        PacketDataSerializer p = PacketDataSerializer.fromByteBuf(Unpooled.buffer(5000));
        p.writeVarInt(0);
        p.writeVarInt(protocol);
        p.writeString(address);
        p.writeShort(port);
        p.writeVarInt(1);
        NettyHelper.sendMessage(channel, p, f -> {
            if (f.isSuccess()) {
                PacketDataSerializer pw = PacketDataSerializer.fromByteBuf(Unpooled.buffer(1));
                pw.writeVarInt(0);
                NettyHelper.sendMessage(channel, pw, f1 -> {
                    if (f1.isSuccess()) {
                        NettyHelper.readMessage(channel, (NettyHelper.MessageCallback<ByteBuf>) (message, error) -> {
                            if (error != null) {
                                callback.done(null, 0, error);
                                return;
                            }
                            PacketDataSerializer serializer = PacketDataSerializer.fromByteBuf(message);
                            serializer.readVarInt();
                            ByteBuf des = message.readBytes(serializer.readVarInt());
                            if (ms) {
                                ByteBuf cp = Unpooled.copiedBuffer(des);

                                PacketDataSerializer data = PacketDataSerializer.fromByteBuf(Unpooled.buffer(1 + Long.BYTES));
                                data.writeVarInt(1);
                                long now = System.currentTimeMillis();
                                data.writeLong(now);
                                NettyHelper.sendMessage(channel, data, fx -> {
                                    if (fx.isSuccess()) {
                                        NettyHelper.readMessage(channel, (NettyHelper.MessageCallback<ByteBuf>) (message1, error1) -> {
                                            long a = System.currentTimeMillis() - now;
                                            callback.done(cp, a, error1);
                                        });
                                    } else {
                                        callback.done(cp, 0, fx.cause());
                                    }
                                });
                            } else {
                                callback.done(des, 0, null);
                            }
                        });
                    } else {
                        callback.done(null, 0, f1.cause());
                    }
                });
            } else {
                callback.done(null, 0, f.cause());
            }
        });
    }

    private static ByteBuf readNullTerminatedString(ByteBuf b) {
        AtomicInteger size = new AtomicInteger(0);
        b.forEachByte(value -> {
            boolean a = value != 0;
            if (a) size.getAndIncrement();
            return a;
        });
        ByteBuf bw = b.readBytes(size.get());
        b.readByte();
        return bw;
    }

    /**
     * Query server info.
     *
     * @param udp       UDP Channel.
     * @param sessionId Any int. Recommended 1
     * @param callback  The callback.
     */
    public static void query(@NotNull Channel udp, int sessionId, @NotNull QueryCallback callback) {
        if (!(udp instanceof DatagramChannel)) {
            callback.error(new IllegalAccessException("Only support UDP channel."));
        }
        udp.pipeline().addLast(new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if (msg instanceof io.netty.channel.socket.DatagramPacket) {
                    msg = ((DatagramPacket) msg).content();
                }
                super.channelRead(ctx, msg);
            }
        }).addLast(NettyHelper.createHooker(udp));

        Runnable w = () -> {
            ByteBuf bx = Unpooled.buffer(19);
            bx.writeShort(0xFEFD);
            bx.writeByte(0);
            bx.writeInt(sessionId);
            bx.writeInt(callback.getChallengeInteger());
            NettyHelper.sendMessage(udp, bx, f -> {
                if (f.isSuccess()) {
                    NettyHelper.readMessage(udp, (NettyHelper.MessageCallback<ByteBuf>) (mg, eg) -> {
                        if (eg == null) {
                            mg.readByte();
                            mg.readInt();
                            callback.done(
                                    readNullTerminatedString(mg).toString(StandardCharsets.UTF_8),
                                    readNullTerminatedString(mg).toString(StandardCharsets.UTF_8),
                                    readNullTerminatedString(mg).toString(StandardCharsets.UTF_8),
                                    readNullTerminatedString(mg).toString(StandardCharsets.UTF_8),
                                    readNullTerminatedString(mg).toString(StandardCharsets.UTF_8),
                                    mg.readUnsignedShort(),
                                    readNullTerminatedString(mg).toString(StandardCharsets.UTF_8)
                            );
                        }
                    });
                } else {
                    callback.error(f.cause());
                }
            });
        };
        if (callback.hasChallengeInteger()) {
            w.run();
        } else {
            ByteBuf bb = Unpooled.buffer(Short.BYTES + 1 + Integer.BYTES);
            bb.writeShort(0xFEFD);
            bb.writeByte(9);
            bb.writeInt(sessionId);
            NettyHelper.sendMessage(udp, bb, b -> {
                if (b.isSuccess()) {
                    NettyHelper.readMessage(udp, (NettyHelper.MessageCallback<ByteBuf>) (msg, err) -> {
                        if (err == null) {
                            msg.readByte();
                            msg.readInt();
                            ByteBuf tok = msg.readBytes(msg.readableBytes() - 1);
                            int challengeInteger = Integer.parseInt(tok.toString(StandardCharsets.UTF_8));
                            callback.setChallengeInteger(challengeInteger);
                            w.run();
                        } else {
                            callback.error(err);
                        }
                    });
                } else {
                    callback.error(b.cause());
                }
            });
        }
    }
}
