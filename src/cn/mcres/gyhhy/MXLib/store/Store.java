/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.store;

public class Store<T> {

    public T v;


    public Store() {
        this(null);
    }

    public Store(T v) {
        this.v = v;
    }
    public T value() {
        return v;
    }
    public Store<T> value(T v) {
        this.v = v;
        return this;
    }
}
