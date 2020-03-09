/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: BukkitHookedPluginOutput.java@author: karlatemp@vip.qq.com: 2020/1/24 上午1:11@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.plugin_class_definer;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.event.EventHandler;
import cn.mcres.karlatemp.mxlib.share.BukkitToolkit;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.objectweb.asm.*;
import cn.mcres.karlatemp.mxlib.logging.Ansi;

import java.io.*;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.logging.Level;

public class BukkitHookedPluginOutput implements EventHandler<BukkitHookToolkit.PluginPreLoadEvent> {
    private static final Object writeLock = new Object();
    private static RandomAccessFile raf;

    static {
        BukkitHookToolkit.PluginPreLoadEvent.handlers.register(new BukkitHookedPluginOutput());
        MXBukkitLib.debug("[BHPO] Hooked.");
        /*try {
            new File("debug.mxlib/output.bin").createNewFile();
            raf = new RandomAccessFile("debug.mxlib/output.bin", "rw");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }*/
    }

    private static void save(String path, byte[] data) {
        if (raf != null) {
            synchronized (writeLock) {
                try {
                    raf.writeUTF(path);
                    raf.writeShort(data.length);
                    raf.write(data);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    static void load() {
        try {
            Class.forName("java.lang.Object");
        } catch (ClassNotFoundException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private static final BiConsumer<Object, Object>
            CCS_SM_S = (cs, ms) -> ((ConsoleCommandSender) cs).sendMessage((String) ms),
            CCS_SM_SA = (cs, ms) -> ((ConsoleCommandSender) cs).sendMessage((String[]) ms),
            CS_SM_S = (cs, ms) -> ((CommandSender) cs).sendMessage((String) ms),
            CS_SM_SA = (cs, ms) -> ((CommandSender) cs).sendMessage((String[]) ms),
            CCS_SRM = (cs, ms) -> ((ConsoleCommandSender) cs).sendRawMessage((String) ms),
            CON_SRM = (cs, ms) -> ((Conversable) cs).sendRawMessage((String) ms);

    private static void process(BiConsumer<Object, Object> main, Object sender, Object data, Plugin caller) {
        if (caller != null) {
            if (sender instanceof ConsoleCommandSender) {
                if (main == CCS_SM_S || main == CS_SM_S) {
                    caller.getLogger().info(Ansi.ec(String.valueOf(data)));
                    return;
                } else if (main == CCS_SM_SA || main == CS_SM_SA) {
                    if (data != null) {
                        for (var s : (String[]) data) {
                            caller.getLogger().info(Ansi.ec(String.valueOf(s)));
                        }
                    }
                    return;
                } else if (main == CCS_SRM || main == CON_SRM) {
                    Bukkit.getConsoleSender().sendRawMessage("[" + caller.getName() + "] " + data);
                    return;
                }
            }
        }
        main.accept(sender, data);
    }

    public static void sendMessage(ConsoleCommandSender consoleSender, String[] msg) {
        process(CCS_SM_SA, consoleSender, msg, BukkitToolkit.getCallerPlugin());
    }

    public static void sendMessage(ConsoleCommandSender consoleSender, String msg) {
        process(CCS_SM_S, consoleSender, msg, BukkitToolkit.getCallerPlugin());
    }

    public static void sendMessage(CommandSender sender, String[] msg) {
        process(CS_SM_SA, sender, msg, BukkitToolkit.getCallerPlugin());
    }

    public static void sendMessage(CommandSender sender, String msg) {
        process(CS_SM_S, sender, msg, BukkitToolkit.getCallerPlugin());
    }

    public static void sendRawMessage(ConsoleCommandSender sender, String raw) {
        process(CCS_SRM, sender, raw, BukkitToolkit.getCallerPlugin());
    }

    public static void sendRawMessage(Conversable sender, String raw) {
        process(CON_SRM, sender, raw, BukkitToolkit.getCallerPlugin());
    }

    public static byte[] readAll(InputStream stream) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(Math.max(1024, stream.available()));
        Toolkit.IO.writeTo(stream, os);
        return os.toByteArray();
    }

    @Override
    public void post(BukkitHookToolkit.PluginPreLoadEvent event) {
        MXBukkitLib.debug(() -> "[BHPO] Plugin load: " + event.getTarget());
        event.getJar().getJarStreamGetEventHandlers().register(loadEvent -> {
            if (loadEvent.getPath().getName().endsWith(".class")) {
                // MXBukkitLib.debug(() -> "[BHPO] Resolving class for " + event.getTarget() + " " + loadEvent.getPath());
                loadEvent.resolve(source -> {
                    var open = source.get();
                    var stream_data = readAll(open);
                    ClassReader reader;
                    try {
                        reader = new ClassReader(new ByteArrayInputStream(stream_data));
                    } catch (Throwable ignore) {
                        return new ByteArrayInputStream(stream_data);
                    }
                    var writer = new ClassWriter(0);
                    var vs = new ClassVisitor(Opcodes.ASM7, writer) {
                        String path;

                        @Override
                        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                            this.path = name;
                            super.visit(version, access, name, signature, superName, interfaces);
                        }

                        @Override
                        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                            return new MethodVisitor(api, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                                @Override
                                public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                                    if (visible) {
                                        if (descriptor.equals("Lcn/mcres/karlatemp/mxlib/annotations/LambdaFromHidden;")) {
                                            descriptor = "Ljava/lang/invoke/LambdaForm$Hidden;";
                                        }
                                    }
                                    return super.visitAnnotation(descriptor, visible);
                                }

                                @Override
                                public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                                    switch (owner) {
                                        case "org/bukkit/command/ConsoleCommandSender":
                                        case "org/bukkit/command/CommandSender":
                                        case "org/bukkit/conversations/Conversable": {
                                            switch (name) {
                                                case "sendMessage":
                                                case "sendRawMessage": {
                                                    super.visitMethodInsn(Opcodes.INVOKESTATIC,
                                                            "cn/mcres/karlatemp/mxlib/common/plugin_class_definer/BukkitHookedPluginOutput", name,
                                                            Type.getMethodDescriptor(
                                                                    Type.VOID_TYPE, Type.getObjectType(owner), Type.getArgumentTypes(descriptor)[0]
                                                            ), false);
                                                    return;
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                                }
                            };
                        }
                    };
                    reader.accept(vs, 0);
                    byte[] data = writer.toByteArray();
                    save(vs.path, data);
                    return new ByteArrayInputStream(data);
                });
            }
        });
    }


}
