/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MessageFactoryBukkitCommandSender.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.logging;

import java.lang.management.LockInfo;
import java.lang.management.ThreadInfo;

/**
 * 带Bukkit颜色支持的信息工厂
 */
public class MessageFactoryBukkitCommandSender extends MessageFactoryChatColor {
    private final IMessageFactory parent;

    public MessageFactoryBukkitCommandSender(IMessageFactory parent) {
        this.parent = parent;
    }

    public MessageFactoryBukkitCommandSender() {
        this(null);
    }

    @Override
    public String getStackTraceElementMessage$return(StackTraceElement stack, String clazz, String zip, String version) {
        if (parent != null) return parent.getStackTraceElementMessage$return(stack, clazz, zip, version);
        return super.getStackTraceElementMessage$return(stack, clazz, zip, version);
    }

    @Override
    public String dump(LockInfo lockInfo) {
        if (parent != null) return parent.dump(lockInfo);
        return super.dump(lockInfo);
    }

    @Override
    public String dump(ThreadInfo inf, boolean fullFrames) {
        if (parent != null) return parent.dump(inf, fullFrames);
        return super.dump(inf, fullFrames);
    }

    @Override
    public String CIRCULAR_REFERENCE(Throwable throwable) {
        if (parent != null) return parent.CIRCULAR_REFERENCE(throwable);
        return super.CIRCULAR_REFERENCE(throwable);
    }

    @Override
    public String dump(Throwable thr) {
        if (parent != null) return parent.dump(thr);
        return super.dump(thr);
    }

    @Override
    public String excpre(String pre) {
        if (parent != null) return parent.excpre(pre);
        return super.excpre(pre);
    }

    @Override
    public String toConsole(String cons) {
        return cons;
    }
}
