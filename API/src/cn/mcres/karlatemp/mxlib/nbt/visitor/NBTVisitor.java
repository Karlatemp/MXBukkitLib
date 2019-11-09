/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NBTVisitor.java@author: karlatemp@vip.qq.com: 19-11-9 下午1:58@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.nbt.visitor;

import cn.mcres.karlatemp.mxlib.nbt.*;

public interface NBTVisitor {
    default void visitByte(String name, NBTTagByte value) {
    }

    default void visitByteArray(String name, NBTTagByteArray value) {
    }

    default void visitDouble(String name, NBTTagDouble value) {
    }

    default void visitEnd() {
    }

    default void visitFloat(String name, NBTTagFloat value) {
    }

    default void visitInt(String name, NBTTagInt value) {
    }

    default void visitIntArray(String name, NBTTagIntArray value) {
    }

    default void visitList(String name, NBTTagList value) {
    }

    default void visitLong(String name, NBTTagLong value) {
    }

    default void visitLongArray(String name, NBTTagLongArray value) {
    }

    default void visitShort(String name, NBTTagShort value) {
    }

    default void visitString(String name, NBTTagString value) {
    }

    default NBTVisitor visitCompound(String name, NBTTagCompound value) {
        return null;
    }
}
