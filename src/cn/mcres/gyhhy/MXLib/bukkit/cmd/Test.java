/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.cmd;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.bukkit.command.CommandSender;

@SubCommand(console = true, checkSupPer = false)
@Command
public class Test {

    @SubCommandHandle
    public static void justdoit(CommandSender sender,String ali,String[] argc) {
        System.out.println("Do it! " + sender);
        System.out.println("ALI: " + ali);
        System.out.println("ARGS: " + Arrays.toString(argc));
    }

    public static void main(String[] arg) throws Throwable {
        Method[] mtt = Test.class.getMethods();
        Method met = null;
        for (Method m : mtt) {
            if (m.getName().equals("justdoit")) {
                met = m;
            }
        }
        SubCommandEX sub = new SubCommandEX(Test.class, met, null);
        sub.exec(null, null, "alax", "awa aedajhx aweaufj".split(" "), null);
//        System.out.println(
//              new File(  Test.class.getResource("/org/bukkit/Server.class").toString().split("\\!")[0].replace("jar:file:",""))
//        );
        Manager.exec(Test.class);
    }
}
