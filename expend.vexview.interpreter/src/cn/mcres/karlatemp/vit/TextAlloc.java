/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/18 23:41:47
 *
 * MXLib/expend.vexview.interpreter/TextAlloc.java
 */

package cn.mcres.karlatemp.vit;

import cn.mcres.karlatemp.mxlib.interpreter.CommandBlock;
import cn.mcres.karlatemp.mxlib.interpreter.CommandLine;
import cn.mcres.karlatemp.mxlib.interpreter.CommandVisitor;
import lk.vexview.builders.TextBuilder;

import java.util.Iterator;

public class TextAlloc implements CommandVisitor<TextBuilder, TextBuilder> {
    public static final TextAlloc INSTANCE = new TextAlloc();

    @Override
    public CommandVisitor<TextBuilder, TextBuilder> visitBlock(CommandBlock block, TextBuilder value, TextBuilder contextReturn) {
        return this;
    }

    @Override
    public CommandVisitor<TextBuilder, TextBuilder> visitLine(
            CommandLine line, TextBuilder value,
            TextBuilder contextReturn) {
        final Iterator<Object> iterator = line.getArguments().iterator();
        switch (iterator.next().toString()) {
            case "move": {
                value.offset(Integer.parseInt(iterator.next().toString()), Integer.parseInt(iterator.next().toString()));
                break;
            }
            case "scale": {
                value.scale(Double.parseDouble(iterator.next().toString()));
                break;
            }
            case "hover": {
                value.hover(
                        INSTANCE.visit(
                                iterator.next(), true, TextBuilder.builder()
                        ).buildHover(),
                        Integer.parseInt(iterator.next().toString())
                );
                break;
            }
            case "line": {
                while (iterator.hasNext()) value.addLine(iterator.next().toString());
                break;
            }
        }
        return null;
    }

    @Override
    public TextBuilder defaultValue(TextBuilder value) {
        return value;
    }
}
