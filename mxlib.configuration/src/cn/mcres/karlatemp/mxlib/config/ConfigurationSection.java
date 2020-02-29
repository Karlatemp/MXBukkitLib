/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/29 15:57:22
 *
 * MXLib/mxlib.configuration/ConfigurationSection.java
 */

package cn.mcres.karlatemp.mxlib.config;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public interface ConfigurationSection {
    /**
     * Merge other section
     *
     * @param section The target.
     * @return this.
     */
    ConfigurationSection merge(ConfigurationSection section);

    /**
     * The splitter using.
     *
     * @return The path splitter.
     */
    char pathSplitter();

    /**
     * Change using splitter. And make {@link #hasCurrentSplitter()} return true.
     * <p>
     * Has no effect on {@link #newContext()} and parent {@link ConfigurationSection}
     *
     * @param splitter The splitter change.
     * @return this.
     */
    ConfigurationSection pathSplitter(char splitter);

    /**
     * Does current context using splitter.
     *
     * @return true if using splitter.
     */
    boolean useSplitter();

    /**
     * Check current context has splitter.
     *
     * @return true if parent is null or {@link #clearSplitter()} invoked.
     */
    boolean hasCurrentSplitter();

    /**
     * Make {@link #hasCurrentSplitter()} return false.
     * This method has no effect if parent is null.
     *
     * @return this
     */
    ConfigurationSection clearSplitter();

    /**
     * Set current context using splitter
     *
     * @param use Is using.
     * @return this.
     */
    ConfigurationSection useSplitter(boolean use);

    String string(String path, Supplier<String> def);

    List<Object> list(String path, Supplier<List<Object>> def);

    default List<Object> list(String path) {
        return list(path, null);
    }

    default List<String> stringList(String path) {
        return stringList(path, null);
    }

    List<String> stringList(String path, Supplier<List<String>> def);

    default String string(String path, String def) {
        return string(path, () -> def);
    }

    default String string(String path) {
        return string(path, (Supplier<String>) null);
    }

    int intV(String path, int def);

    default int intV(String path) {
        return intV(path, 0);
    }

    /**
     * Check this path has ot no vale.
     *
     * @param path The path checking.
     * @return true if path exists.
     */
    boolean exists(String path);

    default Object value(String path, Object def) {
        return value(path, () -> def);
    }

    /**
     * Get the value of path.
     *
     * If you want to get value with Target Path Contains splitter. Use this:
     * <pre>{@code
     *      Object value = section.newContext().useSplitter(false).value(PATH, null);
     * }</pre>
     *
     * @param path The path.
     * @param def  Default value supplier
     * @return The result value.
     */
    Object value(String path, Supplier<Object> def);

    default Object value(String path) {
        return value(path, null);
    }

    short shortV(String path, short def);

    default short shortV(String path) {
        return shortV(path, (short) 0);
    }

    byte byteV(String path, byte def);

    default byte byteV(String path) {
        return byteV(path, (byte) 0);
    }

    default char charV(String path) {
        return charV(path, (char) 0);
    }

    char charV(String path, char def);

    float floatV(String path, float def);

    default float floatV(String path) {
        return floatV(path, 0F);
    }

    double doubleV(String path, double def);

    default double doubleV(String path) {
        return doubleV(path, 0D);
    }

    boolean booleanV(String path, boolean def);

    default boolean booleanV(String path) {
        return booleanV(path, false);
    }

    long longV(String path, long def);

    default long longV(String path) {
        return longV(path, 0L);
    }

    Number number(String path, Number def);

    default Number number(String path) {
        return number(path, 0);
    }

    /**
     * Set the value to ConfigurationSection. This will also affect the values in {@link #newContext()}
     * <p>
     * WARNING: If root (Configuration) unsupported this value. Then will throw a Exception in
     * {@link Configuration#store(OutputStream)}
     * {@link Configuration#store(java.io.Writer)}
     * {@link Configuration#store(File)}
     * {@link Configuration#store(Path)}
     *
     * @param path  The path set.
     * @param value The value.
     * @return this
     */
    ConfigurationSection set(String path, Object value);

    /**
     * Remove a value.
     *
     * @param path The path.
     * @return this.
     */
    ConfigurationSection remove(String path);

    /**
     * Get sub section.
     *
     * @param path The section path.
     * @return Sub Section.(if exist)
     */
    default ConfigurationSection sectionIfExist(String path) {
        var val = value(path);
        if (val instanceof ConfigurationSection) return (ConfigurationSection) val;
        return null;
    }

    @NotNull
    ConfigurationSection sectionOrCreate(String path);

    /**
     * Same as {@link #sectionIfExist(String)}
     *
     * @param path The path
     * @return Sub Section
     */
    default ConfigurationSection section(String path) {
        return sectionIfExist(path);
    }

    /**
     * Get the keys of current section.
     *
     * @return keys.
     */
    @NotNull
    Set<String> keys();

    /**
     * Create a new context / Create a Pointer.
     *
     * @return The pointer created.
     */
    @NotNull
    ConfigurationSection newContext();

    /**
     * Clone current section.
     * Result no parent.
     *
     * @return Cloned Section.
     */
    default @NotNull ConfigurationSection copy() {
        return clone();
    }

    @NotNull ConfigurationSection clone();

    /**
     * Clear section values.
     *
     * @return this.
     */
    ConfigurationSection clear();

    /**
     * Get un-editable values.
     *
     * @return The values of this section.
     */
    Map<String, Object> values();

    static String toStr(ConfigurationSection cs) {
        StringBuilder sb = new StringBuilder("{");
        var it = cs.values().entrySet().iterator();
        if (it.hasNext()) {
            var entry = it.next();
            sb.append(entry.getKey()).append('=').append(entry.getValue());
        }
        while (it.hasNext()) {
            var entry = it.next();
            sb.append(", ").append(entry.getKey()).append('=').append(entry.getValue());
        }
        return sb.append("}").toString();
    }

    /**
     * Get parent.
     *
     * @return The parent
     */
    ConfigurationSection parent();

    default ConfigurationSection root() {
        var c = this;
        do {
            var x = c.parent();
            if (x == null) return c;
            c = x;
        } while (true);
    }

    /**
     * Set parent if absent.
     *
     * @param parent The target.
     * @return this.
     */
    ConfigurationSection parent(ConfigurationSection parent);
}