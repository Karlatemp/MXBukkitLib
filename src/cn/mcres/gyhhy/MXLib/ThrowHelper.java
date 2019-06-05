/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib;

/**
 *
 * @author 32798
 */
public class ThrowHelper {

    private static final ThrowHelper def = new ThrowHelper();
    private static final ThrowHelper instance;
    private static final String code = "yv66vgAAADQADwEAgmNuL21jcmVzL2d5aGh5L01YTGliL2ltcGwvVGhyb3dIZWxwZXJJbXBsW0J1aWxkVGltZT1bVHVlIEp1biAwNCAxMjozNjoxMSBDU1QgMjAxOV0sIEJ1aWxkVG9vbD1KYXZhU3Npc3QsIENvZGVCdWlsZEF0VmVyc2lvbj0xLzAvMF0HAAEBACBjbi9tY3Jlcy9neWhoeS9NWExpYi9UaHJvd0hlbHBlcgcAAwEAClNvdXJjZUZpbGUBAAcwXS5qYXZhAQASVFIuamF2YTxBdXRvQnVpbGQ+AQAGdGhyb3d4AQAYKExqYXZhL2xhbmcvVGhyb3dhYmxlOylWAQAEQ29kZQEABjxpbml0PgEAAygpVgwACwAMCgAEAA0AIQACAAQAAAAAAAIAAQAIAAkAAQAKAAAADgABAAMAAAACK78AAAAAAAEACwAMAAEACgAAABEAAQABAAAABSq3AA6xAAAAAAABAAUAAAACAAc=";

    static {
//        System.out.println()
        ThrowHelper th = null;
        try {
            Class<? extends ThrowHelper> tht
                    = RefUtil.loadClass(code, ThrowHelper.class.getClassLoader())
                            .asSubclass(ThrowHelper.class);
            th = tht.newInstance();
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        instance = th;
    }

    public static void main(String[] args) {
        getInstance().thr(new Throwable("Fuck You Little Man"));
    }

    public static ThrowHelper getInstance() {
        return instance == null ? def : instance;
    }

    public static ThrowHelper getDefault() {
        return def;
    }

    protected void throwx(Throwable thr) {
        if (thr instanceof Error) {
            throw (Error) thr;
        }
        if (thr instanceof RuntimeException) {
            throw (RuntimeException) thr;
        }
        throw new SysRuntimeException(thr.getLocalizedMessage(), thr);
    }

    public <T> T thr(Throwable thr) {
        throwx(thr);
        return null;
    }

    public <T> T thr(String msg, Throwable thr) {
        if (thr instanceof Error) {
            throw (Error) thr;
        }
        if (thr instanceof RuntimeException) {
            throw (RuntimeException) thr;
        }
        return thr(new SysRuntimeException(msg, thr));
    }

    public <T> T thr(String msg) {
        return thr(new SysRuntimeException(msg));
    }

    public <T> T thr() {
        return thr(new SysRuntimeException());
    }
}
