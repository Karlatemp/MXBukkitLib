/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: IEvalProcessor.java@author: karlatemp@vip.qq.com: 19-9-18 下午9:50@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.exceptions.CompeteException;
import cn.mcres.karlatemp.mxlib.exceptions.EvalProcessorInvokingException;

import java.util.Map;

/**
 * 一个命令执行器
 */
public interface IEvalProcessor {
    interface CompetedCode {
        <T> T invoke(Map<String, Object> variables) throws EvalProcessorInvokingException;
    }

    interface Function {
        interface Lambda extends Function {
            Object inv(Object this_, Object... args) throws EvalProcessorInvokingException;

            @SuppressWarnings("unchecked")
            @Override
            default <T> T invoke(Object this_, Object... args) throws EvalProcessorInvokingException {
                return (T) inv(this_, args);
            }
        }

        <T> T invoke(Object this_, Object... args) throws EvalProcessorInvokingException;
    }

    void clearCache();

    void setUsingCache(boolean mode);

    CompetedCode compete(String command, boolean allowInvoking, boolean allowField) throws CompeteException;

    default <T> T eval(String command, Map<String, Object> variables, boolean allowInvoking, boolean allowField) throws CompeteException, EvalProcessorInvokingException {
        return compete(command, allowInvoking, allowField).invoke(variables);
    }
}
