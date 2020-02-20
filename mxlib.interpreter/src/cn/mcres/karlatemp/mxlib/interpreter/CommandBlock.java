/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/18 22:46:10
 *
 * MXLib/mxlib.interpreter/CommandBlock.java
 */

package cn.mcres.karlatemp.mxlib.interpreter;

import java.util.Deque;

public class CommandBlock {
    private final Deque<Object> lines;
    Token source;

    public CommandBlock(Deque<Object> lines) {
        this.lines = lines;
        for (var o : lines) {
            if (o instanceof CommandLine)
                source = ((CommandLine) o).source;
            else if (o instanceof Token)
                source = (Token) o;
            else continue;
            break;
        }
    }

    public Deque<Object> getLines() {
        return lines;
    }

    public Token getSource() {
        return source;
    }
}
