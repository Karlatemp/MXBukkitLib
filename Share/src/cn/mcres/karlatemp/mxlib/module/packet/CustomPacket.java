/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CustomPacket.java@author: karlatemp@vip.qq.com: 19-11-10 下午2:27@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.packet;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface CustomPacket extends RawPacket {

    default void push(@NotNull Player player) {
        send(player);
    }

    default void push(@NotNull Player... players) {
        send((Object[]) players);
    }

    default void push() {
        send();
    }

    default void push(@NotNull Predicate<Player> filter) {
        send(filter);
    }
}
