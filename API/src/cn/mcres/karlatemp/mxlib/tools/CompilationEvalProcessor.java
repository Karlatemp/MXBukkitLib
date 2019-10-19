/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CompilationEvalProcessor.java@author: karlatemp@vip.qq.com: 19-10-19 下午7:04@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.exceptions.CompeteException;
import cn.mcres.karlatemp.mxlib.exceptions.EvalProcessorInvokingException;
import cn.mcres.karlatemp.mxlib.util.DataByteBuffer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * 汇编执行器
 *
 * @since 2.4
 */
public class CompilationEvalProcessor implements IEvalProcessor {
    public static final long MAGIC_CODE =
            0x7fac_66ea;

    public static CompilationEvalProcessor open() {
        CompilationEvalProcessor cp = new CompilationEvalProcessor();
        $init_instance(cp);
        return cp;
    }

    @Override
    public void clearCache() {
    }

    @Override
    public void setUsingCache(boolean mode) {
    }

    @Override
    public CompetedCode compete(String command, boolean allowInvoking, boolean allowField) throws CompeteException {
        try {
            return compile(new Scanner(command));
        } catch (IOException e) {
            throw new CompeteException(e);
        }
    }

    public static class StackTrace {
        Object[] stacks;
        Object[] variables;
        Map<String, Object> context;
        int pos;
        boolean end;

        public Map<String, Object> getContext() {
            return context;
        }

        public StackTrace(int stacks, int variables) {
            this.stacks = new Object[stacks];
            this.variables = new Object[variables];
        }

        public StackTrace as(Map<String, Object> context) {
            this.context = context;
            return this;
        }

        public Object getStack() {
            Object gt = stacks[0];
            System.arraycopy(stacks, 1, stacks, 0, stacks.length - 1);
            return gt;
        }

        public void putStack(Object stack) {
            System.arraycopy(stacks, 0, stacks, 1, stacks.length - 1);
            stacks[0] = stack;
        }
    }

    public final static class InvokeCode implements IEvalProcessor.CompetedCode, IEvalProcessor.Function {
        private final int s, v;
        private final List<Command> c;
        private Map<String, Object> context;

        InvokeCode(List<Command> compiled, int stacks, int variables) {
            this.c = compiled;
            this.s = stacks;
            this.v = variables;
        }

        @Override
        public <T> T invoke(Map<String, Object> variables) throws EvalProcessorInvokingException {
            StackTrace st = new StackTrace(s, v);
            st.context = variables;
            return run(st);
        }

        public <T> T invoke0(Map<String, Object> variables) throws EvalProcessorInvokingException {
            return invoke(variables);
        }

        @SuppressWarnings("unchecked")
        private <T> T run(StackTrace st) {
            while (!st.end) {
                if (st.pos < 0 || st.pos >= c.size()) break;
                try {
                    c.get(st.pos++).invoke(st);
                } catch (EvalProcessorInvokingException ev) {
                    throw ev;
                } catch (Throwable throwable) {
                    throw new EvalProcessorInvokingException(throwable);
                }
            }
            if (st.stacks.length > 0)
                return (T) st.getStack();
            return null;
        }

        public void setContext(Map<String, Object> context) {
            this.context = context;
        }

        public <T> T invoke1(Object this_, Object... args) throws EvalProcessorInvokingException {
            return invoke(this_, args);
        }

        @Override
        public <T> T invoke(Object this_, Object... args) throws EvalProcessorInvokingException {
            StackTrace st = new StackTrace(s, v);
            st.context = this.context;
            st.variables[0] = this_;
            System.arraycopy(args, 0, st.variables, 1, args.length);
            return run(st);
        }
    }

    public interface Command {
        void invoke(StackTrace stack) throws Throwable;
    }

    public interface CommandFactory {
        @NotNull
        Command make(DataInput input) throws IOException, CompeteException;

        @NotNull
        static CommandFactory make(Command cmd) {
            return x -> cmd;
        }
    }

    public interface CommandMnemonicFactory {
        @NotNull
        DataInput make(String line) throws CompeteException;

