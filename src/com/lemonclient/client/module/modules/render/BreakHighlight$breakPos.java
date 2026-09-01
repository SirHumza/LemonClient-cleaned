/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.render;

import com.lemonclient.api.util.world.BlockUtil;
import net.minecraft.util.math.BlockPos;

public static class BreakHighlight.breakPos {
    private BlockPos pos;
    private BlockPos dPos = null;
    private long start;
    private long dStart;
    private long time;
    private long dTime;

    public BreakHighlight.breakPos(BlockPos pos) {
        this.pos = pos;
        this.start = System.currentTimeMillis();
        this.time = INSTANCE.calcBreakTime(pos);
    }

    public void updatePos(BlockPos pos) {
        if (this.dPos == null) {
            this.dPos = this.pos;
            this.dStart = this.start;
            this.dTime = (long)((double)this.time * 1.4);
        }
        this.pos = pos;
        this.start = System.currentTimeMillis();
        this.time = INSTANCE.calcBreakTime(pos);
    }

    public long getEnd() {
        return this.start + this.time;
    }

    public void update() {
        this.time = INSTANCE.calcBreakTime(this.pos);
        if (this.dPos != null && BlockUtil.airBlocks.contains(mc.field_71441_e.func_180495_p(this.dPos).func_177230_c())) {
            this.removeDouble();
        }
    }

    public void remove() {
        this.pos = null;
    }

    public void removeDouble() {
        this.dPos = null;
    }

    static /* synthetic */ BlockPos access$200(BreakHighlight.breakPos x0) {
        return x0.pos;
    }

    static /* synthetic */ BlockPos access$300(BreakHighlight.breakPos x0) {
        return x0.dPos;
    }

    static /* synthetic */ long access$400(BreakHighlight.breakPos x0) {
        return x0.start;
    }

    static /* synthetic */ long access$500(BreakHighlight.breakPos x0) {
        return x0.time;
    }

    static /* synthetic */ long access$600(BreakHighlight.breakPos x0) {
        return x0.dStart;
    }

    static /* synthetic */ long access$700(BreakHighlight.breakPos x0) {
        return x0.dTime;
    }
}
