/**
 * Create at 2020/2/15 0:35
 * Copyright Karlatemp
 * MXLib $
 */module mxlib.message {
    requires org.jetbrains.annotations;
    requires com.google.gson;
    requires com.mojang.brigadier;
    requires mxlib.api;
    requires io.netty.buffer;
    requires io.netty.codec;
    requires io.netty.transport;
    requires io.netty.handler;
    requires io.netty.common;
    requires java.naming;
    requires org.objectweb.asm;
    exports cn.mcres.karlatemp.mxlib.nbt.visitor;
    exports cn.mcres.karlatemp.mxlib.nbt;
    exports cn.mcres.karlatemp.mxlib.network.minecraft;
    exports cn.mcres.karlatemp.mxlib.module.packet;
    exports cn.mcres.karlatemp.mxlib.remote.netty;
    exports cn.mcres.karlatemp.mxlib.remote;
}