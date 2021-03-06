/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: MessageFactoryAnsi.java@author: karlatemp@vip.qq.com: 2020/2/14 下午7:24@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.logging;

//import cn.mcres.karlatemp.mxlib.tools.StringHelper;

import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.nio.CharBuffer;
import java.util.Arrays;

/**
 * 带ANSI支持的日志信息工厂
 */
@SuppressWarnings("Duplicates")
public class MessageFactoryAnsi extends MessageFactoryImpl {
    @Override
    public String getStackTraceElementMessage$return(StackTraceElement stack, String clazz, String zip, String version) {
        StringBuilder sb = new StringBuilder(70);
        sb.append("\t" + Ansi._6 + "at ");

        boolean $$ = false;
        final String classLoaderName = getClassLoaderName(stack);
        if (classLoaderName != null && !classLoaderName.isEmpty()) {
            sb.append(Ansi._2).append(classLoaderName);
            $$ = true;
        }
        final String moduleName = getModuleName(stack);
        if (moduleName != null && !moduleName.isEmpty()) {
            sb.append(Ansi._1).append(moduleName);
            final String version1 = getModuleVersion(stack);
            if (version1 != null && !version1.isEmpty()) {
                sb.append(Ansi.RESET + "@" + Ansi._6).append(version1);
            }
            $$ = true;
        }
        if ($$) {
            sb.append(Ansi.RESET + "/");
        }


        sb.append(Ansi._C).append(clazz)
                .append(Ansi.RESET + "." + Ansi._E).append(stack.getMethodName()).append(Ansi.RESET + "(");
        if (stack.isNativeMethod()) {
            sb.append(Ansi._D + "Native Method");
        } else {
            String fname = stack.getFileName();
            if (fname == null) {
                sb.append(Ansi._7 + "Unknown Source");
            } else {
                sb.append(Ansi._2).append(fname);
                int num = stack.getLineNumber();
                if (num > -1) {
                    sb.append(Ansi._F + ":" + Ansi._6).append(num);
                }
            }
        }
        sb.append(Ansi.RESET + ") [" + Ansi._B);
        if (zip == null) {
            sb.append('?');
        } else {
            sb.append(zip);
        }
        sb.append(Ansi._6 + ":" + Ansi._D);
        if (version == null) {
            sb.append('?');
        } else {
            sb.append(version);
        }
        sb.append(Ansi.RESET + "]");
        return sb.toString();
    }

    @Override
    public String dump(LockInfo lockInfo) {
        return Ansi._2 + lockInfo.getClassName() + Ansi._6 + "@" + Ansi._B
                + lockInfo.getIdentityHashCode();
    }

    @Override
    protected String colored(Thread.State st) {
        String p = "";
        switch (st) {
            case BLOCKED:
                p = Ansi._C;
                break;
            case NEW:
                p = Ansi._6;
                break;
            case RUNNABLE:
                p = Ansi._A;
                break;
            case TERMINATED:
                p = Ansi._D;
                break;
            case TIMED_WAITING:
                p = Ansi._7;
                break;
            case WAITING:
                p = Ansi._4;
                break;
        }
        return p + st;
    }

    @Override
    public String dump(ThreadInfo inf, boolean fullFrames) {
        final LockInfo lockInfo = inf.getLockInfo();
        final Thread.State threadState = inf.getThreadState();
        StringBuilder sb = new StringBuilder(Ansi._9 + "Thread Info: " + Ansi._B)
                .append(inf.getThreadName()).append(Ansi.RESET + "[" + Ansi._B + "id" + Ansi.RESET + "=" + Ansi._5)
                .append(inf.getThreadId()).append(Ansi.RESET + "] ")
                .append(colored(threadState))
                .append(Ansi._9 + " BlockCount" + Ansi.RESET + "[" + Ansi._5).append(inf.getBlockedCount())
                .append(Ansi.RESET + "] BlockTime" + Ansi.RESET + "[" + Ansi._5).append(inf.getBlockedTime()).append(Ansi.RESET + "]");
        if (inf.getLockName() != null) {
            sb.append(Ansi._9 + " lock on " + Ansi._B).append(inf.getLockName());
        }
        if (inf.getLockOwnerName() != null) {
            sb.append(Ansi._9 + " owned by " + Ansi._B).append(inf.getLockOwnerName())
                    .append(Ansi.RESET + "[" + Ansi._B + "id" + Ansi.RESET + "=" + Ansi._5).append(inf.getLockOwnerId())
                    .append(Ansi.RESET);
        }
        sb.append(Ansi._9);
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
            sb.append("\n\t" + Ansi._9 + "Number of locked synchronizers = ").append(locks.length);
            sb.append('\n');
            for (LockInfo li : locks) {
                sb.append("\t- ").append(dump(li));
                sb.append('\n');
            }
        }
        int len = sb.length();
        while (len > 0) {
            char c = sb.charAt(len - 1);
            if (c >= ' ') {
                break;
            } else len--;
        }
        sb.setLength(len);
        return sb.toString();
    }

    @Override
    public String CIRCULAR_REFERENCE(Throwable throwable) {
        return "\t" + Ansi._6 + "[CIRCULAR REFERENCE:" + dump(throwable) + Ansi._6 + "]";
    }

    @Override
    public String dump(Throwable thr) {
        String s = thr.getClass().getName();
        String message = thr.getLocalizedMessage();
        //"\u00a7c%s\u00a7b: \u00a7e%s"
        return (message != null) ? (s + Ansi._B + ": " + Ansi._E + message) : s;
    }

    @Override
    public String excpre(String pre) {
        int s = -1, e = -1;
        CharBuffer buffer = CharBuffer.wrap(pre.toCharArray());
        // \u4E00-\u9FFF
        int length = 0;
        while (buffer.hasRemaining()) {
            char next = buffer.get();
            if (next == '\033') {
                if (buffer.hasRemaining()) {
                    int pos = buffer.position();
                    if (buffer.get() != '[') {
                        buffer.position(pos);
                        continue;
                    }
                    s = pos - 1;
                    while (buffer.hasRemaining()) {
                        if (buffer.get() == 'm') {
                            e = buffer.position();
                            break;
                        }
                    }
                } else {
                    length++;
                }
            } else {
                length++;
                if (next >= '\u4E00' && next <= '\u9FFF')
                    length++;
            }
        }
        boolean hasAnsi = e > s;
        int begin = length;
        if (hasAnsi) {
            length += e - s;
        }
        char[] temp = new char[length];
        Arrays.fill(temp, ' ');
        if (hasAnsi) {
            char[] array = buffer.array();
            System.arraycopy(array, s, temp, begin, begin - length);
        }
        return new String(temp);
    }

    @Override
    public String toConsole(String cons) {
        return cons + Ansi.RESET;
    }
}