        @NotNull
        static CommandMnemonicFactory of(@NotNull DataInput inp) {
            return x -> inp;
        }

        CommandMnemonicFactory EMPTY = of(DataByteBuffer.EMPTY);
    }

    private final List<CommandFactory> factories = new ArrayList<>();
    private final List<CommandMnemonicFactory> factories0 = new ArrayList<>();
    private final List<String> mnemonic = new ArrayList<>();

    public synchronized final CompilationEvalProcessor register(
            String mnemonic, CommandFactory factory,
            CommandMnemonicFactory f2) {
        if (this.mnemonic.contains(mnemonic))
            throw new RuntimeException("Mnemonic[" + mnemonic + "] was registered.");
        this.mnemonic.add(mnemonic);
        this.factories.add(factory);
        this.factories0.add(f2);
        return this;
    }

    private String ct(String awx) {
        int of = awx.indexOf("//");
        if (of != -1) {
            awx = awx.substring(0, of);
        }
        while (!awx.isEmpty()) {
            if (Character.isSpaceChar(awx.charAt(0))) {
                awx = awx.substring(1);
                continue;
            }
            break;
        }
        return awx;
    }

    public InvokeCode compile(Scanner scanner) throws CompeteException, IOException {
        List<Command> commands = new ArrayList<>();
        boolean setted = false;
        int stacks = 0, locals = 0;
        while (scanner.hasNextLine()) {
            String next = ct(scanner.nextLine());
            if (!next.isEmpty()) {
                if (!setted) {
                    int cw = next.indexOf(' ');
                    stacks = Short.toUnsignedInt(Short.parseShort(next.substring(0, cw)));
                    locals = Short.toUnsignedInt(Short.parseShort(next.substring(cw + 1)));
                    setted = true;
                    continue;
                }
                int fw = next.indexOf(' ');
                String ed;
                String ok;
                if (fw == -1) {
                    ed = "";
                    ok = next;
                } else {
                    ed = next.substring(fw + 1);
                    ok = next.substring(0, fw);
                }
                int at = mnemonic.indexOf(ok);
                if (at < 0 || at > mnemonic.size()) {
                    throw new CompeteException("Unknown mnemonic[" + ok + "]");
                }
                commands.add(factories.get(at).make(factories0.get(at).make(ed)));
            }
        }
        return new InvokeCode(commands, stacks, locals);
    }

    public void compile(Scanner scanner, DataOutput out) throws IOException, CompeteException {
        boolean setted = false;
        while (scanner.hasNextLine()) {
            String next = ct(scanner.nextLine());
            if (!next.isEmpty()) {
                if (!setted) {
                    int cw = next.indexOf(' ');
                    out.writeLong(MAGIC_CODE);
                    out.writeShort(Short.parseShort(next.substring(0, cw)));
                    out.writeShort(Short.parseShort(next.substring(cw + 1)));
                    setted = true;
                    continue;
                }
                int fw = next.indexOf(' ');
                String ed;
                String ok;
                if (fw == -1) {
                    ed = "";
                    ok = next;
                } else {
                    ed = next.substring(fw + 1);
                    ok = next.substring(0, fw);
                }
                int at = mnemonic.indexOf(ok);
                if (at < 0 || at > mnemonic.size()) {
                    throw new CompeteException("Unknown mnemonic[" + ok + "]");
                }
                out.writeShort(at);
                DataInput data = factories0.get(at).make(ed);
                if (data instanceof DataByteBuffer) {
                    ByteBuffer buffer = ((DataByteBuffer) data).getBuffer();
                    while (buffer.hasRemaining()) {
                        out.write(buffer.get());
                    }
                } else if (data instanceof InputStream) {
                    InputStream stream = (InputStream) data;
                    byte[] b = new byte[512];
                    while (true) {
                        int reade = stream.read(b);
                        if (reade == -1) break;
                        out.write(b, 0, reade);
                    }
                } else {
                    try {
                        while (true) {
                            out.write(data.readByte());
                        }
                    } catch (EOFException ignore) {
                    }
                }
            }
        }
    }

