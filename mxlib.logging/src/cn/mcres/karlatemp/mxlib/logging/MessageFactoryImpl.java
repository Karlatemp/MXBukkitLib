/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: MessageFactoryImpl.java@author: karlatemp@vip.qq.com: 2020/2/14 下午7:25@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.logging;

import java.io.UnsupportedEncodingException;
import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 一个基础的工厂, 没有任何颜色
 */
@SuppressWarnings("Duplicates")
public class MessageFactoryImpl implements IMessageFactory {
    static boolean CharSet;

    static {
        try {
            URLDecoder.decode("", StandardCharsets.UTF_8);
            CharSet = true;
        } catch (Throwable ignore) {
        }
    }

    protected String unuri(String x) {
        if (CharSet) {
            return URLDecoder.decode(x, StandardCharsets.UTF_8);
        } else {
            try {
                //noinspection CharsetObjectCanBeUsed
                return URLDecoder.decode(x, "utf-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String excpre(String pre) {
        int lg = pre.length();
        // \u4E00-\u9FFF
        for (char c : pre.toCharArray()) {
            if (c >= 0x4E00 && c <= 0x9FFF) {
                lg += 1;
            }
        }
        char[] temp = new char[lg];
        Arrays.fill(temp, ' ');
        return new String(temp);
    }

    protected String getStackTraceElementMessage$zip(
            Class<?> c, URL url, StackTraceElement stack) throws Throwable {
        if (url == null) return null;
        String u = url.toString();
        if (u.startsWith("jar:file:")) {
            String wd = u.substring(9);
            int ed = wd.indexOf('!');
            if (ed != -1) {
                wd = wd.substring(0, ed);
            }
            ed = wd.lastIndexOf('/');
            if (ed != -1) {
                wd = wd.substring(ed + 1);
            }
            return unuri(wd);
        } else if (u.startsWith("file:")) {
            int x = u.lastIndexOf('/');
            return unuri(u.substring(x + 1));
        } else if (u.startsWith("jrt:")) {
            return "JavaRuntime";
        }
        return null;
    }

    protected String getStackTraceElementMessage$version(
            Class<?> c, URL url, StackTraceElement stack) throws Throwable {
        Package p = c.getPackage();
        if (p != null) {
            return p.getImplementationVersion();
        }
        return null;
    }

    protected Class<?> forName(String name) throws ClassNotFoundException {
        try {
            return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
        } catch (Throwable thr) {
            return Class.forName(name, false, getClass().getClassLoader());
        }
    }

    @Override
    public String getStackTraceElementMessage(StackTraceElement stack) {
        String zip = null;
        String version = null;
        String clazz = stack.getClassName();
        try {
            Class<?> c = forName(clazz);
            URL url = c.getResource(c.getSimpleName() + ".class");
            zip = getStackTraceElementMessage$zip(c, url, stack);
            version = getStackTraceElementMessage$version(c, url, stack);
        } catch (ClassNotFoundException | NoClassDefFoundError ex) {
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        return getStackTraceElementMessage$return(stack, clazz, zip, version);
    }

    protected final String getClassLoaderName(StackTraceElement elm) {
        return MessageFactory$return.ret.getClassLoaderName(elm);
    }

    protected final String getModuleName(StackTraceElement elm) {
        return MessageFactory$return.ret.getModuleName(elm);
    }

    protected final String getModuleVersion(StackTraceElement elm) {
        return MessageFactory$return.ret.getModuleVersion(elm);
    }

    @Override
    public String getStackTraceElementMessage$return(
            StackTraceElement stack, String clazz, String zip,
            String version) {
        StringBuilder sb = new StringBuilder(70);
        sb.append("\tat ");
        boolean $$ = false;
        final String classLoaderName = getClassLoaderName(stack);
        if (classLoaderName != null && !classLoaderName.isEmpty()) {
            sb.append(classLoaderName);
            $$ = true;
        }
        final String moduleName = getModuleName(stack);
        if (moduleName != null && !moduleName.isEmpty()) {
            sb.append(moduleName);
            final String version1 = getModuleVersion(stack);
            if (version1 != null && !version1.isEmpty()) {
                sb.append('@').append(version1);
            }
            $$ = true;
        }
        if ($$) {
            sb.append('/');
        }
        sb.append(clazz)
                .append('.').append(stack.getMethodName()).append('(');
        if (stack.isNativeMethod()) {
            sb.append("Native Method");
        } else {
            String fname = stack.getFileName();
            if (fname == null) {
                sb.append("Unknown Source");
            } else {
                sb.append(fname);
                int num = stack.getLineNumber();
                if (num > -1) {
                    sb.append(':').append(num);
                }
            }
        }
        sb.append(") [");
        if (zip == null) {
            sb.append('?');
        } else {
            sb.append(zip);
        }
        sb.append(':');
        if (version == null) {
            sb.append('?');
        } else {
            sb.append(version);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String dump(LockInfo lockInfo) {
        return lockInfo.getClassName() + '@' + lockInfo.getIdentityHashCode();
    }

    protected String colored(Thread.State st) {
        return String.valueOf(st);
    }

    @Override
    public String dump(ThreadInfo inf, boolean fullFrames) {
        final LockInfo lockInfo = inf.getLockInfo();
        final Thread.State threadState = inf.getThreadState();
        StringBuilder sb = new StringBuilder("Thread Info: ")
                .append(inf.getThreadName()).append("[id=")
                .append(inf.getThreadId()).append("] ")
                .append(colored(threadState))
                .append(" BlockCount[").append(inf.getBlockedCount())
                .append("] BlockTime[").append(inf.getBlockedTime()).append("]");
        if (inf.getLockName() != null) {
            sb.append(" lock on ").append(inf.getLockName());
        }
        if (inf.getLockOwnerName() != null) {
            sb.append(" owned by ").append(inf.getLockOwnerName())
                    .append("[id=").append(inf.getLockOwnerId());
        }
        if (inf.isSuspended()) {
            sb.append(" (suspended)");
        }
        if (inf.isInNative()) {
            sb.append(" (in native)");
        }
        sb.append('\n');
        int i = 0;
        final StackTraceElement[] stackTrace = inf.getStackTrace();
        final int MAX_FRAMES = fullFrames ? stackTrace.length : 8;
        for (; i < stackTrace.length && i < MAX_FRAMES; i++) {
            StackTraceElement ste = stackTrace[i];
            sb.append(getStackTraceElementMessage(ste));
            sb.append('\n');
            if (i == 0 && lockInfo != null) {
                Thread.State ts = threadState;
                switch (ts) {
                    case BLOCKED:
                        sb.append("\t-  blocked on ").append(dump(lockInfo)).append('\n');
                        break;
                    case WAITING:
                        sb.append("\t-  waiting on ").append(dump(lockInfo)).append('\n');
                        break;
                    case TIMED_WAITING:
                        sb.append("\t-  waiting on ").append(dump(lockInfo)).append('\n');
                        break;
                    default:
                }
            }

            for (MonitorInfo mi : inf.getLockedMonitors()) {
                if (mi.getLockedStackDepth() == i) {
                    sb.append("\t-  locked ").append(dump(mi));
                    sb.append('\n');
                }
            }
        }
        if (i < stackTrace.length) {
            sb.append("\t...");
            sb.append('\n');
        }

        LockInfo[] locks = inf.getLockedSynchronizers();
        if (locks.length > 0) {
            sb.append("\n\tNumber of locked synchronizers = ").append(locks.length);
            sb.append('\n');
            for (LockInfo li : locks) {
                sb.append("\t- ").append(dump(li));
                sb.append('\n');
            }
        }
        return sb.toString().trim();
    }

    @Override
    public String CIRCULAR_REFERENCE(Throwable throwable) {
        return "\t[CIRCULAR REFERENCE:" + dump(throwable) + "\u00a76]";
    }

    @Override
    public String dump(Throwable thr) {
        String s = thr.getClass().getName();
        String message = thr.getLocalizedMessage();
        //"\u00a7c%s\u00a7b: \u00a7e%s"
        return (message != null) ? (s + ": " + message) : s;
    }
}
