/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MXRCCommandSender.java@author: karlatemp@vip.qq.com: 19-12-17 下午11:33@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.bukkit;

import cn.mcres.karlatemp.mxlib.module.mrcn.NetWorkManager;
import cn.mcres.karlatemp.mxlib.module.mrcn.packet.PacketSystemMessage;
import cn.mcres.karlatemp.mxlib.module.mrcn.packet.PacketSystemMessages;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class MXRCCommandSender implements CommandSender {
    private final NetWorkManager mgr;
    private final boolean sudo;
    private String name;
    private PermissibleBase base = new PermissibleBase(this) {
        @Override
        public void recalculatePermissions() {
            if (!canRecalculate) return;
            if (sudo) return;
            super.recalculatePermissions();
        }

        @Override
        public boolean hasPermission(String inName) {
            if (sudo) return true;
            return super.hasPermission(inName);
        }

        @Override
        public boolean hasPermission(Permission perm) {
            if (sudo) return true;
            return super.hasPermission(perm);
        }
    };
    boolean canRecalculate = false;
    private boolean op;

    public MXRCCommandSender(NetWorkManager manager, String name, boolean sudo) {
        this.mgr = manager;
        this.name = name;
        this.sudo = sudo;
    }

    @Override
    public void sendMessage(String message) {
        if (message == null) message = "null";
        mgr.writePacket(new PacketSystemMessage(message));
    }

    @Override
    public void sendMessage(String[] messages) {
        if (messages == null || messages.length == 0) return;
        mgr.writePacket(new PacketSystemMessages(messages));
    }

    @Override
    public Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isPermissionSet(String name) {
        return base.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return base.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return base.hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return base.hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return base.addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return base.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return base.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return base.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        base.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        base.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return base.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return op;
    }

    @Override
    public void setOp(boolean value) {
        op = value;
    }
}
