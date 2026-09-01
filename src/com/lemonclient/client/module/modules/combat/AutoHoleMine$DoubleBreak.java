/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.combat;

import net.minecraft.util.math.BlockPos;

class AutoHoleMine.DoubleBreak {
    BlockPos packetPos;
    BlockPos doublePos;

    public AutoHoleMine.DoubleBreak(BlockPos packetPos, BlockPos doublePos) {
        this.packetPos = packetPos;
        this.doublePos = doublePos;
    }

    public double maxRange() {
        double packetRange = AutoHoleMine.this.getDistance(this.packetPos);
        double doubleRange = AutoHoleMine.this.getDistance(this.doublePos);
        return Math.max(packetRange, doubleRange);
    }
}
