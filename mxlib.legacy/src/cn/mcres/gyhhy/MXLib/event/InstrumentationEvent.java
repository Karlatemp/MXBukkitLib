/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: InstrumentationEvent.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.event;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;
import org.bukkit.event.HandlerList;

/**
 *
 * @author 32798
 */
public class InstrumentationEvent extends org.bukkit.event.Event implements C {

    private final Class<?> ce;

    private static class I extends InstrumentationEvent {

        private I(Instrumentation i, Class<?> caller) {
            super(i, caller);

        }
    }

    public static class GetAllLoadedClasses extends I {

        public GetAllLoadedClasses(Instrumentation ix, Class<?> caller) {
            super(ix, caller);
        }
    }

    public static class SetNativeMethodPrefix extends I {

        private final ClassFileTransformer cft;
        private final String p;

        public SetNativeMethodPrefix(Instrumentation i, Class<?> caller, ClassFileTransformer transformer, String prefix) {
            super(i, caller);
            this.cft = transformer;
            this.p = prefix;
        }

        public ClassFileTransformer getTransformer() {
            return cft;
        }

        public String getPrefix() {
            return p;
        }
    }

    public static class AppendToSystemClassLoaderSearch extends I {

        private final JarFile jf;

        public JarFile getFile() {
            return jf;
        }

        public AppendToSystemClassLoaderSearch(Instrumentation i, Class<?> caller, JarFile jar) {
            super(i, caller);
            this.jf = jar;
        }
    }

    public static class AppendToBootstrapClassLoaderSearch extends I {

        private final JarFile jf;

        public AppendToBootstrapClassLoaderSearch(Instrumentation i, Class<?> caller, JarFile jar) {
            super(i, caller);
            this.jf = jar;
        }

        public JarFile getFile() {
            return jf;
        }
    }

    public static class GetInitiatedClasses extends I {

        private final ClassLoader cl;

        public GetInitiatedClasses(Instrumentation i, Class<?> caller, ClassLoader cl) {
            super(i, caller);
            this.cl = cl;
        }

        public ClassLoader getClassLoader() {
            return cl;
        }

    }

    public static class RedefineClasses extends I {

        private final ClassDefinition[] cs;

        public RedefineClasses(Instrumentation i, Class<?> caller, ClassDefinition definitions[]) {
            super(i, caller);
            this.cs = definitions;
        }

        public ClassDefinition[] getClasses() {
            return cs.clone();
        }
    }

    public static class RetransformClasses extends I {

        private final Class<?>[] cs;

        public RetransformClasses(Instrumentation i, Class<?> caller, Class<?> classes[]) {
            super(i, caller);
            this.cs = classes;
        }

        public Class<?>[] getClasses() {
            return cs.clone();
        }
    }

    public static class RemoveTransformer extends I {

        private final ClassFileTransformer cft;

        public RemoveTransformer(Instrumentation i, Class<?> caller, ClassFileTransformer transformer) {
            super(i, caller);
            this.cft = transformer;
        }

        public ClassFileTransformer getClassFileTransformer() {
            return cft;
        }
    }

    public static class AddTransformer extends I {

        private final boolean crf;
        private final ClassFileTransformer cft;

        public AddTransformer(Instrumentation i, Class<?> caller, ClassFileTransformer transformer, boolean canRetransform) {
            super(i, caller);
            this.cft = transformer;
            this.crf = canRetransform;
        }

        public ClassFileTransformer getClassFileTransformer() {
            return cft;
        }

        public boolean canRetransform() {
            return crf;
        }
    }
    private static final HandlerList list = new HandlerList();
    private final Instrumentation i;

    public InstrumentationEvent(Instrumentation i, Class<?> caller) {
        this.i = i;
        this.ce = caller;
    }

    public Class<?> getCaller() {
        return ce;
    }

    public Instrumentation getInstrumentation() {
        return i;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public static HandlerList getHandlerList() {
        return list;
    }
    private boolean c;

    @Override
    public void setCancelled(boolean c) {
        this.c = c;
    }

    @Override
    public boolean isCancelled() {
        return c;
    }

}
