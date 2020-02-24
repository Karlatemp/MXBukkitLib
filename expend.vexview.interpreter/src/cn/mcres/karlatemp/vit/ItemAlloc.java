/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/19 19:37:13
 *
 * MXLib/expend.vexview.interpreter/ItemAlloc.java
 */

package cn.mcres.karlatemp.vit;

import cn.mcres.karlatemp.mxlib.interpreter.CommandBlock;
import cn.mcres.karlatemp.mxlib.interpreter.CommandLine;
import cn.mcres.karlatemp.mxlib.interpreter.CommandVisitor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings({"unchecked", "JavaLangInvokeHandleSignature"})
public class ItemAlloc implements CommandVisitor<Void, ItemAlloc> {
    private Material material;
    private static final Function<String, Material> valueOf;
    private static final SetDurability setDurability;
    private short d = -1;
    private Map<Enchantment, Integer> enchantments = new HashMap<>();

    interface SetDurability {
        void run(ItemStack stack, short d);
    }

    private String name;
    private List<String> lore = new ArrayList<>();
    private int amo = 1;

    static {
        var lk = MethodHandles.lookup();
        try {
            valueOf = (Function<String, Material>)
                    LambdaMetafactory.metafactory(
                            lk,
                            "apply",
                            MethodType.methodType(Function.class),
                            MethodType.methodType(Object.class, Object.class),
                            lk.findStatic(Material.class, "valueOf", MethodType.methodType(Material.class, String.class)),
                            MethodType.methodType(Material.class, String.class)).getTarget().invoke();
            setDurability = (SetDurability)
                    LambdaMetafactory.metafactory(
                            lk, "run", MethodType.methodType(SetDurability.class),
                            MethodType.methodType(void.class, ItemStack.class, short.class),
                            lk.findVirtual(ItemStack.class, "setDurability", MethodType.methodType(void.class, short.class)),
                            MethodType.methodType(void.class, ItemStack.class, short.class)

                    ).getTarget().invoke();
        } catch (Throwable throwable) {
            throw new ExceptionInInitializerError(throwable);
        }
    }

    @Override
    public CommandVisitor<Void, ItemAlloc> visitBlock(CommandBlock block, Void value, ItemAlloc contextReturn) {
        return this;
    }

    public ItemStack done() {
        var stack = new ItemStack(material, amo);
        if (d != -1) setDurability.run(stack, d);
        final ItemMeta itemMeta = stack.getItemMeta();
        if (name != null) itemMeta.setDisplayName(name);
        if (!lore.isEmpty()) itemMeta.setLore(lore);
        for (var e : enchantments.entrySet()) {
            itemMeta.addEnchant(e.getKey(), e.getValue(), true);
        }
        stack.setItemMeta(itemMeta);
        return stack;
    }

    @Override
    public CommandVisitor<Void, ItemAlloc> visitLine(CommandLine line, Void value, ItemAlloc contextReturn) {
        var it = line.getArguments().iterator();
        switch (it.next().toString()) {
            case "name":
                name = it.next().toString();
                break;
            case "lore":
                while (it.hasNext()) lore.add(it.next().toString());
                break;
            case "amount":
            case "count":
                amo = Integer.parseInt(it.next().toString());
                break;
            case "durability":
                d = Short.parseShort(it.next().toString());
                break;
            case "material":
                material = valueOf.apply(it.next().toString());
                break;
            case "enchat":
            case "enchant":
            case "enchantments":
            case "enchantment":
                var name = it.next().toString();
                var e = Enchantment.getByName(name);
                if (e == null) throw new RuntimeException("Unknown enchantment: " + name);
                enchantments.put(e, Integer.parseInt(it.next().toString()));
                break;
        }
        return null;
    }

    @Override
    public ItemAlloc defaultValue(Void value) {
        return this;
    }
}
