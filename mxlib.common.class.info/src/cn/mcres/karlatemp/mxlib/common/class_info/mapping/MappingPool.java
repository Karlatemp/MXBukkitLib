/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: MappingPool.java@author: karlatemp@vip.qq.com: 2020/1/13 下午11:34@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info.mapping;

import cn.mcres.karlatemp.mxlib.common.class_info.*;
import cn.mcres.karlatemp.mxlib.common.class_info.internal.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MappingPool extends SimpleClassPool {
    private static final String spl = " -> ";

    public MappingPool() {
    }

    public MappingPool(ClassPool cp) {
        super(cp);
    }

    @Override
    protected ClassInfo findClass(String name) {
        var c = new SimpleClassInfo(name, null, 0);
        classes.add(c);
        return c;
    }

    private static class MappingClass extends SimpleRenameableClassInfo {
        MappingClass(String s, String r) {
            this.javaName = s;
            renamed = r.replace('.', '/');
        }

        Collection<String> rules = new ConcurrentLinkedQueue<>();

        ClassInfo getType(String a, ClassPool p) {
            if (a.endsWith("[]")) {
                return getType(a.substring(0, a.length() - 2), p).array();
            }
            return p.getClass(a);
        }

        @Override
        protected void initialize0(ClassPool pool) {
            for (var rule : rules) {
                //    com.mojang.blaze3d.platform.GlStateManager$TexGenCoord q -> d
                //    1605:1609:void <init>() -> <init>
                int i = rule.indexOf(spl);
                // System.out.append('\t').println(rule);
                String a = rule.substring(0, i);
                String b = rule.substring(i + spl.length());
                if (a.charAt(a.length() - 1) == ')') {
                    // Method
                    int a1 = a.indexOf(':');
                    int a2 = a.indexOf(":", a1 + 1);
                    a = a.substring(a2 + 1);
                    int sd = a.indexOf(' ');
                    ClassInfo returnType = getType(a.substring(0, sd), pool);
                    int sr = a.lastIndexOf('(');
                    String metName = a.substring(sd + 1, sr);
                    String params_str = a.substring(sr + 1, a.length() - 1);
                    var params = Stream.of(params_str.isEmpty() ? new String[0] : params_str.split(",")).map(n -> getType(n, pool)).collect(Collectors.toList());
                    var mtt = new RenameableMethodInfo(metName, this, returnType, params, ~0);
                    mtt.rename(b);
                    methods.add(mtt);
                } else {
                    int x = a.indexOf(' ');
                    String tp = a.substring(0, x);
                    String nq = a.substring(x + 1);
                    var fie = new RenameableFieldInfo(nq, getType(tp, pool), this, ~0, true);
                    fie.rename(b);
                    fields.add(fie);
                }
            }
        }
    }


    public MappingPool(ClassPool parent, Scanner fileReader) {
        super(parent);
        LineReader lr = new LineReader(fileReader);
        while (true) {
            final String next = lr.next();
            if (next == null) break;
            int w = next.indexOf(spl);
            String cn = next.substring(0, w);
            String to = next.substring(w + spl.length(), next.length() - 1);
            var mc = new MappingClass(cn, to);
            classes.add(mc);
            while (true) {
                var ntt = lr.next();
                if (ntt == null) {
                    break;
                }
                if (!Character.isSpaceChar(ntt.charAt(0))) {
                    lr.current(ntt);
                    break;
                }
                mc.rules.add(ntt.trim());
            }
        }

        for (var a : new ConcurrentLinkedQueue<>(classes)) {
            // System.out.println("Initialize " + a);
            a.initialize(this);
        }
    }
}
