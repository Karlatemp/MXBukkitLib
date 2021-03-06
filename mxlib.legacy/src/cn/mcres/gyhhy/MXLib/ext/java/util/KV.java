/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: KV.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.ext.java.util;

public class KV<K, V> implements Comparable<Object> {

    public K k;
    public V v;

    public KV() {
        this(null, null);
    }

    public KV(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public String toString() {
        return String.valueOf(k);
    }

    @Override
    public int compareTo(Object o) {
        return toString().compareTo(String.valueOf(o));
    }

}
