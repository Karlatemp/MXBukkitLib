package cn.mcres.karlatemp.mxlib.logging;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class PrintStreamLogger extends AbstractBaseLogger {
    private final PrefixSupplier prefix;
    private final PrintStream out, err;

    public PrintStreamLogger(
            Object lock,
            IMessageFactory factory,
            PrefixSupplier prefix,
            @NotNull PrintStream out) {
        this(lock, factory, prefix, out, out);
    }

    public PrintStreamLogger(
            Object lock,
            IMessageFactory factory,
            String prefix,
            @NotNull PrintStream out) {
        this(factory, PrefixSupplier.of(prefix), out, out);
    }

    public PrintStreamLogger(
            IMessageFactory factory,
            String prefix,
            @NotNull PrintStream out) {
        this(factory, PrefixSupplier.of(prefix), out);
    }

    public PrintStreamLogger(
            IMessageFactory factory,
            PrefixSupplier prefix,
            @NotNull PrintStream out) {
        this(null, factory, prefix, out);
    }

    public PrintStreamLogger(
            IMessageFactory factory,
            @NotNull String prefix,
            @NotNull PrintStream out,
            @NotNull PrintStream err) {
        this(factory, PrefixSupplier.of(prefix), out, err);
    }

    public PrintStreamLogger(
            IMessageFactory factory,
            PrefixSupplier prefix,
            @NotNull PrintStream out,
            @NotNull PrintStream err) {
        this(null, factory, prefix, out, err);
    }

    public PrintStreamLogger(
            Object lock,
            IMessageFactory factory,
            @NotNull String prefix,
            @NotNull PrintStream out,
            @NotNull PrintStream err) {
        this(lock, factory, PrefixSupplier.of(prefix), out, err);
    }

    public PrintStreamLogger(
            Object lock,
            IMessageFactory factory,
            PrefixSupplier prefix,
            @NotNull PrintStream out,
            @NotNull PrintStream err) {
        super(lock, factory);
        this.prefix = prefix;
        this.out = out;
        this.err = err;
    }

    @Override
    protected void writeLine(String pre, String message, boolean error) {
        if (pre != null && !pre.isEmpty()) message = pre + message;
        (error ? err : out).println(message);
    }

    @Override
    @NotNull
    protected String getPrefix(boolean error, String line, Level lv, LogRecord lr) {
        if (prefix == null) return "";
        return prefix.get(error, line, lv, lr);
    }
}
