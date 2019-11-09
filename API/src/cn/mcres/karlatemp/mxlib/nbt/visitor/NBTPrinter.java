/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NBTPrinter.java@author: karlatemp@vip.qq.com: 19-11-9 下午2:13@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.nbt.visitor;

import cn.mcres.karlatemp.mxlib.nbt.*;
import cn.mcres.karlatemp.mxlib.util.LineWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOError;
import java.io.IOException;
import java.io.PrintStream;

public class NBTPrinter implements NBTVisitor {
    private final LineWriter writer;
    private String prefix = "";
    private String prefixEnd = "";

    public NBTPrinter(@NotNull LineWriter writer) {
        this.writer = writer;
    }


    public void visit(String name, NBTBase base) {
        writer.println(prefix + v(name) + " " + base);
    }

    private static String v(String a) {
        if (a == null) return "";
        return NBTTagCompound.s(a) + ":";
    }

    @Override
    public void visitString(String name, NBTTagString value) {
        visit(name, value);
    }

    @Override
    public void visitByte(String name, NBTTagByte value) {
        visit(name, value);
    }

    @Override
    public void visitByteArray(String name, NBTTagByteArray value) {
        visit(name, value);
    }

    @Override
    public void visitDouble(String name, NBTTagDouble value) {
        visit(name, value);
    }

    @Override
    public void visitEnd() {
        writer.println(prefixEnd + "}");
    }

    @Override
    public void visitFloat(String name, NBTTagFloat value) {
        visit(name, value);
    }

    @Override
    public void visitInt(String name, NBTTagInt value) {
        visit(name, value);
    }

    @Override
    public void visitIntArray(String name, NBTTagIntArray value) {
        visit(name, value);
    }

    @Override
    public void visitList(String name, NBTTagList value) {
        if (value.isEmpty()) {
            writer.println(prefix + v(name) + " []");
            return;
        }
        writer.println(prefix + v(name) + " [");
        NBTPrinter printer = new NBTPrinter(writer);
        printer.prefix = prefix + "\t";
        for (NBTBase base : value) {
            base.accept(null, printer);
        }
        writer.println(prefix + "]");
    }

    @Override
    public void visitLong(String name, NBTTagLong value) {
        visit(name, value);
    }

    @Override
    public void visitLongArray(String name, NBTTagLongArray value) {
        visit(name, value);
    }

    @Override
    public void visitShort(String name, NBTTagShort value) {
        visit(name, value);
    }

    @Override
    public NBTVisitor visitCompound(String name, NBTTagCompound value) {
        writer.println(prefix + v(name) + "{");
        NBTPrinter printer = new NBTPrinter(writer);
        printer.prefix = prefix + "\t";
        printer.prefixEnd = prefix;
        return printer;
    }
}
