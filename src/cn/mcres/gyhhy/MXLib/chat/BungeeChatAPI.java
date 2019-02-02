/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.chat;

import cn.mcres.gyhhy.MXLib.Base64;
import cn.mcres.gyhhy.MXLib.RefUtil;
import cn.mcres.gyhhy.MXLib.bytecode.ByteCodeHelper;
import cn.mcres.gyhhy.MXLib.spigot.SpigotHelper;
import cn.mcres.gyhhy.MXLib.bukkit.MXAPI;
import cn.mcres.gyhhy.MXLib.bukkit.Plugin;
import com.google.common.collect.BiMap;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

/**
 *
 * @author 32798
 */
public class BungeeChatAPI {

    public static final BungeeChatAPI api = new BungeeChatAPI();

    private static Class<? extends BungeeChatPacket> packet;
    private static Constructor<? extends BungeeChatPacket> constructor;

    public static void uninstall() {
        Class<?> EnumProtocol = MXAPI.getNMSClass("EnumProtocol");
        Object ep = RefUtil.get(EnumProtocol, "PLAY");
        {
            Field[] fies = EnumProtocol.getDeclaredFields();
            Field fff = null;
            for (Field f : fies) {
                String s = (f.getGenericType().toString());
                if (s.startsWith("java.util.Map<") && s.contains("EnumProtocolDirection") && s.endsWith(">>>")) {
                    fff = f;
                }
            }
            fff.setAccessible(true);
            Map<Object, BiMap<Integer, Class<?>>> map = RefUtil.get(fff, ep);
            Object setKey = null;
            BiMap<Integer, Class<?>> setValue = null;
            Set<Map.Entry<Object, BiMap<Integer, Class<?>>>> entrySet = map.entrySet();
            for (Map.Entry<Object, BiMap<Integer, Class<?>>> e : entrySet) {
                if (e.getValue() instanceof AWM) {
                    setKey = e.getKey();
                    setValue = e.getValue();
                }
            }
            if (setKey != null) {
                map.put(setKey, ((AWM) setValue).sup);
            }
        }
    }

    public static Class<? extends BungeeChatPacket> getPacketClass() {
        load();
        return packet;
    }
    public static Constructor<? extends BungeeChatPacket> getConstructor(){
        load();
        return constructor;
    }

    public static void load() {
        if (constructor == null) {
            try {
                packet = installClass().asSubclass(BungeeChatPacket.class);
                constructor = packet.getConstructor(BaseComponent[].class);
            } catch (Exception ex) {
                Plugin.plugin.getLoggerEX().printStackTrace(ex);
            }
        }
    }

    public static String version(String vers, String str) {
        return str.replace("[V]", vers);
    }

    private static void install$loadClass(String c1, String c2, ClassPool pool) throws IOException {
        ArrayList<byte[]> bsbsd;
        int size;
        try (InputStream inp = Plugin.plugin.getResource(c1)) {
            bsbsd = new ArrayList<>();
            byte[] buffer = new byte[1024];
            size = 0;
            while (true) {
                int len = inp.read(buffer);
                if (len == -1) {
                    break;
                }
                size += len;
                byte[] nfres = new byte[len];
                System.arraycopy(buffer, 0, nfres, 0, len);
                bsbsd.add(nfres);
            }
        }
        byte[] full = new byte[size];
        size = 0;
        for (byte[] bs : bsbsd) {
            System.arraycopy(bs, 0, full, size, bs.length);
            size += bs.length;

            pool.appendClassPath(new ByteArrayClassPath(c2, full));
        }
    }

