/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CommandListener.java@author: karlatemp@vip.qq.com: 19-12-18 下午10:48@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.bukkit.internal.v1_8_R1;

import cn.mcres.karlatemp.mxlib.module.mrcn.bukkit.MXRCCommandSender;
import net.minecraft.server.v1_8_R1.*;

public class CommandListener implements ICommandListener {
    private final MXRCCommandSender sender;

    public CommandListener(MXRCCommandSender sender) {
        this.sender = sender;
    }

    @Override
    public String getName() {
        return sender.getName();
    }

    @Override
    public IChatBaseComponent getScoreboardDisplayName() {
        return new ChatComponentText(sender.getName());
    }

    @Override
    public void sendMessage(IChatBaseComponent iChatBaseComponent) {
        sender.sendMessage(iChatBaseComponent.getText());
    }

    @Override
    public boolean a(int i, String s) {
        return true;
    }

    @Override
    public BlockPosition getChunkCoordinates() {
        return BlockPosition.ZERO;
    }

    @Override
    public Vec3D d() {
        return new Vec3D(0D, 0D, 0D);
    }

    @Override
    public World getWorld() {
        return MinecraftServer.getServer().worlds.get(0);
    }

    @Override
    public Entity f() {
        return null;
    }

    @Override
    public boolean getSendCommandFeedback() {
        return true;
    }

    @Override
    public void a(EnumCommandResult enumCommandResult, int i) {
    }
}
