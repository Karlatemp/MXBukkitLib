/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ThrowHelper.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib;

/**
 *
 * @author 32798
 */
public class ThrowHelper {

    private static final ThrowHelper def = new ThrowHelper();
    private static final ThrowHelper instance;
    private static final String code = "yv66vgAAADQADwEAR2NuL21jcmVzL2d5aGh5L01YTGliL2ltcGwvVGhyb3dIZWxwZXJJbXBsJEJ1aWxkVGltZV8xNTYxMzcxMTI0OTQ3JDFfNl8xBwABAQAgY24vbWNyZXMvZ3loaHkvTVhMaWIvVGhyb3dIZWxwZXIHAAMBAApTb3VyY2VGaWxlAQAyVGhyb3dIZWxwZXJJbXBsJEJ1aWxkVGltZV8xNTYxMzcxMTI0OTQ3JDFfNl8xLmphdmEBACRUaHJvd0hlbHBlckltcGxNYWtlci5qYXZhPEF1dG9CdWlsZD4BAAZ0aHJvd3gBABgoTGphdmEvbGFuZy9UaHJvd2FibGU7KVYBAARDb2RlAQAGPGluaXQ+AQADKClWDAALAAwKAAQADQAhAAIABAAAAAAAAgABAAgACQABAAoAAAAOAAEAAwAAAAIrvwAAAAAAAQALAAwAAQAKAAAAEQABAAEAAAAFKrcADrEAAAAAAAEABQAAAAIABw==";

    static {
//        System.out.println()
        ThrowHelper th = null;
        try {
            Class<? extends ThrowHelper> tht
                    = RefUtil.loadClass(code, ThrowHelper.class.getClassLoader())
                            .asSubclass(ThrowHelper.class);
            th = tht.newInstance();
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        instance = th;
    }

    public static void main(String[] args) {
        getInstance().thr(new Throwable("Fuck You Little Man"));
    }

    public static ThrowHelper getInstance() {
        return instance == null ? def : instance;
    }

    public static ThrowHelper getDefault() {
        return def;
    }

    protected void throwx(Throwable thr) {
        if (thr instanceof Error) {
            throw (Error) thr;
        }
        if (thr instanceof RuntimeException) {
            throw (RuntimeException) thr;
        }
        throw new SysRuntimeException(thr.getLocalizedMessage(), thr);
    }

    public <T> T thr(Throwable thr) {
        throwx(thr);
        return null;
    }

    public <T> T thr(String msg, Throwable thr) {
        if (thr instanceof Error) {
            throw (Error) thr;
        }
        if (thr instanceof RuntimeException) {
            throw (RuntimeException) thr;
        }
        return thr(new SysRuntimeException(msg, thr));
    }

    public <T> T thr(String msg) {
        return thr(new SysRuntimeException(msg));
    }

    public <T> T thr() {
        return thr(new SysRuntimeException());
    }
}
