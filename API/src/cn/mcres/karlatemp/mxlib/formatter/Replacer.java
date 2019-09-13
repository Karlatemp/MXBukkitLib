/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.karlatemp.mxlib.formatter;

import java.util.function.Function;

/**
 *
 * @author 32798
 */
public interface Replacer extends Function<String, String> {

    boolean containsKey(String key);

    boolean isEmpty();
}
