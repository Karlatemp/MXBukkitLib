/**
 * Create at 2019/12/24 22:26
 * Copyright Karlatemp
 * MXLib $
 */module mxlib.bukkit {
    exports cn.mcres.karlatemp.mxlib.share;
    exports cn.mcres.karlatemp.mxlib.event.bukkit;
    exports cn.mcres.karlatemp.mxlib.logging.bukkit;
    exports cn.mcres.karlatemp.mxlib.module.chat;
    exports cn.mcres.karlatemp.mxlib.module.namespace;
    exports cn.mcres.karlatemp.mxlib.module.translate;
    exports cn.mcres.karlatemp.mxlib.share.system;
    exports cn.mcres.karlatemp.mxlib.share.system.cmds;
    requires mxlib.api;
    requires BungeeChatAPI;
    requires JetBrains.Java.Annotations;
    requires spigot.api;
    requires io.netty.transport;
    requires io.netty.buffer;
    requires io.netty.codec;
    requires java.logging;
    requires com.google.gson;
    requires mxlib.core;
    requires io.netty.common;

}