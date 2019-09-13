/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.http.handlers.rcon;

/**
 * Exception thrown by {@link RconClientException} when the specified password is incorrect.
 */
public class AuthFailureException extends RconClientException {
    public AuthFailureException() {
        super("Authentication failure");
    }
}