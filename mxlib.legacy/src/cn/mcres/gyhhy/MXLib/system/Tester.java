/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Tester.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.system;

import java.io.File;

import cn.mcres.gyhhy.MXLib.event.*;
import cn.mcres.gyhhy.MXLib.event.InstrumentationEvent;

import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.logging.Logger;

import com.avaje.ebean.EbeanServer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.*;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;

/**
 * @author 32798
 */
public class Tester {

    static boolean ec;

    public static void printClassLoaders(Class c, ClassLoader cl) {
        if (cl == null) {
            cl = c.getClassLoader();
        }
        System.out.println("CC " + c);
        while (cl != null) {
            System.out.format("\t%s[%s]%n", cl, cl.getClass().getName());
            cl = cl.getParent();
        }
        System.out.println("OV " + c);
    }

    public static void main(String[] args) {
        EventHelper.registerListener(new Listener() {
            @EventHandler
            public void r(InstrumentationEvent.GetAllLoadedClasses gs) {
                System.out.println("GALC called by " + gs.getCaller());
                gs.setCancelled(ec);
            }
        }, null);
        ConfSave.vmverbose = true;
        ConfSave.InstrumentationEvent = true;
        ConfSave.VMHelperImplAPP_Bootstrap_store_location = new File("G:\\Tempx\\boot.jar");
        VMHelper vvm = VMHelper.getHelper();
        vvm.getInstrumentation();
        Instrumentation ix = VMHelper.root;
        printClassLoaders(ix.getClass(), null);
        printClassLoaders(Tester.class, null);
    }

}
