/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/20 20:52:48
 *
 * MXLib/expend.plugin.base/I18nException.java
 */

package cn.mcres.karlatemp.mxlib.plugin.base;

public class I18nException extends RuntimeException {
    private final Object[] params;
    private final String key;
    public static final I18nException EXIT = new I18nException("");

    public I18nException(String key, Object... params) {
        super(null, null, true, false);
        this.key = key;
        this.params = params;
    }

    public String getKey() {
        return key;
    }

    public Object[] getParams() {
        return params;
    }
}
