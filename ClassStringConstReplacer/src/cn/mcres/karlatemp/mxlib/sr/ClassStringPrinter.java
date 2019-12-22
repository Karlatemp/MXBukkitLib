/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ClassStringPrinter.java@author: karlatemp@vip.qq.com: 19-12-20 下午11:39@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.sr;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ClassStringPrinter implements StringReplacer {
    public interface ClassStringMethodVisitor {
        void visitMethod(@NotNull String value, int index);

        default void visitEnd() {
        }
    }

    public interface ClassStringInfoVisitor {
        void visitField(@NotNull String fieldName, @NotNull String value);

        ClassStringMethodVisitor visitMethod(@NotNull String methodName, @NotNull String methodDesc);

        default void visitEnd() {
        }
    }

    public interface ClassSetInfoVisitor {
        ClassStringInfoVisitor visit(@NotNull String className);

        default void visitEnd(String className) {
        }

        default void visitEnd() {
        }
    }

    private static class MINF {
        String met, desc;
        int ind;

        MINF(String a, String b, int ind) {
            this.met = a;
            this.desc = b;
            this.ind = ind;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(met) ^ Objects.hashCode(desc) | ind;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj instanceof MINF) {
                return Objects.equals(met, ((MINF) obj).met) && Objects.equals(desc, ((MINF) obj).desc) && ind == ((MINF) obj).ind;
            }
            return false;
        }
    }

    private static class Pool {
        final Map<String, String> fields = new LinkedHashMap<>();
        final Map<MINF, String> methods = new LinkedHashMap<>();
    }

    private final Map<String, Pool> map = new LinkedHashMap<>();

    private synchronized Pool pool(String cn) {
        if (map.containsKey(cn)) return map.get(cn);
        Pool p = new Pool();
        map.put(cn, p);
        return p;
    }

    @Override
    public synchronized String replaceField(@NotNull String className, @NotNull String fieldName, @NotNull String defaultValue) {
        pool(className).fields.put(fieldName, defaultValue);
        return defaultValue;
    }

    @Override
    public synchronized String replaceMethod(@NotNull String className, @NotNull String method, @NotNull String method_desc, @NotNull String defaultValue, int index) {
        pool(className).methods.put(new MINF(method, method_desc, index), defaultValue);
        return defaultValue;
    }

    public void accept(ClassSetInfoVisitor visitor) {
        if (visitor != null) {
            for (Map.Entry<String, Pool> pools : map.entrySet()) {
                String cn = pools.getKey();
                final ClassStringInfoVisitor visit = visitor.visit(cn);
                if (visit != null) {
                    Pool p = pools.getValue();
                    for (Map.Entry<String, String> field : p.fields.entrySet()) {
                        visit.visitField(field.getKey(), field.getValue());
                    }
                    final Map<MINF, List<String>> output = p.methods.entrySet().stream().collect(new Collector<Map.Entry<MINF, String>, Map<MINF, Map<Integer, String>>, Map<MINF, List<String>>>() {

                        @Override
                        public Supplier<Map<MINF, Map<Integer, String>>> supplier() {
                            return LinkedHashMap::new;
                        }

                        @Override
                        public BiConsumer<Map<MINF, Map<Integer, String>>, Map.Entry<MINF, String>> accumulator() {
                            return (map, entry) -> {
                                final MINF key = entry.getKey();
                                MINF mi = new MINF(key.met, key.desc, 0);
                                map.computeIfAbsent(mi, k -> new HashMap<>()).put(key.ind, entry.getValue());
                            };
                        }

                        @Override
                        public BinaryOperator<Map<MINF, Map<Integer, String>>> combiner() {
                            return (source, target) -> {
                                source.putAll(target);
                                return source;
                            };
                        }

                        @Override
                        public Function<Map<MINF, Map<Integer, String>>, Map<MINF, List<String>>> finisher() {
                            return map -> {
                                Map<MINF, List<String>> output = new LinkedHashMap<>();
                                for (Map.Entry<MINF, Map<Integer, String>> s : map.entrySet()) {
                                    output.put(s.getKey(), s.getValue().entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getKey)).map(Map.Entry::getValue).collect(Collectors.toList()));
                                }
                                return output;
                            };
                        }

                        @Override
                        public Set<Characteristics> characteristics() {
                            return Collections.emptySet();
                        }
                    });
                    for (Map.Entry<MINF, List<String>> e : output.entrySet()) {
                        MINF mi = e.getKey();
                        final ClassStringMethodVisitor met = visit.visitMethod(mi.met, mi.desc);
                        if (met != null) {
                            int w = 0;
                            for (String s : e.getValue()) {
                                met.visitMethod(s, w++);
                            }
                            met.visitEnd();
                        }
                    }
                    visit.visitEnd();
                }
                visitor.visitEnd(cn);
            }
            visitor.visitEnd();
        }
    }
}
