/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SystemHelper.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.system;

import java.lang.management.ManagementFactory;

public class SystemHelper {

    public static final int pid;
    public static final String pid_;
    /**
     * Computer's name
     */
    public static final String cn;

    public static String getJVMPidAsString() {
        return pid_;
    }

    public static int getJVMPid() {
        return pid;
    }
    public static final boolean supportVirtualMachine;

    public static boolean isSupportVM() {
        return supportVirtualMachine;
    }

    public static String getComputerName() {
        return cn;
    }

    static {
        boolean bool = false;
        try {
            Class.forName("com.sun.tools.attach.VirtualMachine");
            bool = true;
        } catch (Exception ex) {
        }
        supportVirtualMachine = bool;
        String name = ManagementFactory.getRuntimeMXBean().getName();
        int ind = name.indexOf('@');
        pid_ = name.substring(0, ind);
        cn = name.substring(ind + 1);
        try {
            ind = Integer.parseInt(pid_);
        } catch (NumberFormatException ex) {
        }
        pid = ind;
    }

    public static void main(String[] argc) throws Exception {
    }
}
