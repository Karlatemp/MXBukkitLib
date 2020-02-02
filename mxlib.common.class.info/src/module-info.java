/**
 * Create at 2020/1/13 21:06
 * Copyright Karlatemp
 * MXLib $
 */
open module mxlib.common.class_info {
    exports cn.mcres.karlatemp.mxlib.common.class_info;
    exports cn.mcres.karlatemp.mxlib.common.class_info.internal;
    requires org.objectweb.asm;
    requires org.objectweb.asm.tree;
    requires org.jetbrains.annotations;
    requires mxlib.api;
}