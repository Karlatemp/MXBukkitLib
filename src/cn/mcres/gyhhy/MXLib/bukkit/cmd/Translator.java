/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.cmd;

/**
 * Add in version 0.10
 * @author 32798
 */
public interface Translator {
    
    public void setLanguageTranslator(LanguageTranslator lt);

    public LanguageTranslator getLanguageTranslator();
}
