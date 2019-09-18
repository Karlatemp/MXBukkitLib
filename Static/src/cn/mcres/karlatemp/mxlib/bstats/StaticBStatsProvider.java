/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: StaticBStatsProvider.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.bstats;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.tools.IJsonWriter;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

public class StaticBStatsProvider implements BStatsProvider {
    private static final ThreadGroup group;
    private static final AtomicLong threadId = new AtomicLong();

    static {
        ThreadGroup root = Thread.currentThread().getThreadGroup(), tmp = root;
        while (tmp != null) {
            root = tmp;
            tmp = tmp.getParent();
        }
        group = new ThreadGroup(root, "MXLib-BStats Thread Run Pool");
    }

    protected static final ExecutorService pools = Executors.newFixedThreadPool(5,
            runnable -> new Thread(group, runnable, "MXLib-BStats #" + threadId.getAndIncrement())
    );

    static {
    }

    @Override
    public int getPlayerAmount() {
        return 0;
    }

    @Override
    public boolean isOnlineMode() {
        return true;
    }

    @Override
    public String getBukkitVersion() {
        return String.valueOf(getClass().getPackage().getImplementationVersion());
    }

    @Override
    public String getBukkitName() {
        return "StaticInvoke";
    }

    protected final List<BStats> bStats = new ArrayList<>();

    @Override
    public void onCreateBStats(@NotNull BStats bstats) {
        bStats.add(bstats);
    }

    @Override
    public List<Map<String, Object>> getPluginDataList() {
        return bStats.stream().map(BStats::getPluginData).collect(Collectors.toList());
    }

    protected static byte[] compress(final String str) throws IOException {
        if (str == null) {
            return null;
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            try (GZIPOutputStream gzip = new GZIPOutputStream(outputStream)) {
                gzip.write(str.getBytes(StandardCharsets.UTF_8));
            }
            return outputStream.toByteArray();
        }
    }

    @Override
    public void sendData(String url, Map<String, Object> data) {
        pools.isTerminated();
        final Future<Object> submit = pools.submit(() -> {
            String dataraw = MXBukkitLib.getBeanManager().getBeanNonNull(IJsonWriter.class).write(data);
            byte[] send = compress(dataraw);
            if (BStats.logSentData) {
                logger().info("Sending data to bStats: " + dataraw);
            }
            HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
            http.setRequestMethod("POST");
            http.addRequestProperty("Accept", "application/json");
            http.addRequestProperty("Connection", "close");
            http.addRequestProperty("Content-Encoding", "gzip"); // We gzip our request
            http.addRequestProperty("Content-Length", String.valueOf(send.length));
            http.setRequestProperty("Content-Type", "application/json"); // We send our data in JSON format
            http.setRequestProperty("User-Agent", "MC-Server/" + BStats.B_STATS_VERSION);
            // Send data
            http.setDoOutput(true);
            try (DataOutputStream stream = new DataOutputStream(http.getOutputStream())) {
                stream.write(send);
                stream.flush();
            }
            http.connect();
            if (BStats.logResponseStatusText) {
                StringBuilder bui = new StringBuilder(http.getContentLength());
                try (InputStream io = http.getInputStream()) {
                    try (Scanner scanner = new Scanner(io)) {
                        while (scanner.hasNextLine()) {
                            bui.append(scanner.nextLine());
                        }
                    }
                }
                logger().info(() -> "Sent data to bStats and received response: " + bui);
            }
            http.disconnect();
            return null;
        });
        pools.submit(() -> {
            try {
                submit.get();
            } catch (InterruptedException e) {
                MXBukkitLib.getLogger().error("Error in waiting throwable submit for task " + submit);
            } catch (ExecutionException e) {
                MXBukkitLib.getAsJavaLogger().log(Level.WARNING, e.toString(), e.getCause());
            }
        });
    }

    @Override
    public Logger logger() {
        ClassLoader.getSystemClassLoader();
        return MXBukkitLib.getAsJavaLogger();
    }
}
