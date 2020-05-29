/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DefaultCommand.java@author: karlatemp@vip.qq.com: 2019/12/29 下午3:40@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.command;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.arguments.*;
import cn.mcres.karlatemp.mxlib.command.annoations.*;
import cn.mcres.karlatemp.mxlib.command.internal.Tools;
import cn.mcres.karlatemp.mxlib.tools.ClassResourceLoaders;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class DefaultCommand implements ICommand {
    private final String name;
    private final String permission;
    private final String noPermissionMessage;
    private final String description;
    private final String usage;
    private final Method method;
    private final Object self;
    private final ParamSlot[] solts;
    private final CommandProvider provider;
    protected String helpind = "-?";
    private static final UUID NAMESPACE = new UUID(0x3279826484_77EAAFL, 0x2333333333333333L);
    public static final SAttributeKey<Class<?>> SPEC_TYPE = new SAttributeKey<>(NAMESPACE, "class");
    public static final SAttributeKey<MParameter> SPEC_ANNOTATIONS = new SAttributeKey<>(NAMESPACE, "annotation");
    public static final SAttributeKey<AtomicBoolean> SPEC_REQUIRE = new SAttributeKey<>(NAMESPACE, "require");
    public static final SAttributeKey<Annotation[]> SPEC_Annotation = new SAttributeKey<>(NAMESPACE, "p_an");
    public static final SAttributeKey<AtomicInteger> SPEC_Slot = new SAttributeKey<>(NAMESPACE, "p_slot");

    protected final SOptions options = new SOptions();
    protected final Map<String, SSpec<?>> specs = new HashMap<>();
    private Class<?> paramType;
    private SParser parser;

    private interface ParamSlot {
        Object get(Object sender, SParser ag, List<String> un_parsed, List<String> full, List<String> fullCommandArgument, String label_z);
    }

    public DefaultCommand(String name, String permission, String noPermissionMessage,
                          String description, String usage,
                          Method method, Object self, @NotNull CommandProvider provider) {
        this.name = name;
        this.permission = permission;
        this.noPermissionMessage = noPermissionMessage;
        this.description = description;
        this.usage = usage;
        this.method = method;
        this.self = self;
        this.provider = provider;
        this.solts = parseSolts(method);
        initialize();
    }

    private Map<String, CommandParameter> imm_s_cp;

    private void initialize() {
        for (var spec : specs.values()) {
            var type = spec.getAttribute(SPEC_TYPE);
            var anno = spec.getAttribute(SPEC_ANNOTATIONS);
            var atom = new AtomicBoolean();
            spec.putAttribute(SPEC_REQUIRE, atom);
            if (type == boolean.class && !anno.hasParameter()) {
                spec.parser(StandardSpecParsers.BOOLEAN_KEY_EXIST).tabCompiler(StandardSpecParsers.BOOLEAN_KEY_EXIST);
            } else {
                atom.set(anno.require());
                override(spec.getAttribute(SPEC_Slot).get(), spec.getAttribute(SPEC_Annotation), atom);
                var def = StandardSpecParsers.from(type);
                if (def != StandardSpecParsers.BAKED) {
                    spec.parser(def).tabCompiler(def);
                }
            }
        }
        Method $initialize = null;
        try {
            $initialize = method.getDeclaringClass().getDeclaredMethod("$initialize", Map.class);
        } catch (NoSuchMethodException ignored) {
        }
        try {
            if ($initialize != null) {
                var mh = Toolkit.Reflection.getRoot().unreflect($initialize);
                if (mh.type().parameterCount() == 2) {
                    mh.invoke(self, specs);
                } else {
                    mh.invoke(specs);
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        var map = new HashMap<String, CommandParameter>();
        for (var specE : specs.entrySet()) {
            var spec = specE.getValue();
            map.put(specE.getKey(), new SpecArgument(spec));
            if (spec.getParser() == null) {
                spec.parser(StandardSpecParsers.BAKED).tabCompiler(StandardSpecParsers.BAKED);
            }
        }
        this.imm_s_cp = ImmutableMap.copyOf(map);
        parser = options.newParser();
    }

    static class SpecArgument implements CommandParameter {
        private final Class<?> type;
        private final String desc;
        private final String name;
        private final boolean req;

        SpecArgument(SSpec<?> s) {
            var ann = s.getAttribute(SPEC_ANNOTATIONS);
            this.type = s.getAttribute(SPEC_TYPE);
            this.name = ann.name();
            this.desc = ann.description();
            this.req = s.getAttribute(SPEC_REQUIRE).get();
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public Class<?> type() {
            return type;
        }

        @Override
        public String description() {
            return desc;
        }


        @Override
        public boolean require() {
            return req;
        }
    }

    @Override
    public String usage() {
        return usage;
    }

    @Override
    public String description() {
        return description;
    }

    private ParamSlot[] parseSolts(Method method) {
        int size;
        ParamSlot[] s = new ParamSlot[size = method.getParameterCount()];
        final Annotation[][] annotations = method.getParameterAnnotations();
        final Class<?>[] types = method.getParameterTypes();
        next:
        for (int i = 0; i < size; i++) {
            Annotation[] ann = annotations[i];
            for (Annotation a : ann) {
                if (a instanceof MLabel) {
                    if (String.class.isAssignableFrom(types[i])) {
                        s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> label_z;
                    } else {
                        s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> null;
                    }
                    continue next;
                } else if (a instanceof MProvider) {
                    if (types[i].isInstance(provider)) {
                        s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> provider;
                    } else {
                        s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> null;
                    }
                    continue next;
                } else if (a instanceof MSender) {
                    s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> sender;
                    if (paramType != null) {
                        throw new RuntimeException("Cannot input double Sender.");
                    }
                    paramType = types[i];
                    continue next;
                } else if (a instanceof MArguments) {
                    final boolean full1 = ((MArguments) a).full();
                    final boolean allLine = ((MArguments) a).allLine();
                    if (types[i].isArray()) {
                        if (full1) {
                            if (allLine) {
                                s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> fullCommandArgument.toArray(new String[0]);
                            } else {
                                s[i] = (sender, ag, un_parsed, full, fullCommandArgc, label_z) -> full.toArray(new String[0]);
                            }
                        } else {
                            s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> un_parsed.toArray(new String[0]);
                        }
                    } else {
                        if (full1) {
                            if (allLine) {
                                s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> fullCommandArgument;
                            } else {
                                s[i] = (sender, ag, un_parsed, full, fullCommandArgc, label_z) -> full;
                            }
                        } else {
                            s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> un_parsed;
                        }
                    }
                    continue next;
                } else if (a instanceof MParameter) {
                    MParameter par = (MParameter) a;
                    Class<?> type = types[i];
                    var ali = par.alias();
                    String[] fullKey = new String[ali.length + 1];
                    System.arraycopy(ali, 0, fullKey, 0, ali.length);
                    fullKey[ali.length] = par.name();
                    var spec = options.register(fullKey);
                    this.specs.put(par.name(), spec);
                    spec.putAttribute(SPEC_TYPE, type);
                    spec.putAttribute(SPEC_ANNOTATIONS, par);
                    spec.putAttribute(SPEC_Annotation, ann);
                    spec.putAttribute(SPEC_Slot, new AtomicInteger(i));
                    s[i] = (sender, ags, un_parsed, full, fullCommandArgument, label_z) -> {
                        var value = ags.value(spec);
                        if (value == null) return getDefault(type);
                        return value;
                    };
                    continue next;
                }
            }
            Class<?> type = types[i];
            s[i] = (sender, ag, un_parsed, full, fullCommandArgument, label_z) -> getDefault(type);
        }
        return s;
    }

    private Object getDefault(Class<?> type) {
        if (type == int.class) return 0;
        if (type == double.class) return 0d;
        if (type == float.class) return 0f;
        if (type == short.class) return (short) 0;
        if (type == long.class) return 0L;
        if (type == boolean.class) return false;
        if (type == char.class) return '\u0000';
        if (type == byte.class) return (byte) 0;
        return null;
    }

    private boolean scanned = false;
    private MethodNode methodNode;

    private static boolean override$a(AtomicBoolean UNR, String BUG) {
        String str = BUG.toLowerCase();
        if (str.endsWith(".notnull") | str.endsWith(".nonnull")) {
            UNR.set(true);
            return true;
        } else if (str.endsWith(".nullable")) {
            UNR.set(false);
            return true;
        }
        return false;
    }

    private void override(int i, Annotation[] anno, AtomicBoolean ag) {
        for (Annotation a : anno) { // Scan in visitable
            if (override$a(ag, a.annotationType().getName())) return;
        }
        if (!scanned) {
            scanned = true;
            final byte[] found = MXBukkitLib.getBeanManager().getBeanNonNull(ClassResourceLoaders.class)
                    .found(method.getDeclaringClass().getName(), Toolkit.Reflection.getClassLoader(method.getDeclaringClass()), MXBukkitLib.getBeanManager());
            if (found == null) {
                provider.logger().log(Level.WARNING, "[MXLibCommandSystem] Failed to load class source: " + method.getDeclaringClass());
            } else {
                ClassReader reader = new ClassReader(found);
                var node = new ClassNode();
                reader.accept(node, 0);
                for (var s : node.methods) {
                    if (s.name.equals(method.getName())) {
                        if (s.desc.equals(Type.getMethodDescriptor(method))) {
                            methodNode = s;
                            break;
                        }
                    }
                }
            }
        }
        if (methodNode != null) {
            var ins = methodNode.invisibleParameterAnnotations;
            if (ins != null) {
                var ins0 = ins[i];
                if (ins0 != null) {
                    for (var anno0 : ins0) {
                        if (override$a(ag, Type.getType(anno0.desc).getClassName())) return;
                    }
                }
            }
        }
    }

    protected final Object check(Object sender) {
        sender = provider.resolveSender(sender, null);
        if (sender == null) {
            throw new UnsupportedOperationException("Cannot resolve sender.");
        }
        if (!provider.hasPermission(sender, permission)) {
            provider.noPermission(sender, this);
            return null;
        }
        if (paramType != null) {
            Object s = provider.resolveSender(sender, paramType);
            if (s == null) {
                provider.senderNotResolve(sender, paramType);
                return null;
            }
            return s;
        }
        return sender;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void invoke(Object sender, String label, @NotNull List<String> arguments, @NotNull List<String> fillArguments) {
        sender = check(sender);
        if (sender == null) return;
        if (Tools.processHelp(this.helpind, arguments, fillArguments, sender, provider, this, label)) {
            return;
        }
        final SParser p = parser.newContext();
        p.reset();
        var resp = p.parse(arguments.iterator());
        if (!resp.success) {
            provider.translate(Level.WARNING, sender, resp.trans, resp.param);
        }
        {
            var more = new ArrayList<String>();
            for (var spe : specs.entrySet()) {
                var sp = spe.getValue();
                if (sp.getAttribute(SPEC_REQUIRE).get()) {
                    if (!p.contains(sp)) {
                        more.add(spe.getKey());
                    }
                }
            }
            if (!more.isEmpty()) {
                provider.translate(Level.WARNING, sender, "command.need.more.options", more.toArray());
                return;
            }
        }

        Object[] args = new Object[method.getParameterCount()];
        for (int i = 0; i < args.length; i++) {
            args[i] = this.solts[i].get(
                    sender, p, (List<String>) p.value(options.getNoSpec()), arguments,
                    fillArguments, label);
        }
        try {
            // System.out.println(Arrays.toString(args));
            // System.out.println(method);
            method.invoke(self, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public String noPermissionMessage() {
        return noPermissionMessage;
    }

    @Override
    public void tabCompile(Object sender, @NotNull List<String> result, @NotNull List<String> fillArguments, @NotNull List<String> args) {
        parser.tabCompile(args, result);
    }

    @Override
    public Map<String, CommandParameter> parameters() {
        return imm_s_cp;
    }

    @Override
    public CommandProvider provider() {
        return provider;
    }
}
