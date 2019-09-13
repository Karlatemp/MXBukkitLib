package cn.mcres.karlatemp.mxlib.cmd.denied;

import cn.mcres.karlatemp.mxlib.cmd.*;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class AbstractExceptionProcessor implements IExceptionProcessor {
    @Override
    public boolean onCommandDeny(
            @NotNull ICommandSender sender,
            @NotNull ICommand command,
            @NotNull String label,
            @NotNull List<String> args,
            @NotNull List<String> full_args, int type) {
        ICommands root = Toolkit.getRoot(command);
        switch (type) {
            case NO_PERMISSION:
                return permission_denied(sender, command, root, label, args, full_args);
            case ERROR_CATEGORY:
                return error_category(sender, command, root, label, args, full_args);
            case COMMAND_NOT_FOUND:
                return command_not_found(sender, command, root, label, args, full_args);
            default:
                return unknown_optional(sender, command, root, label, args, full_args);
        }
    }

    protected boolean unknown_optional(ICommandSender sender, ICommand command, ICommands root, String label, List<String> args, List<String> full_args) {
        return true;
    }

    protected boolean error_category(ICommandSender sender, ICommand command, ICommands root, String label, List<String> args, List<String> full_args) {
        sender.sendMessage("§cSorry, But you cannot use this command.");
        return true;
    }

    protected boolean command_not_found(ICommandSender sender, ICommand command, ICommands root, String label, List<String> args, List<String> full_args) {
        sender.sendMessage("§cWe cannot found this command. try to use §b/" + label + " help§c to get help");
        return true;
    }

    protected boolean permission_denied(ICommandSender sender, ICommand command, ICommands root, String label, List<String> args, List<String> full_args) {
        sender.sendMessage("§cI'm sorry, but you don't have the permission to perform that.");
        return true;
    }
}
