/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 */
package com.lemonclient.client.module.modules.render;

import com.lemonclient.client.module.modules.render.BreakHighlight;
import net.minecraft.entity.player.EntityPlayer;

class BreakHighlight.renderBlock {
    private final BreakHighlight.breakPos pos;
    private final EntityPlayer player;

    public BreakHighlight.renderBlock(BreakHighlight.breakPos pos, EntityPlayer player) {
        this.pos = pos;
        this.player = player;
    }

    void update() {
        this.pos.update();
        BreakHighlight.this.renderBox(this.pos, this.player);
    }

    static /* synthetic */ BreakHighlight.breakPos access$100(BreakHighlight.renderBlock x0) {
        return x0.pos;
    }
}
