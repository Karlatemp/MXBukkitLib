/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/18 22:46:10
 *
 * MXLib/mxlib.interpreter/CommandInclude.java
 */

package cn.mcres.karlatemp.mxlib.interpreter;

import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandInclude {
    public interface CommandFinder {
        @NotNull CommandBlock find(String path) throws IOException;
    }

    private static final Token word = new Token("$include", Token.Type.DATA, -1, 0, "<include>");

    public static CommandBlock include(String path) {
        return new CommandBlock(new ArrayDeque<>(Collections.singletonList(
                new CommandLine(new ArrayDeque<>(Arrays.asList(
                        word, new Token(path, Token.Type.STRING, -1, 0, "<include>")
                )))
        )));
    }

    public static CommandBlock include(String path, CommandFinder finder) throws IOException {
        var root = include(path);
        replace(root, finder, new ArrayDeque<>(10), 10);
        return root;
    }

    public static CommandBlock include(String path, CommandFinder finder, int maxIncluding) throws IOException {
        var root = include(path);
        replace(root, finder, new ArrayDeque<>(maxIncluding), maxIncluding);
        return root;
    }

    public static StringBuilder stack(StringBuilder sb, Collection<IncludeStack> dejaVu) {
        final Iterator<IncludeStack> iterator = dejaVu.iterator();
        while (iterator.hasNext()) {
            var next = iterator.next();
            if (next.line < 0) continue; // Hidden Frame
            sb.append(next);
            break;
        }
        while (iterator.hasNext()) {
            var next = iterator.next();
            if (next.line < 0) continue;
            sb.append(" > ").append(next);
        }
        return sb;
    }

    public static class IncludeStack {
        long pos, line;
        String source;
        boolean only;

        public IncludeStack(Token token) {
            if (token == null) {
                source = "<unknown>";
                return;
            }
            pos = token.getPos();
            line = token.getLine();
            source = token.getSource();
        }

        public static boolean contains(String path, Collection<IncludeStack> dejaVu) {
            return dejaVu.stream().anyMatch(stack -> stack.source.equals(path));
        }

        public static int size(Collection<IncludeStack> dejaVu) {
            return dejaVu.stream().filter(frame -> frame.line > -1).mapToInt(k -> 1).sum();
        }

        public static boolean isHiddenFrame(IncludeStack loc) {
            return loc.line < 0;
        }

        @Override
        public boolean equals(Object o) {
            if (only) return o == this;
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            IncludeStack that = (IncludeStack) o;

            if (pos != that.pos) return false;
            if (line != that.line) return false;
            return Objects.equals(source, that.source);
        }

        @Override
        public String toString() {
            return source + "@" + line + ":" + pos;
        }

        @Override
        public int hashCode() {
            int result = (int) (pos ^ (pos >>> 32));
            result = 31 * result + (int) (line ^ (line >>> 32));
            result = 31 * result + (source != null ? source.hashCode() : 0);
            result = 31 * result + (only ? 1 : 0);
            return result;
        }
    }

    public static void replace(
            CommandBlock root, CommandFinder finder,
            Collection<IncludeStack> dejaVu,
            int maxLoopingInclude
    ) throws IOException {
        final Deque<Object> lines = root.getLines();
        var temp = new ConcurrentLinkedQueue<>(lines);
        try {
            lines.clear();
        } catch (Throwable throwable) {
            throw new IOException("Command Block not modifiable.", throwable);
        }
        for (var line : temp) {
            if (line instanceof CommandLine) {
                var cl = (CommandLine) line;
                final Deque<Object> arguments = cl.getArguments();
                if (arguments.size() == 2) {
                    var data = arguments.peek();
                    if (data instanceof Token) {
                        var tok = (Token) data;

                        if (tok.getType() == Token.Type.DATA) {
                            if (tok.getData().equals("$include")) {
                                var loc = new IncludeStack(tok);
                                dejaVu.add(loc);


                                var step = arguments.peekLast();
                                if (step instanceof Token) {
                                    var step1 = (Token) step;
                                    if (step1.getType() == Token.Type.STRING) {
                                        var path = step1.getData();
                                        if (IncludeStack.contains(path, dejaVu))
                                            throw new IOException(stack(new StringBuilder("Looping include. "), dejaVu).toString());


                                        if (IncludeStack.size(dejaVu) > maxLoopingInclude) {
                                            throw new IOException(stack(new StringBuilder("Overflow, max ").append(maxLoopingInclude).append(" including."), dejaVu).toString());
                                        }
                                        CommandBlock template;
                                        try {
                                            template = finder.find(path);
                                        } catch (IOException ioe) {
                                            var msg = stack(new StringBuilder(ioe.getMessage()).append(" ("), dejaVu).append(")").toString();
                                            if (ioe instanceof EOFException)
                                                throw (IOException) new EOFException(msg).initCause(ioe);
                                            if (ioe instanceof FileNotFoundException)
                                                throw (FileNotFoundException) new FileNotFoundException(msg).initCause(ioe);
                                            throw new IOException(msg, ioe);
                                        }
                                        replace(template, finder, dejaVu, maxLoopingInclude);
                                        dejaVu.remove(loc);
                                        if (IncludeStack.isHiddenFrame(loc)) {
                                            lines.addAll(template.getLines());
                                        } else {
                                            var size = IncludeStack.size(dejaVu);
                                            StringBuilder sb = new StringBuilder();
                                            while (size-- > 0) sb.append("  ");

                                            lines.add(new Token(sb + " ======== BEGIN Including from " + loc + " -> " + path + " =======", Token.Type.COMMIT, loc.line, loc.pos, loc.source));
                                            lines.addAll(template.getLines());
                                            lines.add(new Token(sb + " ========  END  Including from " + loc + " -> " + path + " =======", Token.Type.COMMIT, loc.line, loc.pos, loc.source));
                                        }
                                        continue;
                                    }
                                }
                                throw new IOException("Except String: (" + tok.getSource() + "@" + tok.getLine() + ":" + tok.getPos() + ")");
                            }
                        }
                    }
                }
                for (var o : arguments) {
                    if (o instanceof CommandBlock) {
                        replace((CommandBlock) o, finder, dejaVu, maxLoopingInclude);
                    }
                }
            }
            lines.add(line);

        }
    }
}
