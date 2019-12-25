/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NameHistory.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.yggdrasil.beans.mojang;

/**
 *
 * @author 32798
 */
public class NameHistory {

    private final NameHistoryElement[] e;

    public NameHistory() {
        this(null);
    }

    public NameHistory(NameHistoryElement[] elements) {
        this.e = elements;
    }

    public NameHistoryElement[] getElements() {
        return e.clone();
    }

    /**
     * Return first name of user
     */
    public String getUserRegisterName() {
        return e[0].n;
    }

    /**
     * Return user current name
     */
    public String getUserName() {
        return e[e.length - 1].n;
    }

    public static class NameHistoryElement {

        private final long c;
        private final String n;

        public NameHistoryElement(String name, long changeToAt) {
            this.n = name;
            this.c = changeToAt;
        }

        public String getName() {
            return n;
        }

        public long getNameAt() {
            return c;
        }
    }
}
