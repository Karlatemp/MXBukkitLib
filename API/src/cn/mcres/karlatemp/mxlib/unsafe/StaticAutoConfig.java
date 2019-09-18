/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: StaticAutoConfig.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.unsafe;

import cn.mcres.karlatemp.mxlib.annotations.Bean;
import cn.mcres.karlatemp.mxlib.annotations.Configuration;
import cn.mcres.karlatemp.mxlib.tools.CollectionsJsonWriter;
import cn.mcres.karlatemp.mxlib.tools.GsonJsonWriter;
import cn.mcres.karlatemp.mxlib.tools.IJsonWriter;

@Configuration
public class StaticAutoConfig {
    @Bean
    public IJsonWriter jsonWriter() {
        //noinspection CatchMayIgnoreException
        try {
            return new GsonJsonWriter();
        } catch (Throwable thr) {
        }
        return new CollectionsJsonWriter();
    }
}
