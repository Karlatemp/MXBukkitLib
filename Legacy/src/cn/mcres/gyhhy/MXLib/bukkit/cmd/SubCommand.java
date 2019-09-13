/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.cmd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {
//    String name() default "";

    String name() default "";

    String permission() default "";

    String desc() default "";

    boolean console() default true;

    boolean checkSupPer() default true;

    boolean noRemoveFirstArg() default false;

    CommandCreateType create() default CommandCreateType.UseMethodHandle;

    Class<? extends CommandTabCompleter> tab() default CommandTabCompleter.class;

    public static enum CommandCreateType {
        /**
         * Using {@link java.lang.reflect.Method} to invoke method
         */
        UseReflectMethod,
        /**
         * Using {@link java.lang.invoke.MethodHandle} to invoke method
         */
        UseMethodHandle;
    }
}
