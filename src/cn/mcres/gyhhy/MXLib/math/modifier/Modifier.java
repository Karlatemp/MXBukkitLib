/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.math.modifier;

public class Modifier {

    private int m;

    public Modifier(int modi) {
        this.m = modi;
    }

    public Modifier() {
        this(0);
    }

    public int modifier() {
        return m;
    }

    @Override
    public int hashCode() {
        return m;
    }

    public static void main(String[] args) {
        Modifier mod = new Modifier();
        mod.add(1 << 5);
        System.out.println(mod);
        System.out.println(mod.testAnyOf(0b110000));
    }

    @Override
    public String toString() {
        StringBuilder bui = new StringBuilder()
                .append("Modifier[m=").append(m).append(", bin=");
        for (int i = 0; i < 32; i++) {
            if ((m & (1 << i)) != 0) {
                bui.append('1');
            } else {
                bui.append('0');
            }
        }
        return bui.append(']').toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Modifier) {
            return ((Modifier) obj).m == m;
        }
        return false;
    }

    public Modifier modifier(int modi) {
        m = modi;
        return this;
    }

    public boolean testAnyOf(int opts) {
        return (m & opts) != 0;
    }

    public boolean testAllOf(int opts) {
        return (m & opts) == opts;
    }

    public Modifier remove(int opts) {
        m &= ~opts;
        return this;
    }

    public Modifier add(int opts) {
        m |= opts;
        return this;
    }
}
