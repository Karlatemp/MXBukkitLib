/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.models.vault;

import net.milkbowl.vault.VaultEco;
import net.milkbowl.vault.economy.Economy;

/**
 *
 * @author 32798
 */
public class VaultMapping {

    private final Economy v;

    VaultMapping(Economy p) {
        this.v = p;
    }
    public Economy getVault(){
        return v;
    }
    
}
