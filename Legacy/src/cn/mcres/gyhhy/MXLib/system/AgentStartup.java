/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AgentStartup.java@author: karlatemp@vip.qq.com: 19-9-11 下午1:48@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.system;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.instrument.Instrumentation;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import cn.mcres.gyhhy.MXLib.bukkit.MXAPI;
import org.bukkit.configuration.file.YamlConfiguration;

public class AgentStartup {

    /**
     * No create closer plugin file...<br>
     * -Dmxbukkitlib.javaagent.noplugin=false -javaagent:......
     */
    public static void appendPlugin() {
        if (Boolean.getBoolean("mxbukkitlib.javaagent.noplugin")) {
            return;
        }
        System.out.println("Checking closer plugin.");
        File plugins = new File("plugins");
        File zz = new File(plugins, "MXBukkitLib.javaagent.closer.jar");
        if (!zz.isFile() || !zz.exists()) {
            InputStream jar = AgentStartup.class.getClassLoader().getResourceAsStream("resources/PreAgentStore.jar");
            if (jar != null) {
                try (ZipInputStream zis = new ZipInputStream(jar)) {
                    plugins.mkdirs();
                    zz.createNewFile();
                    try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zz))) {
                        ZipEntry ze;
                        byte[] buffer = new byte[1024];
                        int lg;
                        while (null != (ze = zis.getNextEntry())) {
                            out.putNextEntry(ze);
                            while (-1 != (lg = zis.read(buffer))) {
                                out.write(buffer, 0, lg);
                            }
                            out.closeEntry();
                        }
                        out.putNextEntry(new ZipEntry("plugin.yml"));
                        out.write(("name: MXBukkitLib-JavaAgent-Closer-" + System.currentTimeMillis()).getBytes());
                        out.write(("\nversion: " + MXAPI.getVersion()).getBytes());
                        out.write("\nauthor: Karlatemp\nprefix: MXBukkitLib-Closer\nmain: cn.mcres.karlatemp.MXLib.PremainStop\nwebsite: https://www.mcres.cn/resources/91/\nload: STARTUP\n".getBytes());
                        out.closeEntry();
                    }
                } catch (Exception ex) {
                    Logger.getLogger(AgentStartup.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void premain(String option, Instrumentation ist) {
        File yml = new File("plugins/MXBukkitLib/config.yml");
        if (yml.isFile() && yml.canRead()) {
            try {
                System.out.println("[MXBukkitLib][JavaAgent] Reading Config from " + yml.getCanonicalPath());
                System.out.println("[MXBukkitLib][JavaAgent] Need reload config please restart server.");
                YamlConfiguration yaml = new YamlConfiguration();
                yaml.load(yml);
                ConfSave.reload(yaml);
            } catch (Throwable ex) {
                Logger.getLogger(AgentStartup.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        appendPlugin();
        agentmain(option, ist);
        try {
            ClassLoader.getSystemClassLoader().loadClass("cn.mcres.gyhhy.MXLib.system.VMHelperImpl");
        } catch (Exception ex) {
            Logger.getLogger(AgentStartup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void agentmain(String option, Instrumentation ist) {
        loadBoots(option, ist);
        AgentCodeShare.setup = false;
        AgentStore.setiit(ist);
        AgentStore.setSI(new AgentStore.SI(ist));
    }

    public static void main(String[] args) {
        AgentSS.main(args);
    }

    private static void loadBoots(String option, Instrumentation ist) {
        if (option == null) {
            return;
        }
        String[] ls = option.split("\\^");
        for (int i = 0; i < ls.length; i++) {
            String s = ls[i];
//            System.out.println("DEC: " + s);
            try {
                String path = new String(Base64.getDecoder().decode(s), StandardCharsets.UTF_8);
                File f = new File(path);
                if (f.isFile() && f.canRead()) {
                    try {
                        JarFile jf;
                        if (i == 0) {
                            try {
                                jf = new JarFile(AgentCodeShare.copy(f));
                            } catch (Throwable thr) {
                                jf = new JarFile(f);
                            }
                        } else {
                            jf = new JarFile(f);
                        }

                        ist.appendToBootstrapClassLoaderSearch(jf);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (Throwable thr) {
                thr.printStackTrace();
            }
        }
    }
}
