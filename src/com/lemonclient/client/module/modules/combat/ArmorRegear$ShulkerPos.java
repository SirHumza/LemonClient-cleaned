/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package com.lemonclient.client.module.modules.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

static class ArmorRegear.ShulkerPos {
    BlockPos pos;
    Vec3d vec;
    BlockPos neighbour;
    EnumFacing opposite;

    public ArmorRegear.ShulkerPos(BlockPos pos, BlockPos neighbour, EnumFacing opposite, Vec3d vec3d) {
        this.pos = pos;
        this.neighbour = neighbour;
        this.opposite = opposite;
        this.vec = vec3d;
    }

    public double getRange(EntityPlayer player) {
        return player.func_70011_f((double)this.pos.field_177962_a + 0.5, (double)this.pos.field_177960_b + 0.5, (double)this.pos.field_177961_c + 0.5);
    }
}
