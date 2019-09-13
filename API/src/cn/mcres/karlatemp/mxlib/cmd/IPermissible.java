package cn.mcres.karlatemp.mxlib.cmd;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;

@ProhibitBean
public interface IPermissible {
    boolean hasPermission(String name);

    boolean isSetPermission(String name);
}
