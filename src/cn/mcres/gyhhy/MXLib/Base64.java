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
}
