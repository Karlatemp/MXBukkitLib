/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Replacer.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.format;

import java.util.function.Function;

/**
 *
 * @author 32798
 */
public interface Replacer extends Function<String, String> {

    boolean containsKey(String key);

    boolean isEmpty();
}
