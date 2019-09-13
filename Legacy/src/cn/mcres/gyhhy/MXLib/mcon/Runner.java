/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Runner.java@author: karlatemp@vip.qq.com: 19-9-11 下午1:55@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.mcon;

import cn.mcres.gyhhy.MXLib.log.BasicLogger;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import cn.mcres.karlatemp.mxlib.share.MXBukkitLibPluginStartup;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class Runner extends MCommandSender implements Runnable, Types {

    private final SocketChannel channel;
    private final TListener server;
    private boolean clientClosed = false;
    private final ByteBuffer intReader = ByteBuffer.allocate(4);

    @Override
    public void sendMessage(String message) {
        try {
            if (!clientClosed) {
                write(UTF_8.encode(message));
            }
        } catch (IOException ex) {
        }
    }

    public Runner(SocketChannel sc, TListener server) {
        this.channel = sc;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            BasicLogger l = server.logger;
            channel.configureBlocking(false);
            l.printf("[MCON] MCON connection from: " + channel.getRemoteAddress());
            run0();
        } catch (IOException ex) {
        }
    }

    private synchronized void write(ByteBuffer bb) throws IOException {
        intReader.position(0);
        intReader.putInt(bb.remaining());
        intReader.position(0);
        channel.write(new ByteBuffer[]{intReader, bb});
    }

    private synchronized ByteBuffer read() throws IOException {
        intReader.position(0);
        int rsize = channel.read(intReader);
        if (rsize == -1) {
            clientClosed = true;
            return null;
        } else if (rsize != 4) {
            return null;
        }
        intReader.position(0);
        int size = intReader.getInt();
        ByteBuffer bb = ByteBuffer.allocateDirect(size);
        channel.read(bb);
        bb.position(0);
        return bb;
    }

    protected void invokeCommand(String line) {
        getServer().getScheduler().runTask(MXBukkitLibPluginStartup.plugin, () -> {
            getServer().dispatchCommand(this, line);
        });
    }

    private void run0() throws IOException {
        try {
            ByteBuffer bb = read();
//            server.logger.printf("First Read.." + bb);
            if (bb != null) {
                byte[] pwd = new byte[bb.remaining()];
                bb.get(pwd);
                ByteBuffer bx = ByteBuffer.allocate(50);
//                server.logger.printf("PWD: " + Arrays.toString(pwd));
                if (Arrays.equals(pwd, server.passwd)) {
                    bx.putInt(200).flip();
                    write(bx);
                    while (!clientClosed) {
                        bb = read();
                        if (bb != null) {
                            int type;
                            if (bb.remaining() >= 4) {
                                type = bb.getInt();
                            } else {
                                type = INVOKE_COMMAND;
                            }
                            if (bb.remaining() > 0) {
                                byte[] dd = new byte[bb.remaining()];
                                bb.get(dd);
                                String line = new String(dd, UTF_8);
//                                server.logger.printf("Type: " + type + ", line: " + line);
                                switch (type) {
                                    case INVOKE_COMMAND: {
                                        invokeCommand(line);
                                        break;
                                    }
                                    case ADD_PERMISSION: {
                                        this.addAttachment(MXBukkitLibPluginStartup.plugin, line, true);
                                        break;
                                    }
                                    case REMOVE_PERMISSION: {
                                        Set<PermissionAttachmentInfo> ps = this.getEffectivePermissions();
                                        Set<PermissionAttachment> rms = new HashSet<>();
                                        for (PermissionAttachmentInfo inf : ps) {
                                            if (inf.getPermission().equalsIgnoreCase(line)) {
                                                rms.add(inf.getAttachment());
                                            }
                                        }
                                        for (PermissionAttachment rn : rms) {
                                            this.removeAttachment(rn);
                                        }
                                        if (this.hasPermission(line)) {
                                            this.addAttachment(MXBukkitLibPluginStartup.plugin, line, false);
                                        }
                                        break;
                                    }
                                    case SET_NAME: {
                                        setName(line);
                                        break;
                                    }
                                    case SET_OP: {
                                        setOp(parseBool(line));
                                        break;
                                    }
                                }
                            } else {
                                if (type == STOP) {
                                    clientClosed = true;
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    bx.putInt(400).put("Worning Passwd".getBytes()).flip();
                    write(bx);
                }
            }
        } finally {
            channel.close();
        }
    }

    protected void setName(String name) {
        this.name = name;
    }
}
