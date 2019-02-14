/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.http;

import static cn.mcres.gyhhy.MXLib.Helper.thr;
import cn.mcres.gyhhy.MXLib.fcs.F3c;
import cn.mcres.gyhhy.MXLib.io.EmptyStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Consumer;

/**
 *
 * @author 32798
 */
public class WebHelper {

    static final WebHelper helper = new WebHelper();


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

    private WebHelper() {
    }

    public static class HttpHelper extends WebHelper {

        final URL link;
        Consumer<URLConnection> header$func;
        F3c<HttpURLConnection, InputStream> response$func;
        private Consumer<Throwable> catch$func;

        public HttpHelper(URL link) {
            this.link = link;
        }

        public HttpHelper(String link) {
            this.link = url(link);
        }

        public HttpHelper onCatch(Consumer<Throwable> func) {
            this.catch$func = func;
            return this;
        }

        void connect0() throws IOException {
            URLConnection connect = link.openConnection();

            HttpURLConnection http = (HttpURLConnection) connect;
            // 设置通用的请求属性
            http.setRequestProperty("accept", "*/*");
            http.setRequestProperty("connection", "Keep-Alive");
            http.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
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
            if(stream == null){
                stream = EmptyStream.stream.asInputStream();
            }
            if (response$func != null) {
                response$func.call(code, http, stream);
            }
            stream.close();
            http.disconnect();
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

        public HttpHelper response(F3c<HttpURLConnection, InputStream> func) {
            this.response$func = func;
            return this;
        }

        public HttpHelper header(Consumer<URLConnection> func) {
            this.header$func = func;
            return this;
        }
    }

    public static class PostHelper extends HttpHelper {

        Consumer<OutputStream> write$func;

        void connect0() throws IOException {
            URLConnection connect = link.openConnection();

            HttpURLConnection http = (HttpURLConnection) connect;
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            // 设置通用的请求属性
            http.setRequestProperty("accept", "*/*");
            http.setRequestProperty("connection", "Keep-Alive");
            http.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (header$func != null) {
                header$func.accept(connect);
            }
            http.connect();
            OutputStream out = http.getOutputStream();
            if (write$func != null) {
                write$func.accept(out);
            }
            int code = http.getResponseCode();
            InputStream stream;
            if (code == 200) {
                stream = http.getInputStream();
            } else {
                stream = http.getErrorStream();
            }
            if(stream == null){
                stream = EmptyStream.stream.asInputStream();
            }
            if (response$func != null) {
                response$func.call(code, http, stream);
            }
            out.close();
            stream.close();
            http.disconnect();
        }

        public PostHelper write(Consumer<OutputStream> func) {
            this.write$func = func;
            return this;
        }

        @Override
        public PostHelper header(Consumer<URLConnection> func) {
            super.header(func);
            return this;
        }

        @Override
        public PostHelper response(F3c<HttpURLConnection, InputStream> func) {
            super.response(func);
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
