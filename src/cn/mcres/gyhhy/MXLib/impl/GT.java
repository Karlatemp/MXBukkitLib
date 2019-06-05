/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.impl;

import cn.mcres.gyhhy.MXLib.log.BasicLogger;
import org.bukkit.event.*;
import cn.mcres.gyhhy.MXLib.event.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import cn.mcres.gyhhy.MXLib.ext.lookup.Looker;
import java.lang.invoke.MethodHandle;
import java.util.logging.ConsoleHandler;

public class GT {

    public static void main(String[] args) {
        SSMapping.UseLibsLogger();
        Logger logger = Logger.getGlobal();
        logger.log(Level.INFO, "Message Test", new RuntimeException("RE"));
        logger.log(Level.INFO, "Message Raw Test");
        logger.log(Level.INFO, "Message Format Test: {0}, {1}", new Object[]{"Arg 0", "Arg 1"});
        logger.log(Level.SEVERE, "Message Error Test");
        logger.log(Level.WARNING, "Message WARNING Test");
        logger = Logger.getLogger("GT");
        logger.log(Level.INFO, "Message Test", new RuntimeException("RE"));
        logger.log(Level.INFO, "Message Raw Test");
        logger.log(Level.INFO, "Message Format Test: {0}, {1}", new Object[]{"Arg 0", "Arg 1"});
        logger.log(Level.SEVERE, "Message Error Test");
        logger.log(Level.WARNING, "Message WARNING Test");
        Looker lk = new Looker(Looker.openLookup(GT.class,~0));
        MethodHandle mh = lk.findSetter(CSMP.class, "sup", ConsoleHandler.class);
        System.out.println(mh);
    }

}
