/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BasicEvanProcessor.java@author: karlatemp@vip.qq.com: 19-9-18 下午9:58@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.exceptions.CompeteException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * 未完成....
 */
@Deprecated
public class BasicEvanProcessor implements IEvalProcessor {
    private static class RD {
        static class Variable extends RD {
            String name;
        }

        static class Str extends RD {
            String v;
        }

        static final RD
                LINE_END = new RD(),
                LR = new RD(),
                RR = new RD();
        static int[] cmap = new int[124];

        static {
            for (int i = 0; i < 10; i++) {
                cmap['0' + i] = i;
            }
            for (int i = 0; i < 6; i++) {
                cmap['a' + i] = 10 + i;
                cmap['A' + i] = 10 + i;
            }
        }

        static char rd(String s, int x) {
            int a = s.charAt(x);
            int b = s.charAt(x + 1);
            int c = s.charAt(x + 2);
            int d = s.charAt(x + 3);
            //noinspection PointlessBitwiseExpression
            return (char) (0 |
                    (cmap[a] << 12) |
                    (cmap[a] << 8) |
                    (cmap[a] << 4) |
                    (cmap[a] << 0)
            );
        }

        static List<RD> parse(String command) {
            List<RD> l = new ArrayList<>();
            StringBuilder buffer = new StringBuilder(command.length() >> 2);
            final int size = command.length();
            for (int i = 0; i < size; i++) {
                char c = command.charAt(i);
                switch (c) {
                    case ';': {
                        l.add(LINE_END);
                        break;
                    }
                    case '(': {
                        l.add(LR);
                        break;
                    }
                    case ')': {
                        l.add(RR);
                        break;
                    }
                    case '"':
                    case '\'': {
                        boolean d = c == '"';
                        lp:
                        for (; i < size; i++) {
                            c = command.charAt(i);
                            switch (c) {
                                case '\\': {
                                    c = command.charAt(++i);
                                    switch (c) {
                                        case 't': {
                                            buffer.append('\t');
                                            break;
                                        }
                                        case 'n': {
                                            buffer.append('\n');
                                            break;
                                        }
                                        case 'r': {
                                            buffer.append('\r');
                                            break;
                                        }
                                        case 'u': {
                                            buffer.append(rd(command, ++i));
                                            i += 3;
                                        }
                                    }
                                    break;
                                }
                                default: {
                                    if (d) {
                                        if (c == '"') break lp;
                                    } else if (c == '\'') break lp;
                                    buffer.append(c);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                    default: {
                        if (!Character.isSpaceChar(c)) {
                            for (; i < size; i++) {
                                c = command.charAt(i);
                                if (Character.isSpaceChar(c)) {
                                    break;
                                }
                                buffer.append(c);
                            }
                            Variable v = new Variable();
                            v.name = buffer.toString();
                            buffer.delete(0, buffer.length());
                            l.add(v);
                        }
                        break;
                    }
                }
            }
            return l;
        }
    }

    private static class CC implements CompetedCode {
        CC(String command, boolean a, boolean f) throws CompeteException {

        }

        @Override
        public <T> T invoke(Map<String, Object> variables) {
            return null;
        }

    }

    @Override
    public void clearCache() {

    }

    @Override
    public void setUsingCache(boolean mode) {

    }

    @Override
    public CompetedCode compete(String command, boolean allowInvoking, boolean allowField) throws CompeteException {
        return new CC(command, allowInvoking, allowField);
    }
}
