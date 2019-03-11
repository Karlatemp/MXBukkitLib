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
import java.io.InputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.nio.charset.StandardCharsets;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Base64;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.bukkit.configuration.InvalidConfigurationException;
import cn.mcres.gyhhy.MXLib.bukkit.MXAPI;

public class AgentStartup {

    public static void appendPlugin() {
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
                            while(-1 != (lg = zis.read(buffer))){
                                out.write(buffer,0,lg);
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
        AgentStore.si = new AgentStore.SI(ist);
    }

    public static void main(String[] args) {
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
                            jf = new JarFile(AgentCodeShare.copy(f));
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
