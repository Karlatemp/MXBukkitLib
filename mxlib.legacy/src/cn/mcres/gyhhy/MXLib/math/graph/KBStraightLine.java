/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: KBStraightLine.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.math.graph;

/**
 * KB Straight Line<br>
 * a straight line represented by a function. <br>
 * 使用一次函数表示的直线<br>
 *
 * <br>
 * The Function in Math:<br>
 * <code> y = kx + b (y = k * x + b)</code>
 */
public class KBStraightLine implements StraightLine {

    private final double b;
    private final double k;

    public KBStraightLine(double k, double b) {
        this.k = k;
        this.b = b;
    }

    protected KBStraightLine(double x1, double y1, double x2, double y2) {
        if (x1 == x2) {
            throw new RuntimeException("Please use VerticalStraightLine");
        }
        // y = kx + b
        // y1 = k * x1 + b
        // y2 = k * x2 + b

        // y2 - y1 = k * x2 + b - k * x1 - b
        // y2-y1 = k * (x2-x1)
        // (y2-y1) / (x2 - x1) = k
        double k = (y2 - y1) / (x2 - x1);
        // y = kx+b
        // y - kx = b
        double b_ = y1 - k * x1;
        this.k = k;
        this.b = b_;
    }

    public KBStraightLine(Point a, Point b) {
        this(a.getXAsDouble(), a.getYAsDouble(), b.getXAsDouble(), b.getYAsDouble());
        double t = a.getZAsDouble();
        if (t != 0 || t != b.getZAsDouble()) {
            throw new NumberFormatException("KB Straight Line unsupported 3D line.");
        }
    }

    @Override
    public Point getRandPoint() {
        return getPoint(Math.random() * 3279826484L);
    }

    public double getK() {
        return k;
    }

    public static void main(String[] args) {
        System.out.println(new KBStraightLine(0, 0, 1, 0).distance(new IntPoint(1, 1)));
    }

    /**
     * 直线方程中 <br>
     * A = y2 - y1,<br>
     * B = x1 - x2, <br>
     * C = (x2 * y1) - (x1 * y2)<br><br>
     * Copy from <a href="https://blog.csdn.net/ffgcc/article/details/79941512">
     * https://blog.csdn.net/ffgcc/article/details/79941512</a>
     */
    public static double distance(double A, double B, double C, Point p) {
        return ABCStraightLine.distance(A, B, C, p);
    }

    @Override
    public double distance(Point p) {
        if (k == 0) {
            return Math.abs(b - p.getYAsDouble());
        }
        /*
        Point 1 (
            0, 0 * k + b = b
        ) ( 0, b );
        Point 2 (
            1, 1 * k + b = k + b
        ) ( 0, k + b )
        A = y2 - y1 = (k + b) - b = k
        B = x1 - x2 = 0 - 1 = -1
        C = (x2 * y1) - (x1 * y2)
              = ( 1 * b ) - (0 * (k+b))
              = b
         */
        return distance(k, -1, b, p);
    }

    public double getB() {
        return b;
    }

    @Override
    public Point getPoint(int x) {
        return getPoint((double) x);
    }

    @Override
    public Point getPoint(double x) {
        return new DoublePoint(x, x * k + b);
    }

    @Override
    public ABCStraightLine toABCStraightLine() {
        return new ABCStraightLine(k, -1, b);
    }

}
