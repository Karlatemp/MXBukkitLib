/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/19 20:08:31
 *
 * MXLib/expend.vexview.interpreter/SlotAlloc.java
 */

package cn.mcres.karlatemp.vit;

import cn.mcres.karlatemp.mxlib.interpreter.CommandBlock;
import cn.mcres.karlatemp.mxlib.interpreter.CommandLine;
import cn.mcres.karlatemp.mxlib.interpreter.CommandVisitor;
import lk.vexview.builders.SlotBuilder;

public class SlotAlloc implements CommandVisitor<SlotBuilder, SlotBuilder> {
    @Override
    public CommandVisitor<SlotBuilder, SlotBuilder> visitBlock(CommandBlock block, SlotBuilder value, SlotBuilder contextReturn) {
        return this;
    }

    @Override
    public CommandVisitor<SlotBuilder, SlotBuilder> visitLine(CommandLine line, SlotBuilder value, SlotBuilder contextReturn) {
        var it = line.getArguments().iterator();
        switch (it.next().toString()) {
            case "location":
            case "move":
                value.offset(Integer.parseInt(it.next().toString()), Integer.parseInt(it.next().toString()));
                break;
            case "item":
                value.item(new ItemAlloc().visit(it.next(), true, null).done());
                break;
        }
        return null;
    }

    @Override
    public SlotBuilder defaultValue(SlotBuilder value) {
        return value;
    }
}
