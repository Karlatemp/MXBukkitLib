/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author 32798
 */
public class Json {

    public static Object read(Reader reader) throws IOException {
        return new Json(reader).read();
    }
    private final Reader r;

    private Json(Reader reader) {
        this.r = reader;
    }
    private volatile int rewrite = 0;

    private int r1() throws IOException {
        if (rewrite != 0) {
            int r = rewrite;
            rewrite = 0;
            return r;
        }
        while (true) {
            int rc = r.read();
//            System.err.println(rc);
            if (rc == -1) {
                return rc;
            }
            if (rc == 10 || rc == ' ') {
                continue;
            }
            if (!Character.isSpaceChar(rc)) {
                return rc;
            }
        }
    }

    private Object read(int rd) throws IOException {
        if (rd == '{') {
            HashMap<Object, Object> mp = new HashMap<>();
            while (true) {
                int r = r1();
                if (r == '}') {
                    return mp;
                }
                Object k = read(r);
                if (r1() != ':') {
                    throw new RuntimeException("U " + k);
                }
                mp.put(k, read());
                r = r1();
                if (r == ',') {
                } else if (r == '}') {
                    return mp;
                } else {
                    throw new RuntimeException("UNC");
                }
            }
        }
        if (rd == '\"') {
            StringBuilder sb = new StringBuilder();
            while (true) {
                int r = this.r.read();
                switch (r) {
                    case '\"':
                        return sb.toString();
                    case '\\':
                        int next = this.r.read();
                        switch (next) {
                            case 'n':
                                sb.append('\n');
                                break;
                            case 't':
                                sb.append('\t');
                                break;
                            case 'u': {
                                char[] b4 = new char[4];
                                this.r.read(b4);
                                sb.append((char) Integer.parseInt(new String(b4), 16));
                                break;
                            }
                            default: {
                                sb.append((char) next);
                            }
                        }
                        break;
                    case '\n':
                    case -1:
                        throw new RuntimeException("Parse exception");
                    default:
                        sb.append((char) r);
                        break;
                }
            }
        }
        if (rd >= '0' && rd <= '9') {
            StringBuilder sb = new StringBuilder();
            while (true) {
                int r = this.r.read();
                if (r >= '0' && r <= '9') {
                    sb.append((char) r);
                } else {
                    rewrite = r;
                    return Integer.parseInt(sb.toString());
                }
            }
        }
        if (rd == '[') {
            ArrayList<Object> arr = new ArrayList<>();
            while (true) {
                int r = r1();
                if (r == -1) {
                    throw new RuntimeException("Parse Exception");
                }
                if (r == ']') {
                    return arr;
                }
                arr.add(read(r));
                r = r1();
                if (r == ']') {
                    return arr;
                }
                if (r != ',') {
                    throw new RuntimeException("Parse Expceiton");
                }
            }
        }
        throw new RuntimeException("Unknown");
    }

    public static void main(String[] args) throws Throwable {
        System.out.println(read(new StringReader("{\n"
                + "    \"id\": \"1405435235813\",\n"
                + "    \"hitokoto\": \"要让一群人团结起来，需要的不是英明的领导，而是共同的敌人。\",\n"
                + "    \"cat\": \"d\",\n"
                + "    \"catname\": \"Novel - 小说\",\n"
                + "    \"author\": \"当歌\",\n"
                + "    \"source\": \"我的青春恋爱物语果然有问题\",\n"
                + "    \"date\": \"1405435235\",\n"
                + "    \"time\": 23333, \"FUCK\": [136,{},[],\"Q\"]"
                + "}")));
    }

    private Object read() throws IOException {
        int rd = r1();
        if (rd == -1) {
            return null;
        }
        return read(rd);
    }
}
