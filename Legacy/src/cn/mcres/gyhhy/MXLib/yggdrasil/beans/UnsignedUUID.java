/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.yggdrasil.beans;

import java.util.UUID;

public final class UnsignedUUID {

    public static void main(String... args) {
        UUID uid = UUID.randomUUID();
        System.out.println(uid);
        String ux = uid.toString().replaceAll("\\-", "");
        System.out.println(ux);
        System.out.println(ux.length());
        System.out.println(ux.substring(0, 16));
        System.out.println(ux.substring(16, 32));
        System.out.println(new UnsignedUUID(uid));
    }
    private static final long[] ff = new long[128];

    static {
        int x = 0;
        for (int i = '0'; i <= '9'; i++, x++) {
            ff[i] = x;
        }
        int rd = 'A' - 'a';
        for (int i = 'a'; i <= 'f'; i++, x++) {
            ff[i] = ff[i + rd] = x;
        }
    }

    public static long dec(String st) {
        long rq = 0;
        for (char c : st.toCharArray()) {
            rq <<= 4;
            rq |= ff[c];
        }
        return rq;
    }

    public static UnsignedUUID parse(String uid) {
//        UUID.fromString(uid);
        if (uid.length() != 32) {
            throw new IllegalArgumentException("Invalid UnsignedUUID string: " + uid);
        }
        return new UnsignedUUID(dec(uid.substring(0, 16)),
                dec(uid.substring(16, 32))
        );
    }
    private final UUID uuid;

    public UnsignedUUID(UUID uid) {
        uuid = uid;
    }

    public UnsignedUUID(long mostSigBits, long leastSigBits) {
        this(new UUID(mostSigBits, leastSigBits));
    }

    public UUID getUUID() {
        return uuid;
    }

    public long getMostSignificantBits() {
        return uuid.getMostSignificantBits();
    }

    public long getLeastSignificantBits() {
        return uuid.getLeastSignificantBits();
    }

    public String toString() {
        return Long.toHexString(uuid.getMostSignificantBits()) + Long.toHexString(uuid.getLeastSignificantBits());
    }

}
