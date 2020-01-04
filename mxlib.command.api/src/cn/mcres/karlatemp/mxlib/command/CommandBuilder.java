/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CommandBuilder.java@author: karlatemp@vip.qq.com: 2019/12/29 下午1:22@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.command;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.exceptions.ScanException;
import cn.mcres.karlatemp.mxlib.tools.IClassScanner;
import cn.mcres.karlatemp.mxlib.tools.IMemberScanner;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Fast Create a command.
 *
 * <pre>{@code ICommand implement = new CommandBuilder()
 * .provider(new PrintStreamProvider(null))
 * .logger(Logger.getLogger("MyCommand"))
 * .ofClass(AnyCommand.class)
 * .buildCommands()}</pre>
 *
 * <pre>{@code
 * this.getCommand("my-command").setExecutor(
 *      new CommandBuilder()
 *          .provider(new BukkitCommandProvider(null))
 *          .logger(this.getLogger())
 *          .ofClass(AnyCommand.class)
 *          .ofFile(this.getFile())
 *          .end(BukkitCommandExecutor::new)
 * );
 * }</pre>
 */
public class CommandBuilder {
    private final List<Class<?>> classes = new ArrayList<>();
    private CommandProvider provider;
    private Package pck;
    private Class<?> source;
    private Logger logger;
    private File file;

    public CommandBuilder provider(CommandProvider provider) {
        this.provider = provider;
        return this;
    }

    public CommandBuilder logger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public CommandBuilder addClasses(Collection<Class<?>> classes) {
        this.classes.addAll(classes);
        return this;
    }

    public CommandBuilder inPackage(Package package_) {
        this.pck = package_;
        return this;
    }

    public CommandBuilder ofClass(Class<?> source) {
        this.source = source;
        return this;
    }

    public CommandBuilder ofFile(File file) {
        this.file = file;
        return this;
    }

    public <T> T end(@NotNull Function<ICommand, T> ending) {
        return ending.apply(buildCommands());
    }

    public synchronized ICommand buildCommands() {
        if (pck == null) {
            pck = source.getPackage();
        }
        if (logger == null) {
            logger = Logger.getGlobal();
        }
        if (provider == null) {
            throw new NullPointerException("Must give a provider.");
        }
        if (provider instanceof BukkitCommandProvider && file == null) {
            throw new IllegalArgumentException("Must give your plugin jar file location.");// When JarUpdate. Using URL Scan will lock the server.
        }
        List<Class<?>> target;
        if (classes.isEmpty()) {
            var loaders = Arrays.asList(
                    Toolkit.Reflection.getClassLoader(Toolkit.Reflection.getCallerClass()),
                    Thread.currentThread().getContextClassLoader(),
                    Toolkit.Reflection.getClassLoader(getClass())
            );
            final IClassScanner scanner = MXBukkitLib.getBeanManager().getBeanNonNull(IClassScanner.class);
            try {
                List<String> list;
                if (file == null) {
                    if (source == null) {
                        throw new IllegalArgumentException("No ClassSource. No FileSource.");
                    }
                    list = scanner.scan(source, new ArrayList<>());
                } else {
                    String pck = this.pck.getName();
                    list = scanner.scan(file, new ArrayList<>()).stream()
                            .filter(a -> a.startsWith(pck))
                            .collect(Collectors.toList());
                }
                target = list.stream().filter(x -> !x.endsWith("package-info"))
                        .map(cn -> {
                            try {
                                return Toolkit.Reflection.loadClassFrom(cn, loaders);
                            } catch (ClassNotFoundException e) {
                                logger.warning("ClassNotFound: " + cn);
                                return (Class<?>) null;
                            }
                        })
                        .filter(Objects::nonNull).collect(Collectors.toList());
            } catch (ScanException e) {
                throw new RuntimeException(e);
            }
        } else {
            target = classes;
        }
        return provider.buildCommands(pck, target);
    }
}
