package cn.mcres.karlatemp.mxlib.share.system;

import cn.mcres.karlatemp.mxlib.share.BukkitCommandConfig;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class MCommandConfig extends BukkitCommandConfig.BukkitCommandByNameConfig {
    @Override
    public String getCommandName() {
        return "mxbukkitlib:mxlib";
    }

    @Nullable
    @Override
    protected Predicate<Class> getClassFilter() {
        return null;
    }

    @Override
    protected boolean check(String cname) {
        return true;
    }
}
