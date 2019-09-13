/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.system;

import cn.mcres.gyhhy.MXLib.Version;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import cn.mcres.gyhhy.MXLib.Core;
import cn.mcres.gyhhy.MXLib.bukkit.cmd.Options;

public class AgentCodeShare {

    @Deprecated
    public static boolean setup = true;

    public static File copy(File f) throws IOException {
        final String package_ = AgentCodeShare.class.getPackage().getName();
        final String CLASS_STARTUP = package_ + ".AgentStartup";
//        final Class<?> CLASS_STARTUP_CLASS = RefUtil.safeLoadClass(CLASS_STARTUP, false, null);
        File file = new File("./plugins/MXBukkitLib/Bootstrap.jar");
        String ver = cn.mcres.gyhhy.MXLib.bukkit.MXAPI.version;
        String over = "0.0.0";
        if (file.exists()) {
            if (file.isFile()) {
                try (JarFile jar = new JarFile(file)) {
                    String temp = jar.getManifest().getMainAttributes().getValue("Signature-Version");
                    if (temp != null) {
                        over = temp;
                    }
                }
            } else {
                throw (new IOException(file + " looks like a directory?"));
            }

        }
        if (Version.compare(ver, over) > 0) {
//            System.out.println("N File");
            file.createNewFile();
            Manifest m = new Manifest();
            Attributes ma = m.getMainAttributes();
            ma.putValue("Signature-Version", ver);
//                String package_ = VMHelper.class.getPackage().getName();
//                String me = package_ + ".AgentStartup";
            ma.putValue("Premain-Class", CLASS_STARTUP);
            ma.putValue("Agent-Class", CLASS_STARTUP);
            ma.putValue("Can-Retransform-Classes", "true");

//                System.out.println("DD OX");
//                m.write(System.out);
//                System.out.println("DD OV");
            List<String> classes = new ArrayList<>();
            Options.searchInPackageWithConnection((List) classes, AgentCodeShare.class);
//                System.out.println(classes);

            try (JarOutputStream jo = new JarOutputStream(new FileOutputStream(file), m)) {
                String aa = (package_ + ".Agent");
//                    System.out.println("X " + aa);
                String[] cx = classes.stream().filter(s -> {
//                        System.out.println("F " + s);
                    if (s.startsWith(aa)) {
                        return true;
                    }
                    return false;
                }).toArray(String[]::new);
                for (String s : cx) {
//                        System.out.println("A " + s);
                    String rs = s.substring(s.lastIndexOf('.') + 1) + ".class";
//                        System.out.println("  R " + rs);
                    InputStream is = VMHelper.class.getResourceAsStream(rs);
                    if (is != null) {
                        byte[] bf = new byte[is.available()];
                        is.read(bf);
                        jo.putNextEntry(new ZipEntry(s.replaceAll("\\.", "/") + ".class"));
                        jo.write(bf);
                        jo.closeEntry();
//                            System.out.println(s);
                    }

                }
            }
        }
        return (file);
    }
}