    @SuppressWarnings("null")
    private static Class<?> installClass() throws Exception {
        ClassPool pool = new ClassPool();
        ClassLoader loader = BungeeChatAPI.class.getClassLoader();
        install$loadClass(BungeeChatPacket.class.getPackage().getName().replaceAll("\\.", "/") + "/BungeeChatPacket.class", "cn.mcres.gyhhy.MXLib.chat.BungeeChatPacket", pool);
        install$loadClass("net/md_5/bungee/chat/ComponentSerializer.class", "net.md_5.bungee.chat.ComponentSerializer", pool);
        install$loadClass("net/md_5/bungee/api/chat/BaseComponent.class", "net.md_5.bungee.chat.ComponentSerializer", pool);
        pool.appendClassPath(new LoaderClassPath(loader));

//        ClassPool create = new ClassPool(pool);
        String ver = MXAPI.getInfo().getServerNMSVersion();

        String className = "net.minecraft.server.FUCK.PacketPlayOutChatFUCK";//version(ver, "net.minecraft.server.[V].PacketPlayOutChat");// BungeeChatAPI.class.getPackage().getName().replaceAll("\\.", "/") + "." + UUID.randomUUID().toString().replaceAll("\\-", "_") + System.currentTimeMillis();
        CtClass bc = pool.get("net.md_5.bungee.api.chat.BaseComponent[]");
        { // Append Field read
            ClassPool pool2 = new ClassPool();
            pool2.appendClassPath(new LoaderClassPath(loader));
            CtClass MC = pool2.makeClass(className);
            MC.addField(new CtField(bc, "bcs", MC));
//            MC.setName(MC.getName() + "_GYHHY");
            pool.appendClassPath(new ByteArrayClassPath(ByteCodeHelper.toJavaName(className), MC.toBytecode()));
        }

//        String ver = "v1_12_R1";
//        CtClass PacketS = pool.get(version(ver, "net.minecraft.server.[V].PacketPlayOutChat"));
//        mkClass.setName(mkClass.getName() + "_GYHHY");
        CtClass mkClass = pool.get(version(ver, "net.minecraft.server.[V].PacketPlayOutChat"));
        mkClass.setName(className);
//        mkClass.addInterface(pool.get(version(ver,"net.minecraft.server.[V].Packet")));
        mkClass.addInterface(pool.get("cn.mcres.gyhhy.MXLib.chat.BungeeChatPacket"));
        mkClass.setModifiers(Modifier.PUBLIC);
        CtMethod[] mets = mkClass.getMethods();
        CtMethod main = null;
        for (CtMethod met : mets) {
            if (met.getName().equals("b")) {
//                System.out.println(met);
//                System.out.println(met.getSignature());
                if (met.getSignature().contains("PacketDataSerializer")) {
//                    System.out.println("The METHOD!");
                    main = met;
                }
            }
        }
        /*{
            CtMethod mmd = main;
            main = new CtMethod(mmd.getReturnType(), mmd.getName(), mmd.getParameterTypes(), mkClass);
        }*/
        mkClass.addField(new CtField(bc, "bcs", mkClass));
        CtConstructor[] costs = mkClass.getConstructors();

        boolean empt = false;
        for (CtConstructor c : costs) {
//            System.out.println("MPACKET: " + c.getSignature());
            if (c.getSignature().equals("([Lnet/md_5/bungee/api/chat/BaseComponent;)V")) {
                empt = true;
            }
        }
        if (!empt) {
            CtConstructor cooc = new CtConstructor(new CtClass[]{bc}, mkClass);
            cooc.setModifiers(Modifier.PUBLIC);
            cooc.setBody("{this.bcs = $1;}");
            mkClass.addConstructor(cooc);
        } else {
            CtConstructor cooc = mkClass.getConstructor("([Lnet/md_5/bungee/api/chat/BaseComponent;)V");
            cooc.setModifiers(Modifier.PUBLIC);
            cooc.insertBeforeBody("{this.bcs = $1;}");
        }

        main.setBody("{}");
        main.insertBefore("$1.a(net.md_5.bungee.chat.ComponentSerializer.toString(this.bcs));$1.writeByte(0);");
        try {
            mkClass.addMethod(main);
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        byte[] codes = mkClass.toBytecode();
//        System.out.println(Base64.encode(codes));
        Class<?> cx = ByteCodeHelper.getDynamicClassLoader().load(mkClass.getName(), codes);
        register(cx, MXAPI.getNMSClass("PacketPlayOutChat"));
        return cx;
//        mkClass.setName(mkClass.);
//        classInfo(mkClass.toClass());
//        net.minecraft.server.v1_12_R1.ChatMessageType;
    }

    public static BungeeChatAPI getAPI() {
        return api;
    }
    private static Class<?> nmsClass;

    private static void register(Class<?> cx, Class<?> nmsClass) throws Exception {
        BungeeChatAPI.nmsClass = nmsClass;
        Class<?> EnumProtocol = MXAPI.getNMSClass("EnumProtocol");
        Object ep = RefUtil.get(EnumProtocol, "PLAY");
        Method met = null;
        Class<?> EnumProtocolDirection = MXAPI.getNMSClass("EnumProtocolDirection");
        {
            Method[] mets = EnumProtocol.getDeclaredMethods();
            for (Method m : mets) {
                if (m.getParameterCount() == 2) {
                    if (m.getParameterTypes()[0] == EnumProtocolDirection) {
                        if (m.getParameterTypes()[1] == Class.class) {
                            met = m;
                        }
                    }
                }
            }
            met.setAccessible(true);
            met.invoke(ep, RefUtil.get(EnumProtocolDirection, "CLIENTBOUND"), cx);
        }
        {
            Field[] fies = EnumProtocol.getDeclaredFields();
            Field fff = null;
            for (Field f : fies) {
                String s = (f.getGenericType().toString());
                if (s.startsWith("java.util.Map<") && s.contains("Packet") && s.endsWith("EnumProtocol>")) {
                    fff = f;
                }
            }
            fff.setAccessible(true);
            Map<Class<?>, Object> map = RefUtil.get(fff, (Object) null);
            Set<Map.Entry<Class<?>, Object>> set = map.entrySet();
            for (Map.Entry<Class<?>, Object> ex : set) {
                if (ex.getKey() == nmsClass) {
                    map.put(cx, ex.getValue());
                    break;
                }
            }
        }
        {
            Field[] fies = EnumProtocol.getDeclaredFields();
            Field fff = null;
            for (Field f : fies) {
                String s = (f.getGenericType().toString());
                if (s.startsWith("java.util.Map<") && s.contains("EnumProtocolDirection") && s.endsWith(">>>")) {
                    fff = f;
                }
            }
            fff.setAccessible(true);
            Map<Object, BiMap<Integer, Class<?>>> map = RefUtil.get(fff, ep);
            Object setKey = null;
            BiMap<Integer, Class<?>> setValue = null;
            Set<Map.Entry<Object, BiMap<Integer, Class<?>>>> entrySet = map.entrySet();
            for (Map.Entry<Object, BiMap<Integer, Class<?>>> e : entrySet) {
                if (e.getValue().containsValue(nmsClass)) {
                    setKey = e.getKey();
                    setValue = e.getValue();
                }
            }
//            System.out.println("SetKey: " + setKey);
//            System.out.println("SetValue: " + setValue);
            map.put(setKey, new AWM(setValue));
//            System.out.println("NewValue: " + map.get(setKey));
        }
    }

    public static class SKS implements BiMap<Class<?>, Integer> {

        private final BiMap<Class<?>, Integer> sup;

        public SKS(BiMap<Class<?>, Integer> sup) {
            this.sup = sup;

        }

        @Override
        public Integer put(Class<?> paramK, Integer paramV) {
            return sup.put(paramK, paramV);
        }

        @Override
        public Integer forcePut(Class<?> paramK, Integer paramV) {
            return sup.forcePut(paramK, paramV);
        }

        @Override
        public void putAll(Map<? extends Class<?>, ? extends Integer> paramMap) {
            sup.putAll(paramMap);
        }

        @Override
        public Set<Integer> values() {
            return sup.values();
        }

        @Override
        public BiMap<Integer, Class<?>> inverse() {
            return sup.inverse();
        }

        @Override
        public int size() {
            return sup.size();
        }

        @Override
        public boolean isEmpty() {
            return sup.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return sup.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return sup.containsValue(value);
        }

        @Override
        public Integer get(Object key) {
            if (key == BungeeChatAPI.packet) {
                return sup.get(nmsClass);
            }
            return sup.get(key);
        }

        @Override
        public Integer remove(Object key) {
            return sup.remove(key);
        }

        @Override
        public void clear() {
            sup.clear();
        }

        @Override
        public Set<Class<?>> keySet() {
            return sup.keySet();
        }

        @Override
        public Set<Entry<Class<?>, Integer>> entrySet() {
            return sup.entrySet();
        }
    }

    public static class AWM implements BiMap<Integer, Class<?>> {

        private final BiMap<Integer, Class<?>> sup;

        public AWM(BiMap<Integer, Class<?>> sup) {
            this.sup = sup;
        }

        @Override
        public Class<?> put(Integer paramK, Class<?> paramV) {
            return sup.put(paramK, paramV);
        }

        @Override
        public Class<?> forcePut(Integer paramK, Class<?> paramV) {
            return sup.forcePut(paramK, paramV);
        }

        @Override
        public void putAll(Map<? extends Integer, ? extends Class<?>> paramMap) {
            sup.putAll(paramMap);
        }

        @Override
        public Set<Class<?>> values() {
            return sup.values();
        }

        @Override
        public BiMap<Class<?>, Integer> inverse() {
            return new SKS(sup.inverse());
        }

        @Override
        public int size() {
            return sup.size();
        }

        @Override
        public boolean isEmpty() {
            return sup.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return sup.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return sup.containsValue(value);
        }

        @Override
        public Class<?> get(Object key) {
            return sup.get(key);
        }

        @Override
        public Class<?> remove(Object key) {
            return sup.remove(key);
        }

        @Override
        public void clear() {
            sup.clear();
        }

        @Override
        public Set<Integer> keySet() {
            return sup.keySet();
        }

        @Override
        public Set<Entry<Integer, Class<?>>> entrySet() {
            return sup.entrySet();
        }
    }

    private BungeeChatAPI() {
    }

    public void send(Player player, BaseComponent bc) {
        send(player, new BaseComponent[]{bc});
    }

    public void send(Player player, BaseComponent... bcs) {
        if (SpigotHelper.isSupportSpigot()/* && false */) {
            try {
                player.spigot().sendMessage(bcs);
            } catch (Throwable thr) {
                newPacket(bcs).sendTo(player);
            }
        } else {
            newPacket(bcs).sendTo(player);
        }
    }

    public BungeeChatPacket newPacket(BaseComponent bc) {
        return newPacket(new BaseComponent[]{bc});
    }

    public BungeeChatPacket newPacket(BaseComponent... bcs) {
        load();
        try {
            return constructor.newInstance((Object) bcs);
        } catch (Error | RuntimeException er) {
            throw er;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void send(Player player, BungeeChatPacket packet) {
        MXAPI.getTitleAPI().sendPacket(player, packet);
    }
}
