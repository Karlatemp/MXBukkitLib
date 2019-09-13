/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.http.handlers.rcon;

/**
 * Generic exception thrown by {@link RconClientException} when any exception occurs.
 */
public class RconClientException extends java.io.IOException {
    public RconClientException(String message) {
        super(message);
    }

    public RconClientException(String message, Throwable cause) {
        super(message, cause);
    }
}