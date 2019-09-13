package cn.mcres.karlatemp.mxlib.bukkit;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public interface TitleAPI {
    void sendPacket(@NotNull Player player, @NotNull Object packet);

    void sendTitle(@NotNull Player player, @Nullable String title, @Nullable String subtitle, int fadeIn, int stay, int fadeOut, @NotNull PacketFormatter formatter);

    void setTabTitle(@NotNull Player player, @NotNull String header, @NotNull String footer, @NotNull PacketFormatter formatter);

    void sendActionBar(@NotNull Player player, @NotNull String action, @NotNull PacketFormatter formatter);
}
