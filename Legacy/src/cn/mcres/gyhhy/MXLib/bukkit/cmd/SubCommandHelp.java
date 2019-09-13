/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.cmd;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author 32798
 */
public class SubCommandHelp extends SubCommandEX {

    public static int length = 5;
    private ExecuterEX exe;

    public SubCommandHelp(ExecuterEX exe) {
        super();
        sc = new SC();
        this.exe = exe;
    }

    public String ax(String a, SubCommandEX e) {
        return String.format("\u00a7e%s\u00a7f: \u00a7b%s", a, e.sc.desc());
    }

    public String[] get(Set<Map.Entry<String, SubCommandEX>> set) {
        String[] sp = new String[set.size()];
        int i = 0;
        for (Map.Entry<String, SubCommandEX> e : set) {
            sp[i] = ax(e.getKey(), e.getValue());
            i++;
        }
        java.util.Arrays.sort(sp);
        return sp;
    }

    public static String getName(Command cmd) {
        if (cmd == null) {
            return "<Unknown>";
        }
        return cmd.getName();
    }

    public void page(CommandSender sender, Command cmd, String ali, String[] argc, Executer exev, int page) {
        page--;

        sender.sendMessage("\u00a7b===============" + getName(cmd) + "===============");
        Map<String, SubCommandEX> map = exev.mmp;
        Set<Map.Entry<String, SubCommandEX>> set = map.entrySet();
        int count = set.size();
        int pli = (int) Math.ceil(((double) count) / length);
        String[] list = get(set);
        count = page * length;
        int i = count;
        count = length;
        for (; i < list.length && count > 0; i++, count--) {
            sender.sendMessage(list[i]);
        }
        page++;
        sender.sendMessage("\u00a7b===============" + page + "/" + pli + "===============");
    }

    public void help(CommandSender sender, Command cmd, String ali, String[] argc, Executer exev, SubCommandEX scb) {
        sender.sendMessage("\u00a7b===============" + getName(cmd) + "===============");
        if (scb == null) {
            sender.sendMessage("\u00a7cNo sub command found");
            return;
        }
        sender.sendMessage(ax(argc[1], scb));
    }

    @Override
    public boolean exec(CommandSender sender, Command cmd, String ali, String[] argc, Executer exev) {
        if (check(sender, exev)) {
            switch (argc.length) {
                case 1: {
                    this.page(sender, cmd, ali, argc, exev, 1);
                    break;
                }
                case 2: {
                    String vl = argc[1];
                    if (isNum(vl)) {
                        int num = Integer.parseInt(vl);
                        this.page(sender, cmd, ali, argc, exev, num);
                    } else {
                        SubCommandEX sub = exev.getSub(vl);
                        this.help(sender, cmd, ali, argc, exev, sub);
                    }
                    break;
                }
                default: {
                    sender.sendMessage("What???");
                }
            }
        }
        return true;
    }

    @Override
    public void onTabComplete(CommandSender cs, Command cmnd, String string, String[] args, SubCommandEX subcommand, List<String> completes) {
        if (args.length < 2) {
            completes.addAll(exe.mmp.keySet());
        }
    }

    private boolean isNum(String vl) {
        return vl.replaceAll("[0-9]", "").isEmpty();
    }

    @SuppressWarnings("AnnotationAsSuperInterface")
    private static class SC implements SubCommand {

        @Override
        public Class<? extends CommandTabCompleter> tab() {
            return CommandTabCompleter.class;
        }

        @Override
        public String name() {
            return "";
        }

        @Override
        public String permission() {
            return "";
        }

        @Override
        public String desc() {
            return "Default help for commands.";
        }

        @Override
        public boolean console() {
            return true;
        }

        @Override
        public boolean checkSupPer() {
            return true;
        }

        @Override
        public boolean noRemoveFirstArg() {
            return false;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return getClass();
        }

        @Override
        public CommandCreateType create() {
            return CommandCreateType.UseMethodHandle;
        }
    }

}
