/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: UniJsonWriter.java@author: karlatemp@vip.qq.com: 19-9-11 下午2:05@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.gson;

import java.lang.reflect.Method;
import java.io.Writer;

import cn.mcres.gyhhy.MXLib.Core;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;


import java.lang.reflect.InvocationTargetException;

/**
 * @author Karlatemp
 */
public class UniJsonWriter extends JsonWriter {

    private static final Method $writeDeferredName, $beforeValue;
    private static final boolean root;

    static {
        Method beforeValue = null, writeDeferredName = null;
        Class<JsonWriter> cl = JsonWriter.class;
        boolean rt = false;
        try {
            @SuppressWarnings("rawtypes")
            Class[] emp = new Class[0];
            writeDeferredName = cl.getDeclaredMethod("writeDeferredName", emp);
            try {
                beforeValue = cl.getDeclaredMethod("beforeValue", emp);
            } catch (Exception eee) {
                beforeValue = cl.getDeclaredMethod("beforeValue", new Class[]{boolean.class});
                rt = true;
            }
        } catch (Exception e) {
            try {
                Core.getBL().printStackTrace(e);
            } catch (Throwable thr) {
                e.printStackTrace();
            }
        }
        $writeDeferredName = writeDeferredName;
        $beforeValue = beforeValue;
        root = rt;
    }

    private Writer out;

    public UniJsonWriter(Writer out) {
        super(out);
        this.out = out;
    }

    @Override
    public JsonWriter value(String value) throws IOException {
        if (value == null) {
            return nullValue();
        }
        writeDeferredName();
        beforeValue();
        string(value);
        return this;
    }

    private void call(Method met) throws IOException {
        met.setAccessible(true);
        try {
            if (met == $beforeValue) {
                if (root) {
                    met.invoke(this, false);
                    return;
                }
            }
            met.invoke(this);

        } catch (IllegalAccessException | IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            Throwable exq = ex.getTargetException();
            if (exq instanceof IOException) {
                throw (IOException) exq;
            }
            if (exq instanceof Error) {
                throw (Error) exq;
            }
            if (exq instanceof RuntimeException) {
                throw (RuntimeException) exq;
            }
            throw new RuntimeException(exq);
        }
    }

    protected final void writeDeferredName() throws IOException {
        call($writeDeferredName);
    }

    protected final void beforeValue() throws IOException {
        call($beforeValue);
    }

    public void string(String value) throws IOException {
        out.write("\"");
        for (char c : value.toCharArray()) {
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) {
                out.write(c);
            } else {
                switch (c) {
                    case '\n': {
                        out.write("\\n");
                        break;
                    }
                    case '\t': {
                        out.write("\\t");
                        break;
                    }
                    case '\r': {
                        out.write("\\r");
                        break;
                    }
                    case '/':
                    case '-':
                    case '_':
                    case ':':
                    case '=':
                    case '&':
                    case '*':
                    case '(':
                    case ')':
                    case '^':
                    case '!':
                    case '@':
                    case '#':
                    case '$':
                    case '%':
                    case '+':
                    case '|': {
                        out.write(c);
                        break;
                    }
                    case '\'':
                    case '"':
                    case '\\': {
                        out.write('\\');
                        out.write(c);
                        break;
                    }
                    default: {
                        out.write("\\u");
                        int a = (c >> 12) & 0xf,
                                b = (c >> 8) & 0xf,
                                cc = (c >> 4) & 0xf,
                                d = c & 0xf;
                        out.write("" + a + b + cc + d);
                        break;
                    }
                }
            }
        }
        out.write("\"");
    }
}
