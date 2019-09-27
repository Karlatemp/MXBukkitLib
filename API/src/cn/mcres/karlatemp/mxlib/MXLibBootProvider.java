/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MXLibBootProvider.java@author: karlatemp@vip.qq.com: 19-9-26 下午2:01@version: 2.0
 */

package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.bean.IBeanManager;

/**
 * The provider.
 * <p>
 * MXLib使用Provider处理核心加载, 如果你需要加载自己的BeanManager, 你需要吧Priority调小<br>
 * MXLib uses the Provider to handle core loading. If you need to load your own BeanManager, you need to adjust the Priority.
 * </p>
 * <p>
 * MXLib 将会根据优先级大小从小到大执行{@link #boot()}方法<br>
 * MXLib will execute the {@link #boot()} method from small to large according to the priority size.
 * </p>
 * <p>
 * 如果你设置了 "mxlib.provider", 这个属性对应的类将会被最先执行, 忽略优先级 <br>
 * If you set the system property "mxlib.provider", The property's class will invoke at first. No Priority
 * See {@link System#getProperty(String)}
 * </p>
 * <p>
 * 以及其他的Provider使用{@link java.util.ServiceLoader} 加载<br>
 * And other's provider will use {@link java.util.ServiceLoader} to load.
 * </p>
 * <p>
 * 如果你需要覆盖掉MXLib的默认BeanManager, 你需要把优先级调成比 1 小的值<br>
 * If you need to override MXLib's default BeanManager, you need to set the priority to a value less than 1.
 * </p>
 * <p>
 * 只能在{@link #setBeanManager()}处调用 {@link MXBukkitLib#setBeanManager(IBeanManager)}<br/>
 * Only invoke {@link MXBukkitLib#setBeanManager(IBeanManager)} in {@link #setBeanManager()}
 * </p>
 *
 * @see java.util.ServiceLoader
 * @see System#getProperty(String)
 * @see MXBukkitLib#setBeanManager(IBeanManager)
 * @since 2.2
 */
public interface MXLibBootProvider {
    /**
     * 在MXLib需要设置BeanManager时调用
     * Invoke when MXLib need a bean manager.(BeanManager == null)
     */
    default void setBeanManager() {
    }

    /**
     * 在这里添加Bean
     * Add beans here
     */
    void boot();

    default int getPriority() {
        return 5;
    }

    static <T> int getPrioritySafe(T t) {
        if (t instanceof MXLibBootProvider) return ((MXLibBootProvider) t).getPriority();
        return 0;
    }
}
