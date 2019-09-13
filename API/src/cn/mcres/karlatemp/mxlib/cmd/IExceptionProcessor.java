package cn.mcres.karlatemp.mxlib.cmd;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@ProhibitBean
public interface IExceptionProcessor {
    int NO_PERMISSION = 0,
            ERROR_CATEGORY = 1,
            COMMAND_NOT_FOUND = 2;

    boolean onCommandDeny(
            @NotNull ICommandSender sender,
            @NotNull ICommand command,
            @NotNull String label,
            @NotNull List<String> args,
            @NotNull List<String> full_args,
            int type);
}
