package cn.mcres.karlatemp.mxlib.logging;

public enum PrintingType {
    RAW(0), COLORED(1), SKIP_COLOR(2);
    private final int type;

    public int type() {
        return type;
    }

    public String toString() {
        return name() + "[" + type + "]";
    }

    private PrintingType(int type) {
        this.type = type;
    }

    public static PrintingType valueOf(int code) {
        for (PrintingType t : values()) {
            if (code == t.type) {
                return t;
            }
        }
        return null;
    }
}