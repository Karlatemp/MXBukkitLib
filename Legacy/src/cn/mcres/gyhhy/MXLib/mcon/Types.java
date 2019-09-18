/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Types.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.mcon;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author 32798
 */
public interface Types {

    int INVOKE_COMMAND = 47;
    int ADD_PERMISSION = 5;
    int REMOVE_PERMISSION = 6;
    int STOP = 7;
    int SET_NAME = 9;
    int SET_OP = 2;
    Charset UTF_8 = StandardCharsets.UTF_8;

    default boolean parseBool(String s) {
        if (s == null) {
            return false;
        }
        s = s.trim();
        if (s.isEmpty()) {
            return false;
        }
        if (s.equalsIgnoreCase("false")) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != '0') {
                return true;
            }
        }
        return false;
    }
}
