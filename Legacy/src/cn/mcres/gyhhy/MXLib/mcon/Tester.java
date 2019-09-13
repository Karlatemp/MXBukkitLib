/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.mcon;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.bukkit.permissions.PermissibleBase;

/**
 *
 * @author 32798
 */
public class Tester {

    public static class RunnerTest extends Runner {

        public RunnerTest(SocketChannel sc, TListener server) {
            super(sc, server);
        }

        @Override
        protected void invokeCommand(String line) {
            System.out.println("Command invoke: " + line);
            sendMessage("You invoked " + line);
        }

        protected void setName(String name) {
            System.out.println("Name set: " + name);
            super.setName(name);
        }

        @Override
        public void setOp(boolean value) {
            System.out.println("Op set: " + value);
            super.setOp(value); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected PermissibleBase initBase() {
            return new PermissibleBase(this) {

                @Override
                public synchronized void clearPermissions() {

                }

                @Override
                public void recalculatePermissions() {
                }

            };
        }

    }

    public static class TListenerTest extends TListener {

        @Override
        protected void open(SocketChannel s) throws IOException {
            super.pool.execute(new RunnerTest(s, this));
        }

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        TListener t = new TListenerTest();
        t.start();
        Thread.sleep(1000L);
        MconClient c = new MconClient("localhost", t.getPort(), t.passwd);
        c.connect();
        c.setName("FUCK");
        c.sendCommand("whoami");
        c.sendCommand("YOU WHAT");
        c.setOp(false);
        Thread.sleep(1000L);
        c.sendCommand("shit");
        ByteBuffer bb;
        byte[] buf = new byte[1024];
        while ((bb = c.read()) != null) {
            int size = bb.remaining();
            while (size > 0) {
                int s = Math.min(size, buf.length);
                bb.get(buf, 0, s);
                size -= s;
                System.out.write(buf, 0, s);
            }
            System.out.println();
        }
        c.close();
        System.out.println("END.");
    }

}
