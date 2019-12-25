/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SharedMXLibBootProvider.java@author: karlatemp@vip.qq.com: 2019/12/24 下午10:07@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.shared;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.MXLibBootProvider;
import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import cn.mcres.karlatemp.mxlib.bean.IInjector;
import cn.mcres.karlatemp.mxlib.tools.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * The default boot provider
 *
 * @since 2.2
 */
public class SharedMXLibBootProvider implements MXLibBootProvider {
    public static class AutoConfigurationProcessor implements MXLibBootProvider {
        @Override
        public void boot() {
            try {
                ReadPropertiesAutoConfigs.load();
            } catch (Throwable thr) {
                MXBukkitLib.getLogger().printStackTrace(thr);
            }
            if (SharedConfigurationProcessor.DEBUG)
                MXBukkitLib.getBeanManager().getBeans().forEach((k, v) -> {
                    MXBukkitLib.getLogger().printf("[BeanManager] " + k + " = " + v);
                });
        }

        @Override
        public int getPriority() {
            return 10;
        }
    }

    @Override
    public void setBeanManager() {
        MXBukkitLib.setBeanManager(new SharedBeanManager());
    }

    @Override
    public void boot() {
        IBeanManager bean = MXBukkitLib.getBeanManager();
        bean.addBean(IInjector.class, new SharedInjector());
        bean.addBean(IObjectCreator.class, new SharedObjectCreator());
        bean.addBean(IMemberScanner.class, new SharedMemberScanner());
        bean.addBean(ClassResourceLoaders.class, new ClassResourceLoaders());
        bean.addBean(ServiceInstallers.class, new ServiceInstallers());
        ResourceLoaders rl = new ResourceLoaders();
        bean.addBean(ResourceLoaders.class, rl);
        {
            // @Deprecated
            rl.add((path, loader) -> {
                @Deprecated final List<Function<String, Collection<InputStream>>> loaders = ReadPropertiesAutoConfigs.resourceLoaders;
                for (@Deprecated Function<String, Collection<InputStream>> loader0 : loaders) {
                    final Collection<InputStream> streams = loader0.apply(path);
                    if (streams != null && !streams.isEmpty()) {
                        final Iterator<InputStream> iterator = streams.iterator();
                        InputStream ref = iterator.next();
                        while (iterator.hasNext()) {
                            try {
                                iterator.next().close();
                            } catch (IOException ignore) {
                            }
                        }
                        return ref;
                    }
                }
                return null;
            });
        }
        if (SharedConfigurationProcessor.DEBUG) {
            MXBukkitLib.getLogger().printf("[BeanManager] Beans loaded.");
        }
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
