/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PluginMXLibLogger.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.legacy;

import cn.mcres.karlatemp.mxlib.logging.MLoggerHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLogger;

import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class PluginMXLibLogger extends PluginLogger {
    private final MLoggerHandler handler;
    private Plugin plugin;
    private String prefix;

    /**
     * Creates a new PluginLogger that extracts the name from a plugin.
     *
     * @param context A reference to the plugin
     */
    public PluginMXLibLogger(Plugin context, MLoggerHandler handler) {
        super(context);
        this.handler = handler;
        this.plugin = context;
        final PluginDescriptionFile description = context.getDescription();
        String pre = context.getName();
        if (description != null) {
            final String prefix = description.getPrefix();
            if (prefix != null && !prefix.isEmpty()) pre = prefix;
        }
        if (pre.contains("Essentials") || pre.contains("CMI")) {
            pre = "§d" + pre;
        } else {
            int hash = Objects.hashCode(pre);
            byte end = 0;
            short a = 0;
            for (int i = 0; i < Integer.SIZE; i += Byte.SIZE / 2, a++) {
                byte ct = (byte) ((hash >>> i) & 0xF);
                switch (a) {
                    case 0: {
                        end |= ct;
                        break;
                    }
                    case 1: {
                        end ^= ct;
                        break;
                    }
                    case 2: {
                        end &= ct;
                        break;
                    }
                    case 3: {
                        end &= ct;
                        break;
                    }
                    case 4: {
                        end |= ~ct;
                        break;
                    }
                    case 5: {
                        end &= ~ct;
                        a = -1;
                        break;
                    }
                }
            }
            end &= 0xF;
            switch (end) {
                case 0:
                    end = 0xb;
                    break;
                case 1:
                    end = 0x3;
                    break;
            }
            pre = "§" + Integer.toHexString(end) + pre;
        }
        prefix = pre;
        LoggerInject.loggerNameMapping.put(context.getClass().getName(), pre);
    }

    @Override
    public void log(LogRecord logRecord) {
        if (isLoggable(logRecord.getLevel())) {
            handler.publish(new PluginLogRecord(logRecord, plugin, prefix));
            // for (Handler other : getHandlers()) other.publish(logRecord);
        }
    }
}
