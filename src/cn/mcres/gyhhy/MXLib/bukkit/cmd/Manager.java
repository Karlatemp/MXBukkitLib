/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.cmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author 32798
 */
public class Manager {

    public static ExecuterEX exec(Class cx) {
        ExecuterEX exo = new ExecuterEX(cx);
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
                                app(exo, name, ul, cx);
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
                    app(exo, f.getName().replace(".class", ""), ul, cx);
                }
            }
        }
//        System.out.println(res.getFile());
//        System.out.println(res);
        return exo;
    }

    public static ExecuterEX exec(Class cx, Object thiz) {
        ExecuterEX exo = new ExecuterEX(cx);
        Method[] mets = cx.getDeclaredMethods();
        for (Method m : mets) {
            if (m.isAnnotationPresent(SubCommand.class)) {
                SubCommand sub = m.getAnnotation(SubCommand.class);
                String name = sub.name();
                if (name.isEmpty()) {
                    name = m.getName();
                }
                exo.reg(name, new SubCommandEX(m, thiz));
            }
        }
        return exo;
    }

    private static void app(ExecuterEX exo, String name, String ul, Class cx) {
//        System.out.println(String.format("%s %s %s %s", exo, name, ul, cx));
        try {
            Class<?> cxwe = cx.getClassLoader().loadClass(ul + name);
            if (cxwe.isAnnotationPresent(SubCommand.class)) {
                Method[] mets = cxwe.getDeclaredMethods();
                Method main = null;
                for (Method met : mets) {
                    if (met.isAnnotationPresent(SubCommandHandle.class)) {
                        main = met;
                        break;
                    }
                }
                
                
                String m = cxwe.getAnnotation(SubCommand.class).name();
                if (m.isEmpty()) {
                    m = cxwe.getSimpleName();
                }
                Object thiz = cxwe.newInstance();
//                System.out.println(m);
//                System.out.println(main);
                if (main == null) {
                    for (Method met : mets) {
                        try {
                            exo.reg(m, new SubCommandEX(cxwe, met, thiz));
                            break;
                        } catch (Throwable thr) {
                        }
                    }
                } else {
                    exo.reg(m, new SubCommandEX(cxwe, main, thiz));
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
