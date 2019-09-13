package cn.mcres.karlatemp.mxlib.cmd;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;
import cn.mcres.karlatemp.mxlib.exceptions.CommandException;
import cn.mcres.karlatemp.mxlib.tools.SafeList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ProhibitBean
public interface IExecutor {
    @Nullable
    default List<String> tabComplete(@NotNull ICommandSender sender,
                                     @NotNull ICommand command,
                                     @NotNull String alias,
                                     @NotNull String[] args) {
        return tabComplete(sender, command, alias, new SafeList<>(new ArrayList<>(Arrays.asList(args))));
    }

    @Nullable
    List<String> tabComplete(@NotNull ICommandSender sender,
                             @NotNull ICommand command,
                             @NotNull String alias,
                             @NotNull SafeList<String> args);

    default boolean command(@NotNull ICommandSender sender,
                            @NotNull ICommand command,
                            @NotNull String label,
                            @NotNull String[] args) throws CommandException {
        return command(sender, command, label, new SafeList<>(new ArrayList<>(Arrays.asList(args))));
    }

    default boolean command(
            @NotNull ICommandSender sender,
            @NotNull ICommand command, @NotNull String label,
            @NotNull SafeList<String> args) throws CommandException {
        return command(sender, command, label, args, new ArrayList<>(args));
    }

    boolean command(
            @NotNull ICommandSender sender,
            @NotNull ICommand command,
            @NotNull String label,
            @NotNull SafeList<String> args,
            @NotNull List<String> full_path) throws CommandException;
}
