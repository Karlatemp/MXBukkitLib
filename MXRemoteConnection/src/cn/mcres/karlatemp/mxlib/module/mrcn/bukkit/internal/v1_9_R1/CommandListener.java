/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CommandListener.java@author: karlatemp@vip.qq.com: 19-12-18 下午10:55@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.bukkit.internal.v1_9_R1;

import cn.mcres.karlatemp.mxlib.module.mrcn.bukkit.MXRCCommandSender;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;

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
        return new ChatComponentText(getName());
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
        return new Vec3D(0, 0, 0);
    }

    @Override
    public World getWorld() {
        return ((CraftServer) Bukkit.getServer()).getServer().worlds.get(0);
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
    public void a(CommandObjectiveExecutor.EnumCommandResult enumCommandResult, int i) {
    }

    @Override
    public MinecraftServer h() {
        return ((CraftServer) Bukkit.getServer()).getServer();
    }
}
