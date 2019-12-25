/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ClassResourceLoaders.java@author: karlatemp@vip.qq.com: 19-9-26 下午6:36@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.annotations.Bean;
import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import javassist.ClassMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Used to found class byte code.
 *
 * @since 2.2
 */
@Bean
public final class ClassResourceLoaders extends ArrayList<ClassResourceLoader> implements ClassResourceLoader {

    public byte[] found(@NotNull String className, @Nullable ClassLoader loader, @NotNull IBeanManager beanManager) {
        for (ClassResourceLoader children : this) {
            byte[] f = children.found(className, loader, beanManager);
            if (f != null) return f;
        }
        // The class bytecode default location.
        final String f = ClassMap.toJvmName(className) + ".class";
        // We hand it over to ResourceLoaders
        return beanManager.getOptional(ResourceLoaders.class).map(
                rl -> rl.found(f, loader)
        ).map(inp -> {
            try (InputStream is = inp) {
                byte[] ref = new byte[is.available()];
                int len;
                if ((len = is.read(ref)) != ref.length) {
                    byte[] cop = new byte[len];
                    System.arraycopy(ref, 0, cop, 0, Math.min(len, ref.length));
                    return cop;
                }
                return ref;
            } catch (IOException ignore) {
            }
            return null;
        }).orElseGet(() -> {
            if (loader != null) {
                InputStream stream = loader.getResourceAsStream(f);
                if (stream != null) {
                    try (InputStream r = stream) {
                        byte[] b = new byte[r.available()];
                        int rd = r.read(b);
                        if (rd != b.length) {
                            byte[] nw = new byte[rd];
                            System.arraycopy(b, 0, nw, 0, Math.min(rd, b.length));
                            return nw;
                        }
                        return b;
                    } catch (IOException ignore) {
                    }
                }
            }
            return null;
        });
    }
}
