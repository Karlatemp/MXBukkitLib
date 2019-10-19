/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BasicEvalProcessor.java@author: karlatemp@vip.qq.com: 19-9-18 下午9:58@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.exceptions.CompeteException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * 未完成....
 */
@Deprecated
public class BasicEvalProcessor implements IEvalProcessor {
    private enum Type {
        STRING, NUMBER, DEFAULT,
        LeftParenthesis, RightParenthesis,
        Point, Raw, BreakPoint,
        M_Add, M_LESS, M_Multiply, M_Except;
    }

    private static class Keyword {
        String cut;
        int line, position;
        Type type;

        public String toString() {
            switch (type) {
                case BreakPoint:
                    return ";";
                case LeftParenthesis:
                    return "(";
                case RightParenthesis:
                    return ")";
                case Point:
                    return ".";
                case M_Add:
                    return "+";
                case M_LESS:
                    return "-";
                case M_Multiply:
                    return "*";
                case M_Except:
                    return "/";
            }
            return cut;
        }
    }

    private static class KeywordReader {
        private final Reader reader;
        private int line, position;
        private final StringBuilder buffer = new StringBuilder(256);

        KeywordReader(Reader reader) {
            this.line = 0;
            this.position = 0;
            this.reader = reader;
        }

        boolean rd = false, rf = false;
        int nt;

        boolean ready() throws IOException {
            if (rd) {
                return rf;
            }
            int next = read();
            rd = true;
            nt = next;
            return rf = next != -1;
        }

        int read() throws IOException {
            if (this.rd) {
                rd = false;
                return nt;
            }
            int rd = reader.read();
            if (rd == 10) {
                line++;
                position = 0;
            }
            return rd;
        }

        int read0() throws IOException, CompeteException {
            if (reader.ready())
                return read();
            throw new CompeteException("Need more data but no any data. [" + line + ":" + position + "]");
        }

        int readUUF(int a) {
            if (a >= '0' && a <= '9') return a - '0';
            if (a >= 'a' && a <= 'f') return a - 'a' + 10;
            if (a >= 'A' && a <= 'F') return a - 'A' + 10;
            return -1;
        }

        boolean readUTF(int a) {
            if (a >= '0' && a <= '9') return true;
            if (a >= 'a' && a <= 'f') return true;
            if (a >= 'A' && a <= 'F') return true;
            return false;
        }

        void readTHR(int fw) throws CompeteException {
            throw new CompeteException("Unable to read bytes in hexadecimal: " + (char) fw + "(0x" + Integer.toHexString(fw) + ") [" + line + ":" + position + "]");
        }

        char readUTF(int a, int b, int c, int d) throws IOException, CompeteException {
            if (readUTF(a)) {
                if (readUTF(b)) {
                    if (readUTF(c)) {
                        if (readUTF(d)) {
                            //noinspection PointlessBitwiseExpression
                            return (char) (0
                                    | readUUF(a) << 12
                                    | readUUF(b) << 8
                                    | readUUF(c) << 4
                                    | readUUF(d) << 0);
                        }
                        readTHR(d);
                    }
                    readTHR(c);
                }
                readTHR(b);
            }
            readTHR(a);
            return 0;
        }

        char readUTF() throws IOException, CompeteException {
            return readUTF(read0(), read0(), read0(), read0());
        }

        void readEscapes() throws IOException, CompeteException {
            if (!reader.ready()) {
                throw new CompeteException("Reading string and escapes but no more data. " + buffer + " [" + line + ":" + position + "]");
            }
            int more = read();
            switch (more) {
                case 'n':
                    buffer.append('\n');
                    break;
                case 't':
                    buffer.append('\t');
                    break;
                case 'r':
                    buffer.append('\r');
                    break;
                case '\\':
                    buffer.append('\\');
                    break;
                case 'u':
                    buffer.append(readUTF());
                    break;
                case '\'':
                    buffer.append('\'');
                    break;
                case '\"':
                    buffer.append('\"');
                    break;
            }
        }

        Keyword string(int ln, int pos) {
            Keyword kw = new Keyword();
            kw.line = ln;
            kw.position = pos;
            kw.cut = buffer.toString();
            kw.type = Type.STRING;
            return kw;
        }

        Keyword bui(int ln, int pos, Type typ) {
            Keyword kw = new Keyword();
            kw.type = typ;
            kw.position = pos;
            kw.line = ln;
            return kw;
        }

        Keyword ntt;

