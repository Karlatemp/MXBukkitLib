/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.system;

import java.util.Arrays;

/**
 *
 * @author 32798
 */
public class Tester {
    static String[] list;
    public static boolean remove(Object o) {
        int ind = -1;
        for (int i = 0; i < list.length; i++) {
            String c = list[i];
            if (c == o) {
                ind = i;
                break;
            }
        }
        String[] nw = new String[list.length - 1];
        System.out.println(ind);
        System.arraycopy(list, 0, nw, 0, ind);
        System.arraycopy(list, ind + 1, nw, ind, nw.length - ind);
        list = nw;
        return true;
    }

    public static void main(String[] args) {
        String ix = "";
        list = new String[]{"a","b",ix,"c","d"};
        remove(ix);
        System.out.println(Arrays.toString(list));
    }

}
