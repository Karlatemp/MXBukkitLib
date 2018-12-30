/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib;
/**
 *
 * @author 32798
 */
public class Helper {
    public static <T> T thr(Throwable thr){
        throw new SysRuntimeException(thr);
    }
    public static <T> T thr(String msg,Throwable thr){
        throw new SysRuntimeException(msg,thr);
    }
    public static <T> T thr(String msg){
        throw new SysRuntimeException(msg);
    }
    public static <T> T thr(){
        throw new SysRuntimeException();
    }
}
