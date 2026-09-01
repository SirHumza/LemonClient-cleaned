/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.client.module.modules.combat.BedAura;
import net.minecraft.util.math.BlockPos;

class BedAura.PlaceInfo {
    BedAura.EntityInfo target;
    BlockPos placePos;
    BlockPos basePos;
    float damage;
    float selfDamage;

    public BedAura.PlaceInfo(BedAura.EntityInfo target, BlockPos placePos, float damage, float selfDamage, BlockPos basePos) {
        this.target = target;
        this.placePos = placePos;
        this.damage = damage;
        this.selfDamage = selfDamage;
        this.basePos = basePos;
    }
}
