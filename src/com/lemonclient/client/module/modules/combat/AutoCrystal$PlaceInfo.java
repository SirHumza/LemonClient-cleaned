/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.client.module.modules.combat.AutoCrystal;
import net.minecraft.util.math.BlockPos;

public static class AutoCrystal.PlaceInfo {
    public BlockPos blockPos;
    public AutoCrystal.PlayerInfo target;
    public double dmg;
    public double selfDmg;

    public AutoCrystal.PlaceInfo(AutoCrystal.PlayerInfo target, BlockPos block, double dmg, double selfDmg) {
        this.blockPos = block;
        this.target = target;
        this.dmg = dmg;
        this.selfDmg = selfDmg;
    }
}
