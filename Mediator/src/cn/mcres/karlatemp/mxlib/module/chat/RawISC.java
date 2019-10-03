/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: RawISC.java@author: karlatemp@vip.qq.com: 19-10-3 下午3:10@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.chat;

import cn.mcres.karlatemp.mxlib.util.GsonHelper;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.inventory.ItemStack;

abstract class RawISC extends BaseComponent implements GsonHelper.JsonSerializable {
    private final String json;

    protected RawISC(ItemStack stack) {
        this.json = toJson(stack);
    }

    protected abstract String toJson(ItemStack stack);

    @Override
    public String toJson() {
        return json;
    }

    @Override
    public BaseComponent duplicate() {
        return null;
    }

    interface CCR {
        RawISC create(ItemStack stack);
    }
}
