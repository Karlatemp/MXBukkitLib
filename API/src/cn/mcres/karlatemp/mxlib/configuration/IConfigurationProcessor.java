/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: IConfigurationProcessor.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.configuration;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IConfigurationProcessor {
    /**
     * The global matcher.
     */
    ConfigurationProcessorPostLoadingMatcher GLOBAL = new ConfigurationProcessorPostLoadingMatcher();

    void load(Class boot);

    /**
     * Get the matcher of this processor.
     * Should check is not GLOBAL matcher.
     *
     * @return The matcher
     * @see #GLOBAL
     * @since 2.2
     */
    @NotNull
    @Contract(pure = true)
    default ConfigurationProcessorPostLoadingMatcher getMatcher() {
        return GLOBAL;
    }
}
