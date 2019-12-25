/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Base64.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib;
import java.nio.charset.StandardCharsets;
/**
 *
 * @author 32798
 */
public class Base64 {
    public static String encode(String str){
        return java.util.Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }
    public static String encode(byte[] bytes){
        return java.util.Base64.getEncoder().encodeToString(bytes);
    }
    public static String decode(String str){
        return new String(java.util.Base64.getDecoder().decode(str),StandardCharsets.UTF_8);
    }
    public static String decode(byte[] bytes){
        return new String(java.util.Base64.getDecoder().decode(bytes),StandardCharsets.UTF_8);
    }
    /**
     * Add in version 0.7
     */
    public static byte[] encodeBytes(String str){
        return java.util.Base64.getEncoder().encode(str.getBytes(StandardCharsets.UTF_8));
    }
    /**
     * Add in version 0.7
     */
    public static byte[] encodeBytes(byte[] bytes){
        return java.util.Base64.getEncoder().encode(bytes);
    }
    /**
     * Add in version 0.7
     */
    public static byte[] decodeBytes(byte[] bytes){
        return java.util.Base64.getDecoder().decode(bytes);
    }
    /**
     * Add in version 0.7
     */
    public static byte[] decodeBytes(String str){
        return java.util.Base64.getDecoder().decode(str);
    }
}
