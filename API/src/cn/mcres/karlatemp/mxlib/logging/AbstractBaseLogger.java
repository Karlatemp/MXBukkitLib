package cn.mcres.karlatemp.mxlib.logging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public abstract class AbstractBaseLogger extends AbstractLogger implements PrefixSupplier {
    protected Level publish_level;
    protected LogRecord publish_record;

    public AbstractBaseLogger(IMessageFactory factory) {
        super(factory);
    }

    public AbstractBaseLogger(Object lock, IMessageFactory factory) {
        super(lock, factory);
    }

    protected abstract void writeLine(String pre, String message, boolean error);

    @NotNull
    protected abstract String getPrefix(boolean error, String line, Level level, LogRecord lr);

    @NotNull
    @Override
    public String get(boolean error, @Nullable String line, @Nullable Level level, @Nullable LogRecord lr) {
        return getPrefix(error, line, level, lr);
    }

    @Override
    protected void write(boolean emp_prefix, boolean error, String message) {
        synchronized (lock) {
            String pre = getPrefix(error, message, publish_level, publish_record);
            write(emp_prefix, pre, error, message);
        }
    }

    protected void write(boolean emp_prefix, String pre, boolean error, String message) {
        synchronized (lock) {
            String pre2;
            if (emp_prefix) {
                pre2 = factory.excpre(pre);
            } else {
                pre2 = factory.toConsole(pre);
            }
            pre = factory.toConsole(pre);
            write(emp_prefix, pre, pre2, error, message);
        }
    }

    protected void write(boolean emp_prefix, String pre, String pre2, boolean error, String message) {
        synchronized (lock) {
            while (message != null && !message.isEmpty()) {
                int end = message.indexOf('\n');
                if (end == -1) {
                    writeLine(pre, factory.toConsole(message), error);
                    message = null;
                } else {
                    String cut = message.substring(0, end);
                    message = message.substring(end + 1);
                    writeLine(pre, factory.toConsole(cut), error);
                }
                pre = pre2;
            }
        }
    }

    @NotNull
    @Override
    public ILogger publish(LogRecord record, Handler handler) {
        if (record != null) {
            synchronized (this) {
                try {
                    publish_record = record;
                    publish_level = record.getLevel();
                    return super.publish(record, handler);
                } finally {
                    publish_record = null;
                    publish_level = null;
                }
            }
        }
        return this;
    }
}
