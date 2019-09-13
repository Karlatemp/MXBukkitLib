package cn.mcres.gyhhy.MXLib.format;

import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DefaultFormatter extends Formatter {

    private char a(char next) {
        switch (next) {
            case 'n':
                return '\n';
            case 't':
                return '\t';
            case 'r':
                return '\r';
            default:
                return next;
        }
    }

    public static void main(String[] args) {
        DefaultFormatter f = new DefaultFormatter();
        Map<String, String> m = new HashMap<>();
        m.put("a", "WWW");
        m.put("b", "CCC");
        m.put("", "YOU");
        m.put("{}", "MAN");
        m.put("CXK", "JNTM");
        Replacer r = mapping(m);
        System.out.println(f.format(null, "Welocome {b} to {a}, Have a nice day.", r));
        System.out.println(f.format(null, "\\{FUCK {} LITTLE {\\{\\}}} {CXK} {VAR}", r));
    }

    @Override
    public String format(final Locale l, final String t, final Replacer u) {
        if (nullCheck(l, t, u)) {
            return t;
        }
        final int size = t.length();
        final CharBuffer cb = CharBuffer.wrap(t);
        final CharBuffer buffer = CharBuffer.allocate(size);
        final char[] array = new char[size];
        final StringBuilder bui = new StringBuilder(size);
        for (int i = 0; cb.remaining() > 0; i++) {
//            cb.limit(size);
            char c = cb.get();
//            System.out.write(c);
            switch (c) {
                case '\\': {
                    i++;
                    if (cb.remaining() > 0) {
                        bui.append(cb.get());
                    }
                    break;
                }
                case '{': {
                    int to = 0;
                    buffer.position(0);
                    buffer.limit(size);
                    for_a:
                    for (int k = i + 1; cb.remaining() > 0; k++) {
                        char a = cb.get();
                        switch (a) {
                            case '\\': {
                                if (cb.remaining() > 0) {
                                    buffer.put(a(cb.get()));
                                }
                                break;
                            }
                            case '}': {
                                to = k;
                                break for_a;
                            }
                            default: {
                                buffer.put(a);
                            }
                        }
                    }
                    if (to == 0) {
                        bui.append('{');
                        cb.position(i);
                    } else {
                        buffer.flip();
                        int rem = buffer.remaining();
                        buffer.get(array, 0, rem);
                        String key = new String(array, 0, rem);
//                        System.out.println("OPEN KEY: " + key);
                        if (u.containsKey(key)) {
                            bui.append(u.apply(key));
                        } else {
                            cb.position(i - 1);
                            cb.limit(to);
                            buffer.position(0);
                            buffer.limit(size);
                            buffer.put(cb);
                            cb.limit(size);
                            buffer.flip();
                            rem = buffer.remaining();
                            buffer.get(array, 0, rem);
                            bui.append(array, 0, rem);
                        }
                    }
                    i = cb.position();
                    break;
                }
                default: {
                    bui.append(c);
                }
            }
        }
        return bui.toString();
    }

}
