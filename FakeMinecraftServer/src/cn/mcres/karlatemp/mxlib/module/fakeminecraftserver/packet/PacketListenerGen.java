/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketListenerGen.java@author: karlatemp@vip.qq.com: 19-11-29 下午9:57@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.packet;

import cn.mcres.karlatemp.mxlib.module.packet.PacketDataSerializer;
import cn.mcres.karlatemp.mxlib.remote.netty.*;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import cn.mcres.karlatemp.mxlib.tools.Unsafe;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class PacketListenerGen {
    static class EC {
        PacketProtocolProvider encoder, decoder;
    }

    static final AttributeKey<EC> ATTRUBUTE_KEY = AttributeKey.valueOf(
            "MXLib-FakeMineCraftServer-Protocol"
    );

    static class W extends PacketProtocolProvider {
        static synchronized PacketProtocolProvider cw(EnumMap<MCProtocols, PacketProviderLink> wwx) {
            ww = wwx;
            return new W();
        }

        volatile static EnumMap<MCProtocols, PacketProviderLink> ww;

        @NotNull
        @Override
        protected Map<PacketProtocol, PacketProvider> initProtocolMap() {
            return ww.entrySet().stream().collect(
                    ConcurrentHashMap::new,
                    (map, entry) -> map.put(entry.getKey().protocol, entry.getValue()),
                    Map::putAll
            );
        }
    }

    static class SB {
        private EnumMap<MCProtocols, PacketProviderLink> toServer = new EnumMap<>(MCProtocols.class);
        private EnumMap<MCProtocols, PacketProviderLink> toClient = new EnumMap<>(MCProtocols.class);
        private List<Class<?>> pks = new ArrayList<>();

        PacketProviderLink a(MCProtocols protocol, Class<?> cw) {
            pks.add(cw);
            EnumMap<MCProtocols, PacketProviderLink> links = PacketToServer.class.isAssignableFrom(cw)
                    ? toServer : PacketToClient.class.isAssignableFrom(cw) ? toClient : null;
            if (links == null) throw new PacketProviderException("No ToServer/ToClient Packet");
            PacketProviderLink link = links.get(protocol);
            if (link == null) {
                links.put(protocol, link = new PacketProviderLink() {
                    @Override
                    protected void writePacketId(PacketDataSerializer serializer, short id) throws PacketProviderException {
                        serializer.writeVarInt(id);
                    }

                    @Override
                    protected short readPacketId(PacketDataSerializer serializer) throws PacketProviderException {
                        return (short) serializer.readVarInt();
                    }
                });
            }
            return link;
        }

        <T extends Packet<T>> SB reg(MCProtocols protocol, Class<T> cw) {
            a(protocol, cw).register(cw);
            return this;
        }

        <T extends Packet<T>> SB reg(MCProtocols protocol, short w, Class<T> cw) {
            a(protocol, cw).register(w, cw);
            return this;
        }
    }

    public enum MCProtocols {
        Handshaking, Status, Login;
        public final PacketProtocol protocol = PacketProtocol.newProtocol(name());
    }

    private static void init(ClassWriter cw) {
        final MethodVisitor init = cw.visitMethod(Modifier.PUBLIC, "<init>", "()V", null, null);
        init.visitCode();
        init.visitVarInsn(Opcodes.ALOAD, 0);
        init.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        init.visitInsn(Opcodes.RETURN);
        init.visitMaxs(1, 1);
        init.visitEnd();
    }

    public static ClassWriter genListenerClass(List<Class<?>> packetClasses, Map<Class<?>, ClassWriter> invokers) {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(52, Modifier.PUBLIC, "cn/mcres/karlatemp/mxlib/module/fakeminecraftserver/packet/PacketListener", null, "java/lang/Object", null);
        cw.visitSource("SysGen", null);
        init(cw);
        {
            final MethodVisitor tick = cw.visitMethod(Modifier.PROTECTED, "tick", "()V", null, null);
            tick.visitCode();
            tick.visitInsn(Opcodes.RETURN);
            tick.visitMaxs(1, 1);
            tick.visitEnd();
        }
        int iww = 0;
        for (Class<?> type : packetClasses) {
            final MethodVisitor method = cw.visitMethod(Modifier.PROTECTED, "listen", "(L" + type.getName().replace('.', '/') + ";)V", null, null);
            method.visitCode();
            method.visitInsn(Opcodes.RETURN);
            method.visitMaxs(1, 2);
            method.visitEnd();
            {
                ClassWriter invoker = new ClassWriter(0);
                invoker.visit(52, Modifier.PUBLIC, "cn/mcres/karlatemp/mxlib/module/fakeminecraftserver/packet/PacketListenerGen$$LAMBDA$" + (iww++), null, "java/lang/Object", new String[]{
                        "java/util/function/BiConsumer"
                });
                init(invoker);
//                java.util.function.BiConsumer c;
                final MethodVisitor accept = invoker.visitMethod(Modifier.PUBLIC, "accept", "(Ljava/lang/Object;Ljava/lang/Object;)V", null, null);
                accept.visitCode();
                accept.visitVarInsn(Opcodes.ALOAD, 1);
                accept.visitTypeInsn(Opcodes.CHECKCAST, "cn/mcres/karlatemp/mxlib/module/fakeminecraftserver/packet/PacketListener");
                accept.visitVarInsn(Opcodes.ALOAD, 2);
                accept.visitTypeInsn(Opcodes.CHECKCAST, type.getName().replace('.', '/'));
                accept.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "cn/mcres/karlatemp/mxlib/module/fakeminecraftserver/packet/PacketListener", "listen",
                        "(L" + type.getName().replace('.', '/') + ";)V", false);
                accept.visitInsn(Opcodes.RETURN);
                accept.visitMaxs(3, 3);
                accept.visitEnd();
                invokers.put(type, invoker);
            }
        }
        return cw;
    }

    private static SB s = new SB()
            .reg(MCProtocols.Handshaking, PacketHandshakingInSetProtocol.class)
            .reg(MCProtocols.Status, PacketStatusInRequest.class)
            .reg(MCProtocols.Status, PacketStatusOutResponse.class)
            .reg(MCProtocols.Status, PacketStatusInPing.class)
            .reg(MCProtocols.Status, PacketStatusOutPong.class)
            .reg(MCProtocols.Login, PacketLoginStart.class)
            .reg(MCProtocols.Login, PacketLoginInEncryptionResponse.class)
            .reg(MCProtocols.Login, PacketLoginDisconnect.class)
            .reg(MCProtocols.Login, PacketLoginOutEncryptionRequest.class)
            .reg(MCProtocols.Login, PacketLoginOutSuccess.class)
            .reg(MCProtocols.Login, PacketLoginOutSetCompression.class);
    static final Map<Class<?>, BiConsumer<Object, Packet<?>>> listenInvokers;

    public static PacketProtocolProvider getClientToServer() {
        return W.cw(s.toServer);
    }

    public static PacketProtocolProvider getServerToClient() {
        return W.cw(s.toClient);
    }

    private static Class<?> load(byte[] a) {
        return Toolkit.Reflection.defineClass(PacketListenerGen.class.getClassLoader(),
                null, a, 0, a.length, PacketListenerGen.class.getProtectionDomain());
    }

    static {
        System.out.println("[PacketListenGen] Class loading.");
        HashMap<Class<?>, ClassWriter> cws = new HashMap<>();
        System.out.println("[PacketListenGen] Gening Listener Bytecode.");
        byte[] code = genListenerClass(s.pks, cws).toByteArray();
        System.out.println("[PacketListenGen] Gened Listener Bytecode.");
        load(code);
        System.out.println("[PacketListenGen] Defined Listener Bytecode.");
        listenInvokers = cws.entrySet().stream().collect(
                HashMap::new,
                (map, entry) -> {
                    try {
                        //noinspection unchecked
                        map.put(entry.getKey(),
                                (BiConsumer<Object, Packet<?>>) Unsafe.getUnsafe().allocateInstance(load(entry.getValue().toByteArray()))
                        );
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    }
                },
                Map::putAll
        );
        System.out.println("[PacketListenGen] Class end loading.");
    }
}
