/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Test.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.math.graph;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

/**
 *
 * @author 32798
 */
public class Test {

    public static void main(String[] args) throws Throwable{
        Distancer obj = new KBStraightLine(0,0);
        Method met = obj.getClass().getMethod("distance",Distancer.class);
        System.out.println(met.getDeclaringClass());
    }
    
}
