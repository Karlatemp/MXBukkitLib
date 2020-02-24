/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/18 23:19:30
 *
 * MXLib/expend.vexview.interpreter/PM.java
 */

package cn.mcres.karlatemp.vit;

import cn.mcres.karlatemp.mxlib.interpreter.*;
import cn.mcres.karlatemp.mxlib.plugin.base.I18n;
import cn.mcres.karlatemp.mxlib.plugin.base.I18nException;
import cn.mcres.karlatemp.mxlib.plugin.base.StandardI18n;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lk.vexview.api.VexViewAPI;
import lk.vexview.builders.Base64ImageBuilder;
import lk.vexview.builders.GuiBuilder;
import lk.vexview.builders.ImageBuilder;
import lk.vexview.builders.TextBuilder;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.VexText;
import lk.vexview.tag.TagDirection;
import lk.vexview.tag.components.VexTag;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;


public class PM extends JavaPlugin {
    private static CommandInclude.CommandFinder finder;
    public static PM instance;
    public static PlaceholderAPISupport support = (player, source) -> source;
    public static boolean cache = true;
    public static final Cache<String, VexGui> cached_gui = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();
    public static final Cache<String, CommandBlock> cached = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();
    public static final Cache<String, ButtonHandle> cached_handles = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();
    public static boolean dump_errors;

    @Override
    public void reloadConfig() {
        saveDefaultConfig();
        super.reloadConfig();
        cache = getConfig().getBoolean("cache", true);
        dump_errors = getConfig().getBoolean("dump_errors", false);
        cached_gui.cleanUp();
        cached.cleanUp();
        cached_handles.cleanUp();
    }

    public void onEnable() {
        instance = this;
        getDataFolder().mkdirs();
        finder = new DefaultFileFinder(getDataFolder());
        try {
            var papi = Class.forName("me.clip.placeholderapi.PlaceholderAPI");
            var lk = MethodHandles.lookup();
            support = (PlaceholderAPISupport) LambdaMetafactory.metafactory(
                    lk, "run", MethodType.methodType(PlaceholderAPISupport.class),
                    MethodType.methodType(String.class, OfflinePlayer.class, String.class),
                    lk.findStatic(papi, "setPlaceholders", MethodType.methodType(String.class, OfflinePlayer.class, String.class)),
                    MethodType.methodType(String.class, OfflinePlayer.class, String.class)
            ).dynamicInvoker().invoke();
            // PlaceholderAPI.setPlaceholders()
        } catch (Throwable ignore) {
        }
        reloadConfig();
        saveResource("i18n.yml", false);
        getCommand("vit").setExecutor(new CommandExec(I18n.load(
                YamlConfiguration.loadConfiguration(new File(getDataFolder(), "i18n.yml"))
        ).extend(StandardI18n.ZH_CN)));
    }

    public static CommandBlock block(String path) {
        if (cache) {
            var b = cached.getIfPresent(path);
            if (b != null) return b;
        }
        try {
            return CommandInclude.include(path, finder);
        } catch (IOException e) {
            if (dump_errors)
                instance.getLogger().log(Level.SEVERE, "Failed to load configuration: " + path, e);
            throw new I18nException("invalid.configuration", path, e.toString());
        }
    }

    public static ButtonHandle buttonHandle(String path) {
        if (cache) {
            var handle = cached_handles.getIfPresent(path);
            if (handle != null) return handle;
        }
        final CommandBlock include = block(path);
        if (include == null) return null;
        ButtonHandle handle = new ButtonHandle();
        try {
            ButtonHandle.HandleAllocator.INSTANCE.visit(include, true, handle);
        } catch (Throwable throwable) {
            instance.getLogger().log(Level.SEVERE, "Failed to parse to handle: " + path, throwable);
            return null;
        }
        if (cache) {
            cached_handles.put(path, handle);
        }
        return handle;
    }

    public static void openGUI(Player player, String target) throws I18nException {
        if (cache) {
            var get = cached_gui.getIfPresent(target);
            if (get != null) {
                VexViewAPI.openGui(player, get);
                return;
            }
        }
        final CommandBlock include = block(target);
        if (include == null) return;
        VexGui gui;
        try {
            gui = new GuiAlloc().visit(include, true, GuiBuilder.builder()).done();
        } catch (Throwable throwable) {
            if (dump_errors)
                instance.getLogger().log(Level.SEVERE, "Failed to parse gui: " + target, throwable);
            throw new I18nException("failed.to.parse.gui", target, throwable.toString());
        }
        if (cache) cached_gui.put(target, gui);
        VexViewAPI.openGui(player, gui);
    }

