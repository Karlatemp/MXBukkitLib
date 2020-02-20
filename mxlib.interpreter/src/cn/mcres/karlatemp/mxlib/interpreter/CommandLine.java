/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/18 22:46:10
 *
 * MXLib/mxlib.interpreter/CommandLine.java
 */

package cn.mcres.karlatemp.mxlib.interpreter;

import java.util.Deque;

public class CommandLine {
    private final Deque<Object> arguments;
    Token source;

    public CommandLine(Deque<Object> arguments) {
        this.arguments = arguments;
        for (var obj : arguments) {
            if (obj instanceof Token) {
                source = (Token) obj;
            } else if (obj instanceof CommandBlock) {
                source = ((CommandBlock) obj).source;
            } else continue;
            break;
        }
    }

    public Deque<Object> getArguments() {
        return arguments;
    }

    public Token getSource() {
        return source;
    }
}
