/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NBTList.java@author: karlatemp@vip.qq.com: 19-11-9 上午11:47@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.nbt;

import java.util.AbstractList;
import java.util.List;

public abstract class NBTList<T extends NBTBase> extends AbstractList<T> implements NBTBase {
    public abstract T set(int index, T value);

    public abstract void add(int index, T value);

    public abstract T remove(int index);

    public abstract boolean setValue(int i, NBTBase base);

    public abstract boolean addValue(int i, NBTBase base);

    public abstract NBTList<T> clone();
}
