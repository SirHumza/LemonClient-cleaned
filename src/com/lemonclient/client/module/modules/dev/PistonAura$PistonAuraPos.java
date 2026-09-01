/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.dev;

import com.lemonclient.api.util.player.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class PistonAura.PistonAuraPos {
    public BlockPos targetPos;
    public BlockPos crystal;
    public BlockPos piston;
    public BlockPos redstone;
    public BlockPos fire;
    public BlockPos offset;
    EntityPlayer target;
    boolean block;

    public PistonAura.PistonAuraPos(BlockPos crystal, BlockPos piston, BlockPos redstone, BlockPos offset, EntityPlayer target, BlockPos targetPos, boolean block) {
        this.crystal = crystal;
        this.piston = piston;
        this.redstone = redstone;
        this.offset = offset;
        this.targetPos = targetPos;
        this.target = target;
        this.block = block;
        this.fire = PistonAura.this.getFirePos(targetPos, crystal, piston, redstone, offset);
    }

    public double range() {
        double crystalRange = PlayerUtil.getDistanceL(this.crystal);
        double pistonRange = PlayerUtil.getDistanceL(this.piston);
        return Math.max(pistonRange, crystalRange);
    }
}
