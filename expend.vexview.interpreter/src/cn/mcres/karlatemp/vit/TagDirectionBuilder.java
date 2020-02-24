/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/20 19:15:31
 *
 * MXLib/expend.vexview.interpreter/TagDirectionBuilder.java
 */

package cn.mcres.karlatemp.vit;

import lk.vexview.tag.TagDirection;

public class TagDirectionBuilder {


    public static TagDirectionBuilder builder() {
        return new TagDirectionBuilder();
    }

    protected TagDirectionBuilder() {
    }

    protected boolean currentPlayerVisible;
    protected boolean forPlayer;
    protected float x, y, z;

    public TagDirectionBuilder rotate(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public TagDirectionBuilder forPlayer(boolean forPlayer) {
        this.forPlayer = forPlayer;
        return this;
    }

    public TagDirectionBuilder currentPlayerVisible(boolean visible) {
        this.currentPlayerVisible = visible;
        return this;
    }

    public TagDirection build() {
        return new TagDirection(x, y, z, forPlayer, currentPlayerVisible);
    }
}
