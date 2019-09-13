/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Store.java@author: karlatemp@vip.qq.com: 19-9-11 下午10:23@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.store;

/**
 * {@link cn.mcres.karlatemp.mxlib.tools.Pointer}
 *
 * @param <T>
 */
@Deprecated
public class Store<T> {

    public T v;


    public Store() {
        this(null);
    }

    public Store(T v) {
        this.v = v;
    }

    public T value() {
        return v;
    }

    public Store<T> value(T v) {
        this.v = v;
        return this;
    }
}
