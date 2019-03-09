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
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.nio.charset.StandardCharsets;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Base64;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class AgentStartup {
    

    public static void premain(String option, Instrumentation ist) {
        agentmain(option, ist);

    }

    public static void agentmain(String option, Instrumentation ist) {
        loadBoots(option, ist);
        AgentStore.setiit(ist);
        AgentStore.si = new AgentStore.SI(ist);
    }

    public static void main(String[] args) {
    }


    private static void loadBoots(String option, Instrumentation ist) {
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
