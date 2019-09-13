/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.system;

import cn.mcres.gyhhy.MXLib.RefUtil;
import cn.mcres.gyhhy.MXLib.ThrowHelper;
import cn.mcres.gyhhy.MXLib.Version;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import cn.mcres.gyhhy.MXLib.bukkit.MXAPI;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import cn.mcres.gyhhy.MXLib.bukkit.cmd.Options;
import java.lang.instrument.Instrumentation;

@SuppressWarnings("unchecked")
public class VMHelperImplAPP extends VMHelper {

    static {
        do {
            final String package_ = VMHelper.class.getPackage().getName();
            final String CLASS_STARTUP = package_ + ".AgentStartup";
            final Class<?> CLASS_STARTUP_CLASS = RefUtil.safeLoadClass(CLASS_STARTUP, false, null);
            if (CLASS_STARTUP_CLASS != null) {
                // Loading from javaagent
                Class<?> temp = RefUtil.safeLoadClass(package_ + ".AgentStore", false, null);
                if (temp != null) {
                    Field fe = RefUtil.getField(temp, "si", false);
                    if (fe != null) {
                        Object i = RefUtil.get(fe, (Object) null);
                        if (i instanceof Instrumentation) {
                            rcc = (Instrumentation) i;
                            break;
                        }
                    }
                }
            }
            if (!RefUtil.classExists("com.sun.tools.attach.VirtualMachine", true, VMHelperImplAPP.class.getClassLoader())) {
                throw new NoClassDefFoundError(
                        "Class com.sun.tools.attach.VirtualMachine not found. "
                        + "If you using JDK, please add <JDK HOME>/lib/tools.jar. "
                        + "If not, please download JDK Tools.jar and attach.dll(<JDK HOME>/jre/bin/attach.dll)(attach.so). And use System.load to load it.");
            }
            File file = ConfSave.VMHelperImplAPP_Bootstrap_store_location;
            if (file == null) {
                throw new LinkageError("MXBukkitLib - You must provide a location "
                        + "for ConfSave.VMHelperImplAPP_Bootstrap_store_location to "
                        + "store Bootstrap.jar to start VMHelper");
            }
            /*if (file.canWrite() && file.canRead()) {
        } else {
            throw new java.io.IOError(new IOException("Bootstrap.jar must be writable and readable."));
        }*/
            String ver = MXAPI.getVersion();
            String over = "0.0.0";
            if (file.exists()) {
                if (file.isFile()) {
                    try (JarFile jar = new JarFile(file)) {
                        String temp = jar.getManifest().getMainAttributes().getValue("Signature-Version");
                        if (temp != null) {
                            over = temp;
                        }
                    } catch (IOException ex) {
                        throw new java.io.IOError(ex);
                    }
                } else {
                    throw new java.io.IOError(new IOException(file + " looks like a directory?"));
                }

            }
            if (Version.compare(ver, over) > 0) {
//            System.out.println("N File");
                try {
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
                    Options.searchInPackageWithConnection((List) classes, VMHelper.class);
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
                } catch (IOException ex) {
                    throw new java.io.IOError(ex);
                }
            }
            try {
                VirtualMachine.attach(SystemHelper.getJVMPidAsString()).loadAgent(file.getCanonicalPath());
            } catch (AttachNotSupportedException | IOException | AgentLoadException | AgentInitializationException ex) {
                ThrowHelper.getInstance().thr(ex);
            }
        } while (false);
    }

    @Override
    public Instrumentation getInstrumentation() {
        return rx();
    }

    public static class S {

        public static void main(String[] arg) {
            System.out.println(Version.compare("1.0.0", "1.0.1"));
        }
    }
}
