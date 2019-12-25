/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: GsonHelper.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

/**
 *
 * @author 32798
 */
public class GsonHelper {
    public static final Gson def = new Gson();
    public static final Gson format = new GsonBuilder().setPrettyPrinting().create();
    public static <T extends JsonWriter> T setPrettyPrinting(Gson gson,T t){
        t.setIndent("  ");
        return t;
    }
}
