/**
 * Command System API.
 *
 * @since 2.11
 */
module mxlib.command.api {
    requires mxlib.api;
    requires org.jetbrains.annotations;
    requires com.google.common;
    requires java.logging;
    requires spigot.api;
    exports cn.mcres.karlatemp.mxlib.command.internal;
    exports cn.mcres.karlatemp.mxlib.command.annoations;
    exports cn.mcres.karlatemp.mxlib.command.exceptions;
    exports cn.mcres.karlatemp.mxlib.command;
}