    public static void openTag(Player player, World world, String path, String id, double x, double y, double z) {
        var block = block(path);
        CommandLine firstLine = null;
        for (var l : block.getLines()) {
            if (l instanceof CommandLine) {
                firstLine = (CommandLine) l;
                break;
            }
        }
        if (firstLine == null) {
            if (dump_errors)
                instance.getLogger().log(Level.WARNING, "No data in " + path);
            throw new I18nException("no.data", path);
        }
        try {
            var i = firstLine.getArguments().iterator();
            VexTag tag;
            var type = i.next().toString();
            var direction = TagDirectionAlloc.INSTANCE.visit(i.next(), false, TagDirectionBuilder.builder()).build();
            switch (type) {
                case "text": {
                    tag = TextAlloc.INSTANCE.visit(i.next(), false, TextBuilder.builder()).toTag(
                            id, x, y, z, Boolean.parseBoolean(i.next().toString()), direction
                    );
                    break;
                }
                case "image": {
                    var img = new ImageAlloc();
                    var builder = img.visit(i.next(), true, ImageBuilder.builder()).builder;
                    if (img.gif) {
                        tag = builder.toGifTag(id, x, y, z, Float.parseFloat(i.next().toString()), Float.parseFloat(i.next().toString()), direction);
                    } else {
                        tag = builder.toTag(id, x, y, z, Float.parseFloat(i.next().toString()), Float.parseFloat(i.next().toString()), direction);
                    }
                    break;
                }
                default:
                    return;
            }
            if (world != null) {
                VexViewAPI.addWorldTag(world, tag);
            } else if (player != null) {
                VexViewAPI.addPlayerTag(player, tag);
            }
        } catch (Throwable throwable) {
            if (dump_errors)
                instance.getLogger().log(Level.WARNING, "Error in parsing tag: " + path, throwable);
            throw new I18nException("invalid.tag.exception", path, throwable.toString());
        }
    }

    public static void openHUD(Player player, String path, String id, int xOffset, int yOffset, int z, int keepTime) {
        var block = block(path);
        CommandLine firstLine = null;
        for (var l : block.getLines()) {
            if (l instanceof CommandLine) {
                firstLine = (CommandLine) l;
                break;
            }
        }
        if (firstLine == null) {
            if (dump_errors)
                instance.getLogger().log(Level.WARNING, "No data in " + path);
            throw new I18nException("no.data", path);
        }
        var it = firstLine.getArguments().iterator();
        switch (it.next().toString()) {
            case "image":
                try {
                    var b = new ImageAlloc().visit(it.next(), true, ImageBuilder.builder()).builder;
                    var hud = b.offset(xOffset, yOffset).toHUD(id, keepTime, z);
                    VexViewAPI.sendHUD(player, hud);
                } catch (Throwable thr) {
                    if (dump_errors)
                        instance.getLogger().log(Level.WARNING, "Error in parsing hud in image: " + path, thr);
                    throw new I18nException("invalid.hud.exception", path, thr.toString());
                }
                break;
            case "text": {
                try {
                    var text = TextAlloc.INSTANCE.visit(it.next(), true, TextBuilder.builder());
                    VexViewAPI.sendHUD(player, text.offset(xOffset, yOffset).toHUD(id, keepTime, z));
                } catch (Throwable thr) {
                    if (dump_errors)
                        instance.getLogger().log(Level.WARNING, "Error in parsing hud in text: " + path, thr);
                    throw new I18nException("invalid.hud.exception", path, thr.toString());
                }
                break;
            }
            default:
                if (dump_errors)
                    instance.getLogger().log(Level.WARNING, "Invalid hud: " + path);
                throw new I18nException("invalid.hud", path);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            try {
                switch (args[0]) {
                    case "gui": {
                        openGUI(Bukkit.getPlayer(args[1]), args[2]);
                        break;
                    }
                    case "hud": {
                        openHUD(Bukkit.getPlayer(args[1]), args[2], args[3], Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]), Integer.parseInt(args[7]));
                        break;
                    }
                    case "tag": {
                        Player player = null;
                        World world = null;
                        switch (args[1]) {
                            case "player":
                                player = Bukkit.getPlayer(args[2]);
                                break;
                            default:
                                world = Bukkit.getWorld(args[2]);
                        }
                        openTag(player, world, args[3], args[4], Double.parseDouble(args[5]), Double.parseDouble(args[6]), Double.parseDouble(args[7]));
                        break;
                    }
                }
            } catch (Throwable e) {
                throw new CommandException("Error", e);
            }
        }
        return true;
    }
}
