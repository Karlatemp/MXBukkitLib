/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PermissibleSet.java@author: karlatemp@vip.qq.com: 19-9-26 下午1:07@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import java.util.Collection;
import java.util.Set;

/**
 * Permission access check set
 *
 * @param <E> The type of this set
 * @see PCollectionPermission
 * @see PermissibleCollection
 * @since 2.2
 */
public class PermissibleSet<E> extends PermissibleCollection<E> implements Set<E> {
    protected PermissibleSet() {
    }

    public PermissibleSet(Set<E> parent) {
        super(parent);
    }

    public PermissibleSet(Collection<E> parent, Set<PCollectionPermission> permissions) {
        super(parent, permissions);
    }
}
