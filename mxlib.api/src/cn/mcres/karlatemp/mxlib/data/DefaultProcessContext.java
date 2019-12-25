/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DefaultProcessContext.java@author: karlatemp@vip.qq.com: 19-11-16 上午12:21@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data;

import cn.mcres.karlatemp.mxlib.data.attribute.AttributeMap;
import cn.mcres.karlatemp.mxlib.tools.FilterIterator;
import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DefaultProcessContext implements DataProcessContext {
    private Supplier<Iterator<DataHandler>> handlers;

    public DefaultProcessContext(@NotNull Supplier<Iterator<DataHandler>> handlers) {
        this.handlers = handlers;
    }

    private volatile boolean processing;
    private AttributeMap attributes;
    private Object result;

    public void writeTo(Object result, @NotNull AttributeMap attributes) {
        this.result = result;
        this.attributes = attributes;
    }

    @SuppressWarnings("unchecked")
    public <T> T result() {
        return (T) result;
    }

    @SuppressWarnings("unchecked")
    public <T> T process(Predicate<DataHandler> filter, Object param, AttributeMap attributes) throws Exception {
        if (processing) {
            throw new IllegalAccessError("This context was running.");
        }
        processing = true;
        Iterator<DataHandler> invoking = new FilterIterator<>(
                handlers.get(), filter
        );
        try {
            result = param;
            this.attributes = attributes;
            Throwable errors = null;
            for (; invoking.hasNext(); ) {
                DataHandler dh = invoking.next();
                try {
                    dh.translate(this, result, this.attributes);
                } catch (Throwable thr) {
                    if (errors == null) {
                        errors = thr;
                    } else {
                        errors.addSuppressed(thr);
                    }
                }
            }
            if (errors != null) {
                ThrowHelper.thrown(errors);
            }
            return (T) result;
        } finally {
            processing = false;
        }
    }
}
