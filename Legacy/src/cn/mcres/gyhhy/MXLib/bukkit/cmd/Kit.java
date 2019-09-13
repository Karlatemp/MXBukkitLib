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
