/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.event;

import org.bukkit.event.Cancellable;

interface C extends Cancellable {

    @Override
    void setCancelled(boolean c);

}
