/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Options.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.cmd;

import cn.mcres.gyhhy.MXLib.RefUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Class add in version 0.20
 *
 * @author 32798
 */
public class Options {

    public static final int TYPE_RAW = 0,
            TYPE_COMMAND_IN_ONE_CLASS = 1,
            TYPE_COMMAND_IN_PACKAGE = 2;
    public static final int PACKAGE_SEARCH_TYPE_DEFAULT = 1,
            PACKAGE_SEARCH_TYPE_USE_CONNECTION = 0;
    private static final Pattern pt = Pattern.compile("\\.[cC][lL][aA][sS]{2}$");

    public static void main(String[] args) throws Exception {
        Class c = Options.class;
        ProtectionDomain pd = c.getProtectionDomain();
        System.out.println(pd);
        CodeSource cs = pd.getCodeSource();
        System.out.println(cs);
        URL loc = cs.getLocation();
        System.out.println(loc);
        URLConnection oc = loc.openConnection();
        System.out.println(oc);
    }

    protected static SubCommandEX createCommand(Class<?> c, Method m, Object t, SubCommand a) {
//        SubCommand.CommandCreateType.UseReflectMethod;
        if (t instanceof SubCommandExecutor) {
            SubCommandEX sce = (SubCommandEX) t;
            if (sce.sc == null) {
                sce.sc = a;
            }
            return sce;
        }
        if (c != null) {
            if (SubCommandExecutor.class.isAssignableFrom(c)) {
                try {
                    SubCommandExecutor exec = (SubCommandExecutor) c.newInstance();
                    exec.setup(m, t, a);
                    if (exec.sc == null) {
                        exec.sc = a;
                    }
                    return exec;
                } catch (InstantiationException | IllegalAccessException ex) {
                    throw new RuntimeException(ex.getLocalizedMessage(), ex);
                }
            }
        }

        switch (a.create()) {
            case UseReflectMethod: {
                if (c == null) {
                    return new SubCommandEX(m, t);
                }

                return new SubCommandEX(c, m, t);
            }
            default: {
                if (c == null) {
                    return new SubCommandMH(m, t);
                }
                return new SubCommandMH(c, m, t);
            }
        }
    }

