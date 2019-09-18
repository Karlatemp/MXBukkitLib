/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Kit.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.cmd;

import cn.mcres.gyhhy.MXLib.ext.lookup.Looker;

interface Kit {

    final static Looker lk = new Looker(Test.class, ~0);

    static Void voids(Object... any) {
        return null;
    }

    static Void voids(Object any) {
        return null;
    }

    static Void voids(Object any, Object any2) {
        return null;
    }

    static Void voids() {
        return null;
    }
}
