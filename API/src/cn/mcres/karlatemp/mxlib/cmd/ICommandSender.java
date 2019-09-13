package cn.mcres.karlatemp.mxlib.cmd;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;

@ProhibitBean
public interface ICommandSender extends IPermissible {
    String getName();

    void sendMessage(String[] messages);

    void sendMessage(String message);
}
