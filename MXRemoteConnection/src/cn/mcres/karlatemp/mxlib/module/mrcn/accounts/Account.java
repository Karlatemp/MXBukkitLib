/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Account.java@author: karlatemp@vip.qq.com: 19-12-17 下午11:52@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.accounts;

import java.util.Map;

public class Account {
    public String user;
    public byte[] passwd;
    public String passwd_encode_type;
    public Map<String, Boolean> permissions;
    public String display;
    public boolean allow_permission_override;
    public boolean is_op;
}
