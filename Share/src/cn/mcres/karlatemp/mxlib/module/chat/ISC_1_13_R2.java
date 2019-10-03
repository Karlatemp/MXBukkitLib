/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ISC_1_13_R2.java@author: karlatemp@vip.qq.com: 19-10-3 下午2:30@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.chat;

import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

class ISC_1_13_R2 extends RawISC {

    protected ISC_1_13_R2(ItemStack stack) {
        super(stack);
    }

    protected String toJson(ItemStack stack) {
        final net.minecraft.server.v1_13_R2.ItemStack nmsCopy = CraftItemStack.asNMSCopy(stack);
        return ChatSerializer.a(nmsCopy.A());
    }

    static class Power implements CCR {
        @Override
        public RawISC create(ItemStack stack) {
            return new ISC_1_13_R2(stack);
        }
    }
}
