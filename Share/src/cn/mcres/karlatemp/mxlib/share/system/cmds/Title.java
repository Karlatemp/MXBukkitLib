package cn.mcres.karlatemp.mxlib.share.system.cmds;

import cn.mcres.karlatemp.mxlib.annotations.CommandHandle;
import cn.mcres.karlatemp.mxlib.annotations.Resource;
import cn.mcres.karlatemp.mxlib.bukkit.PacketFormatter;
import cn.mcres.karlatemp.mxlib.bukkit.TitleAPI;
import org.bukkit.entity.Player;

import java.util.List;

public class Title {
    public static final String $name = "title";
    @SuppressWarnings("unused")
    @Resource
    private TitleAPI titleAPI;

    @CommandHandle
    public void run(Player player, List<String> args) {
        titleAPI.sendTitle(player, args.get(0), args.get(1), 20, 50, 20, PacketFormatter.LOSSLESS);
    }
}
