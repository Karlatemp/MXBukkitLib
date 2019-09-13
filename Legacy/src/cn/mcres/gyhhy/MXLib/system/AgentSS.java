/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AgentSS.java@author: karlatemp@vip.qq.com: 19-9-11 下午1:53@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.system;

import cn.mcres.gyhhy.MXLib.Core;
import cn.mcres.gyhhy.MXLib.http.WebHelper;
import cn.mcres.gyhhy.MXLib.log.BasicLogger;
import java.io.InputStreamReader;
import java.util.Map;
import cn.mcres.gyhhy.MXLib.json.Json;
import java.lang.instrument.Instrumentation;

/**
 *
 * @author 32798
 */
public class AgentSS {

    public static void premain(String option, Instrumentation ist) {
        AgentStartup.premain(option, ist);
    }

    public static void agentmain(String option, Instrumentation ist) {
        AgentStartup.agentmain(option, ist);
    }

    public static void main(String[] args) {
        BasicLogger bl = Core.getBL();
        bl.printf("MXBukkitLib")
                .printf("Version " + Core.getVersion())
                .printf("Copyright (C) 2018-2019 Karlatemp.")
                .printf("   All Rights Reserved.")
                .printf("")
                .printf("GitHub: https://github.com/Karlatemp/MXBukkitLib")
                .printf("        https://dev.tencent.com/u/GYHHY/p/MXBukkitLib/git")
                .printf("")
                .printf("JavaDoc https://karlatemp.github.io/MXBukkitLib/index.html")
                .printf("        http://gyhhy.coding.me/MXBukkitLib/");
        int timeout = 3000;
        boolean verbose = false;
        for (int i = 0; i < args.length; i++) {
            String s = args[i];
            if (s != null) {
                if (s.equals("--no-yan")) {
                    return;
                }
                if (s.equals("--yan-verbose")) {
                    verbose = true;
                }
                if (s.equals("--time-out")) {
                    try {
                        timeout = Integer.parseInt(args[++i]);
                    } catch (Throwable thr) {
                    }
                }
            }
        }
        final int TIMEOUT = timeout;
        final boolean VB = verbose;
        BasicLogger yan = BasicLogger.createRawLogger(null, null, "HTTP");
        WebHelper.http("https://api.ooopn.com/yan/api.php?type=json").header(uc -> {
            uc.setConnectTimeout(TIMEOUT);
            uc.setReadTimeout(TIMEOUT);
        }).response((a, b, c) -> {
            if (a == 200) {
                Object aj = Json.read(new InputStreamReader(c));
                Map<String, String> kvs = (Map<String, String>) aj;
                String msg = kvs.get("hitokoto");
                String author = kvs.get("author");
                String source = kvs.get("source");
                String catname = kvs.get("catname");
                String cat = kvs.get("cat");
                yan.printf(msg + "\n-----------  " + source + "  " + author + "    " + catname + "(" + cat + ")");
            }
        }).onCatch(e -> {
            if (VB) {
                yan.error("Failed to get random message.");
                yan.printStackTrace(e, false);
            }
            WebHelper.http("https://api.ooopn.com/ciba/api.php?type=json").header(uc -> {
                uc.setConnectTimeout(TIMEOUT);
                uc.setReadTimeout(TIMEOUT);
            }).onCatch(ew -> {
                yan.error("Failed to get message.");
                if (VB) {
                    yan.printStackTrace(ew, false);
                }
            }).response((a, b, c) -> {
                if (a == 200) {
                    Object aj = Json.read(new InputStreamReader(c));
                    Map<String, String> kvs = (Map<String, String>) aj;
                    String msg = kvs.get("ciba");
                    String msg_en = kvs.get("ciba-en");
                    yan.printf(msg + "\n" + msg_en);
                }
            }).connect();
        }).connect();
    }

}
