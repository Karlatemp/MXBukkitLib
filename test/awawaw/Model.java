/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package awawaw;
import cn.mcres.gyhhy.MXLib.StringHelper;
import java.io.IOException;

/**
 *
 * @author 32798
 */
public class Model {
    public static void main(String... ara) throws IOException{
        String email = "3279826484@qq.com".toLowerCase();
        System.out.println(StringHelper.md5(email).toLowerCase());
    }
    
}
