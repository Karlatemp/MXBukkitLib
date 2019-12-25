/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MinecraftProtocolHelper.java@author: karlatemp@vip.qq.com: 19-12-22 下午2:26@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.network.minecraft;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.network.NettyHelper;
import cn.mcres.karlatemp.mxlib.network.PipelineUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Fast query MinecraftServer status
 *
 * @since 2.9
 */
public class MinecraftProtocolHelper {
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
        channel.pipeline()
                .addLast(new MinecraftPacketMessageEncoder())
                .addLast(new MinecraftPacketMessageDecoder())
                .addLast(NettyHelper.createHooker(channel));
        PacketDataSerializer p = PacketDataSerializer.fromByteBuf(Unpooled.buffer(5000));
        p.writeVarInt(0);
        p.writeVarInt(498);
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
