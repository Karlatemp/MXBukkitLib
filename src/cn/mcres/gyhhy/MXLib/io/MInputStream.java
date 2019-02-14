/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 32798
 */
public class MInputStream extends InputStream {

    public void close() throws IOException {
        read = false;
        has = false;
        next = -1;
        try {
            io.close();
        } catch (IOException e) {
            throw e;
        } finally {
            io = null;
        }
    }
    private InputStream io;
    private int next = -1;
    private boolean read = true;
    private boolean has = false;

    public MInputStream(InputStream io) {
        this.io = Objects.requireNonNull(io);
    }

    @Override
    public int read() throws IOException {
        if (read) {
            return io.read();
        }
        int i = next;
        next = -1;
        read = true;
        return i;
    }

    public boolean hasNext() {
        if (read) {
            try {
                next = read();
                read = false;
                return has = next != -1;
            } catch (IOException ex) {
                read = true;
                return false;
            }
        } else {
            return has;
        }
    }

}
