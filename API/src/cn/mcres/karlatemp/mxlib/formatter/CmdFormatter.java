/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CmdFormatter.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.formatter;

import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CmdFormatter extends Formatter {

    public static void main(String[] args) {
        CmdFormatter cf = new CmdFormatter();

        Map<String, String> m = new HashMap<>();
        m.put("state", "Good");
        m.put("name", "Karlatemp");

        System.out.println(cf.format("Hey %% %namex% You Are %state%. Are you ok? %SHIT", mapping(m)));
    }

    @Override
    public String format(Locale l, String t, Replacer u) {
        if (nullCheck(l, t, u)) {
            return t;
        }
        final int size = t.length();
        final CharBuffer sb = CharBuffer.wrap(t);
        final char[] buf = new char[size];
        final CharBuffer co = CharBuffer.allocate(size);
        StringBuilder bui = new StringBuilder(size);
        for (int i = 0; sb.remaining() > 0; i++) {
            char c = sb.get();
            switch (c) {
                case '%': {
                    if (sb.remaining() > 0) {
                        int end = 0;
                        co.position(0);
                        co.limit(size);
                        for (int k = i + 1; sb.remaining() > 0; k++) {
                            char cw = sb.get();
                            if (cw == '%') {
                                end = k;
                                break;
                            }
                            co.put(cw);
                        }
                        if (end == 0) {
                            co.flip();
                            int soff = co.remaining();
                            if (soff > 0) {
                                co.get(buf, 0, soff);
                                bui.append('%').append(buf, 0, soff);
                            }
                        } else {
                            co.flip();
                            int ss = co.remaining();
                            if (ss == 0) {
                                bui.append('%');
                                sb.limit(size);
                            } else {
                                co.get(buf, 0, ss);
                                String key = new String(buf, 0, ss);
                                System.out.println("KEY: " + key);
                                if (u.containsKey(key)) {
                                    bui.append(u.apply(key));
                                    sb.position(end + 1);
                                } else {
                                    sb.position(i - 1);
                                    int b = end - i + 2;
                                    sb.get(buf, 0, b);
                                    bui.append(buf, 0, b);
                                }
                            }
                            sb.limit(size);
                        }
                        i = sb.position();
                    } else {
                        bui.append('%');
                    }
                    break;
                }
                default: {
                    bui.append(c);
                }
            }
        }
        return bui.toString();
    }

}
