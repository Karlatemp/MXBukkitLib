/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.fcs;

/**
 *
 * @author 32798
 */
public interface F3c<A,B> extends Func{
    void call(int code,A a, B b);
}
