/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.dev;

import com.lemonclient.api.util.player.PlayerUtil;
import net.minecraft.util.math.BlockPos;

static class AntiHoleCamper.PistonPos {
    public BlockPos piston;
    public BlockPos redstone;
    public BlockPos calcPos;

    public AntiHoleCamper.PistonPos(BlockPos pistonPos, BlockPos redstonePos, BlockPos pos) {
        this.piston = pistonPos;
        this.redstone = redstonePos;
        this.calcPos = pos;
    }

    public double getMaxRange() {
        if (this.piston == null || this.redstone == null) {
            return 999999.0;
        }
        return Math.max(PlayerUtil.getDistance(this.piston), PlayerUtil.getDistance(this.redstone));
    }
}
