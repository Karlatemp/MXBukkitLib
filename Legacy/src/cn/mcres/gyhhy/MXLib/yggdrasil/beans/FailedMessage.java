/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: FailedMessage.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.yggdrasil.beans;

/**
 *
 * @author 32798
 */
public class FailedMessage {

    public String error;
    public String errorMsg;
    public String errorCause;

    public String toString() {
        String er = error;
        if (errorMsg != null) {
            er += ": " + errorMsg;
        }
        if (errorCause != null) {
            er += " # cause by: " + errorCause;
        }
        return er;
    }

    public static class FailedException extends Exception {

        private final FailedMessage fm;

        private FailedException(FailedMessage fm) {
            super(fm.toString(), new Exception(fm.errorCause));
            this.fm = fm;
        }

        public FailedMessage getFailedMessage() {
            return fm;
        }
    }

    public Throwable toThrowable() {
        return new FailedException(this);
    }
}
