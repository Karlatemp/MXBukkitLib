/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BinConfiguration.java@author: karlatemp@vip.qq.com: 2019/12/29 下午2:05@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.configuation.bukkit;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.util.*;

/**
 * Bin Data Configuation.
 *
 * @since 2.11
 */
@SuppressWarnings("rawtypes")
public class BinConfiguration extends FileConfiguration {
    @Override
    public String saveToString() {
        return null;
    }

    @Override
    public void save(File file) throws IOException {
        new File(file, "..").mkdirs();
        file.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            save(fos, true);
        }
    }

    public void save(OutputStream fos, boolean autoClose) throws IOException {
        if (fos instanceof DataOutput) {
            if (autoClose) {
                try (OutputStream oos = fos) {
                    save((DataOutput) oos);
                }
            } else {
                save((DataOutput) fos);
            }
        }
        DataOutputStream ddos = new DataOutputStream(fos);
        if (autoClose) {
            try (DataOutputStream dd = ddos) {
                save(dd);
            }
        } else {
            save(ddos);
        }
    }

    public void save(DataOutput out) throws IOException {
        Map<String, Object> values = getValues(false);
        write(Tools.dump(values), out);
    }

    protected static void write(Object values, DataOutput dox) throws IOException {
        if (values == null) {
            dox.writeShort(Types.NULL.ordinal());
        } else if (values instanceof Map) {
            dox.writeShort(Types.Map.ordinal());
            dox.writeInt(((Map) values).size());
            for (Map.Entry e : ((Map<?, ?>) values).entrySet()) {
                write(e.getKey(), dox);
                write(e.getValue(), dox);
            }
        } else if (values instanceof Collection) {
            Collection ce = (Collection) values;
            dox.writeShort(Types.List.ordinal());
            dox.writeInt(ce.size());
            for (Object v : ce) {
                write(v, dox);
            }
        } else if (values instanceof CharSequence) {
            dox.writeShort(Types.String.ordinal());
            dox.writeUTF(values.toString());
        } else if (values instanceof Boolean) {
            if ((Boolean) values) {
                dox.writeShort(Types.BooleanTrue.ordinal());
            } else {
                dox.writeShort(Types.BooleanFalse.ordinal());
            }
        } else if (values instanceof Short) {
            dox.writeShort(Types.Short.ordinal());
            dox.writeShort(Short.toUnsignedInt((Short) values));
        } else if (values instanceof Integer) {
            dox.writeShort(Types.Integer.ordinal());
            dox.writeInt((Integer) values);
        } else if (values instanceof Double) {
            dox.writeShort(Types.Double.ordinal());
            dox.writeDouble((Double) values);
        } else if (values instanceof Long) {
            dox.writeShort(Types.Long.ordinal());
            dox.writeLong((Long) values);
        } else if (values instanceof Float) {
            dox.writeShort(Types.Float.ordinal());
            dox.writeFloat((Float) values);
        } else if (values instanceof Byte) {
            dox.writeShort(Types.Byte.ordinal());
            dox.writeByte(Byte.toUnsignedInt((Byte) values));
        } else {
            dox.writeShort(Types.NULL.ordinal());
        }
    }

    @Override
    public void load(File file) throws IOException, InvalidConfigurationException {
        load(new FileInputStream(file), true);
    }

    @SuppressWarnings("unchecked")
    protected static Object lx(DataInput dox) throws IOException {
        int w = dox.readUnsignedShort();
        Types tt = Types.values()[w];
        System.out.println(w + "," + tt);
        switch (tt) {
            case NULL:
                return null;
            case String:
                return dox.readUTF();
            case Integer:
                return dox.readInt();
            case Short:
                return dox.readShort();
            case BooleanFalse:
                return false;
            case BooleanTrue:
                return true;
            case Double:
                return dox.readDouble();
            case Long:
                return dox.readLong();
            case Float:
                return dox.readFloat();
            case Byte:
                return dox.readByte();
            case Map:
                LinkedHashMap m = new LinkedHashMap();
                int size = dox.readInt();
                while (size-- > 0) {
                    m.put(lx(dox), lx(dox));
                }
                return Tools.load(m);
            case List:
                ArrayList al = new ArrayList();
                int sw = dox.readInt();
                while (sw-- > 0) {
                    al.add(lx(dox));
                }
                return al;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void load(DataInput io) throws IOException {
        Map<String, Object> values = (Map) lx(io);
        Tools.put(this, values);
    }

    public void load(InputStream io, boolean autoClose) throws IOException {
        if (autoClose) {
            try (InputStream iis = io) {
                if (iis instanceof DataInput) {
                    load((DataInput) iis);
                } else {
                    try (DataInputStream is = new DataInputStream(iis)) {
                        load((DataInput) is);
                    }
                }
            }
        } else {
            if (io instanceof DataInput) {
                load((DataInput) io);
            } else {
                load((DataInput) new DataInputStream(io));
            }
        }
    }

    @Override
    public void load(Reader reader) throws IOException, InvalidConfigurationException {
        throw new InvalidConfigurationException("BinConfiguration un-support load by reader");
    }

    @Override
    public void loadFromString(String s) throws InvalidConfigurationException {
        throw new InvalidConfigurationException("BinConfiguration un-support load by String");
    }

    @Override
    protected String buildHeader() {
        return null;
    }
}