/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.chat;
import org.bukkit.entity.Player;
public interface BungeeChatPacket {
    default void sendTo(Player player){
        BungeeChatAPI.getAPI().send(player,this);
    }
}
