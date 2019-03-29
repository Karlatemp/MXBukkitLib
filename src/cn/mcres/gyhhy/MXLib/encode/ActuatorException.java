/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.encode;

/**
 *
 * @author 32798
 */
public class ActuatorException extends RuntimeException {

    public ActuatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActuatorException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance of <code>ActuatorException</code> without detail
     * message.
     */
    public ActuatorException() {
    }

    /**
     * Constructs an instance of <code>ActuatorException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ActuatorException(String msg) {
        super(msg);
    }
}
