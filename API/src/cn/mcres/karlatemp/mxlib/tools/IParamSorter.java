package cn.mcres.karlatemp.mxlib.tools;

import java.lang.invoke.MethodHandle;

public interface IParamSorter {
    MethodHandle sort(MethodHandle mh, IParamRule... rules);
}
