/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: StringFactoryCreator.java@author: karlatemp@vip.qq.com: 2019/12/25 下午2:04@version: 2.0
 */

package cn.mcres.karlatemp.mxlib;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.SimpleRemapper;

import java.util.HashMap;

public class StringFactoryCreator implements Opcodes {

    public static void dump(String className, int ver, ClassVisitor visitor) {
        try {
            ClassReader reader = new ClassReader(StringFactoryCreator.class.getResourceAsStream("J8StringFactory.class"));
            HashMap<String, String> mapping = new HashMap<>();
            reader.accept(new ClassVisitor(Opcodes.ASM7, new ClassRemapper(visitor, new SimpleRemapper(mapping))) {
                @Override
                public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                    mapping.put(name, className);
                    super.visit(ver, access, name, signature, superName, interfaces);
                }
            }, 0);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

}
