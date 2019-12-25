/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DoublePoint.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.math.graph;

public class DoublePoint implements Point {

    private final double x, y, z;

    public DoublePoint(double x, double y) {
        this(x, y, 0d);
    }

    public DoublePoint(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "Point(" + x + ',' + y + ',' + z + ')';
    }

    @Override
    public int getXAsInt() {
        return (int) x;
    }

    @Override
    public int getYAsInt() {
        return (int) y;
    }

    @Override
    public double getXAsDouble() {
        return x;
    }

    @Override
    public double getYAsDouble() {
        return y;
    }

    @Override
    public Double getX() {
        return x;
    }

    @Override
    public Double getY() {
        return y;
    }

    @Override
    public Double getZ() {
        return z;

    }

    @Override
    public double getZAsDouble() {
        return z;
    }

    @Override
    public int getZAsInt() {
        return (int) z;
    }

}
