package cn.mcres.karlatemp.mxlib.logging;

import cn.mcres.karlatemp.mxlib.tools.StringHelper;

import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;

/**
 * 带ANSI支持的日志信息工厂
 */
@SuppressWarnings("Duplicates")
public class MessageFactoryAnsi extends MessageFactoryImpl {
    @Override
    public String getStackTraceElementMessage$return(StackTraceElement stack, String clazz, String zip, String version) {
        StringBuilder sb = new StringBuilder(70);
        sb.append("\t\u00a76at ");

        boolean $$ = false;
        final String classLoaderName = getClassLoaderName(stack);
        if (classLoaderName != null && !classLoaderName.isEmpty()) {
            sb.append("§2").append(classLoaderName);
            $$ = true;
        }
        final String moduleName = getModuleName(stack);
        if (moduleName != null && !moduleName.isEmpty()) {
            sb.append("§1").append(moduleName);
            final String version1 = getModuleVersion(stack);
            if (version1 != null && !version1.isEmpty()) {
                sb.append("§r@§6").append(version1);
            }
            $$ = true;
        }
        if ($$) {
            sb.append("§r/");
        }


        sb.append("\u00a7c").append(clazz)
                .append("\u00a7r.\u00a7e").append(stack.getMethodName()).append("\u00a7r(");
        if (stack.isNativeMethod()) {
            sb.append("\u00a7dNative Method");
        } else {
            String fname = stack.getFileName();
            if (fname == null) {
                sb.append("\u00a77Unknown Source");
            } else {
                sb.append("\u00a72").append(fname);
                int num = stack.getLineNumber();
                if (num > -1) {
                    sb.append("\u00a7f:\u00a76").append(num);
                }
            }
        }
        sb.append("\u00a7r) [\u00a7b");
        if (zip == null) {
            sb.append('?');
        } else {
            sb.append(zip);
        }
        sb.append("\u00a76:\u00a7d");
        if (version == null) {
            sb.append('?');
        } else {
            sb.append(version);
        }
        sb.append("\u00a7r]");
        return sb.toString();
    }

    @Override
    public String dump(LockInfo lockInfo) {
        return "\u00a72" + lockInfo.getClassName() + "\u00a76@\u00a7b"
                + lockInfo.getIdentityHashCode();
    }

    @Override
    protected String colored(Thread.State st) {
        String p = "";
        switch (st) {
            case BLOCKED:
                p = "\u00a7c";
            case NEW:
                p = "\u00a76";
            case RUNNABLE:
                p = "\u00a7a";
            case TERMINATED:
                p = "\u00a7d";
            case TIMED_WAITING:
                p = "\u00a77";
            case WAITING:
                p = "\u00a74";
        }
        return p + st;
    }

    @Override
    public String dump(ThreadInfo inf, boolean fullFrames) {
        final LockInfo lockInfo = inf.getLockInfo();
        final Thread.State threadState = inf.getThreadState();
        StringBuilder sb = new StringBuilder("\u00a79Thread Info: \u00a7b")
                .append(inf.getThreadName()).append("\u00a7r[\u00a7bid\u00a7r=\u00a75")
                .append(inf.getThreadId()).append("\u00a7r] ")
                .append(colored(threadState)).append(threadState)
                .append("\u00a79 BlockCount\u00a7r[\u00a75").append(inf.getBlockedCount())
                .append("\u00a7r] BlockTime\u00a7r[\u00a75").append(inf.getBlockedTime()).append("\u00a7r]");
        if (inf.getLockName() != null) {
            sb.append("\u00a79 lock on \u00a7b").append(inf.getLockName());
        }
        if (inf.getLockOwnerName() != null) {
            sb.append("\u00a79 owned by \u00a7b").append(inf.getLockOwnerName())
                    .append("\u00a7r[\u00a7bid\u00a7r=\u00a75").append(inf.getLockOwnerId())
                    .append("\u00a7r");
        }
        sb.append("\u00a79");
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
            sb.append("\n\t\u00a79Number of locked synchronizers = \u00a79").append(locks.length);
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
        return "\t\u00a76[CIRCULAR REFERENCE:" + dump(throwable) + "\u00a76]";
    }

    @Override
    public String dump(Throwable thr) {
        String s = thr.getClass().getName();
        String message = thr.getLocalizedMessage();
        //"\u00a7c%s\u00a7b: \u00a7e%s"
        return (message != null) ? (s + "\u00a7b: \u00a7e" + message) : s;
    }

    @Override
    public String excpre(String pre) {
        char end = 0;
        int lg = pre.length();
        // \u4E00-\u9FFF
        char[] cs = (pre.toCharArray());
        fe:
        for (int i = 0; i < cs.length; i++) {
            char c = cs[i];
            switch (c) {
                case '\u00a7': {
                    end = cs[++i];
                    lg -= 2;
                    continue fe;
                }
            }
            if (c >= 0x4E00 && c <= 0x9FFF) {
                lg += 1;
            }
        }
        String b;
        if (end != 0) b = "\u00a7" + end;
        else b = "";
        return StringHelper.fill(' ', lg) + b;
    }

    @Override
    public String toConsole(String cons) {
        return Ansi.ec(cons) + Ansi.RESET;
    }
}
