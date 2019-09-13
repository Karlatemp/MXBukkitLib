package cn.mcres.karlatemp.mxlib.logging;

import org.jetbrains.annotations.NotNull;

import java.util.MissingResourceException;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.Handler;

public class MLogger extends Logger {
    private final Handler logger;

    /**
     * Protected method to construct a logger for a named subsystem.
     * <p>
     * The logger will be initially configured with a null Level
     * and with useParentHandlers set to true.
     *
     * @param name               A name for the logger.  This should
     *                           be a dot-separated name and should normally
     *                           be based on the package name or class name
     *                           of the subsystem, such as java.net
     *                           or javax.swing.  It may be null for anonymous Loggers.
     * @param resourceBundleName name of ResourceBundle to be used for localizing
     *                           messages for this logger.  May be null if none
     *                           of the messages require localization.
     * @throws MissingResourceException if the resourceBundleName is non-null and
     *                                  no corresponding resource can be found.
     */
    public MLogger(@NotNull String name, String resourceBundleName, @NotNull ILogger logger) {
        this(name, resourceBundleName, new MLoggerHandler(logger));
    }

    /**
     * Protected method to construct a logger for a named subsystem.
     * <p>
     * The logger will be initially configured with a null Level
     * and with useParentHandlers set to true.
     *
     * @param name               A name for the logger.  This should
     *                           be a dot-separated name and should normally
     *                           be based on the package name or class name
     *                           of the subsystem, such as java.net
     *                           or javax.swing.  It may be null for anonymous Loggers.
     * @param resourceBundleName name of ResourceBundle to be used for localizing
     *                           messages for this logger.  May be null if none
     *                           of the messages require localization.
     * @throws MissingResourceException if the resourceBundleName is non-null and
     *                                  no corresponding resource can be found.
     */
    protected MLogger(@NotNull String name, String resourceBundleName, @NotNull Handler handler) {
        super(name, resourceBundleName);
        this.logger = handler;
    }

    @Override
    public Logger getParent() {
        return null;
    }

    @Override
    public void setParent(Logger parent) {
    }

    @Override
    public void log(LogRecord record) {
        if (isLoggable(record.getLevel())) {
            final Filter filter = getFilter();
            if (filter != null) {
                if (!filter.isLoggable(record)) return;
            }
            logger.publish(record);
        }
    }
}
