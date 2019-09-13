/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: WebHelper.java@author: karlatemp@vip.qq.com: 19-9-11 下午5:14@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.http;

import static cn.mcres.gyhhy.MXLib.Helper.thr;

import cn.mcres.gyhhy.MXLib.fcs.F3c;
import cn.mcres.gyhhy.MXLib.fcs.TrIv;
import cn.mcres.karlatemp.mxlib.tools.EmptyStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Consumer;

/**
 * @author 32798
 */

@SuppressWarnings("Duplicates")
public class WebHelper {

    public static URL url(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException ex) {
            return thr(ex);
        }
    }

    public static HttpHelper http(String url) {
        return new HttpHelper(url);
    }

    public static HttpHelper http(URL url) {
        return new HttpHelper(url);
    }

    public static PostHelper post(String url) {
        return new PostHelper(url);
    }

    public static PostHelper post(URL url) {
        return new PostHelper(url);
    }

    protected Consumer<Throwable> catch$func;
    protected String contentType;
    /**
     * @version 1.12
     */
    protected Proxy proxy;

    /**
     * @version 1.12
     */
    public Proxy proxy() {
        return proxy;
    }

    /**
     * @version 1.12
     */
    public WebHelper proxy(Proxy p) {
        proxy = p;
        return this;
    }

    public WebHelper onCatch(Consumer<Throwable> func) {
        this.catch$func = func;
        return this;
    }

    public WebHelper contentType(String type) {
        contentType = type;
        return this;
    }

    protected void connect0() throws IOException {
        URLConnection connect;
        if (proxy == null) {
            connect = link.openConnection();
        } else {
            connect = link.openConnection(proxy);
        }
        // 设置通用的请求属性
        connect.setRequestProperty("accept", "*/*");
        connect.setRequestProperty("connection", "Keep-Alive");
        connect.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        if (contentType != null) {
            connect.setRequestProperty("Content-Type", contentType);
        }
        if (header$func != null) {
            header$func.accept(connect);
        }
        connect.connect();
        InputStream stream;
        if (connect.getDoInput()) {
            stream = connect.getInputStream();
        } else {
            stream = EmptyStream.stream.asInputStream();
        }
        if (response$func != null) {
            response$func.call(0, connect, stream);
        }
        stream.close();
    }

    public void connect() {
        try {
            this.connect0();
        } catch (Throwable thr) {
            if (this.catch$func == null) {
                if (thr instanceof Error) {
                    throw (Error) thr;
                }
                if (thr instanceof RuntimeException) {
                    throw (RuntimeException) thr;
                }
                thr.printStackTrace();
            } else {
                this.catch$func.accept(thr);
            }
        }
    }

    protected final URL link;
    protected Consumer<URLConnection> header$func;
    protected F3c<URLConnection, InputStream> response$func;

    public WebHelper(URL link) {
        this.link = link;
    }

    public WebHelper(String link) {
        this.link = url(link);
    }

    @SuppressWarnings("unchecked")
    public <T extends URLConnection> WebHelper onResponse(F3c<T, InputStream> func) {
        this.response$func = (F3c<URLConnection, InputStream>) func;
        return this;
    }

    public WebHelper header(Consumer<URLConnection> func) {
        this.header$func = func;
        return this;
    }

    public static class HttpHelper extends WebHelper {

        public HttpHelper(URL link) {
            super(link);
        }

        public HttpHelper(String link) {
            super(link);
        }

        public HttpHelper onCatch(Consumer<Throwable> func) {
            super.onCatch(func);
            return this;
        }

        public HttpHelper contentType(String type) {
            super.contentType(type);
            return this;
        }

        protected void connect0() throws IOException {
            URLConnection connect;
            if (proxy == null) {
                connect = link.openConnection();
            } else {
                connect = link.openConnection(proxy);
            }
            HttpURLConnection http = (HttpURLConnection) connect;
            // 设置通用的请求属性
            http.setRequestProperty("accept", "*/*");
            http.setRequestProperty("connection", "Keep-Alive");
            http.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (contentType != null) {
                connect.setRequestProperty("Content-Type", contentType);
            }
            if (header$func != null) {
                header$func.accept(connect);
            }
            http.connect();
            int code = http.getResponseCode();
            InputStream stream;
            if (code == 200) {
                stream = http.getInputStream();
            } else {
                stream = http.getErrorStream();
            }
            if (stream == null) {
                stream = EmptyStream.stream.asInputStream();
            }
            if (response$func != null) {
                response$func.call(code, http, stream);
            }
            stream.close();
            http.disconnect();
        }

        public void connect() {
            super.connect();
        }

        public HttpHelper response(F3c<HttpURLConnection, InputStream> func) {
            super.onResponse(func);
            return this;
        }

        public HttpHelper header(Consumer<URLConnection> func) {
            super.header(func);
            return this;
        }
    }

    public static class PostHelper extends HttpHelper {

        TrIv<ByteArrayOutputStream, String> write$func;

        protected void connect0() throws IOException {
            URLConnection connect;
            if (proxy == null) {
                connect = link.openConnection();
            } else {
                connect = link.openConnection(proxy);
            }

            HttpURLConnection http = (HttpURLConnection) connect;
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            // 设置通用的请求属性
            http.setRequestProperty("accept", "*/*");
            http.setRequestProperty("connection", "Keep-Alive");
            http.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (contentType != null) {
                connect.setRequestProperty("Content-Type", contentType);
            }
            if (header$func != null) {
                header$func.accept(connect);
            }
            //Content-Length;
            //Content-Type;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (write$func != null) {
                String type = write$func.run(baos);
                if (type != null) {
                    http.setRequestProperty("Content-Type", type);
                }
            }
            byte[] data = baos.toByteArray();
            http.setRequestProperty("Content-Length", String.valueOf(data.length));
            http.connect();
            OutputStream out = http.getOutputStream();
            out.write(data);
            out.flush();
            out.close();
            int code = http.getResponseCode();
            InputStream stream;
            if (code == 200) {
                stream = http.getInputStream();
            } else {
                stream = http.getErrorStream();
            }
            if (stream == null) {
                stream = EmptyStream.stream.asInputStream();
            }
            if (response$func != null) {
                response$func.call(code, http, stream);
            }
            stream.close();

            http.disconnect();
        }

        @Deprecated
        public PostHelper write(Consumer<OutputStream> func) {
            return write((inv) -> {
                func.accept(inv);
                return null;
            });
        }

        public PostHelper write(TrIv<ByteArrayOutputStream, String> func) {
            this.write$func = func;
            return this;
        }

        @Override
        public PostHelper header(Consumer<URLConnection> func) {
            super.header(func);
            return this;
        }

        public PostHelper response(F3c<HttpURLConnection, InputStream> func) {
            super.onResponse(func);
            return this;
        }

        public PostHelper(String link) {
            super(link);
        }

        public PostHelper(URL link) {
            super(link);
        }
    }
}
