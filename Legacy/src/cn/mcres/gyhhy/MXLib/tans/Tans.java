/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.tans;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author 32798
 */
public class Tans {
    private static Locale defloc = Locale.ENGLISH;
    private Locale loc;
    private Map<Locale,Map<String,String>> langs;
    public Tans() {
        this(Locale.getDefault());
    }

    public Tans(Locale lc) {
        loc = lc;
        this.langs = new LinkedHashMap<>();
    }
    public Map<String,String> get(Locale lang){
        return langs.get(lang);
    }
    public Map<String,String> getOrCreate(Locale lang){
        Map<String,String> get = langs.get(lang);
        if(get == null){
            get = (Map)new java.util.Properties();
            langs.put(loc, get);
        }
        return get;
    }
    public Map<String,String> get(){return get(loc);}
    public Map<String,String> getOrCreate(){return getOrCreate(loc);}
    public Map<String,String> set(Locale lang,Map<String,String> val){
        Map<String,String> od = langs.get(lang);
        langs.put(lang, val);
        return od;
    }
    public Map<String,String> set(Map<String,String> val){
        return set(loc,val);
    }
    public Locale getLocale() {
        return loc;
    }

    public void setLocale(Locale loc) {
        this.loc = Objects.requireNonNull(loc);
    }
}
