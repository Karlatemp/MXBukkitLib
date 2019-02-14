/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.v1_11_R1;

import cn.mcres.gyhhy.MXLib.book.BookAPI;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.List;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import net.minecraft.server.v1_11_R1.NBTTagList;
import net.minecraft.server.v1_11_R1.NBTTagString;
import net.minecraft.server.v1_11_R1.ItemStack;
import net.minecraft.server.v1_11_R1.PacketDataSerializer;
import net.minecraft.server.v1_11_R1.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_11_R1.Packet;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 *
 * @author 32798
 */
public class BookApi extends BookAPI {

    @Override
    protected void sendOpen(Player player) {
        ByteBuf buf = Unpooled.buffer(256);
        buf.setByte(0, 0);
        buf.writerIndex(1);
        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(buf));
        sendPacket(player,packet);
    }
    

    @Override
    protected void sendPacket(Player player, Object packet) {
        if(packet instanceof Packet){
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packet);
        }
    }
    @Override
    protected org.bukkit.inventory.ItemStack book(List<String> pages) {
        org.bukkit.inventory.ItemStack is = new org.bukkit.inventory.ItemStack(Material.WRITTEN_BOOK, 1);
        ItemStack nmsis = CraftItemStack.asNMSCopy(is);
        NBTTagCompound bd = new NBTTagCompound();
        bd.setString("title", "Server book");
        bd.setString("author", "The server");
        NBTTagList bp = new NBTTagList();
        for (String text : pages) {
            bp.add(new NBTTagString(text));
        }
        bd.set("pages", bp);
        nmsis.setTag(bd);
        is = CraftItemStack.asBukkitCopy(nmsis);
        return is;
    }

}
