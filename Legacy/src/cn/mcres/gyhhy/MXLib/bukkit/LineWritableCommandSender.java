/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: LineWritableCommandSender.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit;

import cn.mcres.gyhhy.MXLib.io.LineAppendable;
import cn.mcres.gyhhy.MXLib.io.LinePrintStream;
import cn.mcres.gyhhy.MXLib.io.LinePrintWriter;
import cn.mcres.gyhhy.MXLib.io.LineWritable;
import cn.mcres.gyhhy.MXLib.log.BasicLogger.PrintingType;
import static cn.mcres.gyhhy.MXLib.log.BasicLogger.toPrintingMessage;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class LineWritableCommandSender implements CommandSender {

    private final LineWritable lw;
    private final String name;
    private boolean iop = true;

    protected PrintingType pt = PrintingType.COLORED;

    public LineWritableCommandSender(String name, PrintStream ps) {
        this(name, (LineWritable) new LinePrintStream(ps));
    }

    public LineWritableCommandSender(String name, PrintWriter ps) {
        this(name, (LineWritable) new LinePrintWriter(ps));
    }

    public LineWritableCommandSender(String name, LineWritable lw) {
        this.lw = lw;
        this.name = name == null ? "" : name;
    }

    public LineWritableCommandSender(String name, Appendable append) {
        if (append == null) {
            throw new NullPointerException();
        } else if (append instanceof PrintStream) {
            lw = new LinePrintStream((PrintStream) append);
        } else if (append instanceof PrintWriter) {
            lw = new LinePrintWriter((PrintWriter) append);
        } else {
            lw = new LineAppendable(append);
        }
        this.name = name == null ? "" : name;
    }

    public PrintingType getPrintingType() {
        return pt;
    }

    public LineWritableCommandSender setPrintingType(PrintingType pt) {
        this.pt = pt;
        return this;
    }

    @Override
    public void sendMessage(String msg) {
        lw.println(toPrintingMessage(msg, pt));
    }

    @Override
    public void sendMessage(String[] lines) {
        for (String s : lines) {
            sendMessage(s);
        }
    }

    @Override
    public Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isPermissionSet(String $) {
        return false;
    }

    @Override
    public boolean isPermissionSet(Permission $) {
        return true;
    }

    @Override
    public boolean hasPermission(String $) {
        return true;
    }

    @Override
    public boolean hasPermission(Permission $) {
        return true;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin $, String $_, boolean _$) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PermissionAttachment addAttachment(Plugin $) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PermissionAttachment addAttachment(Plugin $, String $$, boolean $_, int _$) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PermissionAttachment addAttachment(Plugin $, int $$) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeAttachment(PermissionAttachment $) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void recalculatePermissions() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isOp() {
        return iop;
    }

    @Override
    public void setOp(boolean oop) {
        iop = oop;
    }

}
