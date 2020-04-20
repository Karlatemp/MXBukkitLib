/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AlignmentPrefixSupplier.java@author: karlatemp@vip.qq.com: 19-12-7 下午5:22@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.logging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// import java.io.PrintStream;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Fast petty prefix supplier
 * <p><pre>
 * [FMS Class Definer] [INFO] Class end gen.
 * [ FMS Application ] [INFO] Internet port override: 25565
 * [ FakeMineServer  ] [INFO] Starting fake server...
 * [ FakeMineServer  ] [INFO] Server started on port: 25565
 * [ FMS Application ] [INFO] Server Status Request... Modify Message
 * [ FakeMineServer  ] [INFO] /127.0.0.1:60783 lost connection.
 * [     NetWork     ] [WARNING] java.net.SocketTimeoutException: connect timed out
 * [ FakeMineServer  ] [ INFO  ] Karlatemp lost connection.
 * </pre>
 *
 * @since 2.8
 */
public class AlignmentPrefixSupplier implements PrefixSupplier {
    // private static final PrintStream s = System.out;
    protected AtomicInteger
            lvln = new AtomicInteger(4),
            prln = new AtomicInteger(0);
    private PrefixSupplier parent;

    public AlignmentPrefixSupplier(PrefixSupplier parent) {
        this.parent = parent;
    }

    protected static String getEmpty(int length) {
        char[] c = new char[length];
        Arrays.fill(c, ' ');
        return new String(c);
    }

    protected String alignment(int insert, String value) {
        int left = insert / 2;
        int right = insert - left;
        /*s.println(insert);
        s.println(value);
        s.println(left);
        s.println(right);*/
        return getEmpty(left) + value + getEmpty(right);
    }

    protected boolean doInsertLevel(@Nullable Level l) {
        return true;
    }

    @NotNull
    protected String valueOf(@Nullable Level lv) {
        if (lv != null) return lv.getName();
        return "INFO";
    }

    protected int getCharsFontWidth(@NotNull String chars) {
        return chars.length();
    }

    protected int getAlignmentChars(AtomicInteger w, int sw) {
        {
            int v;
            do {
                v = w.get();
                if (v >= sw) {
                    return v - sw;
                }
            } while (!w.compareAndSet(v, sw));
            return 0;
        }
    }

    @NotNull
    @Override
    public String get(boolean error, @Nullable String line, @Nullable Level level, @Nullable LogRecord record) {
        String pre = parent.get(error, line, level, record);
        pre = alignment(getAlignmentChars(prln, getCharsFontWidth(pre)), pre);
        String llv = null;
        if (doInsertLevel(level)) {
            llv = valueOf(level);
            llv = alignment(getAlignmentChars(lvln, getCharsFontWidth(llv)), llv);
        }
        return join(pre, llv);
    }

    protected String join(@NotNull String prefix, @Nullable String level) {
        if (level == null) return "[" + prefix + "] ";
        return "[" + prefix + "] [" + level + "] ";
    }
}
