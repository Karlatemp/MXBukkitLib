/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SharedBeanManager.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;
import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SharedBeanManager implements IBeanManager {
    private Node[] nodes;
    private int limit;
    private int position;

    public SharedBeanManager() {
        this(50);
    }

    public SharedBeanManager(int size) {
        nodes = new Node[size];
        limit = size;
        position = 0;
    }

    static class Node {
        Class c;
        Object bean;

        Node(Class c, Object b) {
            this.c = c;
            this.bean = b;
        }
    }

    private synchronized Node[] expand() {
        int add = Math.max((int) (50 * Math.random()), position) + 6;
        int size = limit + add;
        Node[] sd = new Node[size];
        System.arraycopy(nodes, 0, sd, 0, limit);
        nodes = sd;
        limit = size;
        return sd;
    }

    @Override
    public <T> void addBean(@NotNull Class<T> c, @NotNull T bean) {
        if (!c.isInstance(bean)) {
            throw new ClassCastException();
        }
        // PRE CHECKUP
        if (check(c, c)) {
            return;
        }
        synchronized (this) {
            if (position >= limit) {
                Node[] sd = expand();
                sd[position++] = new Node(c, bean);
            } else {
                Node[] table = nodes;
                for (Node n : table) {
                    if (n == null) break;
                    if (n.c == c) {
                        n.bean = bean;
                        return;
                    }
                }
                table[position++] = new Node(c, bean);
            }
        }
    }

    private boolean check(Class<?> c, Class source) {
        if (c == null) return false;
        ProhibitBean pb = c.getDeclaredAnnotation(ProhibitBean.class);
        if (pb != null) {
            if (c == source) {
                System.out.println("C with Source, SKIP " + source);
                return true;
            }
            switch (pb.value()) {
                case ALL_WITH_SUBCLASS:
                    System.out.println("C with ALL, SKIP " + source + ":" + c);
                    return true;
            }
        }
        if (c.isArray()) return false;
        final Class<?>[] interfaces = c.getInterfaces();
        for (Class inter : interfaces) {
            if (check(inter, source)) return true;
        }
        if (c.isInterface()) return false;
        return check(c.getSuperclass(), source);
    }

    @Nullable
    @Override
    public <T> T getBean(@NotNull Class<T> c) {
        if (position >= limit) {
            synchronized (this) {
                expand();
            }
        }
        if (position > 0) {
            Node[] table = nodes;
            for (Node n : table) {
                if (n == null) break;
                if (n.c == c) //noinspection unchecked
                    return (T) n.bean;
            }
        }
        return null;
    }

    @NotNull
    @Override
    public Map<Class<?>, Object> getBeans() {
        Map<Class<?>, Object> map = new HashMap<>();
        for (int i = 0; i < position; i++) {
            Node n = nodes[i];
            if (n != null) {
                map.put(n.c, n.bean);
            }
        }
        return map;
    }
}
