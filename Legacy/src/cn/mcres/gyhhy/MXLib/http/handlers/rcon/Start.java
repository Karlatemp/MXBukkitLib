package cn.mcres.gyhhy.MXLib.http.handlers.rcon;

import cn.mcres.gyhhy.MXLib.http.URLStreamManager;
import cn.mcres.gyhhy.MXLib.io.MemoryInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Start {

    public static void main(String[] args) throws Exception {

        System.out.println("Connection in.");
        URLStreamManager.registerHandler("rcon", new Handler());
        URL u = new URL("rcon://localhost:25575/");
        RconConnection c = (RconConnection) u.openConnection();
        c.addRequestProperty("passwd", "RCONPWD");
        c.addRequestProperty("blocking", "true");
        c.connect();
        MemoryInputStream inp = c.getInputStream();
        new Thread(() -> {
            byte[] buf = new byte[1024];
            while (true) {
                int l = inp.read(buf);
                if (l == -1) {
                    break;
                }
                System.out.write(buf, 0, l);
            }
        }).start();
        OutputStream os = c.getOutputStream();
        PrintStream ps = new PrintStream(os);
        ps.println("ver");
        ps.println("tps");
        for (int i = 1; i < 31; i++) {
            ps.append("? ").println(i);
        }
        c.disconnect();
        System.out.println("DISCONNECTIONED.");
    }
}
