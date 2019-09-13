package cn.mcres.gyhhy.MXLib;

import cn.mcres.karlatemp.mxlib.impl.ets.MethodHandleEnumTool;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @version 1.10
 */
public abstract class EnumTool {

    private static final List<EnumTool> tools = new ArrayList<>();
    protected static final String VALUES = "$VALUES";
    protected static String[] ENUM_CACHE = {
        /**
         * Sun (Oracle?!?) JDK 1.5/6
         */
        "enumConstants",
        /**
         * IBM JDK
         */
        "enumConstantDirectory"
    };

    protected static void register(EnumTool et) {
        if (tools.contains(et)) {
            return;
        }
        int size = EnumForTest.values().length;
        String prefix = "TEST-" + et.toString() + "#" + size;

        try {
            et.addEnum(EnumForTest.class, prefix, new Class[]{String.class}, new Object[]{
                prefix + "%" + UUID.randomUUID()
            });
//            et.addEnum(EnumForTest.class, new EnumForTest());

        } catch (Throwable thr) {
            return;
        }
        tools.add(et);
    }

    public static List<EnumTool> getInstances() {
        return new ArrayList<>(tools);
    }

    static {
        try {
            register(new MethodHandleEnumTool());
        } catch (Throwable thr) {
        }
    }

    public static void main(String[] args) {
        EnumTool tool = tools.get(0);
        tool.addEnum(EnumForTest.class, VALUES, new Class[]{String.class}, new Object[]{"TTTT"});
        for (Object o : EnumForTest.values()) {
            System.out.println(o);
        }
    }

    protected <E extends Enum<E>> boolean check(Object e, Class<E> ecc) {
        if (e == null) {
            return false;
        }
        Class<?> c = e.getClass();
        if (ecc != null) {
            if (!ecc.isAssignableFrom(c)) {
                return false;
            }
        }
        Class<Enum> ec = Enum.class;
        if (ec.isAssignableFrom(c)) {
            Class<?> s1 = c.getSuperclass();
            if (s1 == ec) {
                return true;
            }
            return s1.getSuperclass() == ec;
        }
        return false;
    }

    protected abstract void setFailsafeFieldValue(Field field, Object target, Object value) throws NoSuchFieldException,
            IllegalAccessException;

    protected void bankField(Class<?> enumClass, String fieldName) throws NoSuchFieldException, IllegalAccessException {

        for (Field field : Class.class.getDeclaredFields()) {
            if (field.getName().contains(fieldName)) {
                AccessibleObject.setAccessible(new Field[]{field}, true);
                setFailsafeFieldValue(field, enumClass, null);
                break;
            }
        }
    }

    protected void clearEnumClassCache(Class<?> enumClass) throws NoSuchFieldException, IllegalAccessException {
        for (String fn : ENUM_CACHE) {
            bankField(enumClass, fn);
        }
    }

    protected abstract Object makeEnum0(Class<?> enumClass, Class<?>[] paramTypes, Object[] paramValues) throws Exception;

    protected Object makeEnum(Class<?> enumClass, String name, int ordinal, Class<?>[] additionalTypes, Object[] additionalValues) throws Exception {
        int count = additionalTypes.length;
        if (count != additionalValues.length) {
            throw new ArrayIndexOutOfBoundsException("The number of types does not match the number of parameters.");
        }
        int length = count + 2;
        Class<?>[] types = new Class[length];
        Object[] val = new Object[length];
        System.arraycopy(additionalTypes, 0, types, 2, count);
        System.arraycopy(additionalValues, 0, val, 2, count);
        types[0] = String.class;
        types[1] = int.class;
        val[0] = name;
        val[1] = ordinal;
        return makeEnum0(enumClass, types, val);
    }

    public abstract <T extends Enum<T>> void addEnum(Class<T> enumClass, String name, Class<?>[] additionalTypes, Object[] additionalValues);

    public abstract <T extends Enum<T>> void addEnum(Class<T> enumClass, T en);

    protected static enum EnumForTest {
        NOPE("Pool");
        private String kk;

        private EnumForTest(String kk) {
            this.kk = kk;
        }

        public String toString() {
            return name() + "[" + kk + "]";
        }
    }
}
