/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/18 22:46:11
 *
 * MXLib/mxlib.interpreter/KBStream.java
 */

package cn.mcres.karlatemp.mxlib.interpreter;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class InterpreterParser {
    public static Deque<Token> readTokens(Reader reader, String source) throws IOException {
        if (source == null) source = "<unknown>";
        var deque = new ConcurrentLinkedDeque<Token>();
        var buffered = new BufferedReader(reader);
        var sb = new StringBuilder();
        var sb1 = new StringBuilder();
        long pos = 0, line = 1;
        do {
            int next = buffered.read();
            if (next == -1) break;
            pos++;
            switch (next) {
                case '\n': {
                    line++;
                    pos = 0;
                    break;
                }
                case '\r':
                case '\t':
                case ' ': {
                    break;
                }
                case '#': {
                    // Commit
                    deque.add(new Token(buffered.readLine(), Token.Type.COMMIT, line, pos, source));
                    pos = 0;
                    line++;
                    break;
                }
                case '\"': {// Read String
                    clear(sb);
                    clear(sb1);
                    long startLine = line;
                    long startPos = pos;
                    rt:
                    do {
                        int next0 = buffered.read();
                        if (next0 != -1) {
                            sb1.append((char) next0);
                        }
                        switch (next0) {
                            case -1: {
                                throw new EOFException(sb1 + "<EOF> (" + source + "@" + line + ":" + pos + ")");
                            }
                            case '\r': {
                                break;
                            }
                            case '\n': {
                                line++;
                                pos = 0;
                                r:
                                do {
                                    buffered.mark(1);
                                    int next1 = buffered.read();
                                    if (next1 != -1) sb1.append((char) next1);
                                    switch (next1) {
                                        case '\n': {
                                            line++;
                                            pos = 0;
                                            break;
                                        }
                                        case '\t':
                                        case ' ':
                                            pos++;
                                            break;
                                        default:
                                            buffered.reset();
                                            if (next1 != -1)
                                                sb1.deleteCharAt(sb1.length() - 1);
                                            break r;
                                    }
                                } while (true);
                                break;
                            }
                            case '"': {
                                deque.add(new Token(sb.toString(), Token.Type.STRING, startLine, startPos, source));
                                pos++;
                                break rt;
                            }
                            case '\\': {
                                pos++;
                                int n = buffered.read();
                                if (n != -1) sb1.append((char) n);
                                switch (n) {
                                    case -1:
                                        throw new EOFException(sb1 + "<EOF> (" + source + "@" + line + ":" + pos + ")");
                                    case 'u': {
                                        int $1 = buffered.read();
                                        int $2 = buffered.read();
                                        int $3 = buffered.read();
                                        int $4 = buffered.read();
                                        if (($1 | $2 | $3 | $4) < 0) {
                                            throw new EOFException(sb1 + ".... (" + line + ", " + pos + ")");
                                        }
                                        var parser = new Object() {
                                            boolean check(int a) {
                                                if (a >= '0' && a <= '9') return false;
                                                if (a >= 'A' && a <= 'F') return false;
                                                return a < 'a' || a > 'f';
                                            }

                                            int parse(int c) {
                                                if (c >= '0' && c <= '9')
                                                    return c - '0';
                                                if (c >= 'A' && c <= 'F')
                                                    return c - 'A' + 0xA;
                                                if (c >= 'a' && c <= 'f')
                                                    return c - 'a' + 0xA;
                                                return -1;
                                            }
                                        };
                                        sb1.append((char) $1);
                                        sb1.append((char) $2);
                                        sb1.append((char) $3);
                                        sb1.append((char) $4);
                                        if (parser.check($1) | parser.check($2) | parser.check($3) | parser.check($4)) {
                                            throw new IOException("Wrong escapes: \\u" + (char) $1 + (char) $2 + (char) $3 + (char) $4 + "\n" + sb1 + " (" + line + ", " + pos + ")");
                                        }
                                        sb.append(
                                                (char) (
                                                        parser.parse($1) << 12 |
                                                                parser.parse($2) << 8 |
                                                                parser.parse($3) << 4 |
                                                                parser.parse($4)
                                                )
                                        );
                                        pos += 5;

                                        break;
                                    }
                                    case '\"': {
                                        sb.append('"');
                                        pos++;
                                        break;
                                    }
                                    default:
                                        throw new IOException("Unknown escape. (" + source + "@" + line + ":" + (pos + 1) + ")");
                                }
                                break;
                            }
                            default: {
                                sb.append((char) next0);
                            }
                        }
                    } while (true);
                    break;
                }
                case '{': {
                    deque.add(new Token("{", Token.Type.BLOCK_START, line, pos, source));
                    break;
                }
                case '}': {
                    deque.add(new Token("}", Token.Type.BLOCK_END, line, pos, source));
                    break;
                }
                case ';': {
                    pos++;
                    deque.add(new Token("}", Token.Type.LINE_END, line, pos, source));
                    break;
                }
                default: {
                    clear(sb);
                    sb.append((char) next);
                    long startLine = line;
                    long startPos = pos;
                    top:
                    do {
                        buffered.mark(1);
                        int n = buffered.read();
                        pos++;
                        switch (n) {
                            case '\n': {
                                line++;
                                pos = 0;
                                break top;
                            }
                            case ' ':
                            case '\r':
                            case '\t':
                            case -1: {
                                break top;
                            }
                            case '{':
                            case '}':
                            case ';':
                            case '#':
                            case '\"': {
                                buffered.reset();
                                pos--;
                                break top;
                            }
                            default:
                                sb.append((char) n);
                        }
                    } while (true);
                    deque.add(new Token(sb.toString(), Token.Type.DATA, startLine, startPos, source));
                    break;
                }
            }
        } while (true);
        return deque;
    }

    private static void clear(StringBuilder sb) {
        sb.delete(0, sb.length());
    }

    private static final Token S = new Token("{", Token.Type.BLOCK_START, 0, 0, "<System>");
    private static final Token E = new Token("}", Token.Type.BLOCK_END, 0, 0, "<System>");


    public static CommandBlock parse(Deque<Token> tokens) throws IOException {
        var fir = tokens.peek();
        if (fir != null)
            tokens.addFirst(new Token("{", Token.Type.BLOCK_START, fir.getLine(), fir.getPos(), fir.getSource()));
        else
            tokens.addFirst(S);
        tokens.addLast(E);
        var result = parseBlock(tokens);
        if (tokens.isEmpty()) return result;
        throw new IOException("Tokens not end.");
    }

    private static CommandBlock parseBlock(Deque<Token> tokens) throws IOException {
        var first = tokens.poll();
        if (first == null) throw new EOFException("No Block Start.");
        if (first.getType() != Token.Type.BLOCK_START) throw new IOException("First token not block start.");
        var line = new ConcurrentLinkedDeque<>();
        var lines = new ConcurrentLinkedDeque<>();
        top:
        do {
            var peek = tokens.peek();
            if (peek == null) {
                throw new EOFException("No Block Ending. (" + first.getSource() + "@" + first.getLine() + ":" + first.getPos() + ")");
            }
            switch (peek.getType()) {
                case BLOCK_END:
                    tokens.poll();
                    break top;
                case COMMIT:
                    tokens.poll();
                    if (line.isEmpty()) {
                        lines.add(peek);
                    } else {
                        line.add(peek);
                    }
                    break;
                case STRING:
                case DATA:
                    tokens.poll();
                    line.add(peek);
                    break;
                case LINE_END:
                    tokens.poll();
                    if (!line.isEmpty()) {
                        var line0 = new CommandLine(new ConcurrentLinkedDeque<>(line));
                        line.clear();
                        if (line0.source == null) {
                            line0.source = first;
                        }
                        lines.add(line0);
                    }
                    break;
                case BLOCK_START:
                    line.add(parseBlock(tokens));
                    break;
            }
        } while (true);
        if (!line.isEmpty()) {
            var line0 = new CommandLine(line);// line unused..
            if (line0.source == null) {
                line0.source = first;
            }
            lines.add(line0);
        }
        var block = new CommandBlock(lines);
        block.source = first;
        return block;
    }
}
