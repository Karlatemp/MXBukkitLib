/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: InstrumentationWrap9.java@author: karlatemp@vip.qq.com: 19-9-12 下午1:48@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.instrumentation;

import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InstrumentationWrap9 extends InstrumentationWrap implements Instrumentation {
    InstrumentationWrap9(Instrumentation p) {
        super(p);
    }

    public void redefineModule(
            Module module,
            Set<Module> extraReads,
            Map<String, Set<Module>> extraExports,
            Map<String, Set<Module>> extraOpens,
            Set<Class<?>> extraUses,
            Map<Class<?>, List<Class<?>>> extraProvides) {
        p.redefineModule(module, extraReads, extraExports, extraOpens, extraUses, extraProvides);
    }

    public boolean isModifiableModule(Module module) {
        return p.isModifiableModule(module);
    }
}
