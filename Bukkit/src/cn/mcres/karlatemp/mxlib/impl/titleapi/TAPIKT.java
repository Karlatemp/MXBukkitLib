package cn.mcres.karlatemp.mxlib.impl.titleapi;

import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

class TAPIKT {
    private static MethodHandle
            PacketPlayOutPlayerListHeaderFooter$header$set,
            PacketPlayOutPlayerListHeaderFooter$footer$set;


    static void PacketPlayOutPlayerListHeaderFooterSetValue(Object packet, Object header, Object footer, Class<?> c) {
        try {
            if (PacketPlayOutPlayerListHeaderFooter$header$set == null) {
                MethodHandles.Lookup lk = Toolkit.Reflection.getRoot();
                try {
                    PacketPlayOutPlayerListHeaderFooter$header$set =
                            lk.unreflectSetter(c.getField("header"));
                    PacketPlayOutPlayerListHeaderFooter$footer$set =
                            lk.unreflectSetter(c.getField("footer"));
                } catch (Throwable thr) {
                    Field f = c.getDeclaredField("a");
                    f.setAccessible(true);
                    PacketPlayOutPlayerListHeaderFooter$header$set =
                            lk.unreflectSetter(f);
                    f = c.getDeclaredField("b");
                    f.setAccessible(true);
                    PacketPlayOutPlayerListHeaderFooter$footer$set =
                            lk.unreflectSetter(f);
                }
            }
            PacketPlayOutPlayerListHeaderFooter$header$set.invoke(packet, header);
            PacketPlayOutPlayerListHeaderFooter$footer$set.invoke(packet, footer);
        } catch (Throwable thr) {
            ThrowHelper.thrown(thr);
        }
    }
}
