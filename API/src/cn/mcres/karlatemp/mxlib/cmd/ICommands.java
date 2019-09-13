package cn.mcres.karlatemp.mxlib.cmd;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;
import cn.mcres.karlatemp.mxlib.exceptions.CommandException;
import cn.mcres.karlatemp.mxlib.tools.SafeList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Here is a command set for a command. 这是一个命令集
 * <p>
 * Build the sub commands 提供了子命令实现
 */
@ProhibitBean
public interface ICommands extends ICommand {
    ICommand getCommand(String name);

    Map<String, ICommand> getCommands();

    ICommands register(ICommand sub, boolean force);

    default ICommands register(ICommand sub) {
        return register(sub, false);
    }

    @NotNull
    @Override
    @Deprecated
    ICommands setParent(@NotNull ICommands parent);

}
