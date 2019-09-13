/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.cmd;

import cn.mcres.gyhhy.MXLib.ThrowHelper;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MethodCommandTabCompleter implements CommandTabCompleter, Kit {

    private static final MethodType methodtype = MethodType.methodType(void.class,
            CommandSender.class, Command.class, String.class, String[].class, SubCommandEX.class, List.class
    );

    public static Void check(MethodHandle target) {
        if (!target.type().equals(methodtype)) {
            throw new WrongMethodTypeException("cannot convert " + target + " to " + methodtype);
        }
        return Kit.voids();
    }

    public static CommandTabCompleter create(Class<?> c, Method m, Object t) {
        MethodHandle mtt;
        if (!Modifier.isStatic(m.getModifiers())) {
            if (t == null) {
                throw new NullPointerException();
            }
            mtt = lk.unreflect(m).bindTo(t);
        } else {
            mtt = lk.unreflect(m);
        }

        return new MethodCommandTabCompleter(mtt, check(mtt));
    }
    private final MethodHandle m;

    public MethodCommandTabCompleter(MethodHandle mtt) {
        this(mtt, check(mtt));
    }

    private MethodCommandTabCompleter(MethodHandle mtt, Void unused) {
        this.m = mtt;
    }

    @Override
    public void onTabComplete(CommandSender cs, Command cmnd, String string, String[] args, SubCommandEX subcommand, List<String> completes) {
        try {
            m.invoke(cs, cmnd, string, args, subcommand, completes);
        } catch (Throwable ex) {
            ThrowHelper.getDefault().thr(ex);
        }
    }

}
