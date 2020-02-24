/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/20 19:19:52
 *
 * MXLib/expend.vexview.interpreter/TagDirectionAlloc.java
 */

package cn.mcres.karlatemp.vit;

import cn.mcres.karlatemp.mxlib.interpreter.CommandBlock;
import cn.mcres.karlatemp.mxlib.interpreter.CommandLine;
import cn.mcres.karlatemp.mxlib.interpreter.CommandVisitor;

public class TagDirectionAlloc implements CommandVisitor<TagDirectionBuilder, TagDirectionBuilder> {
    public static final TagDirectionAlloc INSTANCE = new TagDirectionAlloc();

    @Override
    public CommandVisitor<TagDirectionBuilder, TagDirectionBuilder> visitBlock(CommandBlock block, TagDirectionBuilder value, TagDirectionBuilder contextReturn) {
        return this;
    }

    @Override
    public TagDirectionBuilder defaultValue(TagDirectionBuilder value) {
        return value;
    }

    @Override
    public CommandVisitor<TagDirectionBuilder, TagDirectionBuilder> visitLine(CommandLine line, TagDirectionBuilder value, TagDirectionBuilder contextReturn) {
        var i = line.getArguments().iterator();
        switch (i.next().toString()) {
            case "rotate":
                value.rotate(Float.parseFloat(i.next().toString()), Float.parseFloat(i.next().toString()), Float.parseFloat(i.next().toString()));
                break;
            case "current_player_visitable":
            case "current_player_visible":
            case "player_can_see":
                value.currentPlayerVisible(Boolean.parseBoolean(i.next().toString()));
                break;
            case "forPlayer":
            case "for_player":
                value.forPlayer(Boolean.parseBoolean(i.next().toString()));
                break;
        }
        return null;
    }
}
