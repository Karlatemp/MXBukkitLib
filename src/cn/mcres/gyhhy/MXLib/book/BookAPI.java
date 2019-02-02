/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.book;

import cn.mcres.gyhhy.MXLib.RefUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.PlayerInventory;
import cn.mcres.gyhhy.MXLib.bukkit.MXAPI;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.List;
import org.bukkit.Material;

/**
 *
 * @author 32798
 */
public class BookAPI {
    private static BookAPI api;
    public static BookAPI install() {
        if(api == null){
            try {
                api = Class.forName(MXAPI.class.getPackage().getName() + "." + MXAPI.getInfo().getServerNMSVersion() + ".BookApi").asSubclass(BookAPI.class).newInstance();
            } catch (Exception ex) {
            }
        }
        if(api == null){api = new BookAPI();}
        return api;
    }
    public static BookAPI getApi(){return install();}
    public void sendBook(Player player, List<String> pages) {
        sendBook(player, book(pages));
    }

    public void sendBook(Player player) {
        PlayerInventory pinv = player.getInventory();
        int sort = pinv.getHeldItemSlot();
        ItemStack stack = pinv.getItem(sort);

        if (stack == null || !(stack.getItemMeta() instanceof BookMeta)) {
            throw new java.lang.IllegalArgumentException("Need a book!");
        }
//        pinv.setItem(sort, stack);
        sendOpen(player);
//        pinv.setItem(sort, old);
    }
    
    public void sendBook(Player player, ItemStack stack) {
        if (!(stack.getItemMeta() instanceof BookMeta)) {
            throw new java.lang.IllegalArgumentException("Need a book!");
        }
        PlayerInventory pinv = player.getInventory();
        int sort = pinv.getHeldItemSlot();
        ItemStack old = pinv.getItem(sort);
        pinv.setItem(sort, stack);
        sendOpen(player);
        pinv.setItem(sort, old);
    }

    @java.lang.SuppressWarnings("rawtypes")
    protected void sendPacket(Player player, Object packet) {
        Object handle = RefUtil.ink(player, "getHandle", RefUtil.emptyClassPar, RefUtil.emptyArgPar);
        RefUtil.ink(
                RefUtil.get(handle, "playerConnection"),
                "sendPacket", new Class[]{
                    MXAPI.getNMSClass("Packet")
                }, new Object[]{packet});
    }

    @java.lang.SuppressWarnings({"UseSpecificCatch", "BroadCatchBlock", "TooBroadCatch"})
    protected void sendOpen(Player player) {

        ByteBuf buf = Unpooled.buffer(256);
        buf.setByte(0, 0);
        buf.writerIndex(1);
        if (MXAPI.support(MXAPI.getInfo().getServerMinecraftVersion(), "1.13")) {
            try {
                sendPacket(player, MXAPI.getNMSClass("PacketPlayOutCustomPayload").getConstructor(
                        MXAPI.getNMSClass("MinecraftKey"), MXAPI.getNMSClass("PacketDataSerializer")
                ).newInstance(
                        MXAPI.getNMSClass("MinecraftKey").getConstructor(String.class).newInstance("minecraft:book_open"),
                        MXAPI.getNMSClass("PacketDataSerializer").getConstructor(ByteBuf.class).newInstance(buf)
                ));
//        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload(new MinecraftKey("minecraft:book_open"), new PacketDataSerializer(buf));
            } catch (RuntimeException | Error re) {
                throw re;
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        } else {
            //   PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(buf));
            try {
                sendPacket(player, MXAPI.getNMSClass("PacketPlayOutCustomPayload").getConstructor(
                        String.class, MXAPI.getNMSClass("PacketDataSerializer")
                ).newInstance(
                        "MX|BOpen",
                        MXAPI.getNMSClass("PacketDataSerializer").getConstructor(ByteBuf.class).newInstance(buf)
                ));
            } catch (RuntimeException | Error re) {
                throw re;
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @java.lang.SuppressWarnings({"rawtypes", "UseSpecificCatch", "BroadCatchBlock", "TooBroadCatch"})
    protected ItemStack book(List<String> pages) {
        try {
            ItemStack iis = new ItemStack(Material.WRITTEN_BOOK, 1);
            Object nms = RefUtil.ink(
                    MXAPI.getOBCClass("inventory.CraftItemStack"), "asNMSCopy", new Class[]{ItemStack.class}, new Object[]{iis});
            Object bd = MXAPI.getNMSClass("NBTTagCompound").newInstance();
            RefUtil.ink(bd, "setString", new Class[]{String.class, String.class}, new Object[]{"title", "Server book"});
            RefUtil.ink(bd, "setString", new Class[]{String.class, String.class}, new Object[]{"author", "The server"});
            Object bp = MXAPI.getNMSClass("NBTTagList").newInstance();
            for (String text : pages) {
                RefUtil.ink(bp, "add", new Class[]{MXAPI.getNMSClass("NBTBase")}, new Object[]{
                    MXAPI.getNMSClass("NBTTagString").getConstructor(String.class).newInstance(text)
                });
            }
            RefUtil.ink(bd, "set", new Class[]{String.class, MXAPI.getNMSClass("NBTBase")}, new Object[]{"pages", bp});
            RefUtil.ink(nms, "setTag", new Class[]{MXAPI.getNMSClass("NBTTagCompound")}, new Object[]{bd});
            return RefUtil.ink(
                    MXAPI.getOBCClass("inventory.CraftItemStack"), "asBukkitCopy", new Class[]{MXAPI.getNMSClass("ItemStack")}, new Object[]{nms});
        } catch (RuntimeException | Error re) {
            throw re;
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
        /*org.bukkit.inventory.ItemStack is = new org.bukkit.inventory.ItemStack(Material.WRITTEN_BOOK, 1);
    net.minecraft.server.v1_12_R1.ItemStack nmsis = CraftItemStack.asNMSCopy(is);
    NBTTagCompound bd = new NBTTagCompound();
    bd.setString("title", "Server book");
    bd.setString("author", "The server");
    NBTTagList bp = new NBTTagList();
    for (String text : pages) {
      bp.add(new NBTTagString(color(text).replaceAll("%n", "\n")));
    }
    bd.set("pages", bp);
    nmsis.setTag(bd);
    is = CraftItemStack.asBukkitCopy(nmsis);
    return is;*/
    }
}
