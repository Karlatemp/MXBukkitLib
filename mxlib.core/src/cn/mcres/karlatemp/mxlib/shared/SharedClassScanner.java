/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SharedClassScanner.java@author: karlatemp@vip.qq.com: 2019/12/24 下午10:07@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.shared;

import cn.mcres.karlatemp.mxlib.exceptions.ScanException;
import cn.mcres.karlatemp.mxlib.internal.IHookedJarURLStreamHandler;
import cn.mcres.karlatemp.mxlib.network.NetManager;
import cn.mcres.karlatemp.mxlib.tools.CharCompiler;
import cn.mcres.karlatemp.mxlib.tools.IClassScanner;
import cn.mcres.karlatemp.mxlib.tools.UnclosedInputStream;
import javassist.bytecode.ClassFile;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SharedClassScanner implements IClassScanner {

    private static String dump(String a) {
        if (a == null) return "";
        while (true) {
            if (a.length() > 0) {
                if (a.charAt(0) == '/') {
                    a = a.substring(1);
                    continue;
                }
            }
            break;
        }
        return a;
    }

    @NotNull
    @Override
    public List<String> scan(@NotNull File file, @NotNull List<String> list) throws ScanException {
        if (file.isFile()) {
            try {
                try (FileInputStream clazz = new FileInputStream(file)) {
                    try (DataInputStream dis = new DataInputStream(clazz)) {
                        ClassFile cf = new ClassFile(dis);
                        list.add(replace(cf.getName(), false));
                    }
                } catch (Exception ioe0) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        try (ZipInputStream zip = new ZipInputStream(fis)) {
                            ZipEntry entry;
                            while ((entry = zip.getNextEntry()) != null) {
                                if (!entry.isDirectory()) {
                                    final String name = dump(entry.getName());
                                    if (name.endsWith(".class")) {
                                        if (!name.startsWith("META-INF")) {
                                            list.add(replace(name, true));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException ioe) {
                throw new ScanException(ioe.toString(), ioe);
            }
        } else if (file.isDirectory()) {
            ScanException top = null;
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    try {
                        scan(f, list);
                    } catch (ScanException se) {
                        if (top == null) {
                            top = se;
                        } else {
                            top.addSuppressed(se);
                        }
                    }
                }
            }
            if (top != null) {
                throw top;
            }
        }
        return list;
    }

    private static String replace(String name, boolean a) {
        char[] set = name.toCharArray();
        if (a) {
            char[] nw = new char[set.length - 6];
            System.arraycopy(set, 0, nw, 0, nw.length);
            set = nw;
        }
        for (int i = 0; i < set.length; i++) {
            if (set[i] == '/') {
                set[i] = '.';
            }
        }
        return new String(set);
    }

    @NotNull
    @Override
    public List<String> scan(@NotNull Class c, @NotNull List<String> list) throws ScanException {
        final URL resource = c.getResource(c.getSimpleName() + ".class");
        if (resource != null) {
            return scan(resource, list);
        }
        return list;
    }

    @NotNull
    @Override
    public List<String> scan(@NotNull URL url, @NotNull List<String> list) throws ScanException {
        final String protocol = url.getProtocol();
        switch (protocol.toLowerCase()) {
            case "file": {
                File file = new File(url.getFile());
                if (file.exists()) {
                    if (file.isFile()) {
                        file = new File(file, "..");
                    }
                    return scan(file, list);
                }
                break;
            }
            case "jar": {
                String path = url.getPath();
                if (path.startsWith("file:")) {
                    String file = path.substring(5);
                    int end = file.indexOf('!');
                    String cut, pck;
                    if (end == -1) {
                        cut = file;
                        pck = "";
                    } else {
                        cut = file.substring(0, end);
                        pck = file.substring(end + 1);
                    }
                    if (pck.endsWith(".class")) {
                        int a = pck.lastIndexOf('/');
                        if (a == -1) {
                            pck = "";
                        } else {
                            pck = pck.substring(0, a + 1);
                        }
                    }
                    pck = dump(pck);
                    cut = CharCompiler.decode('%', cut, StandardCharsets.UTF_8);
                    File jar = new File(cut);
                    if (jar.isFile()) {
                        try (FileInputStream fis = new FileInputStream(jar)) {
                            try (ZipInputStream zis = new ZipInputStream(fis)) {
                                try (DataInputStream bin = new DataInputStream(new UnclosedInputStream(zis))) {
                                    ZipEntry ze;
                                    while ((ze = zis.getNextEntry()) != null) {
                                        if (ze.isDirectory()) continue;
                                        String name = ze.getName();
                                        if (name.endsWith(".class")) {
                                            if (dump(name).startsWith(pck)) {
                                                list.add(replace(new ClassFile(bin).getName(), false));
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (IOException ioe) {
                            throw new ScanException(ioe.toString(), ioe);
                        }
                    }
                }
                break;
            }
            default: {
                var handler = NetManager.getHandler(url);
                if (handler instanceof IHookedJarURLStreamHandler) {
                    var jar = ((IHookedJarURLStreamHandler) handler).getJar();
                    var path = url.getFile().substring(1);
                    int l = path.lastIndexOf('/');
                    if (l != -1) path = path.substring(0, l + 1);
                    var itor = jar.entries().asIterator();
                    while (itor.hasNext()) {
                        var next = itor.next();
                        var name = next.getName();
                        if (name.endsWith(".class")) {
                            if (name.startsWith(path)) {
                                list.add(name.substring(0, name.length() - 6).replace('/', '.'));
                            }
                        }
                    }
                }
                break;
            }
        }
        return list;
    }
}
