/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/19 19:11:32
 *
 * MXLib/mxlib.interpreter/DefaultFileFinder.java
 */

package cn.mcres.karlatemp.mxlib.interpreter;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;

public class DefaultFileFinder implements CommandInclude.CommandFinder {
    private static final Pattern ALLOW =
            Pattern.compile("^[a-z_\\-0-9]+:[a-z_\\-0-9/.]+$");
    private static final Pattern DENY =
            Pattern.compile("\\.\\.");
    private final File base;

    public DefaultFileFinder(File dir) {
        this.base = dir;
    }

    @NotNull
    @Override
    public CommandBlock find(String path) throws IOException {
        if (!ALLOW.matcher(path).find()) {
            throw new FileNotFoundException(path + " not match " + ALLOW);
        }
        if (DENY.matcher(path).find())
            throw new FileNotFoundException(path + " match " + DENY);
        var split = path.indexOf(':');
        var namespace = path.substring(0, split);
        var target = path.substring(split + 1);
        var compiled =/**/ new File(base, namespace + '/' + target + ".vit");
        var source =/*  */ new File(base, namespace + '/' + target + ".txt");
        if (compiled.isFile()) {
            try (FileInputStream from = new FileInputStream(compiled)) {
                var tokens = new ConcurrentLinkedDeque<Token>();
                try (var data = new DataInputStream(from)) {
                    InterpreterCompiler.decompile(tokens, data);
                }
                return InterpreterParser.parse(tokens);
            }
        }
        if (source.isFile()) {
            try (FileInputStream from = new FileInputStream(source)) {
                try (var reader = new InputStreamReader(from, StandardCharsets.UTF_8)) {
                    return InterpreterParser.parse(InterpreterParser.readTokens(reader, path));
                }
            }
        }
        throw new FileNotFoundException(path);
    }
}
