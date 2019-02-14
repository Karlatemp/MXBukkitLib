/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.cmd;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author 32798
 */
public class SubCommandEX {
    SubCommandEX(){}
    
    private boolean empc = false;
    private boolean puexec = false;
    private boolean req = false;
    private boolean reqx = false;
    private boolean norem = false;

    private int ali = -1;
    private int send = -1;
    private int argc = -1;

    private Class<? extends CommandSender> st;
    SubCommand sc;
    private Object thiz;
    private Method exec;

    public SubCommand getSC() {
        return sc;
    }

    private void setup(Method met) {
//        this.thiz = thiz;
        this.norem = sc.noRemoveFirstArg();
        puexec = Modifier.isPublic(met.getModifiers());
        if (!Modifier.isStatic(met.getModifiers()) && thiz == null) {
            throw new java.lang.NullPointerException("No \"this\" object and method isnot static method.");
        }
        this.exec = met;
        Class<?> response = met.getReturnType();
        if (response == Boolean.class || response == boolean.class) {
            req = true;
            if (response == boolean.class) {
                reqx = true;
            }
        }
        switch (met.getParameterCount()) {
            case 0: {
                empc = true;
                break;
            }
            case 1: {
                Class<?> c = met.getParameterTypes()[0];
                if (CommandSender.class.isAssignableFrom(c)) {
                    st = c.asSubclass(CommandSender.class);
                    send = 0;
                } else {
                    throw new java.lang.IllegalArgumentException("Method parameter type is not extends with CommandSender.");
                }
                break;
            }
            case 2:
            case 3: {
                Class<?>[] types = met.getParameterTypes();
                for (int i = 0; i < types.length; i++) {
                    Class<?> c = types[i];
                    if (CommandSender.class.isAssignableFrom(c)) {
                        if (send != -1) {
                            throw new java.lang.IllegalArgumentException("Unknown parameter type.");
                        }
                        st = c.asSubclass(CommandSender.class);
                        send = i;
                    } else if (c == String.class) {
                        if (ali != -1) {
                            throw new java.lang.IllegalArgumentException("Unknown parameter type.");
                        }
                        ali = i;
                    } else if (c == String[].class) {
                        if (argc != -1) {
                            throw new java.lang.IllegalArgumentException("Unknown parameter type.");
                        }
                        argc = i;
                    } else {
                        throw new java.lang.IllegalArgumentException("Unknown parameter type.");
                    }
                }
                break;
            }
            default: {
                throw new java.lang.IllegalArgumentException("Unknown parameter type. (Out of count)");
            }
        }
    }

    public SubCommandEX(Method met, Object thiz) {
        if (met == null) {
            throw new java.lang.NullPointerException("Method cannot be null.");
        }
        Annotation sx = met.getAnnotation(SubCommand.class);
        if (sx == null) {
            sx = met.getDeclaringClass().getAnnotation(SubCommand.class);
        }
        if (sx == null) {
            throw new java.lang.NullPointerException(met + " No SubCommand Annotation.");
        }
        SubCommand sb = (SubCommand) sx;
        this.sc = sb;
        this.thiz = thiz;
        setup(met);
    }

    public SubCommandEX(Class cl, Method met, Object thiz) {
        if (cl == null || met == null) {
            throw new java.lang.NullPointerException("Class or Method cannot be null.");
        }
        Annotation sx = cl.getAnnotation(SubCommand.class);
        if (sx == null) {
            throw new java.lang.NullPointerException(cl + " No SubCommand Annotation.");
        }
        SubCommand sb = (SubCommand) sx;
        this.sc = sb;
        this.thiz = thiz;
        setup(met);
    }

    public boolean exec(CommandSender sender, Command cmd, String ali, String[] argc, Executer exev) {
        if (check(sender, exev)) {
            if (empc) {
                try {
                    Object rq;
                    if (puexec) {
                        rq = exec.invoke(thiz);
                    } else {
                        boolean od = exec.isAccessible();
                        exec.setAccessible(true);
                        rq = exec.invoke(thiz);
                        exec.setAccessible(od);
                    }
                    if (reqx) {
                        return (boolean) rq;
                    }
                    if (req) {
                        return (Boolean) rq;
                    }
                } catch (Throwable ex) {
                    cat(ex);
                }
            } else {
                Object[] argv = new Object[exec.getParameterCount()];
                if (this.ali != -1) {
                    argv[this.ali] = ali;
                }
                if (this.argc != -1) {
                    String[] temp;
                    if (norem || argc.length == 0) {
                        temp = argc;
                    } else {
                        temp = new String[argc.length - 1];
                        System.arraycopy(argc, 1, temp, 0, temp.length);
                    }
                    argv[this.argc] = temp;
                }
                if (this.send != -1) {
                    argv[this.send] = sender;
                }

                try {
                    Object rq;
                    if (puexec) {
                        rq = exec.invoke(thiz, argv);
                    } else {
                        boolean od = exec.isAccessible();
                        exec.setAccessible(true);
                        rq = exec.invoke(thiz, argv);
                        exec.setAccessible(od);
                    }
                    if (reqx) {
                        return (boolean) rq;
                    }
                    if (req) {
                        return (Boolean) rq;
                    }
                } catch (Throwable ex) {
                    cat(ex);
                }
            }
        }

        return true;
    }
    public String msg$console_deny = "\u00a7cOnly player can use this command.",
            msg$permission_deny = "\u00a7cYou don't have the permission to do that.",
            msg$no_type_sender = "\u00a7cYou cannot execute this command.";
    protected boolean check(CommandSender sender, Executer exev) {
        if (sc.checkSupPer()) {
            if (!exev.check(sender)) {
                return false;
            }
        }
        if (!sc.console()) {
            if (!(sc instanceof Player)) {
                sender.sendMessage(msg$console_deny);
                return false;
            }
        }
        String perm = sc.permission();
        if (perm == null || perm.isEmpty()) {
        } else {
            if (!sender.hasPermission(perm)) {
                sender.sendMessage(msg$permission_deny);
                return false;
            }
        }
        if (st != null && sender != null) {
            if (!st.isInstance(sender)) {
                sender.sendMessage(msg$no_type_sender);
                return false;
            }
        }
        return true;
    }

    private void cat(Throwable ex) {
        if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        }
        if (ex instanceof Error) {
            throw (Error) ex;
        }
        throw new RuntimeException(ex);
    }

}
