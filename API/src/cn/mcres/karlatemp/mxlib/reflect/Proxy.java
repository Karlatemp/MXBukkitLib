/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Proxy.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.reflect;

import cn.mcres.karlatemp.mxlib.tools.IClassScanner;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;

import java.io.Closeable;
import java.io.DataInputStream;
import java.util.List;

public abstract class Proxy {
    private ProxyHandler handler;

    public static void main(String[] args) throws Throwable{
        ClassGen.gen("fa", new Class[]{
                IClassScanner.class,
                Cloneable.class,
                AutoCloseable.class,
                Closeable.class
        });
        ClassFile cf = new ClassFile(new DataInputStream(Proxy.class.getResourceAsStream("Proxy.class")));
        final List<FieldInfo> fields = cf.getFields();
        for(FieldInfo field : fields){
            System.out.println(field);
        }
    }
}
