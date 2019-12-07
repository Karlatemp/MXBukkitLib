/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: FakeMinecraftServer.java@author: karlatemp@vip.qq.com: 19-11-29 下午11:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.packet;

import cn.mcres.gyhhy.MXLib.yggdrasil.MojangYggdrasil;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.FailedMessage;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.Profile;
import cn.mcres.gyhhy.MXLib.yggdrasil.callbacks.Callback;
import cn.mcres.karlatemp.mxlib.interfaces.ExceptionBiConsumer;
import cn.mcres.karlatemp.mxlib.logging.ILogger;
import cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.event.ServerAuthSuccessEvent;
import cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.event.ServerStatusRequestEvent;
import cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.internal.*;
import cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.model.ServerPing;
import cn.mcres.karlatemp.mxlib.remote.netty.NettyPacketDecoder;
import cn.mcres.karlatemp.mxlib.remote.netty.NettyPacketEncoder;
import cn.mcres.karlatemp.mxlib.remote.netty.Packet;
import cn.mcres.karlatemp.mxlib.scheduler.MXTask;
import cn.mcres.karlatemp.mxlib.scheduler.PowerScheduler;
import cn.mcres.karlatemp.mxlib.scheduler.MXScheduler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FakeMinecraftServer {

    public static MojangYggdrasil yggdrasil = new MojangYggdrasil();
    public static final KeyPair rsa;
    public static int port = 777;

    // public static int networkCompressionThreshold = -1;
    private static final MXScheduler scheduler = new PowerScheduler(
            Executors.newFixedThreadPool(1),
            Executors.newCachedThreadPool(),
            TK.named("FMS Scheduler")
    );

    static {
        TK.DEFAULT_NAME.set("FMS Class Definer");
        System.out.println("Class gen.");
        PacketListenerGen.getClientToServer();
        System.out.println("Class end gen.");
        TK.DEFAULT_NAME.set("RSA Generator");
        try {
            KeyPairGenerator keypairgenerator = KeyPairGenerator.getInstance("RSA");

            keypairgenerator.initialize(1024);
            rsa = keypairgenerator.generateKeyPair();
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
        TK.DEFAULT_NAME.set("System");
    }

    public static void main(String[] args) throws InterruptedException {
        Logger core = Logger.getLogger("FakeMineServer");
        core.info("Starting fake server...");
        new Worker().init();
        new ServerBootstrap().channel(NioServerSocketChannel.class)
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup())
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        final PacketListenerGen.EC ec = new PacketListenerGen.EC();
                        ch.attr(PacketListenerGen.ATTRUBUTE_KEY).set(ec);
                        ec.encoder = PacketListenerGen.getServerToClient();
                        ec.decoder = PacketListenerGen.getClientToServer();
                        ch.pipeline()
                                .addLast("splitter", new PacketSplitter())
                                .addLast("decoder", new NettyPacketDecoder(ec.decoder))

                                .addLast("encoder", new NettyPacketEncoder(ec.encoder))
                                .addBefore("encoder", "prepender", new PacketPrepender())
                                .addLast("packet_handler", new NetMgr(ch));
                        setProtocol(ch, PacketListenerGen.MCProtocols.Handshaking);
                        TickMgr.on(ch);
                        ch.attr(NetMgr.usrName).set(ch.remoteAddress());
                        ch.closeFuture().addListener(f -> {
                            core.info(ch.attr(NetMgr.usrName).get() + " lost connection.");
                        });
                    }
                }).bind(port).sync();
        core.info("Server started on port: " + port);
    }

    static void setErrorHandler(Channel channel, ExceptionBiConsumer<Channel, Throwable> handler) {
        channel.attr(NetMgr.errorhandler).set(handler);
    }

    static class Worker {
        void reg(PacketListener pl) {
            NetMgr.listeners.add(pl);
        }

        void reg(Channel channel, PacketListener pl) {
            final Attribute<Collection<PacketListener>> attr = channel.attr(NetMgr.channelListeners);
            Collection<PacketListener> listeners = attr.get();
            if (listeners == null) {
                attr.set(listeners = new ConcurrentLinkedQueue<>());
            }
            listeners.add(pl);
        }

        static ChannelFuture writePacket(ChannelHandlerContext context, PacketToClient<?> response) {
            return context.channel().writeAndFlush(response).addListener(
                    ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE
            );
        }

        void login(ChannelHandlerContext context) {
            reg(context.channel(), new PacketListener() {
                private byte[] vt;

                {
                    setErrorHandler(context.channel(), (c, cau) -> {
                        disconnect(new TextComponent(cau.toString()));
                    });
                }

                private String usr, sid;
                private int ticked;
                UUID uid;
                private boolean closed = false;
                TK.LoginStatus ls = TK.LoginStatus.REQ_START;


                void disconnect(BaseComponent reason) {
                    if (!context.channel().isOpen()) return;
                    if (closed) return;
                    closed = true;
                    PacketLoginDisconnect d = new PacketLoginDisconnect();
                    d.reason = new BaseComponent[]{reason};
                    writePacket(context, d);
                    context.channel().close();
                }

                @Override
                protected synchronized void tick() {
                    if (ticked++ == 600) {
                        disconnect(new TranslatableComponent("multiplayer.disconnect.unexpected_query_response"));
                        return;
                    }
                    switch (ls) {
                        case AUTHED: {
                            ls = TK.LoginStatus.END;
                            ServerAuthSuccessEvent e = new ServerAuthSuccessEvent(usr, uid);
                            e.setDisconnectMessage(new TranslatableComponent("disconnect.disconnected"));
                            e.post();
                            final BaseComponent message = e.getDisconnectMessage();
                            if (message != null) {
                                disconnect(message);
                            } else {
                                disconnect(new TranslatableComponent("disconnect.disconnected"));
                            }
                            break;
                        }
                        case AFAIL: {
                            ls = TK.LoginStatus.END;
                            disconnect(new TranslatableComponent("multiplayer.disconnect.unverified_username"));
                            break;
                        }
                    }
                }

                protected void listen(PacketLoginStart start) {
                    TK.a(ls == TK.LoginStatus.REQ_START, "Unexpected hello packet");
                    this.usr = start.player_name;
                    start.context.channel().attr(NetMgr.usrName).set(usr);
                    PacketLoginOutEncryptionRequest req = new PacketLoginOutEncryptionRequest();
                    req.public_key = rsa.getPublic().getEncoded();
                    SecureRandom sr = new SecureRandom();
                    this.sid = req.server_id = Long.toHexString(sr.nextLong());
                    this.vt = req.verify_token = new byte[4];
                    sr.nextBytes(vt);
                    writePacket(start.context, req);
                    ls = TK.LoginStatus.KEYED;
                }

                protected void listen(PacketLoginInEncryptionResponse response) {
                    TK.a(ls == TK.LoginStatus.KEYED, "Unexpected key packet");
                    final SecretKey secretKey = MinecraftEncryption.a(rsa.getPrivate(), response.shared_secret);
                    final Channel channel = response.context.channel();
                    channel.pipeline().addBefore("splitter",
                            "decrypt", new PacketDecrypter(MinecraftEncryption.a(2,
                                    secretKey)));
                    channel.pipeline().addBefore("prepender",
                            "encrypt", new PacketEncrypter(MinecraftEncryption.a(1,
                                    secretKey)));
                    ls = TK.LoginStatus.AUTHING;
                    auth(secretKey);
                }

                private String a(SecretKey secretKey, String sid) {
                    try {
                        MessageDigest sha = MessageDigest.getInstance("SHA-1");
                        for (byte[] bit : new byte[][]{
                                sid.getBytes("ISO_8859_1"),
                                secretKey.getEncoded(),
                                rsa.getPublic().getEncoded()
                        }) {
                            sha.update(bit);
                        }
                        return (new BigInteger(sha.digest())).toString(16);
                    } catch (Throwable thr) {
                        throw new RuntimeException(thr);
                    }
                }

                private void auth(SecretKey secretKey) {
                    String s = a(secretKey, sid);
                    scheduler.scheduleAsyncDelayedTask(() -> yggdrasil.queryPlayerHasJoined(this.usr, s, null, new Callback<Profile>() {
                        @Override
                        public void onSuccessful(Profile profile, int NetCode) throws IOException {
                            uid = profile.id.getUUID();
                            ls = TK.LoginStatus.AUTHED;
                        }

                        @Override
                        public void onFailed(FailedMessage err, int NetCode) throws IOException {
                            ls = TK.LoginStatus.AFAIL;
                        }

                        @Override
                        public void onFailed(byte[] datas, int http_code) throws IOException {
                            ls = TK.LoginStatus.AFAIL;
                        }

                        @Override
                        public void onError(Throwable thrown) {
                            ls = TK.LoginStatus.AFAIL;
                            NetMgr.NetWorkLogger.printStackTrace(thrown);
                        }
                    }), 0, TimeUnit.MILLISECONDS);
                }
            });
        }

        void init() {
            reg(new PacketListener() {
                protected void listen(PacketStatusInRequest statusInRequest) {
                    PacketStatusOutResponse response = new PacketStatusOutResponse();
                    ServerPing sp = response.ping = new ServerPing();
                    ServerPing.ServerVersion sv = sp.version = new ServerPing.ServerVersion();
                    sv.name = "1.14.4";
                    sv.protocol = 47;
                    ServerPing.ServerPlayers spp = sp.players = new ServerPing.ServerPlayers();
                    spp.max = 5;
                    spp.online = 1;
                    spp.sample = Collections.singletonList(
                            new ServerPing.ServerPlayers.SamplePlayer("Karlatemp", new UUID(0Xced25dd238d94e00L, 0Xa0a6b22946bf0f8fL))
                    );
                    sp.motd = new TextComponent("Testing Server!");
                    ServerStatusRequestEvent e = new ServerStatusRequestEvent();
                    e.setInfo(sp);
                    e.post();
                    response.ping = e.getInfo();
                    statusInRequest.context.channel().writeAndFlush(response).addListener(
                            ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE
                    );
                }

                protected void listen(PacketStatusInPing ping) {
                    PacketStatusOutPong p = new PacketStatusOutPong();
                    p.payload = ping.payload;
                    ping.context.channel().writeAndFlush(p).addListener(
                            ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE
                    );
                    ping.context.close();
                }

                protected void listen(PacketHandshakingInSetProtocol handshaking) {
                    switch (handshaking.NextState) {
                        case 1: {
                            setProtocol(handshaking.context.channel(), PacketListenerGen.MCProtocols.Status);
                            break;
                        }
                        case 2: {
                            setProtocol(handshaking.context.channel(), PacketListenerGen.MCProtocols.Login);
                            login(handshaking.context);
                            break;
                        }
                        default: {
                            handshaking.context.channel().close();
                            break;
                        }
                    }
                }
            });
        }
    }

    public static void setProtocol(Channel channel, PacketListenerGen.MCProtocols protocol) {
        final PacketListenerGen.EC ec = channel.attr(PacketListenerGen.ATTRUBUTE_KEY).get();
        ec.decoder.setProtocol(null, protocol.protocol);
        ec.encoder.setProtocol(null, protocol.protocol);
    }

    static class NetMgr extends ChannelInboundHandlerAdapter {
        static AttributeKey<Collection<PacketListener>> channelListeners = AttributeKey.valueOf(
                "MXLib-FakeMineCraftServer-ChannelListeners"
        );
        static AttributeKey<ExceptionBiConsumer<Channel, Throwable>> errorhandler = AttributeKey.valueOf(
                "MXLib-FakeMineCraftServer-Channel-Exception-Handler"
        );
        static AttributeKey<Object> usrName = AttributeKey.valueOf(
                "MXLib-FakeMineCraftServer-Channel-Player-Name"
        );
        static ILogger NetWorkLogger = TK.named("NetWork");
        public static final Collection<PacketListener> listeners = new ConcurrentLinkedQueue<>();
        private final Channel ch;

        public NetMgr(Channel ch) {
            this.ch = ch;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof PacketToServer) {
                final BiConsumer<Object, Packet<?>> poster = PacketListenerGen.listenInvokers.get(msg.getClass());
                Packet<?> p = (Packet<?>) msg;
                for (PacketListener listener : listeners) {
                    if (listener != null)
                        poster.accept(listener, p);
                }
                if (ctx.channel().hasAttr(channelListeners)) {
                    for (PacketListener l : ctx.channel().attr(channelListeners).get())
                        if (l != null) poster.accept(l, p);
                }
            } else {
                ctx.fireChannelRead(msg);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            if (ctx.channel().hasAttr(errorhandler)) {
                ctx.channel().attr(errorhandler).get().accept(ctx.channel(), cause);
            }
            super.exceptionCaught(ctx, cause);
        }
    }

    static class TickMgr {
        static void on(Channel channel) {
            final MXTask<Void> task = scheduler.runTaskAsyncTimer(() -> {
                if (channel.hasAttr(NetMgr.channelListeners)) {
                    final Collection<PacketListener> listeners = channel.attr(NetMgr.channelListeners).get();
                    for (PacketListener listener : listeners) {
                        listener.tick();
                    }
                }
            }, 50, 50, TimeUnit.MILLISECONDS);
            channel.closeFuture().addListener((ChannelFutureListener) future -> task.cancel());
        }
    }
}