        Keyword got(int ln, int pos, int ccr) {
            switch (ccr) {
                case '(':
                    return bui(ln, pos, Type.LeftParenthesis);
                case ')':
                    return bui(ln, pos, Type.RightParenthesis);
                case '.':
                    return bui(ln, pos, Type.Point);
                case ';':
                    return bui(ln, pos, Type.BreakPoint);
                case '+':
                    return bui(ln, pos, Type.M_Add);
                case '-':
                    return bui(ln, pos, Type.M_LESS);
                case '*':
                    return bui(ln, pos, Type.M_Multiply);
                case '/':
                    return bui(ln, pos, Type.M_Except);
            }
            return null;
        }

        Keyword next() throws IOException, CompeteException {
            if (ntt != null) {
                Keyword kw = ntt;
                ntt = null;
                return kw;
            }
            if (ready()) {
                buffer.delete(0, buffer.length());
                int first, ln, pos;
                do {
                    int next = read();
                    first = next;
                    ln = line;
                    pos = position;
                    if (!Character.isSpaceChar(next)) break;
                } while (ready());
                if (Character.isSpaceChar(first))
                    throw new CompeteException("Cannot read first char with space char.");
                {
                    Keyword wx = got(ln, pos, first);
                    if (wx != null) return wx;
                }
                switch (first) {
                    case '\"': {
                        boolean end = false;
                        while (ready() && !end) {
                            int next = read();
                            switch (next) {
                                case '\"': {
                                    end = true;
                                    break;
                                }
                                case '\r':
                                case '\t':
                                case '\n':
                                    break;//Skip
                                case '\\': {
                                    readEscapes();
                                    break;
                                }
                                default: {
                                    buffer.append((char) next);
                                }
                            }
                        }
                        if (!end) {
                            throw new CompeteException("Error: No ending string at " + line + ":" + position);
                        }
                        return string(ln, pos);
                    }
                    case '\'': {
                        boolean end = false;
                        while (ready() && !end) {
                            int next = read();
                            switch (next) {
                                case '\'': {
                                    end = true;
                                    break;
                                }
                                case '\r':
                                case '\t':
                                case '\n':
                                    break;//Skip
                                case '\\': {
                                    readEscapes();
                                    break;
                                }
                                default: {
                                    buffer.append((char) next);
                                }
                            }
                        }
                        if (!end) {
                            throw new CompeteException("Error: No ending string at " + line + ":" + position);
                        }
                        return string(ln, pos);
                    }
                    default: {
                        buffer.append((char) first);
                        boolean rn = true;
                        while (ready() && rn) {
                            int nt = read();
                            {
                                Keyword ggt = got(line, pos, nt);
                                if (ggt != null) {
                                    ntt = ggt;
                                    break;
                                }
                            }
                            switch (nt) {
                                case '\"':
                                case '\'': {
                                    throw new CompeteException("Unresolved " + buffer + ((char) nt) + " [" + line + ":" + position + "]");
                                }
                                default: {
                                    if (Character.isSpaceChar(nt)) {
                                        rn = false;
                                    } else {
                                        buffer.append((char) nt);
                                    }
                                    break;
                                }
                            }
                        }
                        Keyword kw = string(ln, pos);
                        kw.type = Type.Raw;
                        return kw;
                    }
                }
            }
            return null;
        }
    }

    @Override
    public void clearCache() {

    }

    @Override
    public void setUsingCache(boolean mode) {

    }

    @Override
    public CompetedCode compete(String command, boolean allowInvoking, boolean allowField) throws CompeteException {
        try (StringReader reader = new StringReader(command)) {
            KeywordReader kr = new KeywordReader(reader);
            List<Keyword> worlds = new ArrayList<>();
            while (true) {
                final Keyword next = kr.next();
                if (next == null) break;
                worlds.add(next);
            }
            return parse(worlds, allowField, allowInvoking);
        } catch (IOException e) {
            throw new CompeteException(e);
        }
    }

    static class LineCompetedCode {
        static class LineNumberCode extends LineCompetedCode {
        }

        static class LineNumberIntegerCode extends LineNumberCode {
            int val;

            LineNumberIntegerCode(int val) {
                this.val = val;
            }
        }

        static LineCompetedCode num(String str) {
            try {
                return new LineNumberIntegerCode(Integer.parseInt(str));
            } catch (NumberFormatException ignore) {
            }
            return null;
        }

        static boolean a(Type t) {
            switch (t) {
                case M_LESS:
                case M_Add:
                case M_Multiply:
                case M_Except:
                    return true;
            }
            return false;
        }

        static boolean a(List<?> wd) {
            return wd.stream().anyMatch(w -> {
                if (w instanceof Keyword)
                    return a(((Keyword) w).type);
                return false;
            });
        }

        static boolean b(List<?> wd) {
            return wd.stream().anyMatch(wx -> {
                if (wx instanceof Keyword) return ((Keyword) wx).type == Type.LeftParenthesis;
                return false;
            });
        }

