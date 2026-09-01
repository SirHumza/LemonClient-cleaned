/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.combat;

import java.util.List;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

class HoleFill.HoleInfo {
    List<BlockPos> posList;
    AxisAlignedBB checkBox;
    AxisAlignedBB box;

    public HoleFill.HoleInfo(List<BlockPos> posList, AxisAlignedBB box) {
        this.posList = posList;
        this.box = box;
        this.checkBox = new AxisAlignedBB(box.field_72340_a - (Double)HoleFill.this.fillRange.getValue(), box.field_72338_b, box.field_72339_c - (Double)HoleFill.this.fillRange.getValue(), box.field_72336_d + (Double)HoleFill.this.fillRange.getValue(), box.field_72337_e + (Double)HoleFill.this.fillYRange.getValue(), box.field_72334_f + (Double)HoleFill.this.fillRange.getValue());
    }
}
