/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Agent.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.models.agent;

import cn.mcres.gyhhy.MXLib.Core;
import cn.mcres.gyhhy.MXLib.models.Model;
import cn.mcres.gyhhy.MXLib.system.VMHelper;
import java.lang.instrument.Instrumentation;

public class Agent extends Model<Instrumentation> {

    static {
        registerModel("javaagent", new Agent());
    }

    public boolean isSupport() {
        return getInstance() != null;
    }

    @Override
    public Instrumentation getInstance() {
        return VMHelper.getHelper().getInstrumentation();
    }

    @Override
    protected String getVersion0() {
        return Core.getVersion();
    }

}