    public InvokeCode compile(DataInput codes) throws IOException, CompeteException {
        long magic = codes.readLong();
        if (magic != MAGIC_CODE) throw new CompeteException("It is not compilation code.");
        List<Command> commands = new ArrayList<>();
        int stacks = codes.readUnsignedShort();
        int variables = codes.readUnsignedShort();
        while (true) {
            int typ = Byte.toUnsignedInt(codes.readByte());
            if (typ == 0) break;
            CommandFactory cf = factories.get(typ);
            commands.add(cf.make(codes));
        }
        return new InvokeCode(commands, stacks, variables);
    }

    @SuppressWarnings("RedundantCast")
    protected static void $init_instance(@NotNull CompilationEvalProcessor p) {
        CommandMnemonicFactory ts = x -> {
            ByteBuffer bb = ByteBuffer.allocate(Short.BYTES);
            bb.putShort(Short.parseShort(x.trim()));
            bb.flip();
            return DataByteBuffer.open(bb);
        };
        p
                .register("put_null", CommandFactory.make(s -> s.putStack(null)), CommandMnemonicFactory.EMPTY)
                .register("put_NaN", CommandFactory.make(s -> s.putStack(Double.NaN)), CommandMnemonicFactory.EMPTY)
                .register("put_Infinity", CommandFactory.make(s -> s.putStack(Double.POSITIVE_INFINITY)), CommandMnemonicFactory.EMPTY)
                .register("put_NInfinity", CommandFactory.make(s -> s.putStack(Double.NEGATIVE_INFINITY)), CommandMnemonicFactory.EMPTY)
                .register("put_int", inp -> {
                    int v = inp.readInt();
                    return s -> s.putStack(v);
                }, o -> new DataByteBuffer((ByteBuffer) ByteBuffer.allocate(Integer.BYTES).putInt(Integer.parseInt(o)).flip()))
                .register("put_long", inp -> {
                    long v = inp.readLong();
                    return s -> s.putStack(v);
                }, o -> new DataByteBuffer((ByteBuffer) ByteBuffer.allocate(Long.BYTES).putLong(Long.parseLong(o)).flip())).register("put_double", inp -> {
            double v = inp.readDouble();
            return s -> s.putStack(v);
        }, o -> new DataByteBuffer((ByteBuffer) ByteBuffer.allocate(Double.BYTES).putDouble(Double.parseDouble(o)).flip()))
                .register("put_short", inp -> {
                    short v = inp.readShort();
                    return s -> s.putStack(v);
                }, o -> new DataByteBuffer((ByteBuffer) ByteBuffer.allocate(Short.BYTES).putShort(Short.parseShort(o)).flip()))
                .register("put_bool", inp -> {
                    boolean v = inp.readBoolean();
                    return s -> s.putStack(v);
                }, o -> new DataByteBuffer((ByteBuffer) ByteBuffer.allocate(1).put((byte) (Boolean.parseBoolean(o) ? 1 : 0)).flip()))
                .register("put_string", inp -> {
                    String str = inp.readUTF();
                    return stack -> stack.putStack(str);
                }, x -> new DataByteBuffer(Toolkit.ofUTF8ByteBuffer(x).position(0)))
                .register("get_context", inp -> {
                    String key = inp.readUTF();
                    return stack -> stack.putStack(stack.context.get(key));
                }, x -> new DataByteBuffer(Toolkit.ofUTF8ByteBuffer(x).position(0)))
                .register("get_var", inp -> {
                    short at = inp.readShort();
                    return stack -> stack.putStack(stack.variables[at]);
                }, ts)
                .register("set_var", inp -> {
                    short at = inp.readShort();
                    return stack -> stack.variables[at] = stack.getStack();
                }, ts)
                .register("xor", CommandFactory.make(s -> {
                    Object a = s.getStack(), w = s.getStack();
                    checknum(a);
                    checknum(w);
                    if (a instanceof BigInteger) {
                        BigInteger bi = (BigInteger) a;
                        if (w instanceof BigInteger) {
                            s.putStack(bi.xor((BigInteger) w));
                        } else if (w instanceof Long) {
                            s.putStack(bi.xor(BigInteger.valueOf((Long) w)));
                        } else {
                            s.putStack(bi.xor(BigInteger.valueOf(((Number) w).longValue())));
                        }
                    } else if (a instanceof Long) {
                        s.putStack(((Long) a) ^ ((Number) w).longValue());
                    } else if (a instanceof Integer) {
                        if (w instanceof Integer) {
                            s.putStack(((Integer) a) ^ (Integer) w);
                        } else if (w instanceof Long) {
                            s.putStack(((Integer) a) ^ (Long) w);
                        } else {
                            s.putStack((Integer) a ^ ((Number) w).intValue());
                        }
                    } else {
                        throw new EvalProcessorInvokingException("failed to xor " + a + " and " + w);
                    }
                }), CommandMnemonicFactory.EMPTY)
                .register("add", CommandFactory.make(s -> {
                    Object a = s.getStack(), b = s.getStack();
                    if (a instanceof String || b instanceof String) {
                        s.putStack(String.valueOf(b) + a);
                        return;
                    }
                    checknum(a);
                    checknum(b);
                    if (a instanceof BigInteger) {
                        BigInteger bi = (BigInteger) a;
                        if (b instanceof BigInteger) {
                            s.putStack(bi.add((BigInteger) b));
                        } else if (b instanceof Long) {
                            s.putStack(bi.add(BigInteger.valueOf((Long) b)));
                        } else {
                            s.putStack(bi.add(BigInteger.valueOf(((Number) b).longValue())));
                        }
                    } else if (a instanceof Long) {
                        s.putStack(((Long) a) + ((Number) b).longValue());
                    } else if (a instanceof Integer) {
                        if (b instanceof Integer) {
                            s.putStack(((Integer) a) + (Integer) b);
                        } else if (b instanceof Long) {
                            s.putStack(((Integer) a) + (Long) b);
                        } else if (b instanceof Double) {
                            s.putStack((Integer) a + ((Double) b));
                        } else {
                            s.putStack((Integer) a + ((Number) b).intValue());
                        }
                    } else if (a instanceof Double) {
                        s.putStack((Double) a + ((Number) b).doubleValue());
                    } else {
                        throw new EvalProcessorInvokingException("failed to add " + a + " and " + b);
                    }
                }), CommandMnemonicFactory.EMPTY)
                .register("subtract", CommandFactory.make(s -> {
                    Object b = s.getStack(), a = s.getStack();
                    checknum(a);
                    checknum(b);
                    if (a instanceof BigInteger) {
                        BigInteger bi = (BigInteger) a;
                        if (b instanceof BigInteger) {
                            s.putStack(bi.subtract((BigInteger) b));
                        } else if (b instanceof Long) {
                            s.putStack(bi.subtract(BigInteger.valueOf((Long) b)));
                        } else {
                            s.putStack(bi.subtract(BigInteger.valueOf(((Number) b).longValue())));
                        }
                    } else if (a instanceof Long) {
                        s.putStack(((Long) a) - ((Number) b).longValue());
                    } else if (a instanceof Integer) {
                        if (b instanceof Integer) {
                            s.putStack(((Integer) a) - (Integer) b);
                        } else if (b instanceof Long) {
                            s.putStack(((Integer) a) - (Long) b);
                        } else if (b instanceof Double) {
                            s.putStack((Integer) a - ((Double) b));
                        } else {
                            s.putStack((Integer) a - ((Number) b).intValue());
                        }
                    } else if (a instanceof Double) {
                        s.putStack((Double) a - ((Number) b).doubleValue());
                    } else {
                        throw new EvalProcessorInvokingException("failed to subtract " + a + " and " + b);
                    }
                }), CommandMnemonicFactory.EMPTY)
                .register("multiply", CommandFactory.make(s -> {
                    Object a = s.getStack(), b = s.getStack();
                    checknum(a);
                    checknum(b);
                    if (a instanceof BigInteger) {
                        BigInteger bi = (BigInteger) a;
                        if (b instanceof BigInteger) {
                            s.putStack(bi.multiply((BigInteger) b));
                        } else if (b instanceof Long) {
                            s.putStack(bi.multiply(BigInteger.valueOf((Long) b)));
                        } else {
                            s.putStack(bi.multiply(BigInteger.valueOf(((Number) b).longValue())));
                        }
                    } else if (a instanceof Long) {
                        s.putStack(((Long) a) * ((Number) b).longValue());
                    } else if (a instanceof Integer) {
                        if (b instanceof Integer) {
                            s.putStack(((Integer) a) * (Integer) b);
                        } else if (b instanceof Long) {
                            s.putStack(((Integer) a) * (Long) b);
                        } else {
                            s.putStack((Integer) a * ((Number) b).intValue());
                        }
                    } else {
                        throw new EvalProcessorInvokingException("failed to multiply " + a + " and " + b);
                    }
                }), CommandMnemonicFactory.EMPTY)
                .register("divide", CommandFactory.make(s -> {
                    Object b = s.getStack(), a = s.getStack();
                    checknum(a);
                    checknum(b);
                    if (a instanceof BigInteger) {
                        BigInteger bi = (BigInteger) a;
                        if (b instanceof BigInteger) {
                            s.putStack(bi.divide((BigInteger) b));
                        } else if (b instanceof Long) {
                            s.putStack(bi.divide(BigInteger.valueOf((Long) b)));
                        } else {
                            s.putStack(bi.divide(BigInteger.valueOf(((Number) b).longValue())));
                        }
                    } else if (a instanceof Long) {
                        s.putStack(((Long) a) / ((Number) b).longValue());
                    } else if (a instanceof Integer) {
                        if (b instanceof Integer) {
                            s.putStack(((Integer) a) / (Integer) b);
                        } else if (b instanceof Long) {
                            s.putStack(((Integer) a) / (Long) b);
                        } else {
                            s.putStack((Integer) a / ((Number) b).intValue());
                        }
                    } else {
                        throw new EvalProcessorInvokingException("failed to divide " + a + " and " + b);
                    }
                }), CommandMnemonicFactory.EMPTY)
                .register("remainder", CommandFactory.make(s -> {
                    Object b = s.getStack(), a = s.getStack();
                    checknum(a);
                    checknum(b);
                    if (a instanceof BigInteger) {
                        BigInteger bi = (BigInteger) a;
                        if (b instanceof BigInteger) {
                            s.putStack(bi.remainder((BigInteger) b));
                        } else if (b instanceof Long) {
                            s.putStack(bi.remainder(BigInteger.valueOf((Long) b)));
                        } else {
                            s.putStack(bi.remainder(BigInteger.valueOf(((Number) b).longValue())));
                        }
                    } else if (a instanceof Long) {
                        s.putStack(((Long) a) % ((Number) b).longValue());
                    } else if (a instanceof Integer) {
                        if (b instanceof Integer) {
                            s.putStack(((Integer) a) % (Integer) b);
                        } else if (b instanceof Long) {
                            s.putStack(((Integer) a) % (Long) b);
                        } else {
                            s.putStack((Integer) a % ((Number) b).intValue());
                        }
                    } else {
                        throw new EvalProcessorInvokingException("failed to remainder " + a + " and " + b);
                    }
                }), CommandMnemonicFactory.EMPTY)
                .register("compare", CommandFactory.make(s -> {
                    Object b = s.getStack(), a = s.getStack();
                    checknum(a);
                    checknum(b);
                    if (a instanceof BigInteger) {
                        BigInteger bi = (BigInteger) a;
                        if (b instanceof BigInteger) {
                            s.putStack(bi.compareTo((BigInteger) b));
                        } else if (b instanceof Long) {
                            s.putStack(bi.compareTo(BigInteger.valueOf((Long) b)));
                        } else {
                            s.putStack(bi.compareTo(BigInteger.valueOf(((Number) b).longValue())));
                        }
                    } else if (a instanceof Long) {
                        s.putStack(Long.compare(((Long) a), ((Number) b).longValue()));
                    } else if (a instanceof Integer) {
                        if (b instanceof Integer) {
                            s.putStack(Integer.compare((Integer) a, (Integer) b));
                        } else if (b instanceof Long) {
                            s.putStack(Long.compare(((Integer) a).longValue(), (Long) b));
                        } else {
                            s.putStack(Integer.compare((Integer) a, ((Number) b).intValue()));
                        }
                    } else if (a instanceof Double) {
                        s.putStack(Double.compare((Double) a, ((Number) b).doubleValue()));
                    } else {
                        throw new EvalProcessorInvokingException("failed to remainder " + a + " and " + b);
                    }
                }), CommandMnemonicFactory.EMPTY)
                .register("pow", CommandFactory.make(s -> {
                    Object b = s.getStack(), a = s.getStack();
                    checknum(a);
                    checknum(b);
                    if (a instanceof BigInteger) {
                        BigInteger bi = (BigInteger) a;
                        s.putStack(bi.pow(((Number) b).intValue()));
                    } else {
                        s.putStack(Math.pow(((Number) a).doubleValue(), ((Number) b).doubleValue()));
                    }
                }), CommandMnemonicFactory.EMPTY)
                .register("abs", CommandFactory.make(s -> {
                    Object a = s.getStack();
                    checknum(a);
                    if (a instanceof BigInteger) {
                        BigInteger bi = (BigInteger) a;
                        s.putStack(bi.abs());
                    } else if (a instanceof Integer) {
                        s.putStack(Math.abs((Integer) a));
                    } else if (a instanceof Long) {
                        s.putStack(Math.abs((Long) a));
                    } else if (a instanceof Float) {
                        s.putStack(Math.abs((Float) a));
                    } else {
                        s.putStack(Math.abs(((Number) a).doubleValue()));
                    }
                }), CommandMnemonicFactory.EMPTY)
                .register("or", CommandFactory.make(s -> {
                    Object a = s.getStack(), b = s.getStack();
                    checknum(a);
                    checknum(b);
                    if (a instanceof BigInteger) {
                        BigInteger bi = (BigInteger) a;
                        if (b instanceof BigInteger) {
                            s.putStack(bi.or((BigInteger) b));
                        } else if (b instanceof Long) {
                            s.putStack(bi.or(BigInteger.valueOf((Long) b)));
                        } else {
                            s.putStack(bi.or(BigInteger.valueOf(((Number) b).longValue())));
                        }
                    } else if (a instanceof Long) {
                        s.putStack(((Long) a) | ((Number) b).longValue());
                    } else if (a instanceof Integer) {
                        if (b instanceof Integer) {
                            s.putStack(((Integer) a) | (Integer) b);
                        } else if (b instanceof Long) {
                            s.putStack(((Integer) a) | (Long) b);
                        } else {
                            s.putStack((Integer) a | ((Number) b).intValue());
                        }
                    } else {
                        throw new EvalProcessorInvokingException("failed to or " + a + " and " + b);
                    }
                }), CommandMnemonicFactory.EMPTY)
                .register("and", CommandFactory.make(s -> {
                    Object a = s.getStack(), b = s.getStack();
                    checknum(a);
                    checknum(b);
                    if (a instanceof BigInteger) {
                        BigInteger bi = (BigInteger) a;
                        if (b instanceof BigInteger) {
                            s.putStack(bi.and((BigInteger) b));
                        } else if (b instanceof Long) {
                            s.putStack(bi.and(BigInteger.valueOf((Long) b)));
                        } else {
                            s.putStack(bi.and(BigInteger.valueOf(((Number) b).longValue())));
                        }
                    } else if (a instanceof Long) {
                        s.putStack(((Long) a) & ((Number) b).longValue());
                    } else if (a instanceof Integer) {
                        if (b instanceof Integer) {
                            s.putStack(((Integer) a) & (Integer) b);
                        } else if (b instanceof Long) {
                            s.putStack(((Integer) a) & (Long) b);
                        } else {
                            s.putStack((Integer) a & ((Number) b).intValue());
                        }
                    } else {
                        throw new EvalProcessorInvokingException("failed to and " + a + " and " + b);
                    }
                }), CommandMnemonicFactory.EMPTY)
                .register("shiftLeft", CommandFactory.make(s -> {
                    Object b = s.getStack(), a = s.getStack();
                    checknum(a);
                    checknum(b);
                    if (a instanceof BigInteger) {
                        BigInteger bi = (BigInteger) a;
                        s.putStack(bi.shiftLeft(((Number) b).intValue()));
                    } else if (a instanceof Long) {
                        s.putStack(((Long) a) << ((Number) b).intValue());
                    } else if (a instanceof Integer) {
                        s.putStack(((Integer) a) << ((Number) b).intValue());
                    } else {
                        throw new EvalProcessorInvokingException("failed to shiftLeft " + a + " with " + b);
                    }
                }), CommandMnemonicFactory.EMPTY)
                .register("shiftRight", CommandFactory.make(s -> {
                    Object b = s.getStack(), a = s.getStack();
                    checknum(a);
                    checknum(b);
                    if (a instanceof BigInteger) {
                        BigInteger bi = (BigInteger) a;
                        s.putStack(bi.shiftRight(((Number) b).intValue()));
                    } else if (a instanceof Long) {
                        s.putStack(((Long) a) >> ((Number) b).intValue());
                    } else if (a instanceof Integer) {
                        s.putStack(((Integer) a) >> ((Number) b).intValue());
                    } else {
                        throw new EvalProcessorInvokingException("failed to shiftLeft " + a + " with " + b);
                    }
                }), CommandMnemonicFactory.EMPTY)
                .register("negate", CommandFactory.make(s -> {
                    Object a = s.getStack();
                    checknum(a);
                    if (a instanceof BigInteger) {
                        BigInteger bi = (BigInteger) a;
                        s.putStack(bi.negate());
                    } else if (a instanceof Long) {
                        s.putStack(~((Long) a));
                    } else if (a instanceof Integer) {
                        s.putStack(~((Integer) a));
                    } else if (a instanceof Short) {
                        s.putStack(~((Short) a));
                    } else {
                        throw new EvalProcessorInvokingException("failed to negate " + a);
                    }
                }), CommandMnemonicFactory.EMPTY)
                .register("invoke", ip -> {
                    short ln = ip.readShort();
                    return s -> {
                        Object[] args = new Object[ln];
                        for (int i = args.length - 1; i >= 0; i--) {
                            args[i] = s.getStack();
                        }
                        Object thiz = s.getStack();
                        Object func = s.getStack();
                        if (!(func instanceof IEvalProcessor.Function)) {
                            throw new EvalProcessorInvokingException(func + " is not a function.");
                        }
                        s.putStack(((IEvalProcessor.Function) func).invoke(thiz, args));
                    };
                }, ts)
                .register("return", CommandFactory.make(s -> s.end = true), CommandMnemonicFactory.EMPTY)
                .register("goto", st -> {
                    short pointer = st.readShort();
                    return x -> x.pos += pointer;
                }, ts)
                .register("goto_r", st -> {
                    short pointer = st.readShort();
                    return x -> x.pos -= pointer;
                }, ts)
                .register("random", CommandFactory.make(s -> s.putStack(Math.random())), CommandMnemonicFactory.EMPTY)
                .register("floor", CommandFactory.make(x -> x.putStack(Math.floor((Double) x.getStack()))), CommandMnemonicFactory.EMPTY)
                .register("max", CommandFactory.make(s -> {
                    Object b = s.getStack(), a = s.getStack();
                    checknum(a);
                    checknum(b);
                    if (a instanceof BigInteger) {
                        BigInteger bi = (BigInteger) a;
                        if (b instanceof BigInteger) {
                            s.putStack(bi.max((BigInteger) b));
                        } else if (b instanceof Long) {
                            s.putStack(bi.max(BigInteger.valueOf((Long) b)));
                        } else {
                            s.putStack(bi.max(BigInteger.valueOf(((Number) b).longValue())));
                        }
                    } else if (a instanceof Long) {
                        s.putStack(Math.max(((Long) a), ((Number) b).longValue()));
                    } else if (a instanceof Integer) {
                        if (b instanceof Integer) {
                            s.putStack(Math.max((Integer) a, (Integer) b));
                        } else if (b instanceof Long) {
                            s.putStack(Math.max(((Integer) a).longValue(), (Long) b));
                        } else if (b instanceof Double) {
                            s.putStack(Math.max((Integer) a, (Double) b));
                        } else {
                            s.putStack(Math.max((Integer) a, ((Number) b).intValue()));
                        }
                    } else if (a instanceof Double) {
                        s.putStack(Math.max((Double) a, ((Number) b).doubleValue()));
                    } else {
                        throw new EvalProcessorInvokingException("failed to get max value of " + a + " and " + b);
                    }
                }), CommandMnemonicFactory.EMPTY)
                .register("min", CommandFactory.make(s -> {
                    Object a = s.getStack(), b = s.getStack();
                    checknum(a);
                    checknum(b);
                    if (a instanceof BigInteger) {
                        BigInteger bi = (BigInteger) a;
                        if (b instanceof BigInteger) {
                            s.putStack(bi.min((BigInteger) b));
                        } else if (b instanceof Long) {
                            s.putStack(bi.min(BigInteger.valueOf((Long) b)));
                        } else {
                            s.putStack(bi.min(BigInteger.valueOf(((Number) b).longValue())));
                        }
                    } else if (a instanceof Long) {
                        s.putStack(Math.min(((Long) a), ((Number) b).longValue()));
                    } else if (a instanceof Integer) {
                        if (b instanceof Integer) {
                            s.putStack(Math.min((Integer) a, (Integer) b));
                        } else if (b instanceof Long) {
                            s.putStack(Math.min(((Integer) a).longValue(), (Long) b));
                        } else if (b instanceof Double) {
                            s.putStack(Math.min((Integer) a, (Double) b));
                        } else {
                            s.putStack(Math.min((Integer) a, ((Number) b).intValue()));
                        }
                    } else if (a instanceof Double) {
                        s.putStack(Math.min((Double) a, ((Number) b).doubleValue()));
                    } else {
                        throw new EvalProcessorInvokingException("failed to get min value of " + a + " and " + b);
                    }
                }), CommandMnemonicFactory.EMPTY)
                .register("sqrt", CommandFactory.make(s -> s.putStack(Math.sqrt((Double) s.getStack()))), CommandMnemonicFactory.EMPTY)
                .register("log", CommandFactory.make(s -> s.putStack(Math.log((Double) s.getStack()))), CommandMnemonicFactory.EMPTY)
                .register("acos", CommandFactory.make(s -> s.putStack(Math.acos((Double) s.getStack()))), CommandMnemonicFactory.EMPTY)
                .register("cos", CommandFactory.make(s -> s.putStack(Math.cos((Double) s.getStack()))), CommandMnemonicFactory.EMPTY)
                .register("cosh", CommandFactory.make(s -> s.putStack(Math.cosh((Double) s.getStack()))), CommandMnemonicFactory.EMPTY)
                .register("asin", CommandFactory.make(s -> s.putStack(Math.asin((Double) s.getStack()))), CommandMnemonicFactory.EMPTY)
                .register("sin", CommandFactory.make(s -> s.putStack(Math.sin((Double) s.getStack()))), CommandMnemonicFactory.EMPTY)
                .register("sinh", CommandFactory.make(s -> s.putStack(Math.sinh((Double) s.getStack()))), CommandMnemonicFactory.EMPTY)
                .register("tan", CommandFactory.make(s -> s.putStack(Math.tan((Double) s.getStack()))), CommandMnemonicFactory.EMPTY)
                .register("tanh", CommandFactory.make(s -> s.putStack(Math.tanh((Double) s.getStack()))), CommandMnemonicFactory.EMPTY)
                .register("atan", CommandFactory.make(s -> s.putStack(Math.atan((Double) s.getStack()))), CommandMnemonicFactory.EMPTY)
                .register("ceil", CommandFactory.make(s -> s.putStack(Math.ceil((Double) s.getStack()))), CommandMnemonicFactory.EMPTY);
    }

    private static void checknum(Object a) throws EvalProcessorInvokingException {
        if (a instanceof Number) return;
        throw new EvalProcessorInvokingException(a + " is not a number.");
    }
}
