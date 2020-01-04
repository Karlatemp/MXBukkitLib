/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DefaultCommand.java@author: karlatemp@vip.qq.com: 2019/12/29 下午3:40@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.command;

import cn.mcres.karlatemp.mxlib.command.annoations.*;
import cn.mcres.karlatemp.mxlib.command.exceptions.ParserFailToParseException;
import cn.mcres.karlatemp.mxlib.command.internal.DefaultParameterParser;
import cn.mcres.karlatemp.mxlib.command.internal.Tools;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class DefaultCommand implements ICommand {
    private final String name;
    private final String permission;
    private final String noPermissionMessage;
    private final String description;
    private final String usage;
    private final Method method;
    private final Object self;
    private final ParamSlot[] solts;
    private final CommandProvider provider;
    protected String helpind = "-?";

    private static class Arguments implements CommandParameter {
        String name, description;
        Class<?> type;
        CommandParamParser parser;
        boolean require;
        boolean hasParam;
        Object value;

        @Override
        public String name() {
            return name;
        }

        @Override
        public Class<?> type() {
            return type;
        }

        @Override
        public String description() {
            return description;
        }

        @Override
        public CommandParamParser parser() {
            return parser;
        }

        @Override
        public boolean require() {
            return require;
        }
    }

    private Class<?> paramType;

    private interface ParamSlot {
        Object get(Object sender, Map<String, Arguments> ag, List<String> un_parsed, List<String> full, List<String> fullCommandArgument, String label_z);
    }

    public DefaultCommand(String name, String permission, String noPermissionMessage,
                          String description, String usage,
                          Method method, Object self, CommandProvider provider) {
        this.name = name;
        this.permission = permission;
        this.noPermissionMessage = noPermissionMessage;
        this.description = description;
        this.usage = usage;
        this.method = method;
        this.self = self;
        this.solts = parseSolts(method);
        this.provider = provider;
    }

    @Override
    public String usage() {
        return usage;
    }

    @Override
    public String description() {
        return description;
    }

    private Map<String, Arguments> args = new HashMap<>();

    private ParamSlot[] parseSolts(Method method) {
        int size;
        ParamSlot[] s = new ParamSlot[size = method.getParameterCount()];
        final Annotation[][] annotations = method.getParameterAnnotations();
        final Class<?>[] types = method.getParameterTypes();
        next:
        for (int i = 0; i < size; i++) {
            Annotation[] ann = annotations[i];
            for (Annotation a : ann) {
                if (a instanceof MLabel) {
                    if (String.class.isAssignableFrom(types[i])) {
                        s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> label_z;
                    } else {
                        s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> null;
                    }
                } else if (a instanceof MProvider) {
                    if (types[i].isInstance(provider)) {
                        s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> provider;
                    } else {
                        s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> null;
                    }
                } else if (a instanceof MSender) {
                    s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> sender;
                    if (paramType != null) {
                        throw new RuntimeException("Cannot input double Sender.");
                    }
                    paramType = types[i];
                    continue next;
                } else if (a instanceof MArguments) {
                    final boolean full1 = ((MArguments) a).full();
                    final boolean allLine = ((MArguments) a).allLine();
                    if (types[i].isArray()) {
                        if (full1) {
                            if (allLine) {
                                s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> fullCommandArgument.toArray(new String[0]);
                            } else {
                                s[i] = (sender, ag, un_parsed, full, fullCommandArgc, label_z) -> full.toArray(new String[0]);
                            }
                        } else {
                            s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> un_parsed.toArray(new String[0]);
                        }
                    } else {
                        if (full1) {
                            if (allLine) {
                                s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> fullCommandArgument;
                            } else {
                                s[i] = (sender, ag, un_parsed, full, fullCommandArgc, label_z) -> full;
                            }
                        } else {
                            s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> un_parsed;
                        }
                    }
                    continue next;
                } else if (a instanceof MParameter) {
                    MParameter par = (MParameter) a;
                    Class<?> type = types[i];
                    Arguments ag = new Arguments();
                    ag.name = par.name();
                    ag.hasParam = par.hasParameter();
                    ag.description = par.description();
                    ag.require = par.require();
                    ag.type = type;
                    this.args.put(ag.name, ag);
                    try {
                        ag.parser = par.parser().getConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new IllegalArgumentException(e);
                    }
                    if (!par.hasParameter() && types[i] == boolean.class) {
                        ag.require = false;
                        s[i] = (sender, ags, un_parsed, full, fullCommandArgument, label_z) -> ags.get(par.name()) != null;
                        continue next;
                    }

                    s[i] = (sender, ags, un_parsed, full, fullCommandArgument, label_z) -> {
                        final Arguments arguments = ags.get(par.name());
                        if (arguments == null) {
                            return ag.parser.getDefaultValue(ag.type);
                        }
                        return arguments.value;
                    };
                    continue next;
                }
            }
            Class<?> type = types[i];
            s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> DefaultParameterParser.getDefault(type);
        }
        return s;
    }

    protected final Object check(Object sender) {
        sender = provider.resolveSender(sender, null);
        if (sender == null) {
            throw new UnsupportedOperationException("Cannot resolve sender.");
        }
        if (!provider.hasPermission(sender, permission)) {
            provider.noPermission(sender, this);
            return null;
        }
        if (paramType != null) {
            Object s = provider.resolveSender(sender, paramType);
            if (s == null) {
                provider.senderNotResolve(sender, paramType);
                return null;
            }
            return s;
        }
        return sender;
    }

    @Override
    public void invoke(Object sender, String label, @NotNull List<String> arguments, @NotNull List<String> fillArguments) {
        List<String> parsing = new ArrayList<>(arguments),
                unprocess = new ArrayList<>();
        sender = check(sender);
        if (sender == null) return;
        if (Tools.processHelp(this.helpind, arguments, fillArguments, sender, provider, this, label)) {
            return;
        }
        Map<String, Arguments> exists = new HashMap<>();
        while (!parsing.isEmpty()) {
            String tok = parsing.remove(0);
            if (!tok.isEmpty()) {
                if (tok.charAt(0) == '-') {//Option arg
                    String opt = tok.substring(1);
                    if (exists.containsKey(opt)) {
                        provider.sendMessage(Level.INFO, sender, "Duplicate option: " + tok);
                        return;
                    }
                    final Arguments option = args.get(opt);
                    if (option == null) {
                        provider.sendMessage(Level.INFO, sender, "Unknown option: " + tok);
                        return;
                    }
                    final Object value;
                    try {
                        value = option.parser.apply(tok, parsing, option.hasParam, option.type);
                    } catch (ParserFailToParseException e) {
                        e.sendToSender(provider, sender);
                        return;
                    }
                    Arguments copy = new Arguments();
                    copy.type = option.type;
                    copy.hasParam = option.hasParam;
                    copy.value = value;
                    exists.put(opt, copy);
                    continue;
                }
            }
            unprocess.add(tok);
        }
        {
            final List<String> requires = args.values().stream().filter(a -> a.require).map(a -> a.name).collect(Collectors.toList());
            final List<String> un_process = requires.stream().filter(key -> !exists.containsKey(key)).map(a -> '-' + a).collect(Collectors.toList());
            if (!un_process.isEmpty()) {
                provider.sendMessage(Level.INFO, sender, "Need more options " + un_process);
                return;
            }
        }
        Object[] args = new Object[method.getParameterCount()];
        for (int i = 0; i < args.length; i++) {
            args[i] = this.solts[i].get(
                    sender, exists, unprocess, arguments,
                    fillArguments, label);
        }
        try {
            System.out.println(Arrays.toString(args));
            method.invoke(self, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public String noPermissionMessage() {
        return noPermissionMessage;
    }

    @Override
    public void tabCompile(Object sender, @NotNull List<String> result, @NotNull List<String> fillArguments, @NotNull List<String> args) {
        if (args.isEmpty()) {
            result.addAll(
                    this.args.keySet().stream()
                            .map(x -> '-' + x).sorted()
                            .collect(Collectors.toList())
            );
            return;
        }
        Set<String> exists = new HashSet<>();
        String last;
        do {
            String l = last = args.remove(0);
            if (!l.isEmpty()) {
                if (l.charAt(0) == '-') {
                    String a = l.substring(1);
                    if (args.isEmpty()) {
                        String b = a.toLowerCase();
                        result.addAll(
                                this.args.keySet().stream()
                                        .filter(w -> !exists.contains(w))
                                        .filter(w -> w.toLowerCase().contains(b))
                                        .map(x -> '-' + x).sorted()
                                        .collect(Collectors.toList())
                        );
                        return;
                    } else {
                        if (exists.contains(a)) return;// Duplicate option
                        if (this.args.containsKey(a)) {
                            final Arguments param = this.args.get(a);
                            exists.add(a);
                            if (param.type == boolean.class && !param.hasParam) continue;
                            if (param.parser.skipParams(args, param.type)) {
                                continue;
                            } else {
                                param.parser.tabCompile(result, args, param.type);
                                return;
                            }
                        } else {
                            return;// Broken option
                        }
                    }
                } else {
                    if (args.isEmpty()) return;
                }
            }
        } while (!args.isEmpty());
        if (last.isEmpty())
            result.addAll(
                    this.args.keySet().stream()
                            .filter(w -> !exists.contains(w))
                            .map(x -> '-' + x).sorted()
                            .collect(Collectors.toList())
            );
    }

    @Override
    public void doHelp(Object sender, @NotNull List<String> args, @NotNull List<String> fullArguments) {
        sender = check(sender);
        if (sender == null) return;

    }

    @Override
    public Map<String, CommandParameter> parameters() {
        if (args.isEmpty()) return Collections.emptyMap();
        return ImmutableMap.copyOf(this.args);
    }
}
