/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ListAndSet.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.ext.java.util;

import java.util.List;
import java.util.Set;
import java.util.Spliterator;

public interface ListAndSet<T> extends Set<T>, List<T> {

    @Override
    Spliterator<T> spliterator();

    @SuppressWarnings("unchecked")
    public static <T> ListAndSet<T> mapping(Set<T> set) {
        if(set instanceof ComparatorSet){
            return new ComparatorSetToList<>((ComparatorSet<T>)set);
        }
        if (set instanceof ListAndSet) {
            return (ListAndSet) set;
        }
        return new SetToList<>(set);
    }

    public static <T> ListAndSet<T> mapping(ListAndSet<T> mapping) {
        return mapping;
    }

    @SuppressWarnings("unchecked")
    public static <T> ListAndSet<T> mapping(List<T> list) {
        if (list instanceof ListAndSet) {
            return (ListAndSet) list;
        }
        return new ListToSet<>(list);
    }
}
