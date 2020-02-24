/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/20 22:45:18
 *
 * MXLib/expend.vexview.interpreter/CMD.java
 */

package cn.mcres.karlatemp.vit;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CMD {

    static BitSet oks;

    static {
        oks = new BitSet();
        oks.set('A', 'Z' + 1, true);
        oks.set('a', 'z' + 1, true);
        oks.set('0', '9' + 1, true);
        oks.set('\u4e00', '\u9fa5' + 1, true);
        for (char c : " ,.<>/?;:'|[]{}-=_+)(*&^%$#@!".toCharArray()) {
            oks.set(c, true);
        }
    }

    public static StringBuilder encode(String str) {
        var sb = new StringBuilder(str.length()).append('\"');
        for (var c : str.toCharArray()) {
            switch (c) {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '"':
                    sb.append("\\\"");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    if (oks.get(c))
                        sb.append(c);
                    else {
                        sb.append("\\u");
                        var hex = Integer.toHexString(c);
                        sb.append("0000".substring(hex.length())).append(hex);
                    }
                    break;
            }
        }
        return sb.append('\"');
    }

    public static String namespace = System.getProperty("namespace", "transform");

    interface Handle {
        void run(File file, ArrayList<String> path) throws IOException;
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("java -jar Vit.jar [plugins dir]");
            System.exit(1);
            return;
        }
        var plugin = args[0];
        // var plugin = "F:\\GYHHY\\Server\\CB\\Servers\\test\\plugins";
        var vv = new File(plugin, "VexView");
        var vit = new File(plugin, "Vit");

        var space = new File(vit, namespace);

        var gui = new File(vv, "gui");
        var image = new File(vv, "image");
        var gif = new File(vv, "gifimage");
        var button = new File(vv, "button");
        var slot = new File(vv, "slot");
        var tag = new File(vv, "tag");
        var text = new File(vv, "text");
        post(gui, new ArrayList<>(), (file, path) -> {
            var p = String.join("/", path).toLowerCase();
            var store = new File(space, "gui/" + p + ".txt");
            store.getParentFile().mkdirs();
            var conf = YamlConfiguration.loadConfiguration(file);
            try (var out = new FileOutputStream(store)) {
                try (var ps = new PrintStream(out, true, StandardCharsets.UTF_8)) {
                    ps.println("# The size of gui.");
                    ps.println("size " + conf.get("width") + " " + conf.get("high") + ";");
                    ps.println("background " + encode(conf.getString("gui")) + ";");
                    ps.println("# The location of gui.");
                    ps.println("location " + conf.get("x") + " " + conf.get("y") + ";");
                    ps.println();
                    ps.println("# ==== Starting import buttons.");
                    for (var but : conf.getStringList("buttons")) {
                        ps.println("button { $include " + encode(namespace + ":button/" + but.toLowerCase()) + " };");
                    }
                    ps.println("# ==== Starting import texts.");
                    for (var t : conf.getStringList("text")) {
                        ps.println("text { $include " + encode(namespace + ":text/" + t.toLowerCase()) + " };");
                    }
                    ps.println("# ==== Starting import images.");
                    for (var i : conf.getStringList("image")) {
                        ps.println("image { $include " + encode(namespace + ":image/" + i.toLowerCase()) + " };");
                    }
                    ps.println("# ==== Starting import gif images.");
                    for (var i : conf.getStringList("gifimage")) {
                        ps.println("image { $include " + encode(namespace + ":image/" + i.toLowerCase() + "-gif") + " };");
                    }
                    ps.println("# ==== Starting import slots.");
                    for (var i : conf.getStringList("slot")) {
                        ps.println("slot { $include " + encode(namespace + ":slot/" + i.toLowerCase()) + " };");
                    }
                }
            }
        });
        post(image, new ArrayList<>(), (file, path) -> {
            var p = String.join("/", path).toLowerCase();
            var store = new File(space, "image/" + p + ".txt");
            store.getParentFile().mkdirs();
            var conf = YamlConfiguration.loadConfiguration(file);
            try (var out = new FileOutputStream(store)) {
                try (var ps = new PrintStream(out, true, StandardCharsets.UTF_8)) {
                    ps.println("# Import from " + p);
                    ps.println("# size <width> <height>;");
                    ps.println("size " + conf.get("width") + " " + conf.get("high") + ";");
                    ps.println("move " + conf.get("x") + " " + conf.get("y") + ";");
                    ps.println("image_size " + conf.get("xshow") + " " + conf.get("yshow") + ";");
                    ps.println("background " + encode(conf.getString("image")) + ";");
                    if (conf.contains("hover")) {
                        ps.println("hover { $include " + encode(namespace + ":text/" + conf.getString("hover").toLowerCase()) + " };");
                    }
                }
            }
        });
        post(button, new ArrayList<>(), (file, path) -> {
            var p = String.join("/", path).toLowerCase();
            var store = new File(space, "button/" + p + ".txt");
            store.getParentFile().mkdirs();
            var conf = YamlConfiguration.loadConfiguration(file);
            try (var out = new FileOutputStream(store)) {
                try (var ps = new PrintStream(out, true, StandardCharsets.UTF_8)) {
                    ps.println("# Import form " + p);
                    ps.println("id " + encode(conf.getString("id")) + ";");
                    ps.println("text " + encode(conf.getString("name")) + ";");
                    ps.println("background " + encode(conf.getString("url")) + " " + encode(conf.getString("url2")) + ";");
                    ps.println("move " + conf.get("x") + " " + conf.get("y") + ";");
                    ps.println("size " + conf.get("width") + " " + conf.get("high") + ";");
                    var commands = conf.getStringList("commands");
                    var to = conf.getString("to");
                    if (!(to.equals("-") && commands.isEmpty())) {
                        ps.println("handle " + encode(namespace + ":handle/" + p) + ";");
                        var handle = new File(space, "handle/" + p + ".txt");
                        handle.getParentFile().mkdirs();
                        try (var hps = new PrintStream(new FileOutputStream(handle), true, StandardCharsets.UTF_8)) {
                            for (var command : commands) {
                                hps.println("command " + encode(command) + ";");
                            }
                            if (!to.equals("-")) {
                                hps.println("open " + encode(to) + ";");
                            }
                            if (conf.getBoolean("close", false)) {
                                hps.println("close;");
                            }
                            if (conf.getBoolean("asop")) {
                                hps.println("add_perm \"*\";");
                            }
                        }
                    }
                }
            }
        });
        post(slot, new ArrayList<>(), (file, path) -> {
            var p = String.join("/", path).toLowerCase();
            var store = new File(space, "slot/" + p + ".txt");
            store.getParentFile().mkdirs();
            var conf = YamlConfiguration.loadConfiguration(file);
            try (var out = new FileOutputStream(store)) {
                try (var ps = new PrintStream(out, true, StandardCharsets.UTF_8)) {
                    ps.println("# Import from " + p);
                    ps.println("move " + conf.get("x") + " " + conf.get("y") + ";");
                    ps.println("item {");
                    if (conf.contains("item.name")) {
                        ps.println("    name " + encode(conf.getString("item.name")) + ";");
                    }
                    ps.println("    material " + encode(conf.getString("item.material")) + ";");
                    if (conf.contains("item.amount")) {
                        ps.println("    amount " + conf.get("item.amount") + ';');
                    }
                    if (conf.contains("item.durability")) {
                        ps.println("    durability " + conf.get("item.durability") + ';');
                    }
                    var lore = conf.getStringList("item.lore");
                    if (!lore.isEmpty()) {
                        ps.println("    lore");
                        for (var l : lore) {
                            ps.println("        " + encode(l));
                        }
                        ps.println("    ;");
                    }
                    ps.println("};");
                }
            }
        });
        post(text, new ArrayList<>(), (file, path) -> {
            var p = String.join("/", path).toLowerCase();
            var store = new File(space, "text/" + p + ".txt");
            store.getParentFile().mkdirs();
            var conf = YamlConfiguration.loadConfiguration(file);
            try (var out = new FileOutputStream(store)) {
                try (var ps = new PrintStream(out, true, StandardCharsets.UTF_8)) {
                    ps.println("# Import from " + p);
                    ps.println("move " + conf.get("x") + " " + conf.get("y") + ';');
                    ps.println("line");
                    for (var l : conf.getStringList("text")) {
                        ps.println("    " + encode(l));
                    }
                    ps.println(";");
                }
            }
        });
        post(tag, new ArrayList<>(), (file, path) -> {
            var p = String.join("/", path).toLowerCase();
            var store = new File(space, "tag/" + p + ".txt");
            store.getParentFile().mkdirs();
            var conf = YamlConfiguration.loadConfiguration(file);
            Supplier<Object> a = () -> {
                var sb = new StringBuilder("{\n");
                sb.append("    rotate ").append(conf.get("direction.rotate.x")).append(" ").append(conf.get("direction.rotate.y")).append(" ").append(conf.get("direction.rotate.z")).append(";\n");
                sb.append("    for_player ").append(conf.getBoolean("direction.for_player", false)).append(";\n");
                sb.append("    current_player_visitable ").append(conf.getBoolean("direction.player_can_see", false)).append(";\n");
                return sb.append("}");
            };
            try (var out = new FileOutputStream(store)) {
                try (var ps = new PrintStream(out, true, StandardCharsets.UTF_8)) {
                    ps.println("# Import from " + p);
                    var type = conf.getString("type");
                    switch (type.toLowerCase()) {
                        case "text": {
                            ps.println("text " + a.get() + " { $include " + encode(namespace + ":text/" + conf.getString("file")) + "; } " + conf.get("direction.text_back"));
                            break;
                        }
                        case "image": {
                            var info = YamlConfiguration.loadConfiguration(new File(image, conf.getString("file") + ".yml"));
                            ps.println("image " + a.get() + "{ $include " + encode(namespace + ":image/" + conf.getString("file")) + "; } " + info.get("width") + " " + info.get("high") + ";");
                            break;
                        }
                        case "gif": {
                            var info = YamlConfiguration.loadConfiguration(new File(gif, conf.getString("file") + ".yml"));
                            ps.println("image " + a.get() + "{ $include " + encode(namespace + ":image/" + conf.getString("file")) + "-gif" + "; } " + info.get("width") + " " + info.get("high") + ";");
                            break;
                        }
                    }
                }
            }
        });
        post(gif, new ArrayList<>(), (file, path) -> {
            var p = String.join("/", path).toLowerCase();
            var store = new File(space, "image/" + p + "-gif.txt");
            store.getParentFile().mkdirs();
            var conf = YamlConfiguration.loadConfiguration(file);
            try (var out = new FileOutputStream(store)) {
                try (var ps = new PrintStream(out, true, StandardCharsets.UTF_8)) {
                    ps.println("# Import from " + p);
                    ps.println("# size <width> <height>;");
                    ps.println("gif 1;");
                    ps.println("size " + conf.get("width") + " " + conf.get("high") + ";");
                    ps.println("move " + conf.get("x") + " " + conf.get("y") + ";");
                    ps.println("image_size " + conf.get("xshow") + " " + conf.get("yshow") + ";");
                    ps.println("background " + encode(conf.getString("image")) + ";");
                    if (conf.contains("hover")) {
                        ps.println("hover { $include " + encode(namespace + ":text/" + conf.getString("hover").toLowerCase()) + " };");
                    }
                }
            }
        });
    }

    private static void post(File start, ArrayList<String> names, Handle handle) throws IOException {
        post(start, names, handle, false);
    }

    private static void post(File start, ArrayList<String> names, Handle handle, boolean addNamespace) throws IOException {
        if (start.isFile()) {
            var name = start.getName();
            if (name.endsWith(".yml")) {
                var size = names.size();
                if (addNamespace)
                    names.add(name.substring(0, name.length() - 4).toLowerCase());
                System.out.println("Invoking " + start + "[" + String.join("/", names) + "]");
                handle.run(start, names);
                if (addNamespace) names.remove(size);
            }
        } else if (start.isDirectory()) {
            var list = start.listFiles();
            if (list != null) {
                var size = names.size();
                if (addNamespace) names.add(start.getName().toLowerCase());
                for (var file : list) {
                    post(file, names, handle, true);
                }
                if (addNamespace) names.remove(size);
            }
        }
    }
}
