/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit;

import org.bukkit.entity.Player;
import cn.mcres.gyhhy.MXLib.gson.GsonHelper;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author 32798
 */
public interface TitleAPI {

    /**
     * Set the format type in title api<br>
     * add in version 0.19
     */
    public static abstract class TextFormatType {

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
                if (MXAPI.gson) {
                    Map<String, String> data = new HashMap<>();
                    data.put("text", s);
                    return GsonHelper.def.toJson(data);
                }
                return DEFAULT.format(s);
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
     * @param name
     * @return a class or null.
     */
    Class<?> getNMSClass(String name);

    /**
     * send title bar.
     *
     * @param player Player
     * @param fadeIn in time
     * @param stay stay time
     * @param fadeOut out time
     * @param title title
     * @param subtitle subtitle
     */
    default void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        sendTitle(player, fadeIn, stay, fadeOut, title, subtitle, TextFormatType.DEFAULT);
    }

    /**
     * send title bar.
     *
     * @param player Player
     * @param fadeIn in time
     * @param stay stay time
     * @param fadeOut out time
     * @param title title
     * @param subtitle subtitle
     */
    void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle, TextFormatType format);

    /**
     * Same as {@link #sendTitle(org.bukkit.entity.Player, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
     * }
     * <br>Add in version 0.7.5
     *
     * @param player Player
     * @param fadeIn Fade in time(tick)
     * @param stay Stay time(tick)
     * @param fadeOut Fade Out time(tick)
     * @param title Title
     * @param subtitle subtitle
     */
    default void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
        sendTitle(player, fadeIn, stay, fadeOut, title, subtitle, TextFormatType.DEFAULT);
    }

    /**
     * Same as {@link #sendTitle(org.bukkit.entity.Player, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
     * }
     * <br>Add in version 0.7.5
     *
     * @param player Player
     * @param fadeIn Fade in time(tick)
     * @param stay Stay time(tick)
     * @param fadeOut Fade Out time(tick)
     * @param title Title
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
     *
     * @param player
     * @param header
     * @param footer
     */
    default void sendTabTitle(Player player, String header, String footer) {
        sendTabTitle(player, header, footer, TextFormatType.DEFAULT);
    }

    /**
     * Set player's [tab] title
     *
     * @param player
     * @param header
     * @param footer
     * @param type format type
     */
    void sendTabTitle(Player player, String header, String footer, TextFormatType type);

    /**
     * send a out chat
     *
     * @param player player
     * @param text text
     */
    default void sendOutChat(Player player, String text) {
        sendOutChat(player, text, TextFormatType.DEFAULT);
    }

    /**
     * send a out chat
     *
     * @param player player
     * @param text text
     * @param type format type
     */
    void sendOutChat(Player player, String text, TextFormatType type);
}
