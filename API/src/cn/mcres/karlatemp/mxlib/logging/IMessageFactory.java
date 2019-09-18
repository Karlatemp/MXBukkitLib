package cn.mcres.karlatemp.mxlib.logging;

import java.lang.management.LockInfo;
import java.lang.management.ThreadInfo;

/**
 * 日志信息格式化工厂
 */
public interface IMessageFactory {
    String getStackTraceElementMessage(StackTraceElement stack);

    String getStackTraceElementMessage$return(StackTraceElement stack, String clazz, String zip, String version);

    String dump(LockInfo lockInfo);

    String dump(ThreadInfo inf, boolean fullFrames);

    String CIRCULAR_REFERENCE(Throwable throwable);

    String dump(Throwable thr);

    String excpre(String pre);

    default String toConsole(String cons) {
        return cons;
    }
}
