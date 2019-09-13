/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.cmd;

import cn.mcres.gyhhy.MXLib.Core;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author 32798
 */
public class SubCommandMH extends SubCommandEX {

    private MethodHandle met;
    private static MethodHandle filterx = lk.findStatic(SubCommandMH.class, "filter", MethodType.methodType(boolean.class, Object.class));

    protected SubCommandMH() {
    }

    public SubCommandMH(Method met, Object thiz) {
        super(met, thiz);
    }

    public SubCommandMH(Class cl, Method met, Object thiz) {
        super(cl, met, thiz);
    }

    public SubCommandMH(MethodHandle mh, SubCommand sc) throws Throwable {
        this.met = make(mh, this);
        this.sc = sc;
        this.norem = sc.noRemoveFirstArg();
        this.loadTabCompleter(sc, null, met);
    }

    protected static boolean filter(Object returnValue) {
        if (returnValue instanceof Boolean) {
            return (Boolean) returnValue;
        }
        return true;
    }

    protected static MethodHandle filter(MethodHandle mh) {
        return MethodHandles.filterReturnValue(mh.asType(MethodType.methodType(Object.class, mh.type())), filterx);
    }

    protected static MethodHandle check(MethodType mt, MethodHandle mh, SubCommandMH mhx) throws Throwable {
        if (mt.parameterCount() > 6) {
            throw new IllegalAccessException("Method Type " + mt + " is out of area.");
        }
        boolean bad = false;
        boolean fCS = false, fLabel = false, fargs = false, fcmd = false, fexe = false;
        Class<?> CSC = null;
        // <editor-fold defaultstate="collapsed" desc="Arg test">
        for (Class<?> c : mt.parameterList()) {
            if (CommandSender.class.isAssignableFrom(c)) {
                if (fCS) {
                    bad = true;
                    break;
                }
                fCS = true;
                CSC = c;
            } else if (String.class == c) {
                if (fLabel) {
                    bad = true;
                    break;
                }
                fLabel = true;
            } else if (c == String[].class) {
                if (fargs) {
                    bad = true;
                    break;
                }
                fargs = true;
            } else if (c == Command.class) {
                if (fcmd) {
                    bad = true;
                    break;
                }
                fcmd = true;
            } else if (c == Executer.class) {
                if (fexe) {
                    bad = true;
                    break;
                }
                fexe = true;
            } else {
                bad = true;
                break;
            }
        }
        // </editor-fold>
        if (bad) {
            throw new IllegalArgumentException("Bad MethodType " + mt);
        }
        List<Class<?>> adds = new ArrayList<>();
        if (!fCS) {
            adds.add(CSC = CommandSender.class);
        }
        if (!fLabel) {
            adds.add(String.class);
        }
        if (!fargs) {
            adds.add(String[].class);
        }
        if (!fcmd) {
            adds.add(Command.class);
        }
        if (!fexe) {
            adds.add(Executer.class);
        }
        if (!adds.isEmpty()) {
            mh = MethodHandles.dropArguments(mh, 0, adds);
        }
        int[] ff = new int[5];
        MethodType mm = MethodType.methodType(mt.returnType(), CSC, Command.class, String.class, String[].class, Executer.class);
        mt = mh.type();
        if (CSC != null) {
            mhx.st = CSC.asSubclass(CommandSender.class);
        }
        if (mt.equals(mm)) {
            return mh;
        }
        Class<?>[] aaxrr = mt.parameterArray();
        Class<?>[] awxx = mm.parameterArray();
        // <editor-fold defaultstate="collapsed" desc="Arg sort">
        for (int i = 0; i < aaxrr.length; i++) {
            for (int j = 0; j < awxx.length; j++) {
                if (aaxrr[i] == awxx[j]) {
                    ff[i] = j;
                    break;
                }
            }
        }
        // </editor-fold>
        return MethodHandles.permuteArguments(mh, mm, ff);
    }

    protected static MethodHandle make(MethodHandle mh, SubCommandMH akkx) throws Throwable {
        return filter(check(mh.type(), mh, akkx));
    }

    @Override
    public boolean exec(CommandSender sender, Command cmd, String ali, String[] argc, Executer exev) {
        if (check(sender, exev)) {
            try {
                String[] temp;
                if (norem || argc.length == 0) {
                    temp = argc;
                } else {
                    temp = new String[argc.length - 1];
                    System.arraycopy(argc, 1, temp, 0, temp.length);
                }
                return (boolean) met.invoke(sender, cmd, ali, temp, exev);
            } catch (Throwable ex) {
                cat(ex);
            }
        }
        return true;
    }

    @Override
    protected void setup(Method met, Object thiz, SubCommand sc) {
//        System.out.println("MH Setup " + this);
        this.norem = sc.noRemoveFirstArg();
        try {
            MethodHandle mh = lk.unreflect(met);
//            System.out.println(this);
//            Core.getBL().printThreadInfo(Thread.currentThread(), true, true);
            if (!Modifier.isStatic(met.getModifiers())) {
                if (thiz == null) {
                    throw new NullPointerException();
                }
                mh = mh.bindTo(thiz);
            }
            this.met = make(mh, this);
        } catch (IllegalArgumentException | Error thrx) {
            throw thrx;
        } catch (Throwable thrx) {
            throw new IllegalArgumentException(thrx);
        }
        this.loadTabCompleter(sc, thiz, met);
//        System.out.println("Setup " + this + " " + this.met + " " + ctc);
    }

}
