package cn.mcres.karlatemp.mxlib.logging;

import cn.mcres.karlatemp.mxlib.tools.Toolkit;

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
        MethodHandles.Lookup lk = Toolkit.Reflection.getRoot();
        Throwable test = new Throwable();
        RFToolkit tk;
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
                tk = new RFToolkit() {
                    @Override
                    StackTraceElement[] a(Throwable t) {
                        return t.getStackTrace();
                    }
                };
            }
        }
        r = tk;
    }
}
