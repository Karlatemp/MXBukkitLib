/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.configuration;

public class TT {

    public static void main(String[] args) throws Throwable {
        JsonConfiguration jc = new JsonConfiguration(true);
        jc.loadFromString("\n"
                + "{\n"
                + "  \"a\": 1.0,\n"
                + "  \"b\": [\n"
                + "    \"WWW\",\n"
                + "    {\n"
                + "      \"\\u003d\\u003d\": \"java.util.LinkedHashMap\",\n"
                + "      \"-\": {\n"
                + "        \"YOU\": \"WHAT\"\n"
                + "      }\n"
                + "    }\n"
                + "  ],\n"
                + "  \"c\": {\n"
                + "    \"x\": 5.0,\n"
                + "    \"kw\": \"WXX\"\n"
                + "  }\n"
                + "}");
        for (String key : jc.getKeys(true)) {
            System.out.println(key + " = " + jc.get(key));
        }
        System.out.println(jc.saveToString());
    }

}
