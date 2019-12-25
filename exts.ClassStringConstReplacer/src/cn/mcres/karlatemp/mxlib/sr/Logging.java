/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Logging.java@author: karlatemp@vip.qq.com: 19-12-22 下午12:38@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.sr;

import cn.mcres.karlatemp.mxlib.logging.*;
import cn.mcres.karlatemp.mxlib.translate.AbstractTranslate;
import cn.mcres.karlatemp.mxlib.translate.MTranslate;
import cn.mcres.karlatemp.mxlib.translate.TranslateLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Logging {
    public static final ILogger logger = new PrintStreamLogger(
            null, new MessageFactoryImpl(), new AlignmentPrefixSupplier(new PrefixSupplier() {
        @NotNull
        @Override
        public String get(boolean error, @Nullable String line, @Nullable Level level, @Nullable LogRecord record) {
            if (record == null) return "<unknown>";
            return record.getLoggerName();
        }
    }) {
        @NotNull
        @Override
        public String get(boolean error, @Nullable String line, @Nullable Level level, @Nullable LogRecord record) {
            return super.get(error, line, level == null ? error ? Level.WARNING : Level.INFO : level, record);
        }
    }, CommandLine.cout, CommandLine.err);
    private static final MLoggerHandler handler = new MLoggerHandler(logger);
    private static final MTranslate translate;

    static {
        System.setOut(logger.getPrintStream());
        System.setErr(logger.getErrorStream());
//        translate = ((Supplier<MTranslate>) () -> {
        MTranslate parent = TranslateLoader.loadTranslate(
                Logging.class::getResourceAsStream,
                "translate",
                null,
                ".properties",
                TranslateLoader.PARSER_PROPERTIES,
                (path, error) -> {
                    System.err.println("Error in loading translate file: " + path);
                    error.printStackTrace();
                }
        );
        translate = new AbstractTranslate() {
            @Override
            public String getValue(@NotNull String key) {
                if (key.startsWith("#")) return key.substring(1);
                return parent.getValue(key);
            }

            @NotNull
            @Override
            public String asMessage(@NotNull String key, Object... params) {
                String k = getValue(key);
                if (k == null)
                    return "Unknown Properties [" + key + "] with params " + Arrays.toString(params);
                return formatter.format(k, params);
            }
        };
//        }).get();
        handler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return null;
            }

            @Override
            public String formatMessage(LogRecord record) {
                if (record.getMessage() == null) return "";
                return translate.asMessage(record.getMessage(), record.getParameters());
            }
        });
    }

    public static void log(String type, Level level, String trans, Object... params) {
        LogRecord record = new LogRecord(level, trans);
        record.setLoggerName(type);
        record.setParameters(params);
        logger.publish(record, handler);
    }

    public static void error(String type, Level level, String reason, Throwable cause) {
        LogRecord record = new LogRecord(level, reason);
        record.setLoggerName(type);
        record.setThrown(cause);
    }

    public static String parseAccess(int access) {
        StringBuilder sb = new StringBuilder();
        if ((access & Opcodes.ACC_PUBLIC) != 0) {
            sb.append("public ");
        }
        if ((access & Opcodes.ACC_DEPRECATED) != 0) {
            sb.append("deprecated ");
        }
        if ((access & Opcodes.ACC_PRIVATE) != 0) {
            sb.append("private ");
        }
        if ((access & Opcodes.ACC_PROTECTED) != 0) {
            sb.append("protected ");
        }
        if ((access & Opcodes.ACC_STATIC) != 0) {
            sb.append("static ");
        }
        if ((access & Opcodes.ACC_ABSTRACT) != 0) {
            sb.append("abstract ");
        }
        if ((access & Opcodes.ACC_ENUM) != 0) {
            sb.append("enum ");
        }
        if ((access & Opcodes.ACC_FINAL) != 0) {
            sb.append("final ");
        }
        if ((access & Opcodes.ACC_TRANSIENT) != 0) {
            sb.append("transient ");
        }
        if ((access & Opcodes.ACC_VOLATILE) != 0) {
            sb.append("volatile ");
        }
        if ((access & Opcodes.ACC_SYNCHRONIZED) != 0) {
            sb.append("synchronized ");
        }
        if ((access & Opcodes.ACC_NATIVE) != 0) {
            sb.append("native ");
        }
        return sb.toString();
    }
}
