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
    public static void main(String[] args) {
        getInstance().thr(new Throwable("Fuck You Little Man"));
    }

    public static ThrowHelper getInstance() {
        return def;
    }

    public static ThrowHelper getDefault() {
        return def;
    }

    protected void throwx(Throwable thr) {
        cn.mcres.karlatemp.mxlib.tools.ThrowHelper.thrown(thr);
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
