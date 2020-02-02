/**
 * Create at 2020/1/31 16:32
 * Copyright Karlatemp
 * MXLib $
 */module mxlib.common.maven {
    exports cn.mcres.karlatemp.common.maven.exceptions;
    exports cn.mcres.karlatemp.common.maven;
    exports cn.mcres.karlatemp.common.maven.annotations;
    requires java.logging;
    requires java.base;
    requires mxlib.api;
    requires org.jetbrains.annotations;
    requires org.objectweb.asm;
    requires dom4j;
}