    @SuppressWarnings({"rawtypes"})
    private static void app(ExecuterEX exo, Object clazz, Class cx, Object thiz, HashMap<Class<?>, Object> mapping) {
//        System.out.println(String.format("%s %s %s %s", exo, name, ul, cx));
        try {
            Class<?> cxwe;
            if (clazz instanceof Class) {
                cxwe = (Class) clazz;
            } else {
                cxwe = cx.getClassLoader().loadClass(String.valueOf(clazz));
            }
            if (Modifier.isAbstract(cxwe.getModifiers()) || cxwe.getDeclaredAnnotation(ClassIgnore.class) != null) {
                return;
            }
            if (thiz != null) {
                cxwe.cast(thiz);
            } else {
                thiz = mapping.get(cxwe);
                if (thiz == null) {
                    try {
                        if (cxwe.getConstructor() == null) {
                            return;
                        }
                    } catch (NoSuchMethodException ex) {
                        return;
                    }
                    thiz = cxwe.newInstance();
                    mapping.put(cxwe, thiz);
                }

            }
            if (cxwe.getDeclaredAnnotation(SubCommand.class) != null) {
                if (SubCommandExecutor.class.isAssignableFrom(cxwe)) {
                    SubCommand sccc = cxwe.getAnnotation(SubCommand.class);
                    String m = sccc.name();
                    if (m.isEmpty()) {
                        m = cxwe.getSimpleName();
                    }
                    exo.reg(m, createCommand(cxwe, null, thiz, sccc));
                    return;
                }
                Method[] mets = cxwe.getDeclaredMethods();
                Method main = null;
                for (Method met : mets) {
                    if (met.isAnnotationPresent(SubCommandHandle.class)) {
                        main = met;
                        break;
                    }
                }
                SubCommand sccc = cxwe.getAnnotation(SubCommand.class);
                String m = sccc.name();
                if (m.isEmpty()) {
                    m = cxwe.getSimpleName();
                }
//                Object thiz = cxwe.newInstance();
//                System.out.println(m);
//                System.out.println(main);
                if (main == null) {
                    for (Method met : mets) {
                        try {
                            exo.reg(m, createCommand(cxwe, met, thiz, sccc));
                            break;
                        } catch (Throwable thr) {
                        }
                    }
                } else {
                    exo.reg(m, createCommand(cxwe, main, thiz, sccc));
                }
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SecurityException | ClassCastException ex) {
            throw new SearchException(ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * (ArrayList&lt;Object>, Class)void -> (List&lt;Object>, Class)void
     */
    @SuppressWarnings({"rawtypes"})
    public static void searchInPackageDefault(ArrayList<Object> sr, Class cx) {
        searchInPackageDefault((List<Object>) sr, cx);
    }

    /**
     * (ArrayList&lt;Object>, Class)void -> (List&lt;Object>, Class)void
     */
    @SuppressWarnings({"rawtypes"})
    public static void searchInPackageWithConnection(ArrayList<Object> sr, Class cx) {
        searchInPackageWithConnection((List<Object>) sr, cx);
    }

    @SuppressWarnings({"rawtypes"})
    public static void searchInPackageWithConnection(List<Object> sr, Class cx) {
        URL url = cx.getResource(cx.getSimpleName() + ".class");
        String pc = url.getProtocol();
        Package pag = cx.getPackage();
        String pn = pag.getName();
//        sun.net.www.protocol.file.FileURLConnection;
        try {
            final List<Object> names = sr;
            if (pc.equalsIgnoreCase("jar")) {
                url = new URL(url.toString().replaceFirst("!/.*$", "!/"));
            }
            URLConnection conn = url.openConnection();
//            System.out.println(conn.getClass());
            if (conn instanceof JarURLConnection) {
                JarURLConnection juc = (JarURLConnection) conn;
                try (JarFile jf = juc.getJarFile()) {
                    jf.stream().filter((je) -> {
                        String name = je.getName();
                        if (name.endsWith(".class")) {
                            if (name.replaceFirst("^/", "").replaceAll("/", ".").startsWith(pn)) {
                                return true;
                            }
                        }
                        return false;
                    }).forEach((je) -> {
                        names.add(
                                pt.matcher(
                                        je.getName().replaceFirst("^/", "").replaceAll("/", ".")
                                ).replaceFirst(""));
                    });
                }
            } else if (pc.equals("file")) {
                File f = RefUtil.get(conn, "file");
                File dir = new File(f, "..");
                class A {

                    void r(File f, String p) {
                        if (f.isFile()) {
                            names.add(p + "." + pt.matcher(f.getName()).replaceFirst(""));
                        } else if (f.isDirectory()) {
                            File[] fs = f.listFiles();
                            if (fs != null) {
                                for (File fx : fs) {
                                    String px = p;
                                    if (fx.isDirectory()) {
                                        px += "." + fx.getName();
                                    }
                                    r(fx, px);
                                }
                            }
                        }
                    }
                }
                new A().r(dir, pn);
            }

        } catch (IOException ex) {
            throw new SearchException(ex.getLocalizedMessage(), ex);
        }
    }

    @SuppressWarnings({"rawtypes"})
    public static void searchInPackageDefault(List<Object> sr, Class cx) {
        Package pag = cx.getPackage();
        String ul = pag.getName() + ".";
        URL res = cx.getResource(cx.getSimpleName() + ".class");
        String pt = res.toString();
        if (pt.startsWith("jar:file:")) {
            pt = pt.replace("jar:file:", "");
            String[] ppx = pt.split("\\!", 2);
//            System.out.println(Arrays.toString(ppx));
            String ppxw = ppx[1].replaceFirst("[a-zA-Z]+\\.class$", "").replaceFirst("^\\/", "");
            File fe = new File(ppx[0]);
//            System.err.println("PPT " + ppxw);
            try {
                FileInputStream in = new FileInputStream(fe);
                ZipInputStream zip = new ZipInputStream(in);
                try {
                    ZipEntry get;
                    while ((get = zip.getNextEntry()) != null) {
                        String name = get.getName().replaceFirst("^\\/", "");
//                        System.err.println("FE " + name);
                        if (name.startsWith(ppxw) && name.endsWith(".class")) {
//                            System.err.println("CAT " + name);
                            if (name.replace(ppxw, "").indexOf('/') == -1) {
                                name = name.replace(ppxw, "").replaceFirst("\\.class$", "");
                                sr.add(ul + name);
                            }
                        }
                    }
                } catch (Throwable trx) {
                    throw trx;
                } finally {
                    in.close();
                    zip.close();
                }
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
        } else if (pt.startsWith("file:")) {
            File ff = new File(pt.replace("file:", "").replaceFirst("[a-zA-Z]+\\.class$", ""));
            File[] list = ff.listFiles((a) -> a.isFile() && a.getName().endsWith(".class"));
            if (list != null) {
                for (File f : list) {
                    sr.add(ul + f.getName().replace(".class", ""));
                }
            }
        }
    }

    private Class<?> main;
    private Object thiz;
    private int type, pst;
    private LanguageTranslator tts;

    public Options() {
    }

    public LanguageTranslator translator() {
        return tts;
    }

    public Options translator(LanguageTranslator tts) {
        this.tts = tts;
        return this;
    }

    public Class<?> main() {
        return main;
    }

    public Options main(Class<?> c) {
        main = c;
        return this;
    }

    public Object thiz() {
        return thiz;
    }

    public Options thiz(Object thiz) {
        this.thiz = thiz;
        return this;
    }

    public int loadType() {
        return type;
    }

    public Options loadType(int t) {
        type = t;
        return this;
    }

    public int packageSearchType() {
        return pst;
    }

    public Options packageSearchType(int pst) {
        this.pst = pst;
        return this;
    }

    @SuppressWarnings({"rawtypes"})
    public List<Object> searchInPackage(Class cx) {
        ArrayList<Object> sr = new ArrayList<>();
        switch (this.pst) {
            case PACKAGE_SEARCH_TYPE_USE_CONNECTION: {
                searchInPackageWithConnection(sr, cx);
                break;
            }
            case PACKAGE_SEARCH_TYPE_DEFAULT: {
                searchInPackageDefault(sr, cx);
                break;
            }
        }
        return sr;
    }

    public ExecuterEX build() {
        final ExecuterEX exe = new ExecuterEX(main);
        switch (type) {
            case TYPE_COMMAND_IN_PACKAGE: {
                HashMap<Class<?>, Object> mapping = new HashMap<>();
                mapping.put(main, thiz);
                searchInPackage(main).forEach((w) -> {
                    app(exe, w, main, null, mapping);
                });
                break;
            }
            case TYPE_RAW: {
                break;
            }
            case TYPE_COMMAND_IN_ONE_CLASS: {
                Method[] mets = main.getDeclaredMethods();
                for (Method m : mets) {
                    if (m.isAnnotationPresent(SubCommand.class)) {
                        SubCommand sub = m.getAnnotation(SubCommand.class);
                        String name = sub.name();
                        if (name.isEmpty()) {
                            name = m.getName();
                        }
                        exe.reg(name, createCommand(null, m, thiz, sub));
                    }
                }
                break;
            }
        }
        if (tts != null) {
            exe.setLanguageTranslator(tts);
        }
        return exe;
    }

    @SuppressWarnings("PublicInnerClass")
    public static class SearchException extends RuntimeException {

        private static final long serialVersionUID = 15478548741189044L;

        public SearchException() {
        }

        public SearchException(String message, Throwable cause) {
            super(message, cause);
        }

        public SearchException(Throwable cause) {
            super(cause);
        }

        public SearchException(String message) {
            super(message);
        }

    }

}
