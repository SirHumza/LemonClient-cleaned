/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.Vec3d
 */
package com.lemonclient.client.module.modules.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public static class AutoCrystal.PlayerInfo {
    EntityPlayer player;
    Vec3d position;
    AxisAlignedBB boundingBox;
    double health;

    public AutoCrystal.PlayerInfo(EntityPlayer player) {
        this.player = player;
        if (player != null) {
            this.position = player.func_174791_d();
            this.boundingBox = player.func_174813_aQ();
            this.health = player.func_110143_aJ() + player.func_110139_bj();
        }
    }

    public AutoCrystal.PlayerInfo(EntityPlayer player, Vec3d position, AxisAlignedBB boundingBox) {
        this.player = player;
        this.position = position;
        this.boundingBox = boundingBox;
        this.health = player.func_110143_aJ() + player.func_110139_bj();
    }
}
