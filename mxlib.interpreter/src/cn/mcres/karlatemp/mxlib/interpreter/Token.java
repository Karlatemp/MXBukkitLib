/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/18 22:46:11
 *
 * MXLib/mxlib.interpreter/Token.java
 */

package cn.mcres.karlatemp.mxlib.interpreter;

import org.jetbrains.annotations.NotNull;

public class Token {
    private final String data;
    private final Type type;
    private final long pos;
    private final long line;
    private final String source;

    public Type getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public enum Type {
        COMMIT, DATA, BLOCK_START, BLOCK_END, LINE_END, STRING;
    }

    // public static final Token BLOCK_START = new Token("{", Type.BLOCK_START);
    // public static final Token BLOCK_END = new Token("}", Type.BLOCK_END);
    // public static final Token LINE_END = new Token(";", Type.LINE_END);

    public Token(@NotNull String data, @NotNull Type type, long line, long pos, String source) {
        this.data = data;
        this.type = type;
        this.line = line;
        this.pos = pos;
        this.source = source;
    }

    public long getLine() {
        return line;
    }

    public long getPos() {
        return pos;
    }

    public String getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (!data.equals(token.data)) return false;
        return type == token.type;
    }

    @Override
    public int hashCode() {
        int result = data.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return data;
    }
}
