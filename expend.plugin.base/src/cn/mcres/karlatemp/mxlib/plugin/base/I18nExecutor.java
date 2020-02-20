/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/20 20:54:18
 *
 * MXLib/expend.plugin.base/I18nExecutor.java
 */

package cn.mcres.karlatemp.mxlib.plugin.base;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class I18nExecutor implements CommandExecutor {
    public abstract static class I18nCommandContext {
        protected String[] args;
        protected CommandSender sender;
        protected String label;
        protected Command command;
        protected I18n i18n;

        public boolean isPlayer() {
            return sender instanceof Player;
        }

        public boolean isRemote() {
            return sender instanceof RemoteConsoleCommandSender;
        }

        public boolean isConsole() {
            return sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender;
        }

        public void send(Object msg) {
            send(sender, msg);
        }

        public void permission(String perm) {
            permission(perm, "no.permission");
        }

        public void permission(String perm, String trans, Object... params) {
            if (!sender.hasPermission(perm))
                fail(trans, params);
        }

        public void send(CommandSender target, Object msg) {
            target.sendMessage(String.valueOf(msg));
        }

        public void trans(String key, Object... msg) {
            trans(sender, key, msg);
        }

        public void trans(CommandSender target, String key, Object... msg) {
            send(target, i18n.translate(key, msg));
        }

        public static <T> T fail(String key, Object... params) {
            throw new I18nException(key, params);
        }

        public static <T> T exit() {
            throw I18nException.EXIT;
        }

        @NotNull
        public static Player getPlayer(String name) {
            var online = Bukkit.getOnlinePlayers();
            try {
                var uuid = UUID.fromString(name);
                for (var p : online) {
                    if (p.getUniqueId().equals(uuid)) return p;
                }
            } catch (Throwable ignore) {
            }
            for (var p : online) {
                if (p.getName().equalsIgnoreCase(name)) {
                    return p;
                }
            }
            return fail("player.not.found", name);
        }

        public boolean isEmptyArgs() {
            return args.length == 0;
        }

        public String get(int index) {
            if (index < 0 || index >= args.length) {
                return argumentMissing(index);
            }
            return args[index];
        }

        public int getInt(int index) {
            var val = get(index);
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException e) {
                return fail("not.a.valid.integer", val);
            }
        }

        public float getFloat(int index) {
            var val = get(index);
            try {
                return Float.parseFloat(val);
            } catch (NumberFormatException e) {
                return fail("not.a.valid.float", val);
            }
        }

        public double getDouble(int index) {
            var val = get(index);
            try {
                return Double.parseDouble(val);
            } catch (NumberFormatException e) {
                return fail("not.a.valid.double", val);
            }
        }

        public long getLong(int index) {
            var val = get(index);
            try {
                return Long.parseLong(val);
            } catch (NumberFormatException e) {
                return fail("not.a.valid.long", val);
            }
        }

        public boolean getBoolean(int index) {
            var val = get(index);
            switch (val.toLowerCase()) {
                case "true":
                case "1":
                case "y":
                case "yes":
                    return true;
            }
            return false;
        }

        public short getShort(int index) {
            var val = get(index);
            try {
                return Short.parseShort(val);
            } catch (NumberFormatException e) {
                return fail("not.a.valid.short", val);
            }
        }

        public byte getByte(int index) {
            var val = get(index);
            try {
                return Byte.parseByte(val);
            } catch (NumberFormatException e) {
                return fail("not.a.valid.byte", val);
            }
        }

        public <T> T argumentMissing(int index) {
            return fail("argument.missing", index + 1);
        }

        public List<String> getIntoEnd(int from) {
            if (from < args.length) {
                if (from > -1) {
                    return Arrays.asList(args).subList(from, args.length);
                }
            }
            return argumentMissing(from);
        }

        @NotNull
        public static World getWorld(String name) {
            try {
                return Bukkit.getWorld(UUID.fromString(name));
            } catch (Throwable ignore) {
            }
            var world = Bukkit.getWorld(name);
            if (world != null) return world;
            return fail("world.not.found", name);
        }

        public abstract void onCommand(CommandSender sender, Command command, String label, String[] args);

        public boolean length(int length) {
            return length <= args.length;
        }

        final void initialize(CommandSender commandSender, Command command, String s, String[] strings, I18n i18n) {
            this.sender = commandSender;
            this.command = command;
            this.label = s;
            this.args = strings;
            this.i18n = i18n;
            onCommand(commandSender, command, s, strings);
        }
    }

    protected I18n i18n;

    public I18nExecutor(@NotNull I18n i18n) {
        this.i18n = i18n;
    }

    @Override
    public final boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        try {
            var context = newContext();
            context.initialize(commandSender, command, s, strings, i18n);
        } catch (I18nException ie) {
            if (ie == I18nException.EXIT) return true;
            commandSender.sendMessage("§c" + i18n.translate(ie.getKey(), ie.getParams()));
            return true;
        }
        return true;
    }

    @NotNull
    public abstract I18nCommandContext newContext();
}
