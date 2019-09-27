/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PCollectionPermission.java@author: karlatemp@vip.qq.com: 19-9-25 下午10:51@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The collection access permissions
 *
 * @see PermissibleMap
 * @see PermissibleCollection
 * @see PermissibleIterator
 * @see PermissibleIterable
 * @see PermissibleSet
 * @see PermissibleList
 * @since 2.2
 */
public enum PCollectionPermission {
    @SuppressWarnings("SpellCheckingInspection") SPLITERATOR,
    STREAM, PARALLEL_STREAM, ITERATOR, FOR_EACH,
    CONTAINS, SIZE, IS_EMPTY,
    TO_ARRAY_RAW,
    /**
     * See this link
     *
     * @see java.util.Collection#toArray(Object[])
     */
    TO_ARRAY_ARR,
    ADD, REMOVE, RETAIN_ALL, CLEAR,
    REPLACE, SORT, GET, SET, INDEX_OF, LAST_INDEX_OF, LIST_ITERATOR, SUB_LIST, TO_STRING, FOR_EACH_REMAINING,
    ENTRY_SET, COMPUTE, MERGE, CONTAINS_VALUE, CONTAINS_KEY, KEY_SET, VALUES;
    /**
     * It is a unmodifiable set.
     */
    public static final Set<PCollectionPermission> ALL = new AbstractSet<PCollectionPermission>() {
        @Override
        public boolean contains(Object o) {
            return true;
        }

        @Override
        public boolean containsAll(@NotNull Collection<?> c) {
            return true;
        }

        @NotNull
        @Override
        public Iterator<PCollectionPermission> iterator() {
            return EnumSet.allOf(PCollectionPermission.class).iterator();
        }

        @Override
        public int size() {
            return values().length;
        }
    };
    /**
     * It is a unmodifiable set.
     */
    public static final Set<PCollectionPermission> ONLY_READ
            = Collections.unmodifiableSet(
            EnumSet.of(STREAM, PARALLEL_STREAM, ITERATOR, FOR_EACH, CONTAINS, SIZE, IS_EMPTY, TO_ARRAY_RAW,
                    TO_ARRAY_ARR, GET, INDEX_OF, LAST_INDEX_OF, LIST_ITERATOR, SUB_LIST, TO_STRING,
                    FOR_EACH_REMAINING, FOR_EACH, ENTRY_SET, CONTAINS_KEY, CONTAINS_VALUE, KEY_SET, VALUES)
    );
    /**
     * It is a unmodifiable set.
     */
    public static final Set<PCollectionPermission> ONLY_SET
            = Collections.unmodifiableSet(EnumSet.of(ADD, REMOVE, CLEAR, REPLACE, SORT, SET, COMPUTE, MERGE)
    );
    /**
     * It is a unmodifiable set.
     */
    public static final Set<PCollectionPermission> ONLY_ADD
            = Collections.unmodifiableSet(EnumSet.of(ADD));

    PCollectionPermission() {
    }
}
