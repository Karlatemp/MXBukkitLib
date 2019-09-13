package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.annotations.Resource;
import cn.mcres.karlatemp.mxlib.bean.IEnvironmentFactory;
import cn.mcres.karlatemp.mxlib.logging.MessageFactoryAnsi;
import cn.mcres.karlatemp.mxlib.logging.PrintStreamLogger;
import cn.mcres.karlatemp.mxlib.tools.*;

import java.util.List;

// Test Only
public class Tester {
    public static String varl;
    @Resource
    private IObjectCreator creator;

    @Resource
    private static IMemberScanner scanner;

    public static void a(int x) {
        while (x-- > 0) System.out.append("  ");
    }

    public static void test(String line, List l1, int it, List l2) {
        System.out.println(line);
        System.out.println(l1);
        System.out.println(l1);
        System.out.println(it);
    }

    public static void print(Object s) {
        System.out.println(s);
    }

    public static Object filter(String pre, String val) {
        return pre + val + "4544";
    }

    public static void dump(SharedCommandProcessor.ClassDatas datas, int i) {
//        a(i);
        System.out.append('$').println(datas.full_package);
        final List<String> cs = datas.classes;
        if (cs != null) {
            for (String s : cs) {
                a(i + 1);
                System.out.append('#').println(s);
            }
        }
        final SharedCommandProcessor.ClassDatas[] sub = datas.sub;
        if (sub != null) {
            for (SharedCommandProcessor.ClassDatas d : sub) {
                dump(d, i + 1);
            }
        }
    }

    static class $A {
        static $A a = new $A();
        $B b = new $B();

        static class $B {
            String value;
            int size;
        }
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Throwable {
        MXLib.boot();
        class InterEnv {
            private int inter;
        }
        class Envs {
            private String a;
            private String b;
            private InterEnv env;

            public String getB() {
                return b;
            }

            public void setB(String b) {
                System.out.println("B Set: " + b);
                this.b = b;
            }
        }
        final SharedEnvironmentFactory factory = new SharedEnvironmentFactory();
        IEnvironmentFactory.IField[] fields = factory.getFields(Envs.class);
        System.out.println(fields);
        for (Object o : fields) {
            System.out.println(o);
        }
        Envs env = factory.loadEnvironment(Envs.class, new MapBuilder().add("a", "ValueA")
                .add("b", "FUCKQ").add("env", new MapBuilder().add("inter", 50)));
        System.out.println(env.a);
        System.out.println(env.b);
        System.out.println(env.env.inter);
        System.out.println(factory.toEnvironment(env));
        System.out.println(factory.toEnvironment($A.a));
        Pointer<Runnable> p = new Pointer<>();
        try {
            p.value(() -> {
                p.value().run();
            });
            p.value().run();
        } catch (Throwable thr) {
            final StackTraceElement[] stackTrace = thr.getStackTrace();
            Pointer<Integer> px = new Pointer<>(0);
            new PrintStreamLogger(null,
                    new MessageFactoryAnsi(), "",
                    System.out) {
                @Override
                protected void printStackTraceElement(StackTraceElement element, boolean err) {
                    px.value(px.value() + 1);
                    super.printStackTraceElement(element, err);
                }
            }.printStackTrace(thr);
            System.out.println(stackTrace.length);
            System.out.println(px.value());
        }
    }
}
