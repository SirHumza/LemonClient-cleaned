/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.api.util.render.RenderUtil;
import com.lemonclient.api.util.world.combat.DamageUtil;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

class HoleFill.renderBlock {
    private final BlockPos pos;
    private long start = System.currentTimeMillis();
    boolean placed;

    public HoleFill.renderBlock(BlockPos pos) {
        this.pos = pos;
        this.placed = false;
    }

    void resetTime() {
        this.start = System.currentTimeMillis();
    }

    void render() {
        if (!this.placed) {
            if (DamageUtil.isResistantMine(this.pos)) {
                this.resetTime();
                this.placed = true;
            } else {
                return;
            }
        }
        AxisAlignedBB alignedBB = new AxisAlignedBB(this.pos);
        if (((Boolean)HoleFill.this.animate.getValue()).booleanValue()) {
            alignedBB = alignedBB.func_186662_g(this.delta() * this.delta() / 2.0 - 1.0);
        }
        if (((Boolean)HoleFill.this.box.getValue()).booleanValue()) {
            RenderUtil.drawBox(alignedBB, true, 1.0, new GSColor(HoleFill.this.color.getColor(), this.returnGradient()), 63);
        }
        if (((Boolean)HoleFill.this.outline.getValue()).booleanValue()) {
            RenderUtil.drawBoundingBox(alignedBB, (double)((Integer)HoleFill.this.width.getValue()).intValue(), new GSColor(HoleFill.this.color.getColor(), this.returnOutGradient()));
        }
    }

    public double delta() {
        long end = this.start + (long)((Integer)HoleFill.this.time.getValue()).intValue();
        double result = (double)(end - System.currentTimeMillis()) / (double)(end - this.start);
        if (result < 0.0) {
            result = 0.0;
        }
        if (result > 1.0) {
            result = 1.0;
        }
        return 1.0 - result;
    }

    public int returnGradient() {
        return (int)((double)((Integer)HoleFill.this.alpha.getValue()).intValue() * (1.0 - this.delta()));
    }

    public int returnOutGradient() {
        return (int)((double)((Integer)HoleFill.this.outAlpha.getValue()).intValue() * (1.0 - this.delta()));
    }

    static /* synthetic */ BlockPos access$000(HoleFill.renderBlock x0) {
        return x0.pos;
    }

    static /* synthetic */ long access$100(HoleFill.renderBlock x0) {
        return x0.start;
    }
}
