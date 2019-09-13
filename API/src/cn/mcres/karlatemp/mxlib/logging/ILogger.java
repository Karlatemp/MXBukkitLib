package cn.mcres.karlatemp.mxlib.logging;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.Locale;
import java.util.logging.*;

public interface ILogger {
    /**
     * Message for trying to suppress a null exception.
     */
    String NULL_CAUSE_MESSAGE = "Cannot suppress a null exception.";
    /**
     * Message for trying to suppress oneself.
     */
    String SELF_SUPPRESSION_MESSAGE = "Self-suppression not permitted";
    /**
     * Caption for labeling causative exception stack traces
     */
    String CAUSE_CAPTION = "Caused by: ";
    /**
     * Caption for labeling suppressed exception stack traces
     */
    String SUPPRESSED_CAPTION = "Suppressed: ";

    String getStackTraceElementMessage(StackTraceElement track);

    @NotNull
    default ILogger println(String line) {
        return printf(line);
    }

    @NotNull
    default ILogger printf(Object data) {
        return printf(String.valueOf(data));
    }

    @NotNull
    default ILogger printf(boolean err, String ln) {
        if (err) return error(ln);
        return printf(ln);
    }

    @NotNull
    ILogger printf(String line);

    @NotNull
    ILogger error(String line);

    @NotNull
    default ILogger error(Object line) {
        return error(String.valueOf(line));
    }

    @NotNull
    default ILogger format(String format, Object... args) {
        return printf(String.format(format, args));
    }

    @NotNull
    default ILogger format(Locale locale, String format, Object... args) {
        return printf(String.format(locale, format, args));
    }

    @NotNull
    @SuppressWarnings("SpellCheckingInspection")
    default ILogger errformat(String format, Object... args) {
        return error(String.format(format, args));
    }

    @NotNull
    @SuppressWarnings("SpellCheckingInspection")
    default ILogger errformat(Locale locale, String format, Object... args) {
        return error(String.format(locale, format, args));
    }

    @NotNull
    PrintStream getPrintStream();

    @NotNull
    PrintStream getErrorStream();

    @NotNull
    ILogger printThreadInfo(@NotNull ThreadInfo info, boolean fullFrames, boolean emptyPrefix);

    @NotNull
    default ILogger printThreadInfo(@NotNull Thread thread, boolean fullFrames, boolean emptyPrefix) {
        return printThreadInfo(
                ManagementFactory.getThreadMXBean()
                        .getThreadInfo(
                                new long[]{thread.getId()},
                                true,
                                true)[0],
                fullFrames, emptyPrefix);
    }

    @NotNull
    default ILogger printStackTrace(@NotNull Throwable thr) {
        return printStackTrace(thr, true, true);
    }

    default boolean isError(Level level) {
        return level.intValue() > Level.INFO.intValue();
    }

    @NotNull
    ILogger printStackTrace(@NotNull Throwable thr, boolean printStacks, boolean isError);

    @NotNull
    default ILogger publish(LogRecord record, Handler handler) {
        if (handler == null || record == null) return this;
        if (!handler.isLoggable(record)) return this;
        Filter filter = handler.getFilter();
        if (filter != null && !filter.isLoggable(record)) return this;
        final boolean err = isError(record.getLevel());
        final String msg = record.getMessage();
        final Throwable thrown = record.getThrown();
        synchronized (this) {
            if (msg != null) {
                Formatter formatter = handler.getFormatter();
                record.setThrown(null);
                String mg = formatter.formatMessage(record);
                record.setThrown(thrown);
                printf(err, mg);
            }
            if (thrown != null) {
                printStackTrace(thrown, true, err);
            }
        }
        return this;
    }
}
