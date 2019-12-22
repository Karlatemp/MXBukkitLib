/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: TranslateLoader.java@author: karlatemp@vip.qq.com: 19-12-22 下午9:40@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.translate;

import cn.mcres.karlatemp.mxlib.util.GsonHelper;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class TranslateLoader {
    private static final Gson g = new Gson();

    public interface ResourceParser {
        @NotNull
        MTranslate parse(@NotNull String path, @NotNull InputStream stream) throws Exception;
    }

    public static final ResourceParser PARSER_PROPERTIES = (path, stream) -> {
        Properties properties = new Properties();
        try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            properties.load(reader);
        }
        return new FunctionTranslate(properties::getProperty);
    }, PARSER_JSON = (path, stream) -> {
        try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            final Map<Object, Object> map = g.fromJson(reader, (Type) Map.class);
            return new FunctionTranslate(k -> {
                Object o = map.get(k);
                if (o == null) return null;
                return String.valueOf(o);
            });
        }
    };

    private static void load(
            Function<String, InputStream> resourceLoader,
            String path,
            ResourceParser parser,
            LinkedTranslate link,
            BiConsumer<String, Throwable> errorCatch) {
        InputStream resource = resourceLoader.apply(path);
        if (resource != null) {
            try {
                link.translates.add(0, parser.parse(path, resource));
            } catch (Throwable e) {
                errorCatch.accept(path, e);
            }
        }
    }

    public static MTranslate loadTranslate(
            @NotNull Function<String, InputStream> resourceLoader,
            @NotNull String name,
            Locale locale,
            @NotNull String suffix,
            @NotNull ResourceParser resourceParser,
            @NotNull BiConsumer<String, Throwable> errorCatch) {
        LinkedTranslate link = new LinkedTranslate();
        load(resourceLoader, name + suffix, resourceParser, link, errorCatch);
        if (locale == null) locale = Locale.getDefault();
        load(resourceLoader, name + '_' + locale.getLanguage() + suffix, resourceParser, link, errorCatch);
        String region = locale.getCountry();
        if (region != null && !region.isEmpty()) {
            load(resourceLoader, name + '_' + locale.getLanguage() + '_' + region + suffix, resourceParser, link, errorCatch);
            String variant = locale.getVariant();
            if (variant != null && !variant.isEmpty()) {
                load(resourceLoader, name + '_' + locale.getLanguage() + '_' + region + '_' + variant + suffix, resourceParser, link, errorCatch);
            }
        }
        return link;
    }
}
