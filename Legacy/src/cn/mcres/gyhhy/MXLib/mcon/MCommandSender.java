/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.mcon;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;

public class MCommandSender extends PBase implements RemoteConsoleCommandSender {

    protected String name = "MCon";

    @Override
    public void sendMessage(String message) {
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String m : messages) {
            sendMessage(m);
        }
    }

    @Override
    public String getName() {
        return name;
    }

}
