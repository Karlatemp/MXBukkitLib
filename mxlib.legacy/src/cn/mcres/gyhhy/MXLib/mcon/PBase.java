/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PBase.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.mcon;

import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class PBase implements Permissible {

    protected final PermissibleBase pb = initBase();

    protected PermissibleBase initBase() {
        return new PermissibleBase(this);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return pb.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return pb.addAttachment(plugin, ticks);
    }

    @Override
    public boolean isPermissionSet(String name) {
        return pb.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return pb.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return pb.hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return pb.hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return pb.addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return pb.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        pb.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        pb.recalculatePermissions();
    }

    public Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return pb.getEffectivePermissions();
    }
    protected boolean op = false;

    @Override
    public boolean isOp() {
        return op;
    }

    @Override
    public void setOp(boolean value) {
        op = value;
    }

}
