/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/18 22:46:10
 *
 * MXLib/mxlib.interpreter/CommandPrinter.java
 */

package cn.mcres.karlatemp.mxlib.interpreter;

import java.io.PrintStream;
import java.util.BitSet;

public class CommandPrinter implements CommandVisitor<PrintStream, Void> {
    @Override
    public CommandVisitor<PrintStream, Void> visitBlock(CommandBlock block, PrintStream value, Void contextReturn) {
        return this;
    }

    @Override
    public Void visitCommit(Token commit, PrintStream value, Void contextReturn) {
        value.append('#').println(commit.getData());
        return null;
    }

    @Override
    public CommandVisitor<PrintStream, Void> visitLine(CommandLine line, PrintStream value, Void contextReturn) {
        return new LinePrinter(0);
    }

    static PrintStream prefix(int count, PrintStream stream) {
        while (count-- > 0) stream.append('\t');
        return stream;
    }

    private static class BlockPrinter implements CommandVisitor<PrintStream, Void> {
        int prefix;
        boolean write;

        @Override
        public Void visitEnd(Object tree, PrintStream value, Void ret) {
            if (write) prefix(prefix, value);
            value.append('}');
            return null;
        }

        @Override
        public Void visitCommit(Token commit, PrintStream value, Void contextReturn) {
            if (!write) {
                value.println();
                write = true;
            }
            prefix(prefix + 1, value).append('#').println(commit.getData());
            write = true;
            return null;
        }

        @Override
        public CommandVisitor<PrintStream, Void> visitLine(CommandLine line, PrintStream value, Void contextReturn) {
            if (write) {
                return new LinePrinter(prefix + 1);
            }
            return new LinePrinter(prefix + 1) {
                @Override
                PrintStream beforeWrite(PrintStream value) {
                    if (!this.write) value.append('\n');
                    return super.beforeWrite(value);
                }

                @Override
                public Void visitEnd(Object tree, PrintStream value, Void ret) {
                    if (write) {
                        BlockPrinter.this.write = true;
                    }
                    return super.visitEnd(tree, value, ret);
                }
            };
        }

        @Override
        public Void visitStart(Object tree, PrintStream value, Void contextReturn) {
            value.append('{');
            return null;
        }
    }

    private static class LinePrinter implements CommandVisitor<PrintStream, Void> {
        int prefix;
        boolean write = false;

        LinePrinter(int prefix) {
            this.prefix = prefix;
        }

        @Override
        public CommandVisitor<PrintStream, Void> visitBlock(CommandBlock block, PrintStream value, Void contextReturn) {
            beforeWrite(value);
            var b = new BlockPrinter();
            b.prefix = prefix;
            return b;
        }

        @Override
        public Void visitWord(Token commit, PrintStream value, Void contextReturn) {
            beforeWrite(value).append(commit.getData());
            return null;
        }

        static BitSet oks;

        static {
            oks = new BitSet();
            oks.set('A', 'Z', true);
            oks.set('a', 'z', true);
            oks.set('0', '9', true);
            oks.set('\u4e00', '\u9fa5', true);
            for (char c : " ,.<>/?;:'|[]{}-=_+)(*&^%$#@!".toCharArray()) {
                oks.set(c, true);
            }
        }

        @Override
        public Void visitString(Token commit, PrintStream value, Void contextReturn) {
            beforeWrite(value);
            value.append('\"');
            for (char c : commit.getData().toCharArray()) {
                if (oks.get(c)) value.append(c);
                else {
                    value.append("\\u");
                    var hex = Integer.toHexString(c);
                    value.append("0000".substring(hex.length())).append(hex);
                }
            }
            value.append('\"');
            return null;
        }

        PrintStream beforeWrite(PrintStream value) {
            if (write) {
                value.append(' ');
            } else {
                write = true;
                prefix(prefix, value);
            }
            return value;
        }

        @Override
        public Void visitEnd(Object tree, PrintStream value, Void ret) {
            if (write) value.println(';');
            return null;
        }
    }

}
