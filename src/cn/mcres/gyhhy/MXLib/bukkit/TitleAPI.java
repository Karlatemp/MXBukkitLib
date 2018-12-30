/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit;

import org.bukkit.entity.Player;

/**
 *
 * @author 32798
 */
public interface TitleAPI {
    /**
     * send a packer
     * @param player player
     * @param packet packet 
     */
    void sendPacket(Player player, Object packet);
    /**
     * get net.minecraft.server.[version].[path]
     * @param name
     * @return a class or null.
     */
    Class<?> getNMSClass(String name);
    /**
     * send title bar.
     * @param player Player
     * @param fadeIn in time
     * @param stay stay time
     * @param fadeOut out time
     * @param title title
     * @param subtitle subtitle
     */
    void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle);
    /**
     * clear a player's title
     * @param player Player
     */
    void clearTitle(Player player);
    /**
     * Set player's [tab] title
     * @param player
     * @param header
     * @param footer 
     */
    void sendTabTitle(Player player, String header, String footer);
    /**
     * send a out chat
     * @param player player
     * @param text text
     */
    void sendOutChat(Player player, String text);
}
