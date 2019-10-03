/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ItemStackComponent.java@author: karlatemp@vip.qq.com: 19-10-3 下午1:41@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.chat;

import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import cn.mcres.karlatemp.mxlib.util.GsonHelper;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.md_5.bungee.api.chat.BaseComponent;
import com.google.gson.annotations.JsonAdapter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * A Component as ItemStack. It is <b>unmodifiable.</b>
 * <p>
 * Example:
 * {@code p.spigot().sendMessage(new ItemStackComponent(p.getInventory().getItemInMainHand()));}
 * <p>
 * It you want to modify ItemStackComponent. Use {@link #duplicate()} to get components as TextComponent.
 *
 * @since 2.3
 */
@JsonAdapter(ItemStackComponent.Adapter.class)
public class ItemStackComponent extends BaseComponent implements GsonHelper.JsonSerializable {
    private RawISC isc;
    private static RawISC.CCR power;

    @Override
    public String toJson() {
        return isc.toJson();
    }

    @Override
    public void setExtra(List<BaseComponent> components) {
    }

    @Override
    public void addExtra(String text) {
    }

    @Override
    public void addExtra(BaseComponent component) {
    }

    @Override
    public void setBold(Boolean bold) {
    }

    @Override
    public void setItalic(Boolean italic) {
    }

    @Override
    public void setUnderlined(Boolean underlined) {
    }

    @Override
    public void setStrikethrough(Boolean strikethrough) {
    }

    @Override
    public void setObfuscated(Boolean obfuscated) {
    }

    @Override
    public void setInsertion(String insertion) {
    }

    @Override
    public void setClickEvent(ClickEvent clickEvent) {
    }

    @Override
    public void setHoverEvent(HoverEvent hoverEvent) {
    }

    @Override
    public String getInsertion() {
        return null;
    }

    @Override
    public List<BaseComponent> getExtra() {
        return Collections.emptyList();
    }

    @Override
    public ClickEvent getClickEvent() {
        return null;
    }

    @Override
    public HoverEvent getHoverEvent() {
        return null;
    }

    static class Adapter extends TypeAdapter<ItemStackComponent> {
        @Override
        public void write(JsonWriter writer, ItemStackComponent bcc) throws IOException {
            if (bcc == null) {
                writer.nullValue();
            } else {
                writer.value(GsonHelper.toRawString(bcc.isc));
            }
        }

        @Override
        public ItemStackComponent read(JsonReader jsonReader) throws IOException {
            return null;// Un-support read.
        }
    }

    public ItemStackComponent(ItemStack stack) {
        isc = power.create(stack);
    }

    @Override
    public TextComponent duplicate() {
        return new TextComponent(
                net.md_5.bungee.chat.ComponentSerializer.parse(isc.toJson())
        );
    }

    public static void setPower(Class<?> token, Object impl) {
        if (token == RawISC.CCR.class) {
            if (impl instanceof RawISC.CCR) {
                power = (RawISC.CCR) impl;
            }
        }
    }

    @Nullable
    public static Class<?> getPowerClass() {
        Class c = Toolkit.Reflection.getCallerClass();
        if (c == null) return null;
        if (c.getClassLoader() != ItemStackComponent.class.getClassLoader())
            return null;
        if (c.getName().startsWith("cn.mcres.karlatemp.mxlib.") ||
                c.getName().startsWith("cn.mcres.gyhhy.MXLib.")) {
            return RawISC.CCR.class;
        }
        return null;
    }
}
