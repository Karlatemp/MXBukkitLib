/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Test.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.cmd;

import java.util.Arrays;
import org.bukkit.command.CommandSender;
import cn.mcres.gyhhy.MXLib.ext.lookup.Looker;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

@Command
public class Test {

    @SubCommand
    @SubCommandHandle
    public static void justdoit(CommandSender sender, String ali, String[] argc) {
        System.out.println("Do it! " + sender);
        System.out.println("ALI: " + ali);
        System.out.println("ARGS: " + Arrays.toString(argc));
    }

    public static void a() {

    }

    public static boolean filter(Object any) {
        System.out.println(any);
        if (any instanceof Boolean) {
            return (Boolean) any;
        }
        return true;
    }
    static Looker lk = new Looker(Test.class, ~0);

    public static void main(String[] arg) throws Throwable {
        MethodHandle got = lk.findStatic(Test.class, "a", MethodType.methodType(void.class));
        MethodHandle filter = lk.findStatic(Test.class, "filter", MethodType.methodType(boolean.class, Object.class));
        System.out.println(got);
        System.out.println(filter);
        MethodHandle make = MethodHandles.filterReturnValue(got.asType(MethodType.methodType(Object.class, got.type())), filter);

        System.out.println(make);
        System.out.println(make.invoke());

        MethodHandle justdoit = lk.findStatic(Test.class, "justdoit", MethodType.methodType(void.class,
                CommandSender.class, String.class, String[].class));
        MethodType mt = MethodType.methodType(justdoit.type().returnType(),
                String[].class, CommandSender.class, String.class);

        System.out.println(
                MethodHandles.permuteArguments(justdoit, mt, 1, 2, 0)
        );
        SubCommandMH command = new SubCommandMH(
                Test.class.getMethod("justdoit", CommandSender.class, String.class, String[].class), null);
        command.exec(null, null, "test", new String[]{"ARGX", "ARG1"}, null);
        justdoit.invoke();

    }
}
