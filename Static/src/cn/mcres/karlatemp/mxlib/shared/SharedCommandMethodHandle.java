package cn.mcres.karlatemp.mxlib.shared;

import cn.mcres.karlatemp.mxlib.cmd.ICommand;
import cn.mcres.karlatemp.mxlib.cmd.ICommandSender;
import cn.mcres.karlatemp.mxlib.cmd.ICommands;
import cn.mcres.karlatemp.mxlib.cmd.IExceptionProcessor;
import cn.mcres.karlatemp.mxlib.exceptions.CommandException;
import cn.mcres.karlatemp.mxlib.tools.IParamRule;
import cn.mcres.karlatemp.mxlib.tools.IParamSorter;
import cn.mcres.karlatemp.mxlib.tools.SafeList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("Duplicates")
public class SharedCommandMethodHandle extends AbstractCommand {
    private final String[] alias;
    protected MethodHandle execute;
    protected MethodHandle tab;
    protected static final IParamRule EXECUTE_RETURN_VALUE_FILTER = new IParamRule(null) {
        private boolean filter(Object v) {
            if (v instanceof Boolean) {
                return (Boolean) v;
            }
            return true;
        }
    }, TAB_RETURN_VALUE_FILTER = new IParamRule(null) {
        private List filter(Object value) {
            if (value == null) return null;
            if (value instanceof List) {
                return (List) value;
            }
            if (value instanceof String[]) {
                return Arrays.asList((String[]) value);
            }
            if (value.getClass().isArray()) {
                int len = Array.getLength(value);
                String[] dump = new String[len];
                for (int i = 0; i < len; i++) {
                    dump[i] = String.valueOf(Array.get(value, i));
                }
                return Arrays.asList(dump);
            }
            return Collections.singletonList(String.valueOf(value));
        }
    };
    private static final IParamRule[] EXECUTE_RULES = new IParamRule[]{
            new IParamRule(ICommandSender.class),
            new IParamRule(ICommand.class),
            new IParamRule(String.class),
            new IParamRule(null, 2) {

                private Object filter(Boolean str, @NotNull List<String> input) {
                    if (str) {
                        return input.toArray(new String[0]);
                    }
                    return input;
                }

                private boolean tt;

                @Override
                public Class getAppendClass() {
                    return List.class;
                }

                @Override
                public boolean match(Class c) {
                    if (c == String[].class || c == List.class) {
                        if (filter.type().parameterCount() == 2) {
                            tt = (c == String[].class);
                        }
                        return true;
                    }
                    return false;
                }

                @Override
                public MethodHandle filter() {
                    return super.filter().bindTo(tt);
                }
            },
            null
    }, TAB_RULES = EXECUTE_RULES;

    static {
        EXECUTE_RULES[4] = EXECUTE_RULES[3];
    }

    protected void $init(IParamSorter sorter) {
        execute = sorter.sort(execute, EXECUTE_RULES);
        execute = MethodHandles.filterReturnValue(
                execute.asType(execute.type().changeReturnType(Object.class)),
                EXECUTE_RETURN_VALUE_FILTER.filter());
        if (tab != null) {
            tab = sorter.sort(tab, TAB_RULES);
            tab = MethodHandles.filterReturnValue(
                    tab.asType(tab.type().changeReturnType(Object.class)),
                    TAB_RETURN_VALUE_FILTER.filter());
        }
    }

    public SharedCommandMethodHandle(String name, MethodHandle execute, MethodHandle tab, String[] alias) {
        super(name);
        this.execute = execute;
        this.tab = tab;
        this.alias = alias;
    }

    @Nullable
    @Override
    public String[] getAlias() {
        return alias;
    }

    @Nullable
    @Override
    public List<String> tabComplete(@NotNull ICommandSender sender,
                                    @NotNull ICommand command,
                                    @NotNull String alias,
                                    @NotNull SafeList<String> args) {
        if (tab != null) {
            try {
                //noinspection unchecked
                return (List<String>) tab.invoke(sender, command, alias, args);
            } catch (Error | RuntimeException re) {
                throw re;
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }
        return null;
    }

    @Override
    public boolean command(@NotNull ICommandSender sender,
                           @NotNull ICommand command,
                           @NotNull String label,
                           @NotNull SafeList<String> args,
                           @NotNull List<String> full_path) throws CommandException {
        if (!checkPermission(sender)) {
            final IExceptionProcessor processor = getProcessor();
            if (processor != null) {
                return processor.onCommandDeny(sender, command, label, args, full_path, IExceptionProcessor.NO_PERMISSION);
            }
            return true;
        }
        try {
            execute.invoke(sender, command, label, args, full_path);
        } catch (CommandException ce) {
            throw ce;
        } catch (Throwable throwable) {
            throw new CommandException(throwable);
        }
        return false;
    }
}
