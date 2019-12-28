/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DefaultFormatter.java@author: karlatemp@vip.qq.com: 2019/12/26 下午10:26@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.formatter;

import cn.mcres.karlatemp.mxlib.tools.StringHelper;
import org.jetbrains.annotations.NotNull;

import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class DefaultFormatter extends Formatter implements BiConsumer<StringBuilder, String> {
    public static void main(String[] args) {
        DefaultFormatter f = new DefaultFormatter();
        Map<String, String> m = new HashMap<>();
        m.put("a", "WWW");
        m.put("b", "CCC");
        m.put("", "YOU");
        m.put("{}", "My CHUNK");
        m.put("CXK", "JNTM");
        Replacer r = mapping(m);
        System.out.println(f.format(null, "Welocome {b} to {a}, Have a nice day.", r));
        System.out.println(f.format(null, "\\{Hey {} ,Here is {\\{\\}}} {CXK} {VAR}", r));
    }

    @Override
    public FormatTemplate parse(@NotNull String template, @NotNull Replacer constants) {
        FormatAction.ActionLink link = new FormatAction.ActionLink();
        final int size = template.length();
        final CharBuffer buffer = CharBuffer.allocate(size);
        final CharBuffer late = CharBuffer.wrap(template);
        // final char[] buffer2 = new char[size];
        final StringBuilder bui = new StringBuilder(size);
        for (; late.hasRemaining(); ) {
            char c = late.get();
            switch (c) {
                case '\\': {
                    if (late.hasRemaining()) {
                        bui.append(p(late));
                    }
                    break;
                }
                case '{': {
                    if (bui.length() > 0) {
                        link.append(bui.toString());
                        bui.delete(0, bui.length());
                    }
                    buffer.clear();
                    boolean hasEnding = false;
                    while (late.hasRemaining() && !hasEnding) {
                        char next = late.get();
                        switch (next) {
                            case '\\': {
                                buffer.put(p(late));
                                break;
                            }
                            case '}': {
                                hasEnding = true;
                                break;
                            }
                            default: {
                                buffer.put(next);
                            }
                        }
                    }
                    buffer.flip();
                    String key = buffer.toString();
                    if (!hasEnding) {
                        link.append(key);
                    } else {
                        link.appendKey(key, this);
                    }
                    break;
                }
                default: {
                    bui.append(c);
                }
            }
        }
        if (bui.length() > 0) {
            link.append(bui.toString());
        }
        return new SimpleFormatTemplate(link);
    }

    private char p(CharBuffer late) {
        char next = late.get();
        switch (next) {
            case 'n':
                return '\n';
            case 't':
                return '\t';
            case 'r':
                return '\r';
            case 'u': {
                return StringHelper.parseUnicode(late.get(), late.get(), late.get(), late.get());
            }
        }
        return next;
    }

    @Override
    public void accept(StringBuilder builder, String s) {
        builder.append('{').append(s).append('}');
    }
}
