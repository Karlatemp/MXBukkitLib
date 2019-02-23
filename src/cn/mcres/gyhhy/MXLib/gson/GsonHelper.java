/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.gson;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

/**
 *
 * @author 32798
 */
public class GsonHelper {
    public static <T extends JsonWriter> T setPrettyPrinting(Gson gson,T t){
        t.setIndent("  ");
        return t;
    }
}
