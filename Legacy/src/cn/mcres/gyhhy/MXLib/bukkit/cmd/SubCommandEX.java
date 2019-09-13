/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.cmd;

import cn.mcres.gyhhy.MXLib.Core;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author 32798
 */
public class SubCommandEX implements Translator, Kit, CommandTabCompleter {

    private LanguageTranslator lt;

    protected boolean empc = false;
    protected boolean puexec = false;
    protected boolean req = false;
    protected boolean reqx = false;
    protected boolean norem = false;

    private int ali = -1;
    private int send = -1;
    private int argc = -1;

    protected Class<? extends CommandSender> st;
    SubCommand sc;
    protected Object thiz;
    protected Method exec;
    public String msg$console_deny = "\u00a7cOnly player can use this command.";
    public String msg$permission_deny = "\u00a7cYou don't have the permission to do that.";
    public String msg$no_type_sender = "\u00a7cYou cannot execute this command.";

    protected SubCommandEX() {
    }

    public SubCommandEX(Method met, Object thiz) {
        SubCommand sb = checkup(null, met, false);
        this.sc = sb;
        this.thiz = thiz;
        setup(met, thiz, sb);
    }

    protected SubCommand checkup(Class<?> cl, Method met, boolean cclass) {
        if (met == null) {
            throw new NullPointerException("Method cannot be null.");
        }
        if (cclass && cl == null) {
            throw new NullPointerException("Method cannot be null.");
        }
        SubCommand sx;
        if (cclass) {
            sx = cl.getAnnotation(SubCommand.class);
        } else {
            sx = met.getAnnotation(SubCommand.class);
        }
        if (sx == null) {
            throw new NullPointerException(cl + " No SubCommand Annotation.");
        }
        return sx;
    }
    protected Class<?> declareClass;

    public SubCommandEX(Class cl, Method met, Object thiz) {
        SubCommand sb = checkup(cl, met, true);
        this.sc = sb;
        this.thiz = thiz;
        this.declareClass = cl;
        setup(met, thiz, sb);
    }

    public void setLanguageTranslator(LanguageTranslator lt) {
        Objects.requireNonNull(lt);
        this.lt = lt;
    }

    public LanguageTranslator getLanguageTranslator() {
        if (lt == null) {
            lt = LanguageTranslator.getDefault();
        }
        return lt;
    }

    public SubCommand getSC() {
        return sc;
    }

    protected void setup(Method met, Object thiz, SubCommand sb) {
//        this.thiz = thiz;
        this.norem = sc.noRemoveFirstArg();
        puexec = Modifier.isPublic(met.getModifiers());
        if (!Modifier.isStatic(met.getModifiers()) && thiz == null) {
            throw new NullPointerException("No \"this\" object and method isnot static method.");
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
                    throw new IllegalArgumentException("Method parameter type is not extends with CommandSender.");
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
                            throw new IllegalArgumentException("Unknown parameter type.");
                        }
                        st = c.asSubclass(CommandSender.class);
                        send = i;
                    } else if (c == String.class) {
                        if (ali != -1) {
                            throw new IllegalArgumentException("Unknown parameter type.");
                        }
                        ali = i;
                    } else if (c == String[].class) {
                        if (argc != -1) {
                            throw new IllegalArgumentException("Unknown parameter type.");
                        }
                        argc = i;
                    } else {
                        throw new IllegalArgumentException("Unknown parameter type.");
                    }
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown parameter type. (Out of count)");
            }
        }
        loadTabCompleter(sb, thiz, met);
    }

    protected void loadTabCompleter(SubCommand sb, Object thiz, Object met) {
        Class<? extends CommandTabCompleter> wwwwx = sb.tab();
        if (wwwwx != CommandTabCompleter.class) {
            try {
                ctc = wwwwx.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                Core.getBL().error("Error in appending tab completer in registering sub-command " + met + ".").printStackTrace(ex);
            }
        }
        if (ctc == null) {
            if (thiz instanceof CommandTabCompleter) {
                ctc = (CommandTabCompleter) thiz;
            }
        }
        if (ctc == null) {
            if (declareClass != null) {
                loadTabCompleterFromClass(declareClass, thiz);
            }
        }
    }

    protected void loadTabCompleterFromClass(Class<?> clazz, Object thiz) {
        Method[] mets = clazz.getDeclaredMethods();
        Method main = null;
        for (Method met : mets) {
            if (met.isAnnotationPresent(SubCommandTabCompleter.class)) {
                main = met;
                break;
            }
        }
        if (main != null) {
            ctc = createTabCompleterFromMethod(clazz, main, thiz);
        }
    }

    protected CommandTabCompleter createTabCompleterFromMethod(Class<?> c, Method m, Object t) {
        return MethodCommandTabCompleter.create(c, m, t);
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

    protected boolean check(CommandSender sender, Executer exev) {
        if (sc.checkSupPer()) {
            if (exev != null) {
                if (!exev.check(sender)) {
                    return false;
                }
            }
        }
        if (!sc.console()) {
            if (!(sender instanceof Player)) {
                this.getLanguageTranslator().consoleDeny(exev, this, sender);
                return false;
            }
        }
        String perm = sc.permission();
        if (perm == null || perm.isEmpty()) {
        } else {
            if (!sender.hasPermission(perm)) {
                this.getLanguageTranslator().noPermission(exev, this, sender, perm);
                return false;
            }
        }
        if (st != null && sender != null) {
            if (!st.isInstance(sender)) {
                this.getLanguageTranslator().senderTypeError(exev, this, sender, st);
                return false;
            }
        }
        return true;
    }

    protected void cat(Throwable ex) {
        if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        }
        if (ex instanceof Error) {
            throw (Error) ex;
        }
        throw new RuntimeException(ex);
    }
    protected CommandTabCompleter ctc;

    @Override
    public void onTabComplete(CommandSender cs, Command cmnd, String string, String[] args, SubCommandEX subcommand, List<String> completes) {
        if (ctc != null && ctc != this) {
            ctc.onTabComplete(cs, cmnd, string, args, subcommand, completes);
        }
    }

}
