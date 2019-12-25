/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Ascii.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author 32798
 */
public class Ascii {

    public static String ec(String... s) {
        return encodeToConsole(s);
    }
    public static final String RESET = "\033[m",
            _1 = "\033[0;34;22m",
            _2 = "\033[0;32;22m",
            _3 = "\033[0;36;22m",
            _4 = "\033[0;31;22m",
            _5 = "\033[0;35;22m",
            _6 = "\033[0;33;22m",
            _7 = "\033[0;37;22m",
            _8 = "\033[0;30;1m",
            _9 = "\033[0;34;1m",
            _0 = "\033[0;30;22m",
            _A = "\033[0;32;1m",
            _B = "\033[0;36;1m",
            _C = "\033[0;31;1m",
            _D = "\033[0;35;1m",
            _E = "\033[0;33;1m",
            _F = "\033[0;37;1m";

    public static String skipColor(String line) {
        StringBuilder sb = new StringBuilder();
        char[] cc = line.toCharArray();
        for (int i = 0; i < cc.length; i++) {
            if (cc[i] == '\u00a7') {
                i++;
            } else {
                sb.append(cc[i]);
            }
        }
        return sb.toString();
    }

    private static class A {

        ArrayList<String> strs = new ArrayList<>();
        String reading;
        int index = 0;

        int next() {
            String reading = reading();
            if (reading != null) {
                if (index >= reading.length()) {
                    this.reading = null;
                    reading = reading();
                    index = 0;
                }
                if (reading != null) {
                    return reading.charAt(index++);
                }
            }
            return -1;
        }

        private String reading() {
            if (reading != null) {
                return reading;
            }
            if (strs.isEmpty()) {
                return null;
            }
            reading = strs.remove(0);
            return reading;
        }
    }

    public static String encodeToConsole(String... s) {
        StringBuilder bui = new StringBuilder();
        A a = new A();
        a.strs.addAll(Arrays.asList(s));
        while (true) {
            int cr = a.next();
            if (cr == -1) {
                break;
            }
            switch (cr) {
                case '\u00a7': {
//                    if (i < s.length()) {
                    switch (a.next()) {
                        case '1':
                            bui.append(_1);
                            break;
                        case '2':
                            bui.append(_2);
                            break;
                        case '3':
                            bui.append(_3);
                            break;
                        case '4':
                            bui.append(_4);
                            break;
                        case '5':
                            bui.append(_5);
                            break;
                        case '6':
                            bui.append(_6);
                            break;
                        case '7':
                            bui.append(_7);
                            break;
                        case '8':
                            bui.append(_8);
                            break;
                        case '9':
                            bui.append(_9);
                            break;
                        case '0':
                            bui.append(_0);
                            break;
                        case 'a':
                        case 'A':
                            bui.append(_A);
                            break;
                        case 'b':
                        case 'B':
                            bui.append(_B);
                            break;
                        case 'c':
                        case 'C':
                            bui.append(_C);
                            break;
                        case 'd':
                        case 'D':
                            bui.append(_D);
                            break;
                        case 'e':
                        case 'E':
                            bui.append(_E);
                            break;
                        case 'f':
                        case 'F':
                            bui.append(_F);
                            break;
                        case 'r':
                        case 'R':
                            bui.append(RESET);
                            break;
                    }
//                    }
                    break;
                }
                default:
                    bui.append((char) cr);
                    break;
            }
        }
        return bui.toString();
    }

    public static void main(String... s) {
        System.out.println(ec("F", "AQ", "\u00a7c", "Little man"));
    }
}
