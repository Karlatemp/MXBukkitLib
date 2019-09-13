package cn.mcres.karlatemp.mxlib.cmd;

import cn.mcres.karlatemp.mxlib.configuration.ConfigEditor;
import cn.mcres.karlatemp.mxlib.configuration.ICommandConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

@SuppressWarnings("deprecation")
public interface ICommandProcessor extends ConfigEditor {
    ICommand parse(@NotNull ICommandConfig config, @NotNull Package pck);

    ICommand parse(@NotNull ICommandConfig config, @NotNull Class clazz);

    void load(@NotNull ICommandConfig config, @NotNull ClassLoader loader, String[] classes);

    ICommand parse(@Nullable Object instance, @NotNull Method method);

    ICommand boxingCommand(@NotNull Object command);

    <T> T boxing(Object any);

    <T> T unboxing(Object command);

    default <T> T unboxingSender(ICommandSender sender) {
        return null;
    }

    default <T> T unboxingCommand(ICommand command) {
        return null;
    }

    ICommandSender boxingSender(@NotNull Object sender);
}
