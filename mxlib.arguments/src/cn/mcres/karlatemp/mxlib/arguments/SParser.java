/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 15:55:17
 *
 * MXLib/mxlib.arguments/SParser.java
 */

package cn.mcres.karlatemp.mxlib.arguments;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SParser {
    private SSpec<?> noSpec;
    private Map<String, SSpec<?>> specMapping = new HashMap<>();
    private Map<String, String[]> keysMapping = new HashMap<>();
    private Map<SSpec<?>, Object> values = new HashMap<>();
    private Collection<SSpec<?>> specs = new ArrayList<>();
    private boolean strictly;
    private static final Function<SSpec<?>, Object> MULTIPLE_RESULT = key -> new HashSet<>();

    public SParser(SOptions options) {
        strictly = options.strictly;
        noSpec = options.noSpec;
        for (var spec : options.specs) {
            var keys = spec.getKeys();
            specs.add(spec);
            for (var k : keys) {
                var kl = k.toLowerCase();
                keysMapping.put(kl, keys);
                specMapping.put(kl, spec);
            }
        }
    }

    public boolean contains(SSpec<?> sp) {
        return values.containsKey(sp);
    }

    public static class ParseResult {
        public boolean success;
        public String trans;
        public Object[] param;
        public static final String NO_VALUE_FOUND = "no.value.found",
                FAILED_TO_FORMAT_NUMBER = "failed.to.format.number";

        public static ParseResult of(boolean success) {
            return of(success, null, (Object[]) null);
        }

        public static ParseResult of(boolean success, String trans, Object... param) {
            var r = new ParseResult();
            r.param = param;
            r.trans = trans;
            r.success = success;
            return r;
        }
    }

    @SuppressWarnings("unchecked")
    public @NotNull ParseResult parse(Iterator<String> arguments) {
        var invoked = new ArrayList<>(keysMapping.size());
        var def = new ArrayList<String>();
        var parseCompile = ParseResult.of(true);
        while (arguments.hasNext()) {
            var next = arguments.next();
            if (next.isEmpty()) {
                def.add(next);
            } else if (next.charAt(0) == '-') {
                var key = next.substring(1).toLowerCase();
                if (invoked.contains(key)) {
                    if (strictly)
                        return ParseResult.of(false, "multiple.not.support", key, keysMapping.get(key)); // DONE
                    def.add(next);
                } else if (specMapping.containsKey(key)) {
                    var spec = specMapping.get(key);
                    var val = spec.getParser().apply(arguments, parseCompile);
                    if (!parseCompile.success) return parseCompile;
                    if (spec.isMultiple()) {
                        ((Collection<Object>) values.computeIfAbsent(spec, MULTIPLE_RESULT)).add(val);
                    } else {
                        values.put(spec, val);
                        invoked.addAll(Arrays.asList(keysMapping.get(key)));
                    }
                } else if (strictly) {
                    return ParseResult.of(false, "invalid.key", key); // DONE
                } else def.add(next);
            } else {
                def.add(next);
            }
        }
        var parser = noSpec.getParser();
        if (parser != null) {
            var val = parser.apply(def.iterator(), parseCompile);
            if (!parseCompile.success) return parseCompile;
            values.put(noSpec, val);
        } else {
            values.put(noSpec, def);
        }
        return ParseResult.of(true);
    }

    private SParser(SParser parser) {
        specMapping.putAll(parser.specMapping);
        keysMapping.putAll(parser.keysMapping);
        values.putAll(parser.values);
        strictly = parser.strictly;
        noSpec = parser.noSpec;
        specs.addAll(parser.specs);
    }

    public SParser newContext() {
        return new SParser(this);
    }

    public void reset() {
        values.clear();
    }

    @SuppressWarnings("unchecked")
    public <V> V value(SSpec<V> spec) {
        if (spec.isMultiple()) throw new IllegalArgumentException("multiple spec.");
        if (values.containsKey(spec)) {
            return (V) values.get(spec);
        }
        final Supplier<V> defaultValue = spec.getDefaultValue();
        if (defaultValue != null) return defaultValue.get();
        return null;
    }

    @SuppressWarnings("unchecked")
    public <V> Collection<V> multiple(SSpec<V> spec) {
        if (spec.isMultiple()) {
            if (values.containsKey(spec))
                return (Collection<V>) values.get(spec);
            return new HashSet<>();
        } else return Collections.singletonList(value(spec));
    }

    public void tabCompile(List<String> values, List<String> result) {
        tabCompile(values.iterator(), result);
    }

    public void tabCompile(Iterator<String> iterator, List<String> result) {
        var invoked = new HashSet<>();
        var def = new ArrayList<String>();
        Consumer<String> ADD = result::add;
        while (iterator.hasNext()) {
            result.clear();
            var next = iterator.next();
            if (next.isEmpty()) {
                def.add(next);
                if (!iterator.hasNext()) {
                    specMapping.keySet().stream().filter(x -> !invoked.contains(x)).sorted().map(v -> '-' + v).forEach(ADD);
                    break;
                }
            } else if (next.charAt(0) == '-') {
                var key = next.substring(1).toLowerCase();
                if (invoked.contains(key)) {
                    if (strictly) return;
                    def.add(next);
                    continue;
                }
                if (specMapping.containsKey(key)) {
                    var spec = specMapping.get(key);
                    final BiConsumer<Iterator<String>, Consumer<String>> compiler = spec.getTabCompiler();
                    compiler.accept(iterator, ADD);
                    if (!iterator.hasNext()) return;
                    if (!spec.isMultiple()) {
                        invoked.addAll(Arrays.asList(spec.getKeys()));
                    }
                } else {
                    if (iterator.hasNext()) {
                        if (strictly) return;
                        def.add(next);
                    } else {
                        specMapping.keySet().stream().filter(x -> x.startsWith(key)).map(v -> '-' + v).sorted().forEach(ADD);
                        return;
                    }
                }
            } else {
                def.add(next);
            }
        }
        final BiConsumer<Iterator<String>, Consumer<String>> tabCompiler = noSpec.getTabCompiler();
        if (tabCompiler != null) tabCompiler.accept(def.iterator(), ADD);
    }
}
