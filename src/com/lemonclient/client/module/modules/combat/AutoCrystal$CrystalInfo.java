/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.item.EntityEnderCrystal
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.client.module.modules.combat.AutoCrystal;
import net.minecraft.entity.item.EntityEnderCrystal;

public static class AutoCrystal.CrystalInfo {
    EntityEnderCrystal crystal;
    AutoCrystal.PlayerInfo player;
    double damage;

    public AutoCrystal.CrystalInfo(EntityEnderCrystal crystal, AutoCrystal.PlayerInfo player, double damage) {
        this.crystal = crystal;
        this.player = player;
        this.damage = damage;
    }
}
