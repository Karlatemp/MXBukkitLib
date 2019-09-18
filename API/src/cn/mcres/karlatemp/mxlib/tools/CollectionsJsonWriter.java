/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CollectionsJsonWriter.java@author: karlatemp@vip.qq.com: 19-9-17 下午10:24@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CollectionsJsonWriter implements IJsonWriter {
    private static final BitSet noencode = new BitSet();
    private static final char[] char_map =
            "0123456789abcdef".toCharArray();

    static {
        for (int i = 'a'; i <= 'z'; i++) {
            noencode.set(i);
            noencode.set(i + ('A' - 'a'));
        }
        for (int i = '0'; i <= '9'; i++) {
            noencode.set(i);
        }
        for (char c : "!@#$%^&*()_-+=|';:[]{},.<>?/ `~".toCharArray()) {
            noencode.set(c);
        }
    }

    @Override
    public void write(Appendable appendable, Object obj) throws IOException {
        if (obj == null) {
            appendable.append("null");
        } else if (obj instanceof String) {
            appendable.append('"');
            for (char c : ((String) obj).toCharArray()) {
                if (noencode.get(c)) {
                    appendable.append(c);
                } else {
                    appendable.append('\\');
                    switch (c) {
                        case '\t': {
                            appendable.append('t');
                            break;
                        }
                        case '\n': {
                            appendable.append('n');
                            break;
                        }
                        case '\r': {
                            appendable.append('r');
                            break;
                        }
                        case '\\': {
                            appendable.append('\\');
                            break;
                        }
                        default: {
                            appendable.append('u');
                            appendable.append(char_map[(c >> (4 * 3)) & 0xF]);
                            appendable.append(char_map[(c >> (4 * 2)) & 0xF]);
                            //noinspection PointlessArithmeticExpression
                            appendable.append(char_map[(c >> (4 * 1)) & 0xF]);
                            //noinspection PointlessArithmeticExpression
                            appendable.append(char_map[(c >> (4 * 0)) & 0xF]);
                            break;
                        }
                    }
                }
            }
            appendable.append('"');
        } else if (obj instanceof Number || obj instanceof Boolean) {
            appendable.append(String.valueOf(obj));
        } else if (obj instanceof Map) {
            appendable.append('{');
            boolean k = false;
            for (Map.Entry entry : ((Map<?, ?>) obj).entrySet()) {
                if (k) {
                    appendable.append(',');
                }
                k = true;
                write(appendable, entry.getKey());
                appendable.append(':');
                write(appendable, entry.getValue());
            }
            appendable.append('}');
        } else if (obj instanceof Collection) {
            appendable.append('[');
            boolean k = false;
            for (Object entry : (Collection) obj) {
                if (k) {
                    appendable.append(',');
                }
                k = true;
                write(appendable, entry);
            }
            appendable.append(']');
        } else if (obj instanceof Object[]) {
            appendable.append('[');
            boolean k = false;
            for (Object entry : (Object[]) obj) {
                if (k) {
                    appendable.append(',');
                }
                k = true;
                write(appendable, entry);
            }
            appendable.append(']');
        } else {
            if (obj.getClass().isArray()) {
                appendable.append('[');
                int size = Array.getLength(obj);
                for (int i = 0; i < size; i++) {
                    if (i != 0) appendable.append(',');
                    write(appendable, Array.get(obj, i));
                }
                appendable.append(']');
            } else {
                write(appendable, String.valueOf(obj));
            }
        }
    }
}
