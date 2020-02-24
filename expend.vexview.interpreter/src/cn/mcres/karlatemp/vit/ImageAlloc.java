/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/18 23:48:55
 *
 * MXLib/expend.vexview.interpreter/ImageAlloc.java
 */

package cn.mcres.karlatemp.vit;

import cn.mcres.karlatemp.mxlib.interpreter.CommandBlock;
import cn.mcres.karlatemp.mxlib.interpreter.CommandLine;
import cn.mcres.karlatemp.mxlib.interpreter.CommandVisitor;
import lk.vexview.builders.ImageBuilder;
import lk.vexview.builders.TextBuilder;
import lk.vexview.gui.components.VexComponents;

import java.util.Iterator;

public class ImageAlloc implements CommandVisitor<ImageBuilder, ImageAlloc> {
    public boolean gif;
    public int ticks;
    public ImageBuilder builder;

    @Override
    public CommandVisitor<ImageBuilder, ImageAlloc> visitBlock(CommandBlock block, ImageBuilder value, ImageAlloc contextReturn) {
        builder = value;
        return this;
    }

    @Override
    public ImageAlloc defaultValue(ImageBuilder value) {
        return this;
    }

    public VexComponents done() {
        if (gif) return builder.gif(ticks);
        return builder.build();
    }

    @Override
    public CommandVisitor<ImageBuilder, ImageAlloc> visitLine(
            CommandLine line,
            ImageBuilder value,
            ImageAlloc contextReturn) {
        final Iterator<Object> iterator = line.getArguments().iterator();
        switch (iterator.next().toString()) {
            case "hover": {
                builder = builder.hover(TextAlloc.INSTANCE.visit(iterator.next(), true, TextBuilder.builder()).buildHover());
                break;
            }
            case "move":
            case "offset":
                builder = builder.offset(Integer.parseInt(iterator.next().toString()), Integer.parseInt(iterator.next().toString()));
                break;
            case "gif":
                gif = true;
                ticks = Integer.parseInt(iterator.next().toString());
                break;
            case "size":
                builder = builder.size(Integer.parseInt(iterator.next().toString()), Integer.parseInt(iterator.next().toString()));
                break;
            case "image_size":
            case "imageSize":
                builder = builder.imageSize(Integer.parseInt(iterator.next().toString()), Integer.parseInt(iterator.next().toString()));
                break;
            case "background":
            case "address":
            case "url":
                builder = builder.background(iterator.next().toString());
                break;
            case "split":
                builder = builder.split();
                break;
            case "splitOffset":
            case "split_offset":
                builder = builder.split().splitOffset(Integer.parseInt(iterator.next().toString()), Integer.parseInt(iterator.next().toString()));
                break;
            case "splitSize":
            case "split_size":
                builder = builder.split().splitSize(Integer.parseInt(iterator.next().toString()), Integer.parseInt(iterator.next().toString()));
                break;
            case "mcImage":
            case "mc":
            case "mc_image":
                builder = builder.mcImage();
                break;
        }
        return null;
    }
}
