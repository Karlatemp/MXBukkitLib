/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: WrappedTranslate.java@author: karlatemp@vip.qq.com: 2020/1/14 下午3:14@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.translate;

import cn.mcres.karlatemp.mxlib.share.BukkitToolkit;
import cn.mcres.karlatemp.mxlib.translate.MFormatter;
import cn.mcres.karlatemp.mxlib.translate.MTranslate;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

/**
 * A wrapped translate
 * @since 2.12
 */
public class WrappedTranslate implements BTranslate {
    private final Plugin owner;
    protected BTranslate mapped;

    public Plugin getOwner() {
        return owner;
    }

    public WrappedTranslate(BTranslate mapped, Plugin owner) {
        this.mapped = mapped;
        this.owner = owner;
    }

    public void setMapped(BTranslate mapped) {
        if (owner != null) {
            if (BukkitToolkit.getCallerPlugin() != owner) {
                throw new IllegalAccessError("Only owner can set mapped translate.");
            }
        }
        this.mapped = mapped;
    }

    public BTranslate getMapped() {
        return mapped;
    }

    @Override
    public BaseComponent[] asComponents(@NotNull String key) {
        return mapped.asComponents(key);
    }

    @Override
    public String getValue(@NotNull String key) {
        return mapped.getValue(key);
    }

    @Override
    public @NotNull String asMessage(@NotNull String key) {
        return mapped.asMessage(key);
    }

    @Override
    public @NotNull String asMessage(@NotNull String key, Object... params) {
        return mapped.asMessage(key, params);
    }

    @Override
    public @NotNull MFormatter getFormatter() {
        return mapped.getFormatter();
    }

    @Override
    public @NotNull MTranslate setFormatter(@NotNull MFormatter formatter) {
        mapped.setFormatter(formatter);
        return this;
    }

    @Override
    public boolean color() {
        return mapped.color();
    }

    @Override
    public @NotNull MTranslate color(boolean isEnable) {
        mapped.color(isEnable);
        return this;
    }

    @Override
    public ResourceBundle asResourceBundle() {
        return mapped.asResourceBundle();
    }
}
