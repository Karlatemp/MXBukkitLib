package cn.mcres.karlatemp.mxlib.logging;

import java.io.UnsupportedEncodingException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class MLoggerHandler extends Handler {
    private final ILogger logger;

    {
        setFormatter(new SimpleFormatter());
        setLevel(Level.INFO);
        try {
            setEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
    }

    public MLoggerHandler(ILogger logger) {
        this.logger = logger;
    }

    @Override
    public void publish(LogRecord record) {
        if (isLoggable(record)) {
            logger.publish(record, this);
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
}