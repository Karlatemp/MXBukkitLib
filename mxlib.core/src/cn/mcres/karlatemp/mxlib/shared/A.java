/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: A.java@author: karlatemp@vip.qq.com: 2019/12/24 下午10:07@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.shared;

import cn.mcres.karlatemp.mxlib.tools.IParamRule;

import java.lang.invoke.MethodHandle;

class A {
    static class Insert {
        int index;
        Class[] inserts;
    }

    public MethodHandle s(MethodHandle mh, IParamRule... rules) {
        return null;
    }

    static boolean c(String pt, String mt) {
        if (pt == null || mt == null) return false;
        if (pt.startsWith(JVM_Package)) {
            return pt.substring(JVM_Package.length()).equals(mt);
        }
        if (pt.startsWith(JRE_Package)) {
            return pt.substring(JRE_Package.length()).equals(mt);
        }
        return false;
    }

    static String
            JVM_Package = "cn/mcres/karlatemp/mxlib/annotations/",
            JRE_Package = "cn.mcres.karlatemp.mxlib.annotations.",
            Resource = "Resource",
            Depend = "Depend",
            AutoInstall = "AutoInstall";
}
