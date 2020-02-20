/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/18 22:46:11
 *
 * MXLib/mxlib.interpreter/CommandVisitor.java
 */

package cn.mcres.karlatemp.mxlib.interpreter;

import java.util.ArrayDeque;
import java.util.Collection;

public interface CommandVisitor<T, R> {
    default R visit(Object tree, boolean ignoreCommit, T value) {
        return visit(tree, ignoreCommit, value, defaultValue(value), new ArrayDeque<>());
    }

    default R visit(Object tree, boolean ignoreCommit, T value, R contextReturn, Collection<CommandInclude.IncludeStack> stacks) {
        try {
            R def = contextReturn;
            if (tree instanceof CommandBlock) {
                var block = (CommandBlock) tree;
                var loc = new CommandInclude.IncludeStack(block.getSource());
                loc.only = true;
                stacks.add(loc);
                var visitor = visitBlock(block, value, def);
                if (visitor != null) {
                    def = visitor.visitStart(block, value, def);
                    for (var l : block.getLines()) {
                        def = visitor.visit(l, ignoreCommit, value, def, stacks);
                    }
                    def = visitor.visitEnd(block, value, def);
                }
                stacks.remove(loc);
                return def;
            } else if (tree instanceof CommandLine) {
                var line = (CommandLine) tree;
                var loc = new CommandInclude.IncludeStack(line.getSource());
                loc.only = true;
                stacks.add(loc);
                var visitor = visitLine(line, value, def);
                if (visitor != null) {
                    def = visitor.visitStart(line, value, def);
                    for (var l : line.getArguments()) {
                        def = visitor.visit(l, ignoreCommit, value, def, stacks);
                    }
                    def = visitor.visitEnd(line, value, def);
                }
                stacks.remove(loc);
                return def;
            } else if (tree instanceof Token) {
                var tok = (Token) tree;
                var loc = new CommandInclude.IncludeStack(tok);
                loc.only = true;
                stacks.add(loc);
                switch (tok.getType()) {
                    case STRING:
                        return visitString(tok, value, def);
                    case DATA:
                        return visitWord(tok, value, def);
                    case COMMIT:
                        if (ignoreCommit) return def;
                        return visitCommit(tok, value, def);
                }
                stacks.remove(loc);
            }
            return visitUnknown(tree, value, def);
        } catch (Throwable throwable) {
            for (var x : throwable.getSuppressed()) {
                if (x instanceof StackTraces) throw throwable;
            }
            throwable.addSuppressed(new StackTraces(stacks));
            throw throwable;
        }
    }

    public static class StackTraces extends Exception {
        public StackTraces(Collection<CommandInclude.IncludeStack> stack) {
            super(CommandInclude.stack(new StringBuilder("StackTraces: "), stack).toString(), null, false, false);
        }
    }

    default R defaultValue(T value) {
        return null;
    }

    default R visitUnknown(Object unknown, T value, R contextReturn) {
        return contextReturn;
    }

    default R visitStart(Object tree, T value, R contextReturn) {
        return contextReturn;
    }

    default R visitEnd(Object tree, T value, R ret) {
        return ret;
    }

    default R visitCommit(Token commit, T value, R contextReturn) {
        return contextReturn;
    }

    default R visitWord(Token commit, T value, R contextReturn) {
        return contextReturn;
    }

    default CommandVisitor<T, R> visitBlock(CommandBlock block, T value, R contextReturn) {
        return null;
    }

    default CommandVisitor<T, R> visitLine(CommandLine line, T value, R contextReturn) {
        return null;
    }

    default R visitString(Token commit, T value, R contextReturn) {
        return contextReturn;
    }
}
