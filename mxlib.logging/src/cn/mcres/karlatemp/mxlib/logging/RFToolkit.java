/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: RFToolkit.java@author: karlatemp@vip.qq.com: 2020/2/14 下午7:32@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.logging;

// import cn.mcres.karlatemp.mxlib.tools.Toolkit;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

abstract class RFToolkit {
    abstract StackTraceElement[] a(Throwable t);

    static final RFToolkit r;

    static class x extends RFToolkit {
        MethodHandle gt;

        @Override
        StackTraceElement[] a(Throwable t) {
            try {
                return (StackTraceElement[]) gt.invoke(t);
            } catch (NullPointerException ne) {
                throw ne;
            } catch (Throwable throwable) {
                return t.getStackTrace();
            }
        }
    }

    static {
        MethodHandles.Lookup lk;
        try {
            lk = (MethodHandles.Lookup)
                    Class.forName("cn.mcres.karlatemp.mxlib.tools.Toolkit$Reflection")
                            .getMethod("getRoot")
                            .invoke(null);
        } catch (Throwable any) {
            lk = null;
        }
        Throwable test = new Throwable();
        RFToolkit deftk = new RFToolkit() {
            @Override
            StackTraceElement[] a(Throwable t) {
                return t.getStackTrace();
            }
        }, tk = deftk;
        if (lk != null) {
            try {
                MethodHandle getOutStackTrace =
                        lk.findSpecial(Throwable.class,
                                "getOurStackTrace",
                                MethodType.methodType(StackTraceElement[].class), Throwable.class);
                getOutStackTrace.invoke(test);
                x w;
                tk = w = new x();
                w.gt = getOutStackTrace;
            } catch (Throwable thr) {
                try {
                    MethodHandle gt = lk.findSpecial(Throwable.class, "getStackTrace", MethodType.methodType(StackTraceElement[].class), Throwable.class);
                    gt.invoke(test);
                    x m = new x();
                    m.gt = gt;
                    tk = m;
                } catch (Throwable tt) {
                    tk = deftk;
                }
            }
        }
        r = tk;
    }
}
