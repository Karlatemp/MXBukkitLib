/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: HelpTemplate.java@author: karlatemp@vip.qq.com: 2020/1/4 下午1:52@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.command;

import cn.mcres.karlatemp.mxlib.formatter.*;
import cn.mcres.karlatemp.mxlib.formatter.Formatter;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class HelpTemplate {
    public static final Formatter formatter = new PunctuateFormatter("${", "}");
    public static final String LINE, EMPTY;

    static {
        char[] c = new char[100];
        Arrays.fill(c, '=');
        LINE = new String(c);
        Arrays.fill(c, ' ');
        EMPTY = new String(c);
    }

    public static final FormatTemplate
            HEADER = (Locale locale, @NotNull Replacer replacer) -> {
        StringBuilder builder = new StringBuilder();
        replacer.apply(builder, 0);
        int spl = builder.length();
        int inserts = 50 - spl;
        if (inserts < 0) return builder.toString();
        int left = inserts / 2;
        int right = inserts - left;
        builder.insert(0, '[')
                .insert(0, LINE, 0, left)
                .insert(0, "§6")
                .append(']')
                .append(LINE, 0, right);
        return builder.toString();
    }, PARAMETERS = (l, r) -> {
        StringBuilder sb = new StringBuilder();
        @SuppressWarnings("unchecked")
        Map<String, CommandParameter> params = (Map<String, CommandParameter>) r.magicValue();
        int max = 0;
        for (String p : params.keySet()) {
            max = Math.max(max, p.length());
        }
        for (Map.Entry<String, CommandParameter> parameter : params.entrySet()) {
            String n = parameter.getKey();
            sb.append("§6-").append(n);
            sb.append(EMPTY, 0, max - n.length());
            sb.append("§f:§c ").append(parameter.getValue().description());
            if (parameter.getValue().require()) {
                sb.append("§c(require)");
            }
            sb.append('\n');
        }
        return sb.toString();
    }, COMMANDS = (l, r) -> {
        StringBuilder sb = new StringBuilder();
        @SuppressWarnings("unchecked") Map<String, ICommand> cmds = (Map<String, ICommand>) r.magicValue();
        int max = 0;
        for (String p : cmds.keySet()) {
            max = Math.max(max, p.length());
        }
        for (Map.Entry<String, ICommand> parameter : cmds.entrySet()) {
            String n = parameter.getKey();
            sb.append("\n§6").append(n);
            sb.append(EMPTY, 0, max - n.length());
            sb.append("§f:§c ").append(parameter.getValue().description());
        }
        return sb.toString();
    };

    public void processHelp(
            Object sender,
            CommandProvider provider,
            List<String> invokePath,
            List<String> args,
            String label,
            ICommand command) {
        provider.sendMessage(Level.ALL, sender, HEADER.format(null, new AbstractReplacer() {
            @Override
            public void apply(StringBuilder builder, int slot) {
                builder.append(label);
                for (String path : invokePath) {
                    builder.append(' ').append(path);
                }
            }
        }));
        provider.sendMessage(Level.ALL, sender, "§6" + command.description());
        if (command instanceof ICommands) {
            final Map<String, ICommand> subCommands = ((ICommands) command).getSubCommands();
            List<String> cmds = subCommands.keySet().stream().sorted().collect(Collectors.toList());
            if (!cmds.isEmpty()) {
                int start = 0;
                try {
                    start = Integer.parseInt(args.get(0));
                } catch (Throwable ignore) {
                }
                int pageSize = 5;
                try {
                    pageSize = Math.max(1, Integer.parseInt(args.get(1)));
                } catch (Throwable ignore) {
                }
                int pages = cmds.size() / pageSize;
                if (cmds.size() % pageSize > 0) pages++;
                if (start >= 0 && start < pages) {
                    List<String> filt = cmds.subList(start * pageSize, Math.min(cmds.size(), (start + 1) * pageSize));
                    provider.sendMessage(Level.ALL, sender, COMMANDS.format(null, new AbstractReplacer() {
                        @Override
                        public Object magicValue() {
                            return subCommands.entrySet().stream().filter(a -> filt.contains(a.getKey())).collect(
                                    Toolkit.toMapCollector(HashMap::new)
                            );
                        }
                    }));
                }
                int st = start + 1, pg = pages;
                provider.sendMessage(Level.ALL, sender, HEADER.format(null, new AbstractReplacer() {
                    @Override
                    public void apply(StringBuilder builder, int slot) {
                        builder.append(st).append(" / ").append(pg);
                    }
                }));
            }
        } else {
            final Map<String, CommandParameter> parameters = command.parameters();
            provider.sendMessage(Level.ALL, sender, "§c" + command.usage() + "\n");
            if (parameters != null && !parameters.isEmpty()) {
                provider.sendMessage(Level.ALL, sender, PARAMETERS.format(null, new AbstractReplacer() {
                    @Override
                    public Object magicValue() {
                        return parameters;
                    }
                }));
            }
        }
    }
}
