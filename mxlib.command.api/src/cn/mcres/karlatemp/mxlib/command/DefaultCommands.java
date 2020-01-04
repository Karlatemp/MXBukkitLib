/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DefaultCommands.java@author: karlatemp@vip.qq.com: 2019/12/29 下午2:51@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.command;

import cn.mcres.karlatemp.mxlib.command.internal.Tools;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class DefaultCommands implements ICommands {
    private final String name;
    private final String permission;
    private final String permissionMessage;
    private final CommandProvider provider;
    private final String description;

    @Override
    public String description() {
        return description;
    }

    public DefaultCommands(String name, String permission,
                           String permissionMessage,
                           String description,
                           CommandProvider provider) {
        this.name = name;
        this.permission = permission;
        this.permissionMessage = permissionMessage;
        this.description = description;
        this.provider = provider;
    }

    protected Map<String, ICommand> commands = new HashMap<>();
    protected String helpInd = "-?";

    @Override
    public void invoke(Object sender, String label, @NotNull List<String> arguments, @NotNull List<String> fillArguments) {
        sender = provider.resolveSender(sender, null);
        if (sender == null) {
            throw new IllegalArgumentException("Cannot resolve sender");
        }
        if (provider.hasPermission(sender, getPermission())) {
            if (arguments.isEmpty()) {
                provider.sendMessage(Level.WARNING, sender, "Please type \"" + label + " help\" for help.");
            } else {
                if (arguments.get(0).equals(helpInd)) {
                    if (Tools.processHelp(helpInd, arguments, fillArguments, sender, provider, this, label)) {
                        return;
                    } /* else throw new RuntimeException("??????????????");*/
                }
                String sub = arguments.get(0);
                ICommand cmd = getSubCommand(sub);
                if (cmd == null) {
                    if (Tools.processHelp(helpInd, arguments, fillArguments, sender, provider, this, label)) {
                        return;
                    }
                    provider.sendMessage(Level.WARNING, sender, "Unknown sub command " + sub);
                } else {
                    arguments.remove(0);
                    cmd.invoke(sender, label, arguments, fillArguments);
                }
            }
        } else {
            provider.noPermission(sender, this);
        }
    }

    @Override
    public ICommand getSubCommand(String name) {
        return commands.get(name.toLowerCase());
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
    public String usage() {
        return "";
    }

    @Override
    public String noPermissionMessage() {
        return permissionMessage;
    }

    @Override
    public void tabCompile(Object sender, @NotNull List<String> result, @NotNull List<String> fillArguments, @NotNull List<String> args) {
        sender = provider.resolveSender(sender, null);
        if (sender != null) {
            if (!args.isEmpty()) {
                if (args.size() == 1) {
                    String test = args.remove(0);
                    result.addAll(commands.keySet().stream().filter(a -> a.contains(test))
                            .sorted().collect(Collectors.toList()));
                    return;
                }
                ICommand ic = getSubCommand(args.remove(0));
                if (ic != null) {
                    ic.tabCompile(sender, result, fillArguments, args);
                }
            }
        }
    }

    @Override
    public void doHelp(Object sender, @NotNull List<String> args, @NotNull List<String> fullArguments) {

    }

    @NotNull
    public Map<String, ICommand> getSubCommands() {
        return ImmutableMap.copyOf(commands);
    }

    public boolean register(@NotNull String name, @NotNull ICommand sub) {
        commands.put(name.toLowerCase(), sub);
        return true;
    }

    public void dump(String prefix, PrintStream stream) {
        for (Map.Entry<String, ICommand> ic : commands.entrySet()) {
            stream.append(prefix).append(ic.getKey()).append(" = ").println(ic.getValue());
            if (ic.getValue() instanceof DefaultCommands) {
                ((DefaultCommands) ic.getValue()).dump(prefix + '\t', stream);
            }
        }
    }
}
