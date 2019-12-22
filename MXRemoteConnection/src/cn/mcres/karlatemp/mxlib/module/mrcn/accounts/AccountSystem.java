/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AccountSystem.java@author: karlatemp@vip.qq.com: 19-12-17 下午11:53@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.accounts;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public interface AccountSystem {
    @NotNull
    Collection<Account> getAccounts();

    @Nullable
    Account getAccount(@NotNull String user_id);

    boolean createAccount(@NotNull Account account);

    Function<byte[], byte[]> passwdEncoder(@NotNull String type);

    boolean registerPasswdEncoder(@NotNull String type, @NotNull Function<byte[], byte[]> encoder);

    Map<String, Function<byte[], byte[]>> getPasswdEncoders();

    boolean saveAccount(@NotNull Account account);
}
