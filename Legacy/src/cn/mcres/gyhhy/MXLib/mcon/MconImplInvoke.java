package cn.mcres.gyhhy.MXLib.mcon;

public class MconImplInvoke {

    static final TListener tl = new TListener();

    public static void start(String pwd, int port) {
        tl.setPort(port);
        tl.setPwd(pwd.getBytes(Types.UTF_8));
        tl.start();
    }

    public static void close() {
        tl.shutdown();
    }
}
