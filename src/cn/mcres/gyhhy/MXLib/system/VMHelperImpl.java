/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.system;

import cn.mcres.gyhhy.MXLib.Base64;
import cn.mcres.gyhhy.MXLib.bukkit.Plugin;
import cn.mcres.gyhhy.MXLib.http.WebHelper;
import cn.mcres.gyhhy.MXLib.store.LongStore;
import com.sun.tools.attach.VirtualMachine;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VMHelperImpl extends VMHelper {

    private static void copy() {
    }

    static {
        if (!Plugin.plugin.getConfig().getBoolean("jvm.enable", false)) {
            // Stop to use VMHelperImpl
            throw new java.security.AccessControlException("JVM Helper is not enable.");
        }
    }

    static {
        final String AgentStartup_c = VMHelper.class.getPackage().getName() + ".AgentStartup";
        final String pp;
        {
            String ppx;
            try {
                ppx= AgentCodeShare.copy(Plugin.plugin.getFile()).getCanonicalPath();
            } catch (IOException ex) {
                ppx = "";
            }
            pp =ppx;
        }
        try {
            // CHeck Sun VM tool
            ClassLoader.getSystemClassLoader().loadClass(VirtualMachine.class.getName());
            try {
                // Checkup AgentStartup
                ClassLoader.getSystemClassLoader().loadClass(AgentStartup_c);
                // Class loaded. finish.
            } catch (ClassNotFoundException | NoClassDefFoundError ne) {
                try {
                    VirtualMachine.attach(SystemHelper.getJVMPidAsString()).loadAgent(pp, Base64.decode(pp));
                } catch (Exception exe) {
                    throw new RuntimeException(exe.getLocalizedMessage(), exe);
                }
            }
        } catch (ClassNotFoundException | NoClassDefFoundError ne) {
            File jdktool;
            {
                String jhome = System.getProperty("java.home");
                if (jhome.endsWith(File.separatorChar + "jre")) {
                    jhome = jhome.replaceFirst(".jre$", "");
                }
                Plugin.plugin.getLoggerEX().printf("Patch java home: "+jhome);
                jdktool = new File(jhome, "lib/tools.jar");
                if (jdktool.isFile()) {
                    Plugin.plugin.getLoggerEX().printf("Found jdk tools.");
                    Plugin.plugin.getLoggerEX().printf("At " + jdktool);
                } else {
                    Plugin.plugin.getLoggerEX().printf("Unfound jdk tools.");
                    jdktool = new File(Plugin.plugin.getDataFolder(), "jdktool.jar");
                    final File jdktools = jdktool;
                    if (!jdktool.isFile()) {
                        String link = Plugin.plugin.getConfig().getString("jvm.jdktool");
                        Plugin.plugin.getLoggerEX().printf("\u00a7bDownload jdk tool from \u00a76" + link);
                        LongStore ls = new LongStore(System.currentTimeMillis());
                        WebHelper.http(link).response((code, connection, is) -> {
                            if (code != 200) {
                                throw new java.lang.NullPointerException();
                            }
                            Plugin.plugin.getLoggerEX().printf("\u00a7bConnected. Start download." + (System.currentTimeMillis() - ls.v()) + "ms");
                            ls.v(System.currentTimeMillis());
                            try {
                                jdktools.createNewFile();
                                byte[] buffer = new byte[64];
                                try (FileOutputStream fos = new FileOutputStream(jdktools)) {
                                    int leng;
                                    long size = 0;
                                    while ((leng = is.read(buffer)) != -1) {
                                        size += leng;
                                        if (System.currentTimeMillis() - ls.v() > 200) {
                                            String ap;
                                            if (size < 1024) {
                                                ap = size + " B";
                                            } else {
                                                ap = (size / 1024) + " KB";
                                            }
                                            Plugin.plugin.getLoggerEX().printf("Downloaded " + ap);
                                        }
                                        ls.v(System.currentTimeMillis());
                                        fos.write(buffer, 0, leng);
                                    }
                                }
                            } catch (IOException ex1) {
                                throw new java.lang.RuntimeException(ex1.getLocalizedMessage(), ex1);
                            }
                        }).connect();
                    }
                }
            }
            try {
                Thread ct = Thread.currentThread();
                ClassLoader ccl = ct.getContextClassLoader();
                URLClassLoader ucl = new URLClassLoader(new URL[]{jdktool.toURI().toURL()}, ccl);
                try {
                    ct.setContextClassLoader(ucl);
                    Class<?> VirtualMachine_C = ucl.loadClass("com.sun.tools.attach.VirtualMachine");

                    {
                        // Load attach c lib
                        String lib = Plugin.plugin.getConfig().getString("jvm.clib", null);

                        if (lib != null) {
                            File f = new File(lib);
                            if (f.isFile() && f.canRead()) {
                                Runtime.getRuntime().load(f.getCanonicalPath());
                            }
                        }
                    }

                    VirtualMachine_C.getMethod("loadAgent", String.class, String.class).invoke(
                            VirtualMachine_C.getMethod("attach", String.class).invoke(null, SystemHelper.getJVMPidAsString()),
                            pp, Base64.encode(pp) + '^' + Base64.encode(jdktool.getCanonicalPath()));

                    ct.setContextClassLoader(ccl);
                } catch (Exception thr) {
                    ct.setContextClassLoader(ccl);
                    throw thr;
                }
//                    ucl.loadClass("com.sun.tools.attach.VirtualMachine");
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
        try {
            ClassLoader.getSystemClassLoader().loadClass(AgentStartup_c);
        } catch (ClassNotFoundException ex) {
            throw new java.lang.ArithmeticException("Load VMHelper Exception.");
        }
    }

    public static void check() throws Error, RuntimeException {
        try {
            Class.forName(VMHelperImpl.class.getName());
        } catch (ClassNotFoundException ex) {
            throw new java.lang.NoClassDefFoundError(ex.getMessage());
        }
    }

    @Deprecated
    @Override
    public void onDisable() {
        try {
            ClassLoader.getSystemClassLoader().loadClass("cn.mcres.gyhhy.MXLib.system.AgentStore").getMethod("clearup").invoke(null);
        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public Instrumentation getInstrumentation() {
        try {
            return (Instrumentation) ClassLoader.getSystemClassLoader().loadClass("cn.mcres.gyhhy.MXLib.system.AgentStore").getField("si").get(null);
        } catch (SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException ex) {
            throw new RuntimeException(ex.getLocalizedMessage(), ex);
        }
    }
}
