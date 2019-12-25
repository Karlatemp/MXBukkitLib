/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ISC_1_8_R1.java@author: karlatemp@vip.qq.com: 19-10-3 下午1:58@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.chat;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

class ISC_1_8_R1 extends RawISC {

    protected ISC_1_8_R1(ItemStack stack) {
        super(stack);
    }

    protected String toJson(ItemStack stack) {
        final net.minecraft.server.v1_8_R1.ItemStack nmsCopy = CraftItemStack.asNMSCopy(stack);
        return ChatSerializer.a(nmsCopy.C());
    }

    static class Power implements CCR {
        @Override
        public RawISC create(ItemStack stack) {
            return new ISC_1_8_R1(stack);
        }
    }
}
