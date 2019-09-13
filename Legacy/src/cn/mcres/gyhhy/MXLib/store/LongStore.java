/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.store;

public class LongStore extends Store<Long> {

    private long value;

    public LongStore() {
        this(0);
    }

    public LongStore(long v) {
        value = v;
    }

    public Long value() {
        return value;
    }

    public LongStore value(long value) {
        this.value = value;
        return this;
    }

    public LongStore value(Long value) {
        this.value = value;
        return this;
    }

    public long v() {
        return value;
    }

    public LongStore v(long v) {
        value = v;
        return this;
    }
}
