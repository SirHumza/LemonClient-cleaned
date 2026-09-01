/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.misc;

import net.minecraft.util.math.BlockPos;

private static class AutoSpawner.BodyParts {
    private static final BlockPos[] bodyBase = new BlockPos[]{new BlockPos(0, 1, 0), new BlockPos(0, 2, 0)};
    private static final BlockPos[] ArmsX = new BlockPos[]{new BlockPos(-1, 2, 0), new BlockPos(1, 2, 0)};
    private static final BlockPos[] ArmsZ = new BlockPos[]{new BlockPos(0, 2, -1), new BlockPos(0, 2, 1)};
    private static final BlockPos[] headsX = new BlockPos[]{new BlockPos(0, 3, 0), new BlockPos(-1, 3, 0), new BlockPos(1, 3, 0)};
    private static final BlockPos[] headsZ = new BlockPos[]{new BlockPos(0, 3, 0), new BlockPos(0, 3, -1), new BlockPos(0, 3, 1)};
    private static final BlockPos[] head = new BlockPos[]{new BlockPos(0, 3, 0)};

    private AutoSpawner.BodyParts() {
    }

    static /* synthetic */ BlockPos[] access$000() {
        return bodyBase;
    }

    static /* synthetic */ BlockPos[] access$100() {
        return ArmsX;
    }

    static /* synthetic */ BlockPos[] access$200() {
        return ArmsZ;
    }

    static /* synthetic */ BlockPos[] access$300() {
        return headsX;
    }

    static /* synthetic */ BlockPos[] access$400() {
        return headsZ;
    }

    static /* synthetic */ BlockPos[] access$500() {
        return head;
    }
}
