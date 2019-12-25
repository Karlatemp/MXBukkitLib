/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: WrappedClosable.java@author: karlatemp@vip.qq.com: 19-11-22 下午12:31@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class WrappedClosable<T extends Closeable> extends WrappedObject<T> implements Closeable {
    private Callable<Void> close_hook;

    public WrappedClosable() {
    }

    public WrappedClosable(T value) {
        super(value);
    }

    public WrappedClosable(@Nullable Consumer<Consumer<T>> hook_setter) {
        super(hook_setter);
    }

    public WrappedClosable(T value, @Nullable Consumer<Consumer<T>> hook_setter) {
        super(value, hook_setter);
    }

    public WrappedClosable(T value, @Nullable Consumer<Consumer<T>> hook_setter, Callable<Void> close_hook) {
        super(value, hook_setter);
        this.close_hook = close_hook;
    }

    @Override
    public void close() throws IOException {
        Throwable ioex = null;
        try {
            if (close_hook != null) {
                try {
                    close_hook.call();
                } catch (IOException ioe) {
                    throw ioe;
                } catch (Exception e) {
                    throw new IOException(e);
                } finally {
                    close_hook = null;
                }
            }
        } catch (Throwable iw) {
            ioex = iw;
        } finally {
            try {
                object.close();
            } catch (Throwable ioe) {
                if (ioex == null) {
                    ThrowHelper.thrown(ioe);
                } else {
                    ioex.addSuppressed(ioe);
                    Throwable tmp = ioex;
                    ioex = null;
                    ThrowHelper.thrown(tmp);
                }
            } finally {
                if (ioex != null) {
                    ThrowHelper.thrown(ioex);
                }
            }
        }
    }
}
