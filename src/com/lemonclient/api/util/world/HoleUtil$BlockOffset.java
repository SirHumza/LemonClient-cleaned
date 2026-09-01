/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.api.util.world;

import net.minecraft.util.math.BlockPos;

public static enum HoleUtil.BlockOffset {
    DOWN(0, -1, 0),
    UP(0, 1, 0),
    NORTH(0, 0, -1),
    EAST(1, 0, 0),
    SOUTH(0, 0, 1),
    WEST(-1, 0, 0);

    private final int x;
    private final int y;
    private final int z;

    private HoleUtil.BlockOffset(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos offset(BlockPos pos) {
        return pos.func_177982_a(this.x, this.y, this.z);
    }

    public BlockPos forward(BlockPos pos, int scale) {
        return pos.func_177982_a(this.x * scale, 0, this.z * scale);
    }

    public BlockPos backward(BlockPos pos, int scale) {
        return pos.func_177982_a(-this.x * scale, 0, -this.z * scale);
    }

    public BlockPos left(BlockPos pos, int scale) {
        return pos.func_177982_a(this.z * scale, 0, -this.x * scale);
    }

    public BlockPos right(BlockPos pos, int scale) {
        return pos.func_177982_a(-this.z * scale, 0, this.x * scale);
    }
}
