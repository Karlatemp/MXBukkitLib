package cn.mcres.karlatemp.mxlib.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface BookAPI {
    void sendBook(Player player, List<String> pages);

    void sendBook(Player player);

    void sendBook(Player player, ItemStack stack);
}
