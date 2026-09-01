/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.BlockPos$MutableBlockPos
 */
package com.lemonclient.api.util.player;

import net.minecraft.util.math.BlockPos;

public class MutableBlockPosHelper {
    public BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

    public static BlockPos.MutableBlockPos set(BlockPos.MutableBlockPos mutablePos, double x, double y, double z) {
        return mutablePos.func_189532_c(x, y, z);
    }

    public static BlockPos.MutableBlockPos set(BlockPos.MutableBlockPos mutablePos, BlockPos pos) {
        return mutablePos.func_181079_c(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
    }

    public static BlockPos.MutableBlockPos set(BlockPos.MutableBlockPos mutablePos, BlockPos pos, double x, double y, double z) {
        return mutablePos.func_189532_c((double)pos.func_177958_n() + x, (double)pos.func_177956_o() + y, (double)pos.func_177952_p() + z);
    }

    public static BlockPos.MutableBlockPos set(BlockPos.MutableBlockPos mutablePos, BlockPos pos, int x, int y, int z) {
        return mutablePos.func_181079_c(pos.func_177958_n() + x, pos.func_177956_o() + y, pos.func_177952_p() + z);
    }

    public static BlockPos.MutableBlockPos set(BlockPos.MutableBlockPos mutablePos, int x, int y, int z) {
        return mutablePos.func_181079_c(x, y, z);
    }

    public static BlockPos.MutableBlockPos setAndAdd(BlockPos.MutableBlockPos mutablePos, int x, int y, int z) {
        return mutablePos.func_181079_c(mutablePos.func_177958_n() + x, mutablePos.func_177956_o() + y, mutablePos.func_177952_p() + z);
    }

    public static BlockPos.MutableBlockPos setAndAdd(BlockPos.MutableBlockPos mutablePos, double x, double y, double z) {
        return mutablePos.func_189532_c((double)mutablePos.func_177958_n() + x, (double)mutablePos.func_177956_o() + y, (double)mutablePos.func_177952_p() + z);
    }

    public static BlockPos.MutableBlockPos setAndAdd(BlockPos.MutableBlockPos mutablePos, BlockPos pos) {
        return mutablePos.func_181079_c(mutablePos.func_177958_n() + pos.func_177958_n(), mutablePos.func_177956_o() + pos.func_177956_o(), mutablePos.func_177952_p() + pos.func_177952_p());
    }

    public static BlockPos.MutableBlockPos setAndAdd(BlockPos.MutableBlockPos mutablePos, BlockPos pos, double x, double y, double z) {
        return mutablePos.func_189532_c((double)(mutablePos.func_177958_n() + pos.func_177958_n()) + x, (double)(mutablePos.func_177956_o() + pos.func_177956_o()) + y, (double)(mutablePos.func_177952_p() + pos.func_177952_p()) + z);
    }

    public BlockPos.MutableBlockPos set(double x, double y, double z) {
        return this.mutablePos.func_189532_c(x, y, z);
    }

    public BlockPos.MutableBlockPos set(BlockPos pos) {
        return this.mutablePos.func_181079_c(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
    }

    public BlockPos.MutableBlockPos set(BlockPos pos, double x, double y, double z) {
        return this.mutablePos.func_189532_c((double)pos.func_177958_n() + x, (double)pos.func_177956_o() + y, (double)pos.func_177952_p() + z);
    }

    public BlockPos.MutableBlockPos set(BlockPos pos, int x, int y, int z) {
        return this.mutablePos.func_181079_c(pos.func_177958_n() + x, pos.func_177956_o() + y, pos.func_177952_p() + z);
    }

    public BlockPos.MutableBlockPos set(int x, int y, int z) {
        return this.mutablePos.func_181079_c(x, y, z);
    }

    public BlockPos.MutableBlockPos setAndAdd(int x, int y, int z) {
        return this.mutablePos.func_181079_c(this.mutablePos.func_177958_n() + x, this.mutablePos.func_177956_o() + y, this.mutablePos.func_177952_p() + z);
    }

    public BlockPos.MutableBlockPos setAndAdd(double x, double y, double z) {
        return this.mutablePos.func_189532_c((double)this.mutablePos.func_177958_n() + x, (double)this.mutablePos.func_177956_o() + y, (double)this.mutablePos.func_177952_p() + z);
    }

    public BlockPos.MutableBlockPos setAndAdd(BlockPos pos) {
        return this.mutablePos.func_181079_c(this.mutablePos.func_177958_n() + pos.func_177958_n(), this.mutablePos.func_177956_o() + pos.func_177956_o(), this.mutablePos.func_177952_p() + pos.func_177952_p());
    }

    public BlockPos.MutableBlockPos setAndAdd(BlockPos pos, double x, double y, double z) {
        return this.mutablePos.func_189532_c((double)(this.mutablePos.func_177958_n() + pos.func_177958_n()) + x, (double)(this.mutablePos.func_177956_o() + pos.func_177956_o()) + y, (double)(this.mutablePos.func_177952_p() + pos.func_177952_p()) + z);
    }
}
