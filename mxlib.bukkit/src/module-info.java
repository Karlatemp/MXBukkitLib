/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: module-info.java@author: karlatemp@vip.qq.com: 2019/12/26 下午6:39@version: 2.0
 */
/**
 * Here is toolkit for Bukkit/Spigot.
 */
open module mxlib.bukkit {
    exports cn.mcres.karlatemp.mxlib.share;
    exports cn.mcres.karlatemp.mxlib.event.bukkit;
    exports cn.mcres.karlatemp.mxlib.logging.bukkit;
    exports cn.mcres.karlatemp.mxlib.module.chat;
    exports cn.mcres.karlatemp.mxlib.module.namespace;
    exports cn.mcres.karlatemp.mxlib.module.translate;
    requires mxlib.api;
    requires mxlib.common.maven;
    requires BungeeChatAPI;
    requires org.jetbrains.annotations;
    requires spigot.api;
    requires io.netty.transport;
    requires io.netty.buffer;
    requires io.netty.codec;
    requires java.logging;
    requires com.google.gson;
    requires mxlib.core;
    requires io.netty.common;
    requires org.objectweb.asm;
    requires mxlib.logging;
    requires mxlib.message;

}