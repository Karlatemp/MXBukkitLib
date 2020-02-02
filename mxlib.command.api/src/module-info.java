/**
 * Command System API.
 *
 * @since 2.11
 */
open module mxlib.command.api {
    requires mxlib.api;
    requires org.jetbrains.annotations;
    requires com.google.common;
    requires java.logging;
    requires spigot.api;
    requires org.objectweb.asm;
    requires org.objectweb.asm.tree;
    requires mxlib.bukkit;
    exports cn.mcres.karlatemp.mxlib.command.internal;
    exports cn.mcres.karlatemp.mxlib.command.annoations;
    exports cn.mcres.karlatemp.mxlib.command.exceptions;
    exports cn.mcres.karlatemp.mxlib.command;
}