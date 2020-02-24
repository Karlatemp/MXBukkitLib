/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/18 23:21:42
 *
 * MXLib/expend.vexview.interpreter/ButtonAlloc.java
 */

package cn.mcres.karlatemp.vit;

import cn.mcres.karlatemp.mxlib.interpreter.CommandBlock;
import cn.mcres.karlatemp.mxlib.interpreter.CommandLine;
import cn.mcres.karlatemp.mxlib.interpreter.CommandVisitor;
import cn.mcres.karlatemp.mxlib.interpreter.Token;
import lk.vexview.builders.ButtonBuilder;
import lk.vexview.builders.TextBuilder;

import java.util.Iterator;

public class ButtonAlloc implements CommandVisitor<ButtonBuilder, ButtonBuilder> {
    @Override
    public CommandVisitor<ButtonBuilder, ButtonBuilder> visitBlock(
            CommandBlock block,
            ButtonBuilder value,
            ButtonBuilder contextReturn) {
        return this;
    }

    @Override
    public CommandVisitor<ButtonBuilder, ButtonBuilder> visitLine(
            CommandLine line, ButtonBuilder value, ButtonBuilder contextReturn) {
        final Iterator<Object> iterator = line.getArguments().iterator();
        switch (iterator.next().toString()) {
            case "size": {
                value.size(Integer.parseInt(iterator.next().toString()), Integer.parseInt(iterator.next().toString()));
                break;
            }
            case "background": {
                value.background(iterator.next().toString(), iterator.next().toString());
                break;
            }
            case "move": {
                value.offset(Integer.parseInt(iterator.next().toString()), Integer.parseInt(iterator.next().toString()));
                break;
            }
            case "hover": {
                value.hover(TextAlloc.INSTANCE.visit(iterator.next(), true, TextBuilder.builder()).buildHover());
                break;
            }
            case "text": {
                value.text(iterator.next().toString());
                break;
            }
            case "id": {
                value.id(iterator.next().toString());
                break;
            }
            case "handle": {
                value.click(PM.buttonHandle(iterator.next().toString()));
                break;
            }
        }
        return null;
    }

    @Override
    public ButtonBuilder defaultValue(ButtonBuilder value) {
        return value;
    }
}
