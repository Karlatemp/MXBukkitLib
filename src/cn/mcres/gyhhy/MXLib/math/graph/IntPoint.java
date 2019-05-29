/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.math.graph;

public class IntPoint implements Point {

    private final int x, y, z;

    public IntPoint(int x, int y) {
        this(x, y, 0);
    }

    public IntPoint(int x, int y, int z) {
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
        return x;
    }

    @Override
    public int getYAsInt() {
        return y;
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
    public Integer getX() {
        return x;
    }

    @Override
    public Integer getY() {
        return y;
    }

    @Override
    public Integer getZ() {
        return z;
    }

    @Override
    public int getZAsInt() {
        return z;
    }

    @Override
    public double getZAsDouble() {
        return z;
    }

}
