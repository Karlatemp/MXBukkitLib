/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.encryption;

/**
 *
 * @author 32798
 */
public class EncryptionException extends RuntimeException {

    public EncryptionException(Throwable cause) {
        super(cause);
    }

    public EncryptionException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Creates a new instance of <code>EncryptionException</code> without detail
     * message.
     */
    public EncryptionException() {
    }

    /**
     * Constructs an instance of <code>EncryptionException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EncryptionException(String msg) {
        super(msg);
    }
}
