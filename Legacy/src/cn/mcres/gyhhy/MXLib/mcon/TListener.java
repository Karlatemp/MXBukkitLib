package cn.mcres.gyhhy.MXLib.mcon;

import cn.mcres.gyhhy.MXLib.Core;
import cn.mcres.gyhhy.MXLib.log.BasicLogger;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TListener implements Runnable, ThreadFactory {

    public static TListener open() {
        return new TListener();
    }
    final ExecutorService pool;
    int port = 5417;
    final BasicLogger logger;
    public byte[] passwd = "MCON".getBytes();

    public int getPort() {
        return port;
    }

    public TListener setPort(int port) {
        if (server != null) {
            throw new java.security.AccessControlException("Listener started.");
        }
        this.port = port;
        return this;
    }
    protected final AtomicInteger ai = new AtomicInteger(1);
    protected final ThreadGroup tg = initThreadGroup();

    protected ThreadGroup initThreadGroup() {
        return new ThreadGroup("MCON Listener");
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(tg, r, "MCON Listener Accepter #" + ai.getAndIncrement());
        t.setDaemon(true);
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }

    protected ExecutorService initExecutorService() {
        return Executors.newFixedThreadPool(port, this);
    }

    protected TListener() {
        this.pool = initExecutorService();
        logger = Core.getBL();
    }
    private ServerSocketChannel server;

    public void start() {
        if (server == null) {
            pool.execute(this);
        }
    }

    public void shutdown() {
        try {
            server.close();
        } catch (IOException ex) {
        }
        pool.shutdown();
    }

    protected void open(SocketChannel s) throws IOException {
        pool.execute(new Runner(s, this));
    }

    @Override
    public void run() {
        if (server != null) {
            throw new java.security.AccessControlException("Listener started.");
        }
        try {
            ServerSocketChannel ss = ServerSocketChannel.open();
            ss.socket().bind(new InetSocketAddress(port));
            server = ss;
            logger.printf("[MCON] MCON running on 0.0.0.0:" + (port = ss.socket().getLocalPort()));
            while (ss.isOpen()) {
                SocketChannel s = ss.accept();
                logger.printf("[MCON] Accepted " + s);
                if (s != null) {
                    open(s);
                }
            }
            logger.printf("[MCON] Listener stoped.");
        } catch (IOException ex) {
        }
    }

    public void setPwd(byte[] bytes) {
        this.passwd = bytes;
    }
}
