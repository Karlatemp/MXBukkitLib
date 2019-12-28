/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CmdFormatter.java@author: karlatemp@vip.qq.com: 2019/12/26 下午11:29@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.formatter;

import org.jetbrains.annotations.NotNull;

import java.nio.CharBuffer;
import java.util.function.BiConsumer;

public class CmdFormatter extends Formatter implements BiConsumer<StringBuilder, String> {
    @Override
    public FormatTemplate parse(@NotNull String template, @NotNull Replacer constants) {
        FormatAction.ActionLink link = new FormatAction.ActionLink();
        CharBuffer late = CharBuffer.wrap(template);
        CharBuffer tmp = CharBuffer.allocate(template.length());
        StringBuilder keyword = new StringBuilder();
        while (late.hasRemaining()) {
            char next = late.get();
            switch (next) {
                case '^': {
                    do {
                        int n = late.get();
                        if (n == '\r') continue;
                        break;
                    } while (true);
                    break;
                }
                case '%': {
                    char n = late.get();
                    if (n == '%') {
                        keyword.append('%');
                        break;
                    }
                    if (keyword.length() > 0) {
                        link.append(keyword.toString());
                        keyword.delete(0, keyword.length());
                    }
                    tmp.clear().put(n);
                    boolean hasEnding = false;
                    while (late.hasRemaining()) {
                        char w = late.get();
                        if (w == '%') {
                            hasEnding = true;
                            break;
                        }
                        tmp.put(w);
                    }
                    String key = tmp.flip().toString();
                    if (!hasEnding) {
                        link.append(key);
                    } else {
                        link.appendKey(key, this);
                    }
                    break;
                }
                default: {
                    keyword.append(next);
                    break;
                }
            }
        }
        if (keyword.length() > 0) {
            link.append(keyword.toString());
        }

        return new SimpleFormatTemplate(link);
    }

    @Override
    public void accept(StringBuilder builder, String s) {
        builder.append('%').append(s).append('%');
    }
}
