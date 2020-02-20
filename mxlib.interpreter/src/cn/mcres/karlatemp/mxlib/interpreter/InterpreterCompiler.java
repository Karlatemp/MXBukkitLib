/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/19 18:23:37
 *
 * MXLib/mxlib.interpreter/InterpreterCompiler.java
 */

package cn.mcres.karlatemp.mxlib.interpreter;

import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Objects;

public class InterpreterCompiler {
    static class Link {
        String context;
        Link next;

        String get(long index) {
            var testing = this;
            while (index-- != 0) testing = testing.next;
            return testing.context;
        }

        long size() {
            long a = 0;
            var testing = this;
            do {
                a++;
                testing = testing.next;
            } while (testing != null);
            return a;
        }

        long put(String val) {
            var a = 0;
            var testing = this;
            do {
                if (testing.context == null) {
                    testing.context = val;
                    break;
                }
                if (testing.context.equals(val)) return a;
                var next = testing.next;
                if (next == null) {
                    testing.next = new Link();
                }
                a++;
                testing = testing.next;
            } while (true);
            return a;
        }
    }

    private static void tokens(Deque<Token> store, Object tree, boolean blockStart) {
        if (tree instanceof Token) {
            store.add((Token) tree);
        } else if (tree instanceof CommandBlock) {
            final Token source = ((CommandBlock) tree).getSource();
            if (blockStart)
                store.add(new Token("{", Token.Type.BLOCK_START, source.getLine(), source.getPos(), source.getSource()));
            for (var line : ((CommandBlock) tree).getLines()) {
                if (line instanceof Token) {// Commit
                    store.add((Token) line);
                } else {
                    var l = (CommandLine) line;
                    var ls = l.getSource();
                    var x = l.getArguments();
                    if (x.isEmpty()) continue; // ???
                    for (var w : x) {
                        tokens(store, w, true);
                    }
                    store.add(new Token(";", Token.Type.LINE_END, ls.getLine(), ls.getPos(), source.getSource()));
                }
            }
            if (blockStart)
                store.add(new Token("}", Token.Type.BLOCK_END, source.getLine(), source.getPos(), source.getSource()));
        }
    }

    public static void tokens(Deque<Token> store, Object tree) {
        tokens(store, tree, false);
    }

    @SuppressWarnings("ConstantConditions")
    public static void decompile(Deque<Token> store, DataInput source) throws IOException {
        var link = new Link();
        long size = source.readLong();
        while (size-- > 0) {
            link.put(source.readUTF());
        }
        long line = 0, pos = 0;
        String from = null, data = null;
        final Token.Type[] values = Token.Type.values();
        do {
            int type = source.readUnsignedByte();
            switch (type) {
                case 0:
                    return;
                case 1: {
                    store.add(new Token(data, values[source.readUnsignedShort()], line, pos, from));
                    break;
                }
                case 2:
                    line = source.readLong();
                    break;
                case 3:
                    pos = source.readLong();
                    break;
                case 4:
                    from = link.get(source.readLong());
                    break;
                case 5:
                    data = link.get(source.readLong());
                    break;
            }
        } while (true);
    }

    public static void compile(Deque<Token> tokens, DataOutput out) throws IOException {
        var pool = new Link();
        for (Token token : tokens) {
            pool.put(token.getSource());
            pool.put(token.getData());
        }
        out.writeLong(pool.size());
        {
            var testing = pool;
            do {
                out.writeUTF(testing.context);
                testing = testing.next;
            } while (testing != null);
        }
        long l = 0, p = 0;
        String source = null, data = null;
        for (Token token : tokens) {
            long a = token.getLine();
            long b = token.getPos();
            if (a != l) {
                out.writeByte(2);
                out.writeLong(a);
                l = a;
            }
            if (b != p) {
                out.writeByte(3);
                out.writeLong(b);
                p = b;
            }
            var s = token.getSource();
            var d = token.getData();
            if (!Objects.equals(s, source)) {
                out.writeByte(4);
                out.writeLong(pool.put(source = s));
            }
            if (!Objects.equals(d, data)) {
                out.writeByte(5);
                out.writeLong(pool.put(data = d));
            }
            out.writeByte(1);
            out.writeShort(token.getType().ordinal());
        }
        out.writeByte(0);
    }
}
