/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author 32798
 */
public class AgentCodeShare {

    public static File copy(File f) throws IOException {
        File store = new File("./plugins/MXBukkitLib/Bootstrap.jar");
        if (!store.isFile() || store.lastModified() < f.lastModified()) {
            try (ZipInputStream zip = new ZipInputStream(new FileInputStream(f))) {
                new File(store, "..").mkdirs();
                store.createNewFile();
                try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(store))) {
                    ZipEntry e;
                    byte[] buf = new byte[1024];
                    int lg;
                    while ((e = zip.getNextEntry()) != null) {
                        String name = e.getName();
                        String path = name.toLowerCase();
                        if ((name.endsWith(".class") && path.startsWith("cn/mcres/gyhhy/mxlib/system/agent"))
                                || (name.equals("META-INF/MANIFEST.MF"))) {
                            out.putNextEntry(e);
                            while ((lg = zip.read(buf)) != -1) {
                                out.write(buf, 0, lg);
                            }
                            out.closeEntry();
                        }
                    }
                }
            }
        }
        return (store);
    }
}
