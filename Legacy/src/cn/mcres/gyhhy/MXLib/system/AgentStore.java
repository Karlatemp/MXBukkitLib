/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.system;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarFile;

/**
 *
 * @author 32798
 */
public class AgentStore {

    final static OS cfts = new OS();

    public static void setiit(Instrumentation t) {
        if (iit != null) {
            throw new IllegalAccessError("FAQ");
        }
        iit = t;
    }
    public static void setSI(SI six){
        if (si != null) {
            throw new IllegalAccessError("FAQ");
        }
        si=six;
    }
    static class OS implements Set<ClassFileTransformer> {

        ClassFileTransformer[] list = new ClassFileTransformer[0];

        @Override
        public int size() {
            return list.length;
        }

        @Override
        public boolean isEmpty() {
            return list == null || list.length == 0;
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof ClassFileTransformer)) {
                throw new ClassCastException();
            }
            for (ClassFileTransformer c : list) {
                if (c == o) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Iterator<ClassFileTransformer> iterator() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ClassFileTransformer[] toArray() {
            return list;
        }

        @Override
        public <T> T[] toArray(T[] a) {
            System.arraycopy(list, 0, a, 0, Math.min(a.length, list.length));
            return a;
        }

        @Override
        public boolean add(ClassFileTransformer e) {
            Objects.requireNonNull(e);
            if (this.contains(e)) {
                return false;
            }
            synchronized (this) {
                ClassFileTransformer[] nw = new ClassFileTransformer[list.length + 1];
                System.arraycopy(list, 0, nw, 0, list.length);
                nw[list.length] = e;
                list = nw;
            }
            return true;
        }

        @Override
        @SuppressWarnings("element-type-mismatch")
        public boolean remove(Object o) {
            Objects.requireNonNull(o);
            if (this.contains(o)) {
                synchronized (this) {
                    int ind = -1;
                    for (int i = 0; i < list.length; i++) {
                        ClassFileTransformer c = list[i];
                        if (c == o) {
                            ind = i;
                            break;
                        }
                    }
                    ClassFileTransformer[] nw = new ClassFileTransformer[list.length - 1];
                    System.arraycopy(list, 0, nw, 0, ind);
                    System.arraycopy(list, ind + 1, nw, ind, nw.length - ind);
                    list = nw;
                }
                return true;
            }
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean addAll(Collection<? extends ClassFileTransformer> c) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void clear() {
            list = new ClassFileTransformer[0];
        }
    }

    public static class SI implements Instrumentation {

        final Instrumentation ix;

        public SI(Instrumentation ix) {
            this.ix = ix;
        }

        @Override
        public void addTransformer(ClassFileTransformer transformer, boolean canRetransform) {
            cfts.add(transformer);
            try {
                System.out.println("[MXBukkitLib][HotAgent] Append Class File Transformer: " + transformer + ", canRetransform = " + canRetransform);
            } catch (Throwable t) {
                System.out.println("[MXBukkitLib][HotAgent] Append Class File Transformer: " + transformer.getClass().getName() + ", canRetransform = " + canRetransform);
            }
            ix.addTransformer(transformer, canRetransform);
        }

        @Override
        public void addTransformer(ClassFileTransformer transformer) {
            cfts.add(transformer);
            try {
                System.out.println("[MXBukkitLib][HotAgent] Append Class File Transformer: " + transformer);
            } catch (Throwable t) {
                System.out.println("[MXBukkitLib][HotAgent] Append Class File Transformer: " + transformer.getClass().getName());
            }
            ix.addTransformer(transformer);
        }

        @Override
        public boolean removeTransformer(ClassFileTransformer transformer) {
            cfts.remove(transformer);
            try {
                System.out.println("[MXBukkitLib][HotAgent] Remove Class File Transformer: " + transformer);
            } catch (Throwable t) {
                System.out.println("[MXBukkitLib][HotAgent] Remove Class File Transformer: " + transformer.getClass().getName());
            }
            return ix.removeTransformer(transformer);
        }

        @Override
        public boolean isRetransformClassesSupported() {
            return ix.isRetransformClassesSupported();
        }

        @Override
        public void retransformClasses(Class<?>... classes) throws UnmodifiableClassException {
            for (Class<?> c : classes) {
                System.out.println("[MXBukkitLib][HotAgent] Retransform Classes: " + c);
            }
            ix.retransformClasses(classes);
        }

        @Override
        public boolean isRedefineClassesSupported() {
            return ix.isRedefineClassesSupported();
        }

        @Override
        public void redefineClasses(ClassDefinition... definitions) throws ClassNotFoundException, UnmodifiableClassException {
            for (ClassDefinition c : definitions) {
                System.out.println("[MXBukkitLib][HotAgent] Redefine Classes: " + c);
            }
            ix.redefineClasses(definitions);
        }

        @Override
        public boolean isModifiableClass(Class<?> theClass) {
            return ix.isModifiableClass(theClass);
        }

        @Override
        public Class[] getAllLoadedClasses() {
            return ix.getAllLoadedClasses();
        }

        @Override
        public Class[] getInitiatedClasses(ClassLoader loader) {
            return ix.getInitiatedClasses(loader);
        }

        @Override
        public long getObjectSize(Object objectToSize) {
            return ix.getObjectSize(objectToSize);
        }

        @Override
        public void appendToBootstrapClassLoaderSearch(JarFile jarfile) {
            System.out.println("[MXBukkitLib][HotAgent] Append " + jarfile.getName() + " to Bootstarp Class Loader search");
            ix.appendToBootstrapClassLoaderSearch(jarfile);
        }

        @Override
        public void appendToSystemClassLoaderSearch(JarFile jarfile) {
            System.out.println("[MXBukkitLib][HotAgent] Append " + jarfile.getName() + " to System Class Loader search");
            ix.appendToSystemClassLoaderSearch(jarfile);
        }

        @Override
        public boolean isNativeMethodPrefixSupported() {
            return ix.isNativeMethodPrefixSupported();
        }

        @Override
        public void setNativeMethodPrefix(ClassFileTransformer transformer, String prefix) {
            cfts.add(transformer);
            try {
                System.out.println("[MXBukkitLib][HotAgent] SetNativeMethodPrefix: " + transformer + ", prefix=" + prefix);
            } catch (Throwable t) {
                System.out.println("[MXBukkitLib][HotAgent] SetNativeMethodPrefix: " + transformer.getClass().getName() + ", prefix=" + prefix);
            }
            ix.setNativeMethodPrefix(transformer, prefix);
        }
    }
    private static Instrumentation iit;
    static SI si;

    public static void clearup() {
        synchronized (cfts) {
            for (ClassFileTransformer cft : cfts.toArray().clone()) {
                si.removeTransformer(cft);
            }
        }
    }

}
