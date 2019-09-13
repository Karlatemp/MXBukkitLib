package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.annotations.Bean;
import cn.mcres.karlatemp.mxlib.annotations.Configuration;
import cn.mcres.karlatemp.mxlib.bean.IEnvironmentFactory;
import cn.mcres.karlatemp.mxlib.bukkit.IBukkitConfigurationProcessor;
import cn.mcres.karlatemp.mxlib.cmd.ICommandProcessor;

@Configuration
public class PluginAutoConfig {
    static class BukkitCheck {
        static boolean bukkit;

        static {
            try {
                Class<?> b = Class.forName("org.bukkit.Bukkit");
                bukkit = b.getMethod("getServer").invoke(null) != null;
            } catch (Throwable thr) {
            }
        }
    }

    @Bean
    IBukkitConfigurationProcessor bc() {
        return new BukkitConfigurationProcessor();
    }

    @Bean
    ICommandProcessor processor(ICommandProcessor p) {
        if (BukkitCheck.bukkit)
            return new BukkitCommandProcessor();
        return p;
    }

    @Bean
    void zboot(IEnvironmentFactory factory, IBukkitConfigurationProcessor bc) {
        $MXBukkitLibConfiguration.load(factory, bc);
    }
}
