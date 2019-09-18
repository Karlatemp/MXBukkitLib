/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: StraightLine.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.math.graph;

public interface StraightLine extends Distancer {

    /**
     * Get a random point from this line.<br>
     * 获取直线上的随机一个点
     *
     * @return Random point
     */
    default Point getRandPoint() {
        return getPoint(Math.random() * 3279826484L);
    }

    default Point getPoint(int x) {
        return getPoint((double) x);
    }

    Point getPoint(double x);

    /**
     * Return two points that form a line.<br>
     * 返回构造直线的两个点
     *
     * @return a Point array of length 2
     */
    default Point[] getPointSet() {
        return new Point[]{getRandPoint(), getRandPoint()};
    }

    ABCStraightLine toABCStraightLine();

    @Override
    default double distance(Distancer d) {
        if (d instanceof Point) {
            return distance((Point) d);
        }
        return toABCStraightLine().distance(d);
    }

}
