/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.AxisAlignedBB
 */
package com.lemonclient.api.util.world;

import com.lemonclient.api.util.world.HoleUtil;
import net.minecraft.util.math.AxisAlignedBB;

public static class HoleUtil.HoleInfo {
    private HoleUtil.HoleType type;
    private HoleUtil.BlockSafety safety;
    private AxisAlignedBB centre;

    public HoleUtil.HoleInfo() {
        this(HoleUtil.BlockSafety.UNBREAKABLE, HoleUtil.HoleType.NONE);
    }

    public HoleUtil.HoleInfo(HoleUtil.BlockSafety safety, HoleUtil.HoleType type) {
        this.type = type;
        this.safety = safety;
    }

    public void setType(HoleUtil.HoleType type) {
        this.type = type;
    }

    public void setSafety(HoleUtil.BlockSafety safety) {
        this.safety = safety;
    }

    public void setCentre(AxisAlignedBB centre) {
        this.centre = centre;
    }

    public HoleUtil.HoleType getType() {
        return this.type;
    }

    public HoleUtil.BlockSafety getSafety() {
        return this.safety;
    }

    public AxisAlignedBB getCentre() {
        return this.centre;
    }
}
