package cn.mcres.karlatemp.mxlib.testing.nex;

import cn.mcres.karlatemp.mxlib.configuration.ICommandNestingConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class TestCommandNestingConfig extends ICommandNestingConfig {
    @Nullable
    @Override
    protected Predicate<Class> getClassFilter() {
        return null;
    }

    @Override
    protected boolean check(String cname) {
        return true;
    }

    @NotNull
    public String getName() {
        return "test";
    }
}
