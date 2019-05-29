/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.math.graph;

/**
 *
 * @author 32798
 */
public interface Distancer {
    /**
     * Get the distance between thie point and this object.<br>
     * 获取提供的点与该对象的距离.
     * @param o The point
     * @return The distance
     */
    double distance(Point o);
}
