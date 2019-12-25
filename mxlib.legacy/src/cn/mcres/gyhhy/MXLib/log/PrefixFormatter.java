/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PrefixFormatter.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
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
public interface PrefixFormatter {

    String format(String prefix, String msg);

    String format(String prefix, String msg, boolean isErrorPrefix);

    /**
     * Lambda
     */
    public static interface PrefixFormatterNoBool extends PrefixFormatter {

        @Override
        default String format(String prefix, String msg, boolean isErrorPrefix) {
            return format(prefix, msg);
        }
    }

    /**
     * Lambda
     */
    public static interface PrefixFormatterWithBool extends PrefixFormatter {

        @Override
        default String format(String prefix, String msg) {
            return format(prefix, msg, false);
        }
    }
}
