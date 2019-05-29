/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.math.graph;

public interface Point extends Distancer {
    Number getX();

    Number getY();

    Number getZ();

    default int getZAsInt() {
        return getZ().intValue();
    }

    default double getZAsDouble() {
        return getZ().doubleValue();
    }

    default int getXAsInt() {
        return getX().intValue();
    }

    default int getYAsInt() {
        return getY().intValue();
    }

    default double getXAsDouble() {
        return getX().doubleValue();
    }

    default double getYAsDouble() {
        return getY().doubleValue();
    }

    @Override
    default double distance(Point p) {
        double x = this.getXAsDouble() - p.getXAsDouble();
        double y = this.getYAsDouble() - p.getYAsDouble();
        double z = this.getZAsDouble() - p.getZAsDouble();
        if (x == 0) {
            if (y == 0) {
                return Math.abs(z);
            }
            if (z == 0) {
                return Math.abs(y);
            }
        }
        if (y == 0) {
            if (z == 0) {
                return Math.abs(x);
            }
        }
        return Math.sqrt(x * x + y * y + z * z);
    }
    
}
