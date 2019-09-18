/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SharedParamSorter.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.exceptions.MessageDump;
import cn.mcres.karlatemp.mxlib.tools.IParamRule;
import cn.mcres.karlatemp.mxlib.tools.IParamSorter;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.*;

public class SharedParamSorter implements IParamSorter {
    public MethodHandle sort(MethodHandle mh, IParamRule... rules) {
        return sort(mh, rules, new ArrayList<>(Arrays.asList(rules)));
    }

    private MethodHandle sort(MethodHandle mh, IParamRule[] rules, List<Object> locks) {
        if (locks.isEmpty())
            return s(mh, rules);
        synchronized (locks.remove(0)) {
            return sort(mh, rules, locks);
        }

    }

    static MethodHandle s(MethodHandle mh, IParamRule... rules) {
        RuntimeException ne = null;
        Collection<IParamRule> tmp = new HashSet<>();
        final MethodType type = mh.type();
        if (type.parameterCount() > rules.length) throw new IllegalArgumentException();
        final Class<?>[] classes = type.parameterArray();
        final int length = classes.length, r_length = rules.length;
        final int[] maps = new int[length];
        final int[] points = new int[r_length];
        final List<Class<?>> buffer = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Class<?> c = classes[i];
            for (int jk = 0; jk < r_length; jk++) {
                IParamRule rule = rules[jk];
                if (points[jk] == 0) {// check up
                    if (rule.match(c)) {
                        points[jk] = i + 1; // Check
                        maps[i] = jk;
                        break;
                    }
                }
            }
        }
        // DO REMOVE AND SORT
        final Class<?>[] dmp = new Class[length];
        for (int i = 0; i < length; i++) {
            int point = maps[i];
            int dump = point;
            try {
                for (int jk = 0; jk < point; jk++) {
                    if (points[jk] == 0) {
                        dump--;
                    }
                }
                maps[i] = dump;
                dmp[dump] = classes[i];
            } catch (RuntimeException re) {
                if (ne == null) {
                    ne = re;
                } else {
                    ne.addSuppressed(re);
                }
                MessageDump md = MessageDump.create("Dumping at " + i + " with value (" + point + ", " + dump + ")");
                re.addSuppressed(md);
            }
        }
        //noinspection ConstantConditions
        if (ne != null) throw ne;
        // CHECK NULL CLASS
        for (int i = 0; i < length; i++) {
            Class c = dmp[i];
            if (c == null) {
                NullPointerException nex =
                        new NullPointerException(
                                "No permute class at " + i
                        );
                if (ne == null) {
                    ne = nex;
                    ne.addSuppressed(MessageDump.create("Classes: " + Arrays.toString(dmp)));
                    ne.addSuppressed(MessageDump.create("Indexes: " + Arrays.toString(maps)));
                    ne.addSuppressed(MessageDump.create("Points:  " + Arrays.toString(points)));
                } else ne.addSuppressed(nex);
            }
        }
        if (ne != null) {
            throw ne;
        }
        try {
            mh = MethodHandles.permuteArguments(mh,
                    MethodType.methodType(type.returnType(), dmp), maps);
        } catch (RuntimeException re) {
            re.addSuppressed(MessageDump.create("Here is SYSTEM Error."));
            re.addSuppressed(MessageDump.create("Old Types: " + type));
            re.addSuppressed(MessageDump.create("New Types: " + Arrays.toString(dmp)));
            re.addSuppressed(MessageDump.create("Reorder: " + Arrays.toString(maps)));
            throw re;
        }
        // INSERT DROP ARGUMENTS AND ADD PARAM FILTER
        for (int jk = 0; jk < r_length; jk++) {
            if (points[jk] == 0) {
                int from = jk;
                while (jk <= r_length) {
                    if (jk == r_length) {
                        jk++;
                        break;
                    }
                    if (points[jk++] != 0) break;
                }
                jk--;
                buffer.clear();
                for (int kar98k = from; kar98k < jk; kar98k++) {
                    buffer.add(rules[kar98k].getAppendClass());
                }
                /*
                System.out.println("  ===\n" +
                        "  Old: " + mh.type() + "\n" +
                        "  inserts: " + buffer + "\n" +
                        "  Positions: " + from + ", " + jk + " (" + (jk - from) + ")");
                */
                mh = MethodHandles.dropArguments(mh, from, buffer);
            }
        }
        for (int jk = 0; jk < r_length; jk++) {
            IParamRule rule = rules[jk];
            final MethodHandle filter = rule.filter();
            if (filter != null) {
                final Class<?> ret = filter.type().returnType();
                final Class<?> par = mh.type().parameterType(jk);
                if (!(par == ret || par.isAssignableFrom(ret))) {
                    mh = mh.asType(mh.type().changeParameterType(jk, Object.class));
                }
                try {
                    mh = MethodHandles.filterArguments(mh, jk, filter);
                } catch (RuntimeException re) {
                    re.addSuppressed(new Throwable("Filter: " + filter + ", Pos: " + jk + ", Length: " + r_length + ", Method: " + mh + ", Rule: " + rule));
                    throw re;
                }
            }
        }

        // System.out.println("=== " + Arrays.toString(points));
        return mh;
    }

}
