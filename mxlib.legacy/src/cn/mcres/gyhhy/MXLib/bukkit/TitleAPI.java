
/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: TitleAPI.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.bukkit;

import cn.mcres.karlatemp.mxlib.bukkit.PacketFormatter;
import org.bukkit.entity.Player;
import cn.mcres.gyhhy.MXLib.gson.GsonHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link cn.mcres.karlatemp.mxlib.bukkit.TitleAPI}
 *
 * @author Karlatemp
 */
@Deprecated
public interface TitleAPI {

    /**
     * Set the format type in title api<br>
     * add in version 0.19
     * {@link PacketFormatter}
     */
    @Deprecated
    abstract class TextFormatType implements PacketFormatter {

        /**
         * &gt; "{\"text\":\"A\"}"<br>
         * &lt; "{\"text\":\"A\"}"
         */
        public static TextFormatType PAINT = new TextFormatType() {
            @Override
            public String format(String s) {
                return s;
            }
        };
        /**
         * &gt; "Text" <br>
         * &lt; "{\"text\":\"Text\"}"
         */
        public static TextFormatType GSON = new TextFormatType() {
            @Override
            public String format(String s) {
                return PacketFormatter.LOSSLESS.format(s);
            }
        };
        /**
         * &gt; "Text"<br>
         * return "{\"text\":\"" + input + "\"}"<br>
         * &lt; "{\"text\":\"A\"}"
         */
        public static TextFormatType DEFAULT = new TextFormatType() {
            @Override
            public String format(String s) {
                return "{\"text\":\"" + s + "\"}";
            }
        };

        public static TextFormatType custom(java.util.function.Function<String, String> func) {
            return new TextFormatType() {
                @Override
                public String format(String s) {
                    return func.apply(s);
                }
            };
        }

        public abstract String format(String s);
    }

    /**
     * send a packer
     *
     * @param player player
     * @param packet packet
     */
    void sendPacket(Player player, Object packet);

    /**
     * get net.minecraft.server.[version].[path]
     *
     * @return a class or null.
     */
    Class<?> getNMSClass(String name);

    /**
     * send title bar.
     *
     * @param player   Player
     * @param fadeIn   in time
     * @param stay     stay time
     * @param fadeOut  out time
     * @param title    title
     * @param subtitle subtitle
     */
    default void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        sendTitle(player, fadeIn, stay, fadeOut, title, subtitle, TextFormatType.DEFAULT);
    }

    /**
     * send title bar.
     *
     * @param player   Player
     * @param fadeIn   in time
     * @param stay     stay time
     * @param fadeOut  out time
     * @param title    title
     * @param subtitle subtitle
     */
    void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle, TextFormatType format);

    /**
     * Same as {@link #sendTitle(Player, Integer, Integer, Integer, String, String)
     * }
     * <br>Add in version 0.7.5
     *
     * @param player   Player
     * @param fadeIn   Fade in time(tick)
     * @param stay     Stay time(tick)
     * @param fadeOut  Fade Out time(tick)
     * @param title    Title
     * @param subtitle subtitle
     */
    default void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
        sendTitle(player, fadeIn, stay, fadeOut, title, subtitle, TextFormatType.DEFAULT);
    }

    /**
     * Same as {@link #sendTitle(Player, Integer, Integer, Integer, String, String)
     * }
     * <br>Add in version 0.7.5
     *
     * @param player   Player
     * @param fadeIn   Fade in time(tick)
     * @param stay     Stay time(tick)
     * @param fadeOut  Fade Out time(tick)
     * @param title    Title
     * @param subtitle subtitle
     */
    default void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle, TextFormatType format) {
        this.sendTitle(player, (Integer) fadeIn, (Integer) stay, (Integer) fadeOut, title, subtitle, format);
    }

    /**
     * clear a player's title
     *
     * @param player Player
     */
    void clearTitle(Player player);

    /**
     * Set player's [tab] title
     */
    default void sendTabTitle(Player player, String header, String footer) {
        sendTabTitle(player, header, footer, TextFormatType.DEFAULT);
    }

    /**
     * Set player's [tab] title
     *
     * @param type format type
     */
    void sendTabTitle(Player player, String header, String footer, TextFormatType type);

    /**
     * send a out chat
     *
     * @param player player
     * @param text   text
     */
    default void sendOutChat(Player player, String text) {
        sendOutChat(player, text, TextFormatType.DEFAULT);
    }

    /**
     * send a out chat
     *
     * @param player player
     * @param text   text
     * @param type   format type
     */
    void sendOutChat(Player player, String text, TextFormatType type);
}
