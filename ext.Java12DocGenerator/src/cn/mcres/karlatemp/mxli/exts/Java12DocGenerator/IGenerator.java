/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: IGenerator.java@author: karlatemp@vip.qq.com: 2019/12/26 下午9:58@version: 2.0
 */

package cn.mcres.karlatemp.mxli.exts.Java12DocGenerator;

import cn.mcres.karlatemp.mxlib.util.ClassPathBuilder;

import java.util.function.Consumer;

public interface IGenerator {
    IGenerator project(String projectLocation);

    IGenerator addModule(String moduleName);

    default IGenerator addCustomArgs(String... args) {
        return addCustomArgs(w -> {
            for (String s : args) {
                w.force(s);
            }
        });
    }

    IGenerator addCustomArgs(Consumer<ClassPathBuilder> action);

    String getProjectType();
}
