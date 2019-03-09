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
            throw new java.lang.IllegalAccessError("FAQ");
        }
        iit = t;
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
                throw new java.lang.ClassCastException();
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
            if (this.contains(e)) {
                return false;
            }
            synchronized (this) {
                ClassFileTransformer[] nw = new ClassFileTransformer[list.length + 1];
                System.arraycopy(list, 0, nw, 0, list.length);
                nw[list.length] = e;
                list = nw;
            }
            try {
                System.out.println("[MXBukkitLib][HotAgent] Append Class File Transformer: " + e);
            } catch (Throwable t) {
                System.out.println("[MXBukkitLib][HotAgent] Append Class File Transformer: " + e.getClass().getName());
            }
            return true;
        }

        @Override
        @SuppressWarnings("element-type-mismatch")
        public boolean remove(Object o) {
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
                try {
                    System.out.println("[MXBukkitLib][HotAgent] Remove Class File Transformer: " + o);
                } catch (Throwable t) {
                    System.out.println("[MXBukkitLib][HotAgent] Remove Class File Transformer: " + o.getClass().getName());
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
            ix.addTransformer(transformer, canRetransform);
        }

        @Override
        public void addTransformer(ClassFileTransformer transformer) {
            cfts.add(transformer);
            ix.addTransformer(transformer);
        }

        @Override
        public boolean removeTransformer(ClassFileTransformer transformer) {
            cfts.remove(transformer);
            return ix.removeTransformer(transformer);
        }

        @Override
        public boolean isRetransformClassesSupported() {
            return ix.isRetransformClassesSupported();
        }

        @Override
        public void retransformClasses(Class<?>... classes) throws UnmodifiableClassException {
            ix.retransformClasses(classes);
        }

        @Override
        public boolean isRedefineClassesSupported() {
            return ix.isRedefineClassesSupported();
        }

        @Override
        public void redefineClasses(ClassDefinition... definitions) throws ClassNotFoundException, UnmodifiableClassException {
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
            ix.appendToBootstrapClassLoaderSearch(jarfile);
        }

        @Override
        public void appendToSystemClassLoaderSearch(JarFile jarfile) {
            ix.appendToSystemClassLoaderSearch(jarfile);
        }

        @Override
        public boolean isNativeMethodPrefixSupported() {
            return ix.isNativeMethodPrefixSupported();
        }

        @Override
        public void setNativeMethodPrefix(ClassFileTransformer transformer, String prefix) {
            cfts.add(transformer);
            ix.setNativeMethodPrefix(transformer, prefix);
        }
    }
    private static Instrumentation iit;
    public static SI si;

    public static void clearup() {
        synchronized (cfts) {
            for (ClassFileTransformer cft : cfts.toArray().clone()) {
                si.removeTransformer(cft);
            }
        }
    }

}
