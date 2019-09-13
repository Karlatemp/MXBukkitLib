package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.cmd.ICommand;
import cn.mcres.karlatemp.mxlib.cmd.ICommandSender;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class BukkitCommandSender implements ICommandSender, CommandSender {
    private final CommandSender sender;

    public CommandSender getSender() {
        return sender;
    }

    @Override
    public void sendMessage(String s) {
        sender.sendMessage(s);
    }

    @Override
    public void sendMessage(String[] strings) {
        sender.sendMessage(strings);
    }

    @Override
    public Server getServer() {
        return sender.getServer();
    }

    @NotNull
    @Override
    public String getName() {
        return sender.getName();
    }

    @Override
    public boolean isPermissionSet(String s) {
        return sender.isPermissionSet(s);
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return sender.isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(String s) {
        return sender.hasPermission(s);
    }

    @Override
    public boolean isSetPermission(String name) {
        return isPermissionSet(name);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) {
        return sender.addAttachment(plugin, s, b);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return sender.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) {
        return sender.addAttachment(plugin, s, b, i);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int i) {
        return sender.addAttachment(plugin, i);
    }

    @Override
    public void removeAttachment(PermissionAttachment permissionAttachment) {
        sender.removeAttachment(permissionAttachment);
    }

    @Override
    public void recalculatePermissions() {
        sender.recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return sender.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return sender.isOp();
    }

    @Override
    public void setOp(boolean b) {
        sender.setOp(b);
    }

    public BukkitCommandSender(CommandSender sender) {
        this.sender = sender;
    }
}
