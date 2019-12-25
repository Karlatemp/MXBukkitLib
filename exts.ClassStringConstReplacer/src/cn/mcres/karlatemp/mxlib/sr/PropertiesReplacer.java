/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PropertiesReplacer.java@author: karlatemp@vip.qq.com: 19-12-21 上午12:32@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.sr;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PropertiesReplacer implements StringReplacer {
    private final Map<String, String> fields = new HashMap<>();
    private final Map<String, List<String>> methods = new HashMap<>();

    @Override
    public String replaceField(@NotNull String className, @NotNull String fieldName, @NotNull String defaultValue) {
        if (fields.containsKey(className + '.' + fieldName)) {
            return fields.get(className + '.' + fieldName);
        }
        return defaultValue;
    }

    @Override
    public String replaceMethod(@NotNull String className, @NotNull String method, @NotNull String method_desc, @NotNull String defaultValue, int index) {
        String k = className + '.' + method + method_desc;
        if (methods.containsKey(k)) {
            List<String> ls = methods.get(k);
            if (ls != null)
                if (index > -1 && index < ls.size()) {
                    return ls.get(index);
                }
        }
        return defaultValue;
    }

    public PropertiesReplacer addField(String className, String fieldName, String value) {
        fields.put(className + '.' + fieldName, value);
        return this;
    }

    public PropertiesReplacer addMethod(String className, String methodName, String methodDesc, String val) {
        String s = className + '.' + methodName;
        if (methodDesc != null) s += methodDesc;
        methods.computeIfAbsent(s, k -> new ArrayList<>()).add(val);
        return this;
    }

    private String readVal(String next, Scanner scanner) {
        StringBuilder sb = new StringBuilder();
        do {
            if (next == null)
                next = scanner.nextLine();

            if (next.equals(PropertiesPrinter.UN_OVERRIDE) && sb.length() == 0) return null;
            if (next.endsWith(PropertiesPrinter.BOK)) {
                sb.append(next, 0, next.length() - PropertiesPrinter.BOK.length()).append('\n');
            } else if (next.endsWith(PropertiesPrinter.STRING_END)) {
                sb.append(next, 0, next.length() - PropertiesPrinter.STRING_END.length());
                break;
            } else {
                sb.append(next).append('\n');
            }
            next = null;
        } while (true);
        return sb.toString();
    }

    public void load(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String className = scanner.nextLine();
            while (true) {
                String next = scanner.nextLine();
                if (next.equals(PropertiesPrinter.END_CLASS)) {
                    break;
                }
                if (next.startsWith(PropertiesPrinter.FIELD_START)) {
                    addField(className, next.substring(PropertiesPrinter.FIELD_START.length()), readVal(null, scanner));
                } else if (next.startsWith(PropertiesPrinter.METHOD_START)) {
                    String mn = next.substring(PropertiesPrinter.METHOD_START.length());
                    do {
                        String nw = scanner.nextLine();
                        if (nw.equals(PropertiesPrinter.END_METHOD)) break;
                        addMethod(className, mn, null, readVal(nw, scanner));
                    } while (true);
                } else {
                    throw new UnsupportedOperationException("Unknown line " + next);
                }
            }
        }
    }
}
