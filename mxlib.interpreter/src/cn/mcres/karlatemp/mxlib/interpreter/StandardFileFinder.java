/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/18 22:50:06
 *
 * MXLib/mxlib.interpreter/StandardFileFinder.java
 */

package cn.mcres.karlatemp.mxlib.interpreter;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;

public class StandardFileFinder implements CommandInclude.CommandFinder {
    private final String suffix;
    private final File dir;
    private final boolean allowUnsafeAccessing;

    public StandardFileFinder(File dir, String suffix, boolean allowUnsafeAccessing) {
        this.dir = dir;
        this.suffix = suffix;
        this.allowUnsafeAccessing = true;
    }

    public static String parse(String path) throws IOException {

        var deque = new ArrayDeque<String>();
        path = path.replace('\\', '/');
        var a = path.indexOf('/');
        if (a == -1) {
            if (path.equals("..")) unsafeAccess(path);
        } else if (a == 0) unsafeAccess(path);
        var temp = path.substring(0, a);
        if (temp.equals("..")) unsafeAccess(path);
        deque.add(temp);
        a++;
        do {
            var e = path.indexOf('/', a);
            String tok;
            if (e == -1) {
                tok = path.substring(a);
            } else {
                tok = path.substring(a, e);
                a = e + 1;
            }
            if (tok.equals("..")) {
                if (deque.isEmpty()) unsafeAccess(path);
                deque.removeLast();
            } else {
                deque.add(tok);
            }
            if (e == -1) break;
        } while (true);
        return String.join("/", deque);
    }

    @NotNull
    @Override
    public CommandBlock find(String path) throws IOException {
        if (!allowUnsafeAccessing) {
            path = parse(path);
        }
        try (var stream = new FileInputStream(new File(dir, path + suffix))) {
            try (var reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                return InterpreterParser.parse(InterpreterParser.readTokens(reader, path));
            }
        }
    }

    public static void unsafeAccess(String path) throws FileNotFoundException {
        throw new FileNotFoundException("Unsafe Access: " + path);
    }
}
