/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package com.lemonclient.api.util.player;

import com.lemonclient.api.util.player.RotationUtil;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class SpeedUtil {
    static Minecraft mc = Minecraft.func_71410_x();
    public static final double LAST_JUMP_INFO_DURATION_DEFAULT = 3.0;
    public static boolean didJumpThisTick = false;
    public static boolean isJumping = false;
    public double firstJumpSpeed = 0.0;
    public double lastJumpSpeed = 0.0;
    public double percentJumpSpeedChanged = 0.0;
    public double jumpSpeedChanged = 0.0;
    public boolean didJumpLastTick = false;
    public long jumpInfoStartTime = 0L;
    public boolean wasFirstJump = true;
    public double speedometerCurrentSpeed = 0.0;
    public HashMap<EntityPlayer, Info> playerInfo = new HashMap();

    public static void setDidJumpThisTick(boolean val) {
        didJumpThisTick = val;
    }

    public static void setIsJumping(boolean val) {
        isJumping = val;
    }

    public float lastJumpInfoTimeRemaining() {
        return (float)(Minecraft.func_71386_F() - this.jumpInfoStartTime) / 1000.0f;
    }

    public void update() {
        double distTraveledLastTickX = SpeedUtil.mc.field_71439_g.field_70165_t - SpeedUtil.mc.field_71439_g.field_70169_q;
        double distTraveledLastTickZ = SpeedUtil.mc.field_71439_g.field_70161_v - SpeedUtil.mc.field_71439_g.field_70166_s;
        this.speedometerCurrentSpeed = distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ;
        if (didJumpThisTick && (!SpeedUtil.mc.field_71439_g.field_70122_E || isJumping)) {
            if (!this.didJumpLastTick) {
                this.wasFirstJump = this.lastJumpSpeed == 0.0;
                this.percentJumpSpeedChanged = this.speedometerCurrentSpeed != 0.0 ? this.speedometerCurrentSpeed / this.lastJumpSpeed - 1.0 : -1.0;
                this.jumpSpeedChanged = this.speedometerCurrentSpeed - this.lastJumpSpeed;
                this.jumpInfoStartTime = Minecraft.func_71386_F();
                this.lastJumpSpeed = this.speedometerCurrentSpeed;
                this.firstJumpSpeed = this.wasFirstJump ? this.lastJumpSpeed : 0.0;
            }
            this.didJumpLastTick = didJumpThisTick;
        } else {
            this.didJumpLastTick = false;
            this.lastJumpSpeed = 0.0;
        }
        this.updatePlayers();
    }

    public void updatePlayers() {
        for (EntityPlayer player : SpeedUtil.mc.field_71441_e.field_73010_i) {
            int distance = 20;
            if (!(SpeedUtil.mc.field_71439_g.func_70068_e((Entity)player) < (double)(distance * distance))) continue;
            Vec3d lastPos = null;
            if (this.playerInfo.get(player) != null) {
                Info info = this.playerInfo.get(player);
                lastPos = info.pos;
            }
            this.playerInfo.put(player, new Info(player, lastPos));
        }
    }

    public double getPlayerSpeed(EntityPlayer player) {
        if (player == null) {
            return 0.0;
        }
        if (this.playerInfo.get(player) == null) {
            return 0.0;
        }
        return this.turnIntoKpH(this.playerInfo.get((Object)player).speed);
    }

    public Vec3d getPlayerLastPos(EntityPlayer player) {
        if (player == null) {
            return null;
        }
        if (this.playerInfo.get(player) == null) {
            return null;
        }
        return this.playerInfo.get((Object)player).lastPos;
    }

    public double getPlayerMoveYaw(EntityPlayer player) {
        if (player == null) {
            return 0.0;
        }
        if (this.playerInfo.get(player) == null) {
            return 0.0;
        }
        return this.playerInfo.get((Object)player).yaw;
    }

    public double turnIntoKpH(double input) {
        return (double)MathHelper.func_76133_a((double)input) * 71.2729367892;
    }

    public double getSpeedKpH() {
        double speedometerkphdouble = this.turnIntoKpH(this.speedometerCurrentSpeed);
        speedometerkphdouble = (double)Math.round(10.0 * speedometerkphdouble) / 10.0;
        return speedometerkphdouble;
    }

    public double getSpeedMpS() {
        double speedometerMpsdouble = this.turnIntoKpH(this.speedometerCurrentSpeed) / 3.6;
        speedometerMpsdouble = (double)Math.round(10.0 * speedometerMpsdouble) / 10.0;
        return speedometerMpsdouble;
    }

    public static double calcSpeed(EntityPlayer player) {
        double distTraveledLastTickX = player.field_70165_t - player.field_70169_q;
        double distTraveledLastTickZ = player.field_70161_v - player.field_70166_s;
        return distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ;
    }

    public static class Info {
        double speed;
        Vec3d pos;
        Vec3d lastPos;
        double yaw;

        public Info(EntityPlayer player, Vec3d lastPos) {
            this.speed = SpeedUtil.calcSpeed(player);
            this.pos = player.func_174791_d();
            this.yaw = RotationUtil.getRotationTo((Vec3d)this.pos, (Vec3d)new Vec3d((double)player.field_70169_q, (double)player.field_70167_r, (double)player.field_70166_s)).field_189982_i;
            this.lastPos = lastPos;
        }
    }
}
