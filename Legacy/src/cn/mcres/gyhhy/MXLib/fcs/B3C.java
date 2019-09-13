/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.fcs;

import java.io.IOException;

/**
 *
 * @author 32798
 */
public interface B3C {

    void c(byte[] data, int off, int limit) throws IOException;
}
