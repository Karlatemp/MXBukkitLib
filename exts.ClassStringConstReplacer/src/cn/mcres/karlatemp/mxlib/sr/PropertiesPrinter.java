/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PropertiesPrinter.java@author: karlatemp@vip.qq.com: 19-12-21 上午12:20@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.sr;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;

public class PropertiesPrinter implements ClassStringPrinter.ClassSetInfoVisitor {
    public static final String END_CLASS = "-------<END CLASS>------",
            END_METHOD = "|    --------<END METHOD>--------",
            STRING_END = "<StringEnd>",
            UN_OVERRIDE = "<UN_SET>",
            BOK = "<>",
            FIELD_START = "|    [Field]",
            METHOD_START = "|    [Method]";
    public final PrintStream pr;

    public PropertiesPrinter(PrintStream pr) {
        this.pr = pr;
    }

    public void printValue(String val) {
        int st = 0;
        boolean next = true;
        do {
            int w = val.indexOf('\n', st);
            if (w == -1) {
                next = false;
                w = val.length();
            }
            String cut = val.substring(st, w);
            if (cut.endsWith(BOK) || cut.endsWith(UN_OVERRIDE) || cut.endsWith(STRING_END)) {
                pr.println(cut + BOK);
            } else {
                if (next) {
                    pr.println(cut);
                } else {
                    pr.println(cut + STRING_END);
                }
            }
            st = w + 1;
        } while (next);
    }

    @Override
    public ClassStringPrinter.ClassStringInfoVisitor visit(@NotNull String className) {
        pr.println(className);
        return new ClassStringPrinter.ClassStringInfoVisitor() {
            @Override
            public void visitField(@NotNull String fieldName, @NotNull String value) {
                pr.println(FIELD_START + fieldName);
                printValue(value);
            }

            @Override
            public ClassStringPrinter.ClassStringMethodVisitor visitMethod(@NotNull String methodName, @NotNull String methodDesc) {
                pr.println(METHOD_START + methodName + methodDesc);
                return new ClassStringPrinter.ClassStringMethodVisitor() {
                    @Override
                    public void visitMethod(@NotNull String value, int index) {
                        printValue(value);
                    }

                    @Override
                    public void visitEnd() {
                        pr.println(END_METHOD);
                    }
                };
            }

            @Override
            public void visitEnd() {
                pr.println(END_CLASS);
            }
        };
    }
}
