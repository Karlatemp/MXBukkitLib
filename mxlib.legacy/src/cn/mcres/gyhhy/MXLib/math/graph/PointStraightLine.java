/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PointStraightLine.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.math.graph;

import static java.lang.Math.*;

public class PointStraightLine implements StraightLine {

    public static void main(String[] args) {
        PointStraightLine l = new PointStraightLine(new DoublePoint(1, 0, 7), new DoublePoint(57, 15, 5));
        System.out.format("%s,%s,%s%n%s,%s,%s%n", l.x, l.y, l.z, l.xa, l.ya, l.za);
        System.out.println(l.getPoint(0));
    }

    /**
     * Copy from
     * <a href="https://blog.csdn.net/piaoxuezhong/article/details/71519426">
     * https://blog.csdn.net/piaoxuezhong/article/details/71519426
     * </a>
     *
     * @param a The Point A from Straight Line
     * @param b The Point B from Straight Line
     * @param s The Point for distance
     * @return The Disance
     */
    public static double DistanceOfPointToLine(Point a, Point b, Point s) {
        double ab = sqrt(pow((a.getXAsDouble() - b.getXAsDouble()), 2.0) + pow((a.getYAsDouble() - b.getYAsDouble()), 2.0) + pow((a.getZAsDouble() - b.getZAsDouble()), 2.0));
        double as = sqrt(pow((a.getXAsDouble() - s.getXAsDouble()), 2.0) + pow((a.getYAsDouble() - s.getYAsDouble()), 2.0) + pow((a.getZAsDouble() - s.getZAsDouble()), 2.0));
        double bs = sqrt(pow((s.getXAsDouble() - b.getXAsDouble()), 2.0) + pow((s.getYAsDouble() - b.getYAsDouble()), 2.0) + pow((s.getZAsDouble() - b.getZAsDouble()), 2.0));
        double cos_A = (pow(as, 2.0) + pow(ab, 2.0) - pow(bs, 2.0)) / (2 * ab * as);
        double sin_A = sqrt(1 - pow(cos_A, 2.0));
        return as * sin_A;
    }

    private final double x, y, z, xa, ya, za;
    private final Point a, b;

    public PointStraightLine(Point a, Point b) {
        this.a = a;
        this.b = b;
        double x1 = a.getXAsDouble();
        double y1 = a.getYAsDouble();
        double z1 = a.getZAsDouble();
        double x2 = b.getXAsDouble();
        double y2 = b.getYAsDouble();
        double z2 = b.getZAsDouble();
        double t;
        if (x1 > x2) {
            t = x2;
            x2 = x1;
            x1 = t;

            t = y2;
            y2 = y1;
            y1 = t;

            t = z2;
            z2 = z1;
            z1 = t;
        }
        x2 -= x1;
        y2 -= y1;
        z2 -= z1;
//        double len = Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2);
        double len = x2;
//        System.out.println("L:X " + len + "," + x2);
        if (x2 != 0) {
            x2 /= len;
            y2 /= len;
            z2 /= len;
        }
        this.x = x1;
        this.y = y1;
        this.z = z1;
        this.xa = x2;
        this.ya = y2;
        this.za = z2;
    }

    @Override
    public Point getPoint(double x) {
        double xx = x - this.x;
        return new DoublePoint(this.x + xa * xx, this.y + ya * xx, this.z + za * xx);
    }

    @Override
    public Point[] getPointSet() {
        return new Point[]{a, b};
    }

    @Override
    public double distance(Point o) {
        return DistanceOfPointToLine(a, b, o);
    }

    @Override
    public ABCStraightLine toABCStraightLine() {
        /*
         * A = y2 - y1,
         * B = x1 - x2,
         * C = (x2 * y1) - (x1 * y2)
         */
        return new ABCStraightLine(
                b.getYAsDouble() - a.getYAsDouble(),
                a.getXAsDouble() - b.getXAsDouble(),
                (b.getXAsDouble() * a.getYAsDouble()) - (a.getXAsDouble() * b.getYAsDouble())
        );
    }
}
