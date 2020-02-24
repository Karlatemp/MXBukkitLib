/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/19 20:28:10
 *
 * MXLib/expend.vexview.interpreter/PlaceholderAPISupport.java
 */

package cn.mcres.karlatemp.vit;

import org.bukkit.OfflinePlayer;

public interface PlaceholderAPISupport {
    String run(OfflinePlayer player, String source);
}
