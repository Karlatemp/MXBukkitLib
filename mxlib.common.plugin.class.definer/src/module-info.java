/**
 * Plugin Class bytecode overrider.
 * <p>
 * If you want to use this module. Let MXLib load first.
 * (Rename MXBukkitLib.jar to 0.MXBukkitLib.jar)<br/>
 * Then let your plugin load after MXLib.
 * (Rename YourPlugin.jar to 1.YourPlugin.jar)<br/>
 * Use it in {@code static{}} block. NOT onEnable() OR onLoad() block.
 *
 * <pre>{@code
 *      BukkitHookToolkit.PluginPreLoadEvent.handlers.register(event->{
 *          System.out.println("Plugin load: " + event.getTarget());
 *          event.getJar().getJarStreamGetEventHandlers().register(resourceLoadEvent->{
 *              if(resourceLoadEvent.getPath().getName().endsWith(".class")) {
 *                  resourceLoadEvent.resolve(source->{
 *                      // @NotNull InputStream resolve(@NotNull InputStreamSuppler stream) throws IOException;
 *
 *                      ClassReader reader = new ClassReader(source);
 *                      ClassWriter writer = new ClassWriter(0);
 *
 *                      // Do class editing here.
 *
 *                      return new ByteArrayInputStream(writer.toByteArray());
 *                  });
 *              }
 *          });
 *      });
 * }</pre>
 */
open module mxlib.common.plugin.class_definer {
    requires mxlib.api;
    requires spigot.api;
    requires org.jetbrains.annotations;
    requires mxlib.bukkit;
    requires java.logging;
    requires org.objectweb.asm;
    requires org.objectweb.asm.commons;
    requires mxlib.common.maven;
    requires mxlib.logging;
    exports cn.mcres.karlatemp.mxlib.common.plugin_class_definer;
}