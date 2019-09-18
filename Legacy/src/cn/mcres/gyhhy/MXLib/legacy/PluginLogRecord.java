/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PluginLogRecord.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.legacy;

import org.bukkit.plugin.Plugin;

import java.time.Clock;
import java.time.Instant;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class PluginLogRecord extends LogRecord {
    private final LogRecord parent;
    private final Plugin plugin;
    private final String prefix;

    /**
     * Construct a LogRecord with the given level and message values.
     * <p>
     * The sequence property will be initialized with a new unique value.
     * These sequence values are allocated in increasing order within a VM.
     * <p>
     * Since JDK 9, the event time is represented by an {@link Instant}.
     * The instant property will be initialized to the {@linkplain
     * Instant#now() current instant}, using the best available
     * {@linkplain Clock#systemUTC() clock} on the system.
     * <p>
     * The thread ID property will be initialized with a unique ID for
     * the current thread.
     * <p>
     * All other properties will be initialized to "null".
     *
     * @param parent The parent
     * @see Clock#systemUTC()
     */
    public PluginLogRecord(LogRecord parent, Plugin plugin, String prefix) {
        super(parent.getLevel(), parent.getMessage());
        this.parent = parent;
        this.plugin = plugin;
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public String getLoggerName() {
        return parent.getLoggerName();
    }

    @Override
    public void setLoggerName(String name) {
        parent.setLoggerName(name);
    }

    @Override
    public ResourceBundle getResourceBundle() {
        return parent.getResourceBundle();
    }

    @Override
    public void setResourceBundle(ResourceBundle bundle) {
        parent.setResourceBundle(bundle);
    }

    @Override
    public String getResourceBundleName() {
        return parent.getResourceBundleName();
    }

    @Override
    public void setResourceBundleName(String name) {
        parent.setResourceBundleName(name);
    }

    @Override
    public Level getLevel() {
        return parent.getLevel();
    }

    @Override
    public void setLevel(Level level) {
        parent.setLevel(level);
    }

    @Override
    public long getSequenceNumber() {
        return parent.getSequenceNumber();
    }

    @Override
    public void setSequenceNumber(long seq) {
        parent.setSequenceNumber(seq);
    }

    @Override
    public String getSourceClassName() {
        return parent.getSourceClassName();
    }

    @Override
    public void setSourceClassName(String sourceClassName) {
        parent.setSourceClassName(sourceClassName);
    }

    @Override
    public String getSourceMethodName() {
        return parent.getSourceMethodName();
    }

    @Override
    public void setSourceMethodName(String sourceMethodName) {
        parent.setSourceMethodName(sourceMethodName);
    }

    @Override
    public String getMessage() {
        return parent.getMessage();
    }

    @Override
    public void setMessage(String message) {
        parent.setMessage(message);
    }

    @Override
    public Object[] getParameters() {
        return parent.getParameters();
    }

    @Override
    public void setParameters(Object[] parameters) {
        parent.setParameters(parameters);
    }

    @Override
    public int getThreadID() {
        return parent.getThreadID();
    }

    @Override
    public void setThreadID(int threadID) {
        parent.setThreadID(threadID);
    }

    @Override
    public Throwable getThrown() {
        return parent.getThrown();
    }

    @Override
    public void setThrown(Throwable thrown) {
        parent.setThrown(thrown);
    }
}
