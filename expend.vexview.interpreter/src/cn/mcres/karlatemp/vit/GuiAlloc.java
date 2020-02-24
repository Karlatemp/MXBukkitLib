/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/19 24:02:46
 *
 * MXLib/expend.vexview.interpreter/GuiAlloc.java
 */

package cn.mcres.karlatemp.vit;

import cn.mcres.karlatemp.mxlib.interpreter.CommandBlock;
import cn.mcres.karlatemp.mxlib.interpreter.CommandLine;
import cn.mcres.karlatemp.mxlib.interpreter.CommandVisitor;
import lk.vexview.builders.ButtonBuilder;
import lk.vexview.builders.GuiBuilder;
import lk.vexview.builders.ImageBuilder;
import lk.vexview.builders.SlotBuilder;
import lk.vexview.gui.VexGui;

import java.util.Iterator;

public class GuiAlloc implements CommandVisitor<GuiBuilder, GuiAlloc> {

    private String url;
    private int x;
    private int y;
    private GuiBuilder builder;

    @Override
    public CommandVisitor<GuiBuilder, GuiAlloc> visitBlock(CommandBlock block, GuiBuilder value, GuiAlloc contextReturn) {
        this.builder = value;
        return this;
    }

    @Override
    public CommandVisitor<GuiBuilder, GuiAlloc> visitLine(CommandLine line, GuiBuilder value, GuiAlloc contextReturn) {
        final Iterator<Object> iterator = line.getArguments().iterator();
        switch (iterator.next().toString()) {
            case "size": {
                value.size(Integer.parseInt(iterator.next().toString()), Integer.parseInt(iterator.next().toString()));
                break;
            }
            case "origin":
            case "move":
                value.offset(Integer.parseInt(iterator.next().toString()), Integer.parseInt(iterator.next().toString()));
                break;
            case "text": {
                value.text(text -> TextAlloc.INSTANCE.visit(iterator.next(), true, text));
                break;
            }
            case "image": {
                value.addComponent(new ImageAlloc().visit(iterator.next(), true, ImageBuilder.builder()).done());
                break;
            }
            case "button": {
                value.button(button -> new ButtonAlloc().visit(iterator.next(), true, button));
                break;
            }
            case "background":
            case "address":
            case "url": {
                this.url = iterator.next().toString();
                break;
            }
            case "location": {
                this.x = Integer.parseInt(iterator.next().toString());
                this.y = Integer.parseInt(iterator.next().toString());
                break;
            }
            case "apply":
                return this;
            case "slot": {
                value.slot(builder -> new SlotAlloc().visit(iterator.next(), true, builder));
                break;
            }
        }
        return null;
    }

    public VexGui done() {
        return builder.build(url, x, y);
    }

    @Override
    public GuiAlloc defaultValue(GuiBuilder value) {
        return this;
    }
}
