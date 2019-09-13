/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.models.javassist;

import cn.mcres.gyhhy.MXLib.RefUtil;
import cn.mcres.gyhhy.MXLib.ThrowHelper;
import cn.mcres.gyhhy.MXLib.models.Model;
import java.lang.invoke.MethodHandle;
import javassist.CtClass;
import cn.mcres.gyhhy.MXLib.ext.lookup.Looker;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaSsist extends Model<Void> {

    public static final boolean IS_SUPPORT = RefUtil.classExists("javassist.CtClass", true, JavaSsist.class.getClassLoader());
    public static final MethodHandle getVersion;

    static {
        if (IS_SUPPORT) {
            getVersion = new Looker(Looker.openLookup(JavaSsist.class, ~0)).findStaticGetter(CtClass.class, "version", String.class);
        } else {
            getVersion = null;
        }
    }

    public static void main(String[] args) {
        System.out.println(new JavaSsist().getVersion());
    }

    @Override
    public Void getInstance() {
        return null;
    }

    @Override
    protected String getVersion0() {
        if (getVersion == null) {
            return null;
        }
        try {
            return (String) getVersion.invoke();
        } catch (Throwable ex) {
            return ThrowHelper.getInstance().thr(ex);
        }
    }

}
