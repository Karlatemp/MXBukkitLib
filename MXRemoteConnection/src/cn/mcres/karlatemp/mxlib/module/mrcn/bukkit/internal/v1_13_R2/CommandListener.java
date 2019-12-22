/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CommandListener.java@author: karlatemp@vip.qq.com: 19-12-18 下午11:04@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.bukkit.internal.v1_13_R2;

import cn.mcres.karlatemp.mxlib.module.mrcn.bukkit.MXRCCommandSender;
import net.minecraft.server.v1_13_R2.CommandListenerWrapper;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.ICommandListener;
import org.bukkit.command.CommandSender;

public class CommandListener implements ICommandListener {
    private final MXRCCommandSender sender;

    public CommandListener(MXRCCommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(IChatBaseComponent iChatBaseComponent) {
        sender.sendMessage(iChatBaseComponent.getText());
    }

    @Override
    public boolean a() {
        return true;
    }

    @Override
    public boolean b() {
        return true;
    }

    @Override
    public boolean B_() {
        return true;
    }

    @Override
    public CommandSender getBukkitSender(CommandListenerWrapper commandListenerWrapper) {
        return sender;
    }
}
