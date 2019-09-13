/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.models.vault;

import cn.mcres.gyhhy.MXLib.Core;
import cn.mcres.gyhhy.MXLib.RefUtil;
import cn.mcres.gyhhy.MXLib.models.Model;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import net.milkbowl.vault.economy.Economy;

public class Vault extends Model<VaultMapping> {

    static VaultMapping vm;

    static {
        if (RefUtil.classExists("org.bukkit.Bukkit")
                && RefUtil.classExists("org.bukkit.Server")
                && RefUtil.classExists("net.milkbowl.vault.economy.Economy", true, Vault.class.getClassLoader())) {
            Server ser = Bukkit.getServer();
            if (ser != null) {
                ServicesManager sm = ser.getServicesManager();
                if (sm != null) {
                    RegisteredServiceProvider<Economy> rsp = sm.getRegistration(Economy.class);
                    if (rsp != null) {
                        Economy p = rsp.getProvider();
                        vm = new VaultMapping(p);
                    }
                }
            }
        }
    }

    @Override
    public VaultMapping getInstance() {
        return vm;
    }

    @Override
    public boolean isSupport() {
        return vm != null;
    }

    @Override
    protected String getVersion0() {
        return Core.getJarVersion(vm.getVault().getClass());
    }

}
