/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 15:26:38
 *
 * MXLib/mxlib.arguments/SOptions.java
 */

package cn.mcres.karlatemp.mxlib.arguments;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SOptions {
    final Collection<SSpec<?>> specs = new ConcurrentLinkedQueue<>();
    final Collection<String> values = new HashSet<>();
    static final String[] NO_SPEC = new String[]{"<no-spec>"};
    final SSpec<?> noSpec = new SSpec<>(NO_SPEC);
    boolean strictly;

    public SSpec<?> getNoSpec() {
        return noSpec;
    }

    public SOptions() {
    }

    public Collection<SSpec<?>> getSpecs() {
        return specs;
    }

    public Collection<String> getValues() {
        return values;
    }

    public SOptions strictly() {
        strictly = true;
        return this;
    }

    public SSpec<?> register(String... keys) {
        var spec = new SSpec<>(keys);
        for (var k : keys) {
            if (values.contains(k.toLowerCase())) {
                throw new IllegalArgumentException("Key " + k + " was registered.");
            }
        }
        specs.add(spec);
        for (var k : keys) values.add(k.toLowerCase());
        return spec;
    }

    public SParser newParser() {
        return new SParser(this);
    }
}
