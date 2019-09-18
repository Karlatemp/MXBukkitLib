package cn.mcres.karlatemp.mxlib.logging;

/**
 * 带Bukkit颜色支持的信息工厂
 */
public class MessageFactoryBukkitCommandSender extends MessageFactoryAnsi {
    @Override
    public String toConsole(String cons) {
        return cons;
    }
}
