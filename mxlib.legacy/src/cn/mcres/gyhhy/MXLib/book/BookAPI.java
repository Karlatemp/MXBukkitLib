
/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BookAPI.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.book;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * {@link cn.mcres.karlatemp.mxlib.bukkit.BookAPI}
 *
 * @author Karlatemp
 */
@Deprecated
public class BookAPI {
    private final cn.mcres.karlatemp.mxlib.bukkit.BookAPI bapi;

    private BookAPI() {
        final IBeanManager manager = MXBukkitLib.getBeanManager();
        bapi = manager.getBeanNonNull(cn.mcres.karlatemp.mxlib.bukkit.BookAPI.class);
    }

    private static BookAPI api;

    public static BookAPI install() {
        if (api == null) {
            api = new BookAPI();
        }
        return api;
    }

    public static BookAPI getApi() {
        return install();
    }

    public void sendBook(Player player, List<String> pages) {
        bapi.sendBook(player, pages);
    }

    public void sendBook(Player player) {
        bapi.sendBook(player);
    }

    public void sendBook(Player player, ItemStack stack) {
        bapi.sendBook(player, stack);
    }
}
