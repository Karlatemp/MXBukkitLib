/**
 * Create at 2019/12/24 18:20
 * Copyright Karlatemp
 * MXLib $
 */
open module mxlib.api {

    uses cn.mcres.karlatemp.mxlib.MXLibBootProvider;
    exports cn.mcres.karlatemp.mxlib.annotations;
    exports cn.mcres.karlatemp.mxlib.bean;
    exports cn.mcres.karlatemp.mxlib.bukkit;
    exports cn.mcres.karlatemp.mxlib.cmd;
    exports cn.mcres.karlatemp.mxlib.cmd.denied;
    exports cn.mcres.karlatemp.mxlib.configuration;
    exports cn.mcres.karlatemp.mxlib.data;
    exports cn.mcres.karlatemp.mxlib.data.attribute;
    exports cn.mcres.karlatemp.mxlib.data.utils;
    exports cn.mcres.karlatemp.mxlib.encrypt;
    exports cn.mcres.karlatemp.mxlib.encryption;
    exports cn.mcres.karlatemp.mxlib.event;
    exports cn.mcres.karlatemp.mxlib.event.core;
    exports cn.mcres.karlatemp.mxlib.event.network;
    exports cn.mcres.karlatemp.mxlib.exceptions;
    exports cn.mcres.karlatemp.mxlib.files;
    exports cn.mcres.karlatemp.mxlib.formatter;
    exports cn.mcres.karlatemp.mxlib.interfaces;
    exports cn.mcres.karlatemp.mxlib.internal;
    exports cn.mcres.karlatemp.mxlib.logging;
    exports cn.mcres.karlatemp.mxlib.module.packet;
    exports cn.mcres.karlatemp.mxlib.nbt;
    exports cn.mcres.karlatemp.mxlib.nbt.visitor;
    exports cn.mcres.karlatemp.mxlib.network;
    exports cn.mcres.karlatemp.mxlib.reflect;
    exports cn.mcres.karlatemp.mxlib.remote;
    exports cn.mcres.karlatemp.mxlib.remote.netty;
    exports cn.mcres.karlatemp.mxlib.scheduler;
    exports cn.mcres.karlatemp.mxlib.tools;
    exports cn.mcres.karlatemp.mxlib.tools.module;
    exports cn.mcres.karlatemp.mxlib.tools.security;
    exports cn.mcres.karlatemp.mxlib.translate;
    exports cn.mcres.karlatemp.mxlib.util;
    exports cn.mcres.karlatemp.mxlib;
    requires JetBrains.Java.Annotations;
    requires java.base;
    requires java.logging;
    requires org.javassist;
    requires org.objectweb.asm;
    requires spigot.api;
    requires com.google.gson;
    requires io.netty.codec.http;
    requires io.netty.buffer;
    requires jdk.unsupported;
    requires java.management;
    requires io.netty.codec;
    requires com.google.common;
    requires io.netty.transport;
    requires io.netty.handler;
    requires io.netty.common;
    requires io.netty.transport.epoll;
    requires BungeeChatAPI;
}