        static LineCompetedCode parse(List worlds, boolean allowField, boolean allowInvoking) {
            if (b(worlds)) {
                List lw = new ArrayList();
                int lff = 0;
                for (int i = 0; i < worlds.size(); i++) {
                    Object ok = worlds.get(i);
                    if (ok instanceof Keyword) {
                        Type typ = ((Keyword) ok).type;
                        if (typ == Type.LeftParenthesis) {
                            lff++;
                            int st = i + 1;
                            for (i++; i < worlds.size(); i++) {
                                Object okw = worlds.get(i);
                                if (okw instanceof Keyword) {
                                    Type tt = ((Keyword) okw).type;
                                    if (tt == Type.LeftParenthesis) lff++;
                                    else if (tt == Type.RightParenthesis) {
                                        lff--;
                                        if (lff == 0) {
                                            lw.add(parse(worlds.subList(st, i), allowField, allowInvoking));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    lw.add(ok);
                }
                return parse(lw, allowField, allowInvoking);
            }
            if (a(worlds)) {
                int f = 0;
                List ddf = new ArrayList();
                for (int i = 0; i < worlds.size(); i++) {
                    Object got = worlds.get(i);
                    if (got instanceof Keyword) {
                        if (a(((Keyword) got).type)) {
                            ddf.add(parse(worlds.subList(f, i), allowField, allowInvoking));
                            ddf.add(got);
                            f = i + 1;
                        }
                    }
                }
                if (f != worlds.size()) {
                    ddf.add(parse(worlds.subList(f, worlds.size()), allowField, allowInvoking));
                }
                return nmsl(ddf, allowField, allowInvoking);
            }
            return null;
        }

        private static LineCompetedCode nmsl(List ddf, boolean allowField, boolean allowInvoking) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private CompetedCode parseLine(CompetedCode last, List<Keyword> worlds, boolean allowField, boolean allowInvoking) throws CompeteException {
        if (worlds.isEmpty())
            return last;
        CompetedCode cd;
        Keyword word = worlds.remove(0);
        if (word.type == Type.Raw) {
            String val = word.cut;
            if (last != null) throw new CompeteException("Error at " + word.line + ":" + word.position);
            class NUM implements CompetedCode {
                Number v;

                @Override
                public <T> T invoke(Map<String, Object> variables) {
                    return (T) v;
                }
            }
            NUM nn = new NUM();
            cd = nn;
            try {
                nn.v = Integer.decode(val);
            } catch (NumberFormatException nnn2) {
                try {
                    nn.v = Long.decode(val);
                } catch (NumberFormatException nnn) {
                    try {
                        nn.v = Double.parseDouble(val);
                    } catch (NumberFormatException wax) {
                        cd = null;
                    }
                }
            }
            if (cd == null) {
                cd = new CompetedCode() {
                    @Override
                    public <T> T invoke(Map<String, Object> variables) {
                        return (T) variables.get(val);
                    }
                };
            }
        } else if (word.type == Type.STRING) {
            cd = new CompetedCode() {
                @Override
                public <T> T invoke(Map<String, Object> variables) {
                    return (T) word.cut;
                }
            };
        } else {
            throw new CompeteException("Error type.");
        }
        if (!worlds.isEmpty()) {
            Keyword next = worlds.remove(0);
            final CompetedCode lt = cd;
            switch (next.type) {
                case Point:
                    String vv = next.cut;
                    cd = new CompetedCode() {
                        @Override
                        public <T> T invoke(Map<String, Object> variables) {
                            Object got = lt.invoke(variables);
                            if (got instanceof Map)
                                return (T) ((Map) got).get(vv);
                            throw new RuntimeException("Failed to get " + vv + " from " + got);
                        }
                    };
                case M_Add:
                    cd = new CompetedCode() {
                        @Override
                        public <T> T invoke(Map<String, Object> variables) {
                            Object g = lt.invoke(variables);
                            
                            return (T) (Double) Double.NaN;
                        }
                    };
            }
        }
        return cd;
    }

    private CompetedCode parseLine(List<Keyword> worlds, boolean allowField, boolean allowInvoking) throws CompeteException {
        return parseLine(null, worlds, allowField, allowInvoking);
    }

    private CompetedCode parse(List<Keyword> worlds, boolean allowField, boolean allowInvoking) throws CompeteException {
        List<Keyword> kw = new ArrayList<>();
        List<CompetedCode> lines = new ArrayList<>();
        while (!worlds.isEmpty()) {
            Keyword rm = worlds.remove(0);
            if (rm.type == Type.BreakPoint) {
                lines.add(parseLine(kw, allowField, allowInvoking));
                kw.clear();
            }
        }
        if (!kw.isEmpty()) {
            lines.add(parseLine(kw, allowField, allowInvoking));
        }
        return null;
    }
}
