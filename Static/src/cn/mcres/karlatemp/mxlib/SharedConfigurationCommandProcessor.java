/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SharedConfigurationCommandProcessor.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:55@version: 2.0
 */

package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import cn.mcres.karlatemp.mxlib.cmd.ICommandProcessor;
import cn.mcres.karlatemp.mxlib.configuration.ICommandConfig;
import cn.mcres.karlatemp.mxlib.tools.IObjectCreator;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class SharedConfigurationCommandProcessor extends SharedConfigurationProcessor {


    @Override
    protected RuntimeException post_load(Class boot,
                                         ClassLoader loader,
                                         List<String> classes,
                                         RuntimeException errors, IBeanManager beans) {
        super.post_load(boot, loader, classes, errors, beans);
        Collection<String> pcks = new HashSet<>();
        classes.sort(Toolkit.getPackageComparator());
        for (String s : classes) {
//            System.out.println("< " + s);
            if (s.endsWith("CommandConfig")) {
                if (DEBUG) {
                    MXBukkitLib.getLogger().printf("[CommandProcessor] Found CommandConfig: " + s);
                }
                String pck = Toolkit.getPackageByClassName(s);
                if (pcks.contains(pck)) continue;
                pcks.add(pck);
                String sd = pck + '.';
                String[] subs = classes.stream().filter(a -> a.startsWith(sd)).toArray(String[]::new);
                if (DEBUG) {
                    for (String sb : subs) {
                        MXBukkitLib.getLogger().printf("    SubClass: " + sb);
                    }
                }
                try {
                    Class<?> c = Class.forName(s, true, loader);
                    if (ICommandConfig.class.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers())) {
                        IObjectCreator creator = beans.getBean(IObjectCreator.class);
                        ICommandConfig ic = creator.newInstance(c.asSubclass(ICommandConfig.class));
                        subs = ic.filterWithNames(subs);
                        beans.getBean(ICommandProcessor.class).load(ic, loader, subs);
                    }
                } catch (Exception exc) {
                    errors = a(errors, exc);
                }
            }
        }
        return errors;
    }
}
