/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: JarEdit.java@author: karlatemp@vip.qq.com: 2019/12/25 下午2:04@version: 2.0
 */

package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.reflect.WrappedClassImplements;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import cn.mcres.karlatemp.mxlib.util.IteratorSupplier;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class JarEdit {
    private static final ConcurrentLinkedQueue<File> temps = new ConcurrentLinkedQueue<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (File f : temps) {
                f.delete();
            }
        }));
    }


    public static void mk(File f, File output, Predicate<String> filter) throws IOException {
        File tmp = new File("out/temp." + f.getName());
        new File(tmp, "..").mkdirs();
        tmp.createNewFile();
        temps.add(tmp);
        ZipFile zip = new ZipFile(f);
        class CMember {
            boolean isInterface;
            String name, desc, owner;
            String openName, openSetName, openGetName;
            boolean setExist, getExist;
            boolean isStatic;
            String wrappedConsDesc;

            CMember(String name, String desc, String owner) {
                this.name = name;
                this.desc = desc;
                this.owner = owner;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                CMember cMember = (CMember) o;
                return Objects.equals(name, cMember.name) &&
                        Objects.equals(desc, cMember.desc) &&
                        Objects.equals(owner, cMember.owner);
            }

            @Override
            public int hashCode() {
                return Objects.hash(name, desc, owner);
            }
        }
        class CAccess {
            Deque<CMember> fields = new ConcurrentLinkedDeque<>();
            Deque<CMember> methods = new ConcurrentLinkedDeque<>();

            CMember find(String name, String desc, String owner, boolean isField) {
                var deque = isField ? fields : methods;
                for (var x : deque) {
                    if (x.desc.equals(desc) && x.name.equals(name) && x.owner.equals(owner)) return x;
                }
                return null;
            }
        }
        var privates = new HashMap<String, CAccess>();
        var access = new HashMap<String, ClassNode>();
        var allFieldAccess = new HashSet<CMember>();
        var allMethodAccess = new HashSet<CMember>();

        var nodes = new HashMap<String, ClassNode>();
        for (var entry : new IteratorSupplier<>(zip.entries().asIterator())) {
            if (entry.getName().endsWith(".class") && filter.test(entry.getName())) {
                try (var is = zip.getInputStream(entry)) {
                    ClassReader cr = new ClassReader(is);
                    var node = new ClassNode();
                    cr.accept(node, 0);
                    nodes.put(node.name, node);
                    var ca = new CAccess();
                    privates.put(node.name, ca);
                    if (node.methods != null) for (var method : node.methods) {
                        if (method.name.equals("<init>")) {
                            method.access &= ~Opcodes.ACC_PRIVATE;
                            continue;
                        }
                        if ((method.access & Opcodes.ACC_PRIVATE) != 0) {
                            var method0 = new CMember(method.name, method.desc, node.name);
                            method0.isStatic = (method.access & Opcodes.ACC_STATIC) != 0;
                            if ((node.access & Opcodes.ACC_INTERFACE) != 0) {
                                if ((method.access & Opcodes.ACC_ABSTRACT) != 0) {
                                    method0.isInterface = true;
                                }
                            }
                            ca.methods.add(method0);
                        }
                    }
                    if (node.fields != null) for (var field : node.fields) {
                        if ((field.access & Opcodes.ACC_PRIVATE) != 0) {
                            var field0 = new CMember(field.name, field.desc, node.name);
                            field0.isStatic = (field.access & Opcodes.ACC_STATIC) != 0;
                            ca.fields.add(field0);
                        }
                    }
                }
            }
        }
        for (var node : nodes.values()) {
            access.put(node.name, node);
            if (node.methods != null) for (var method : node.methods) {
                if (method.instructions != null) {
                    var instructions = method.instructions;
                    for (var i = 0; i < instructions.size(); i++) {
                        var instruction = instructions.get(i);
                        if (instruction instanceof FieldInsnNode) {
                            var field = (FieldInsnNode) instruction;
                            if (field.owner.equals(node.name)) continue;
                            var ownClass = privates.get(field.owner);
                            if (ownClass != null) {
                                var find = ownClass.find(field.name, field.desc, field.owner, true);
                                if (find != null) {
                                    allFieldAccess.add(find);
                                    switch (field.getOpcode()) {
                                        case Opcodes.PUTSTATIC:
                                        case Opcodes.PUTFIELD: {
                                            find.setExist = true;
                                            break;
                                        }
                                        case Opcodes.GETSTATIC:
                                        case Opcodes.GETFIELD: {
                                            find.getExist = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        } else if (instruction instanceof MethodInsnNode) {
                            var m_node = (MethodInsnNode) instruction;
                            if (node.name.equals(m_node.owner)) continue;
                            var ownClass = privates.get(m_node.owner);
                            if (ownClass != null) {
                                var find = ownClass.find(m_node.name, m_node.desc, m_node.owner, false);
                                if (find != null) {
                                    allMethodAccess.add(find);
                                }
                            }
                        }
                    }
                }
            }
        }
        {
            var index = 0;
            for (var field : allFieldAccess) {
                var node = nodes.get(field.owner);
                if (node == null) continue;
                if (field.getExist) {
                    var acc = node.visitMethod(
                            Opcodes.ACC_SYNTHETIC | (field.isStatic ? Opcodes.ACC_STATIC : 0),
                            field.openGetName = "access$get$" + index++, Type.getMethodDescriptor(
                                    Type.getType(field.desc)
                            ), null, null
                    );
                    var x = 0;
                    if (!field.isStatic) {
                        x = 1;
                        acc.visitVarInsn(Opcodes.ALOAD, 0);
                    }
                    acc.visitFieldInsn(
                            field.isStatic ? Opcodes.GETSTATIC : Opcodes.GETFIELD,
                            node.name, field.name, field.desc
                    );
                    acc.visitMaxs(x = WrappedClassImplements.putTypeInsn(Type.getType(field.desc), x, true, acc), x);
                }
                if (field.setExist) {
                    var acc = node.visitMethod(
                            Opcodes.ACC_SYNTHETIC | (field.isStatic ? Opcodes.ACC_STATIC : 0),
                            field.openSetName = "access$set$" + index++, Type.getMethodDescriptor(
                                    Type.VOID_TYPE, Type.getType(field.desc)
                            ), null, null
                    );
                    var x = 0;
                    if (!field.isStatic) {
                        x = 1;
                        acc.visitVarInsn(Opcodes.ALOAD, 0);
                    }
                    WrappedClassImplements.putTypeInsn(
                            Type.getType(field.desc), x, false, acc
                    );
                    acc.visitFieldInsn(
                            field.isStatic ? Opcodes.PUTSTATIC : Opcodes.PUTFIELD,
                            node.name, field.name, field.desc
                    );
                    acc.visitInsn(Opcodes.RETURN);
                    x += Type.getType(field.desc).getSize();
                    acc.visitMaxs(x, x);
                }
            }
            for (var method : allMethodAccess) {
                var node = nodes.get(method.owner);
                if (node == null) continue;
                if (method.name.equals("<init>")) {
                    String desc = Type.getMethodDescriptor(
                            Type.getObjectType(method.owner), Type.getArgumentTypes(method.desc)
                    );
                    var cons = node.visitMethod(Opcodes.ACC_SYNTHETIC | Opcodes.ACC_STATIC,
                            method.openName = "access$cons$" + index++, desc, null, null);
                    // NEW cn/mcres/karlatemp/mxlib/tools/EmptyStream
                    // DUP
                    // INVOKESPECIAL cn/mcres/karlatemp/mxlib/tools/EmptyStream.<init> ()V
                    cons.visitTypeInsn(Opcodes.NEW, method.owner);
                    cons.visitInsn(Opcodes.DUP);
                    int slot = 0;
                    for (var arg : Type.getArgumentTypes(method.desc)) {
                        slot = WrappedClassImplements.putTypeInsn(
                                arg, slot, false, cons
                        );
                    }
                    slot++;
                    cons.visitMethodInsn(Opcodes.INVOKESPECIAL, method.owner, "<init>", method.desc, false);
                    cons.visitInsn(Opcodes.ARETURN);
                    cons.visitMaxs(slot + 1, slot);
                    continue;
                }
                var mtt = node.visitMethod(Opcodes.ACC_SYNTHETIC | (method.isStatic ? Opcodes.ACC_STATIC : 0),
                        method.openName = "access$invoke$" + index++, method.desc, null, null);
                var slot = 0;
                if (!method.isStatic) {
                    mtt.visitVarInsn(Opcodes.ALOAD, slot++);
                }
                for (var arg : Type.getArgumentTypes(method.desc)) {
                    slot = WrappedClassImplements.putTypeInsn(
                            arg, slot, false, mtt
                    );
                }
                mtt.visitMethodInsn(
                        method.isStatic ? Opcodes.INVOKESTATIC :
                                method.isInterface ? Opcodes.INVOKEINTERFACE : Opcodes.INVOKEVIRTUAL,
                        method.owner, method.name, method.desc, method.isInterface);
                var rt = Type.getReturnType(method.desc);
                WrappedClassImplements.putTypeInsn(
                        rt, 0, true, mtt
                );
                mtt.visitMaxs(slot + rt.getSize(), slot);
            }
        }
        {
            for (var node : access.values()) {
                for (var met : node.methods) {
                    var instructions = met.instructions;
                    if (instructions != null) {
                        for (var i = 0; i < instructions.size(); i++) {
                            var inst = instructions.get(i);
                            if (inst instanceof MethodInsnNode) {
                                var m = (MethodInsnNode) inst;
                                if (m.owner.equals(node.name)) continue;
                                var ownClass = privates.get(m.owner);
                                if (ownClass != null) {
                                    var find = ownClass.find(m.name, m.desc, m.owner, false);
                                    if (find != null) {
                                        if (find.openName != null) {
                                            var x = m.name;
                                            m.name = find.openName;
                                            if (x.equals("<init>")) {
                                                // System.err.println(node.name + "," + m.owner + ", " + x + " " + m.desc);
                                                boolean removed = false;
                                                {
                                                    AbstractInsnNode watching = m;
                                                    do {
                                                        var xw = watching.getPrevious();
                                                        watching = xw;
                                                        if (xw instanceof TypeInsnNode) {
                                                            var tin = (TypeInsnNode) xw;
                                                            if (tin.getOpcode() == Opcodes.NEW) {
                                                                if (tin.desc.equals(m.owner)) {
                                                                    var next = tin.getNext();
                                                                    if (next instanceof InsnNode) {
                                                                        if (next.getOpcode() == Opcodes.DUP) {
                                                                            i--;
                                                                            instructions.remove(next);
                                                                        }
                                                                    }
                                                                    instructions.remove(tin);
                                                                    i--;
                                                                    m.setOpcode(Opcodes.INVOKESTATIC);
                                                                    m.desc = Type.getMethodDescriptor(Type.getObjectType(m.owner), Type.getArgumentTypes(m.desc));
                                                                    removed = true;
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    } while (watching != null);
                                                }
                                                if (!removed) {
                                                    // Extend Method.
                                                    if (find.wrappedConsDesc == null) {
                                                        find.wrappedConsDesc = createWrappedConsDesc(
                                                                nodes.get(m.owner), m.desc
                                                        );
                                                    }
                                                    m.name = "<init>";
                                                    m.desc = find.wrappedConsDesc;
                                                    instructions.insert(m.getPrevious(), new InsnNode(Opcodes.ACONST_NULL));
                                                    i++;
                                                    met.maxStack++;
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (inst instanceof FieldInsnNode) {
                                var f0 = (FieldInsnNode) inst;
                                if (f0.owner.equals(node.name)) continue;
                                var ownClass = privates.get(f0.owner);
                                if (ownClass != null) {
                                    var find = ownClass.find(f0.name, f0.desc, f0.owner, true);
                                    if (find != null) {
                                        var isGet = f0.getOpcode() == Opcodes.GETSTATIC || f0.getOpcode() == Opcodes.GETFIELD;
                                        instructions.set(inst, new MethodInsnNode(
                                                find.isStatic ? Opcodes.INVOKESTATIC : Opcodes.INVOKEVIRTUAL,
                                                f0.owner,
                                                isGet ? find.openGetName : find.openSetName,
                                                isGet ? "()" + f0.desc : "(" + f0.desc + ")V", false
                                        ));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        {
            String factoryName;
            {
                String package0 = "cn/mcres/karlatemp/mxlib/Java9ToJava8/" + UUID.randomUUID().toString().replace('-', '_');

                factoryName = package0 + "/StringFactory"; // Will add mapping here.
            }
            for (var node : nodes.values()) {
                node.version = Opcodes.V1_8;
                for (var met : node.methods) {
                    var is = met.instructions;
                    if (is != null) {
                        for (var inst : is) {
                            if (inst instanceof InvokeDynamicInsnNode) {
                                var inv = (InvokeDynamicInsnNode) inst;
                                if (inv.name.equals("makeConcatWithConstants") && inv.bsm.getOwner().equals("java/lang/invoke/StringConcatFactory")) {
                                    var bsm = inv.bsm;
                                    inv.bsm = new Handle(
                                            bsm.getTag(),
                                            factoryName,
                                            bsm.getName(),
                                            bsm.getDesc(),
                                            bsm.isInterface()
                                    );
                                }
                            }
                        }
                    }
                }
            }
            var factory = new ClassNode();
            StringFactoryCreator.dump(factoryName, Opcodes.V1_8, factory);
            nodes.put(factory.name, factory);
        }
        {
            for (var node : nodes.values()) {
                for (var met : node.methods) {
                    var ma = met.visibleAnnotations;
                    if (ma != null) {
                        ma.replaceAll(opt -> {
                            if (opt.desc.equals("Lcn/mcres/karlatemp/mxlib/annotations/LambdaFromHidden;")) {
                                opt.desc = "Ljava/lang/invoke/LambdaForm$Hidden;";
                            }
                            return opt;
                        });
                    }
                }
            }
        }
        {
            try (var out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(tmp)))) {
                var write = new ConcurrentLinkedQueue<String>();
                for (var node : nodes.values()) {
                    var path = node.name + ".class";
                    write.add(path);
                    out.putNextEntry(new ZipEntry(path));
                    ClassWriter cw = new ClassWriter(0);
                    node.accept(cw);
                    out.write(cw.toByteArray());
                }
                for (var entry : new IteratorSupplier<>(zip.entries().asIterator())) {
                    if (!write.contains(entry.getName())) {
                        out.putNextEntry(entry);
                        zip.getInputStream(entry).transferTo(out);
                    }
                }
            }
        }
        Files.deleteIfExists(output.toPath());
        Files.move(tmp.toPath(), output.toPath());
    }

    private static String createWrappedConsDesc(ClassNode node, String desc) {
        var list = new ArrayList<>(Arrays.asList(Type.getArgumentTypes(desc)));
        list.add(Type.getObjectType("java/lang/Void"));
        var md = Type.getMethodDescriptor(Type.VOID_TYPE, list.toArray(Type[]::new));
        var met = node.visitMethod(Opcodes.ACC_SYNTHETIC, "<init>", md, null, null);
        met.visitVarInsn(Opcodes.ALOAD, 0);
        var slot = 1;
        for (Type t : Type.getArgumentTypes(desc)) {
            WrappedClassImplements.putTypeInsn(t, slot, false, met);
        }
        met.visitMethodInsn(Opcodes.INVOKESPECIAL, node.name, "<init>", desc, false);
        met.visitInsn(Opcodes.RETURN);
        met.visitMaxs(slot, slot);
        return md;
    }
}
