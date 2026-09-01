/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.Vec3d
 */
package com.lemonclient.api.util.player;

import com.lemonclient.api.util.player.RotationUtil;
import com.lemonclient.api.util.player.SpeedUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public static class SpeedUtil.Info {
    double speed;
    Vec3d pos;
    Vec3d lastPos;
    double yaw;

    public SpeedUtil.Info(EntityPlayer player, Vec3d lastPos) {
        this.speed = SpeedUtil.calcSpeed(player);
        this.pos = player.func_174791_d();
        this.yaw = RotationUtil.getRotationTo((Vec3d)this.pos, (Vec3d)new Vec3d((double)player.field_70169_q, (double)player.field_70167_r, (double)player.field_70166_s)).field_189982_i;
        this.lastPos = lastPos;
    }
}
