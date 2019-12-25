/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Distancer.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.math.graph;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 32798
 */
public interface Distancer {

    /**
     * Get the distance between thie point and this object.<br>
     * 获取提供的点与该对象的距离.
     *
     * @param o The point
     * @return The distance
     */
    double distance(Point o);

    default double distance(Distancer d) {
        if (d instanceof Point) {
            return distance((Point) d);
        }
        try {
            Method met = d.getClass().getMethod("distance", Distancer.class);
            if (met.getDeclaringClass() != Distancer.class) {
                return d.distance(this);
            }
        } catch (NoSuchMethodException | SecurityException ex) {
        }
        return Double.NaN;
    }
    
}
