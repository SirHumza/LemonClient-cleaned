/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.MobEffects
 */
package com.lemonclient.api.util.world;

import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;

public class MotionUtil {
    public static boolean isMoving(EntityLivingBase entity) {
        return entity.field_191988_bg != 0.0f || entity.field_70702_br != 0.0f || entity.field_70701_bs != 0.0f || entity.field_70181_x > -0.078;
    }

    public static boolean moving(EntityLivingBase entity) {
        return entity.field_191988_bg != 0.0f || entity.field_70702_br != 0.0f;
    }

    public static double getMotion(EntityPlayer entity) {
        return Math.abs(entity.field_70159_w) + Math.abs(entity.field_70179_y);
    }

    public static void setSpeed(EntityLivingBase entity, double speed) {
        double[] dir = MotionUtil.forward(speed);
        entity.field_70159_w = dir[0];
        entity.field_70179_y = dir[1];
    }

    public static double getBaseMoveSpeed() {
        double result = 0.2873;
        if (Minecraft.func_71410_x().field_71439_g.func_70644_a(MobEffects.field_76424_c)) {
            result += 0.2873 * (double)(Objects.requireNonNull(Minecraft.func_71410_x().field_71439_g.func_70660_b(MobEffects.field_76424_c)).func_76458_c() + 1) * 0.2;
        }
        if (Minecraft.func_71410_x().field_71439_g.func_70644_a(MobEffects.field_76421_d)) {
            result -= 0.2873 * (double)(Objects.requireNonNull(Minecraft.func_71410_x().field_71439_g.func_70660_b(MobEffects.field_76421_d)).func_76458_c() + 1) * 0.15;
        }
        return result;
    }

    public static double[] forward(double speed) {
        float forward = Minecraft.func_71410_x().field_71439_g.field_71158_b.field_192832_b;
        float side = Minecraft.func_71410_x().field_71439_g.field_71158_b.field_78902_a;
        float yaw = Minecraft.func_71410_x().field_71439_g.field_70126_B + (Minecraft.func_71410_x().field_71439_g.field_70177_z - Minecraft.func_71410_x().field_71439_g.field_70126_B) * Minecraft.func_71410_x().func_184121_ak();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static double[] forward(double speed, float yaw) {
        float forward = 1.0f;
        float side = 0.0f;
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static double calcMoveYaw() {
        float yawIn = Minecraft.func_71410_x().field_71439_g.field_70177_z;
        float moveForward = MotionUtil.getRoundedMovementInput(Minecraft.func_71410_x().field_71439_g.field_71158_b.field_192832_b);
        float moveString = MotionUtil.getRoundedMovementInput(Minecraft.func_71410_x().field_71439_g.field_71158_b.field_78902_a);
        float strafe = 90.0f * moveString;
        float yaw = yawIn - (strafe *= moveForward != 0.0f ? moveForward * 0.5f : 1.0f);
        return Math.toRadians(yaw -= moveForward < 0.0f ? 180.0f : 0.0f);
    }

    public static float getRoundedMovementInput(float input) {
        if (input > 0.0f) {
            return 1.0f;
        }
        if (input < 0.0f) {
            return -1.0f;
        }
        return 0.0f;
    }
}
