/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NonFormatter.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.log;

/**
 *
 * @author 32798
 */
public class NonFormatter implements PrefixFormatter.PrefixFormatterNoBool {

    private static final NonFormatter instance = new NonFormatter();

    public static NonFormatter getInstance() {
        return instance;
    }

    @Override
    public String format(String prefix, String msg) {
        return prefix;
    }

}
