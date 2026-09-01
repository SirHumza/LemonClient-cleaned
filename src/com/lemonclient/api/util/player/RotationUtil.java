/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package com.lemonclient.api.util.player;

import com.lemonclient.api.util.world.BlockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class RotationUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();

    public static EnumFacing getFacing(double rotationYaw) {
        return EnumFacing.func_176731_b((int)(MathHelper.func_76128_c((double)(rotationYaw * 4.0 / 360.0 + 0.5)) & 3));
    }

    public static Vec2f getRotationTo(AxisAlignedBB box) {
        EntityPlayerSP player = RotationUtil.mc.field_71439_g;
        if (player == null) {
            return Vec2f.field_189974_a;
        }
        Vec3d eyePos = player.func_174824_e(1.0f);
        if (player.func_174813_aQ().func_72326_a(box)) {
            return RotationUtil.getRotationTo(eyePos, box.func_189972_c());
        }
        double x = MathHelper.func_151237_a((double)eyePos.field_72450_a, (double)box.field_72340_a, (double)box.field_72336_d);
        double y = MathHelper.func_151237_a((double)eyePos.field_72448_b, (double)box.field_72338_b, (double)box.field_72337_e);
        double z = MathHelper.func_151237_a((double)eyePos.field_72449_c, (double)box.field_72339_c, (double)box.field_72334_f);
        return RotationUtil.getRotationTo(eyePos, new Vec3d(x, y, z));
    }

    public static Vec2f getRotationTo(Vec3d posTo) {
        EntityPlayerSP player = RotationUtil.mc.field_71439_g;
        return player != null ? RotationUtil.getRotationTo(player.func_174824_e(1.0f), posTo) : Vec2f.field_189974_a;
    }

    public static Vec2f getRotationTo(Vec3d posFrom, Vec3d posTo) {
        return RotationUtil.getRotationFromVec(posTo.func_178788_d(posFrom));
    }

    public static Vec2f getRotationFromVec(Vec3d vec) {
        double lengthXZ = Math.hypot(vec.field_72450_a, vec.field_72449_c);
        double yaw = RotationUtil.normalizeAngle(Math.toDegrees(Math.atan2(vec.field_72449_c, vec.field_72450_a)) - 90.0);
        double pitch = RotationUtil.normalizeAngle(Math.toDegrees(-Math.atan2(vec.field_72448_b, lengthXZ)));
        return new Vec2f((float)yaw, (float)pitch);
    }

    public static double normalizeAngle(double angle) {
        if ((angle %= 360.0) >= 180.0) {
            angle -= 360.0;
        }
        if (angle < -180.0) {
            angle += 360.0;
        }
        return angle;
    }

    public static float normalizeAngle(float angle) {
        if ((angle %= 360.0f) >= 180.0f) {
            angle -= 360.0f;
        }
        if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }

    public static boolean isInFov(BlockPos pos) {
        return pos != null && (RotationUtil.mc.field_71439_g.func_174818_b(pos) < 4.0 || RotationUtil.yawDist(pos) < (double)(RotationUtil.getHalvedfov() + 2.0f));
    }

    public static boolean isInFov(Entity entity) {
        return entity != null && (RotationUtil.mc.field_71439_g.func_70068_e(entity) < 4.0 || RotationUtil.yawDist(entity) < (double)(RotationUtil.getHalvedfov() + 2.0f));
    }

    public static double yawDist(BlockPos pos) {
        if (pos != null) {
            Vec3d difference = new Vec3d((Vec3i)pos).func_178788_d(RotationUtil.mc.field_71439_g.func_174824_e(mc.func_184121_ak()));
            double d = Math.abs((double)RotationUtil.mc.field_71439_g.field_70177_z - (Math.toDegrees(Math.atan2(difference.field_72449_c, difference.field_72450_a)) - 90.0)) % 360.0;
            return d > 180.0 ? 360.0 - d : d;
        }
        return 0.0;
    }

    public static double yawDist(Entity e) {
        if (e != null) {
            Vec3d difference = e.func_174791_d().func_72441_c(0.0, (double)(e.func_70047_e() / 2.0f), 0.0).func_178788_d(RotationUtil.mc.field_71439_g.func_174824_e(mc.func_184121_ak()));
            double d = Math.abs((double)RotationUtil.mc.field_71439_g.field_70177_z - (Math.toDegrees(Math.atan2(difference.field_72449_c, difference.field_72450_a)) - 90.0)) % 360.0;
            return d > 180.0 ? 360.0 - d : d;
        }
        return 0.0;
    }

    public static float transformYaw() {
        float yaw = RotationUtil.mc.field_71439_g.field_70177_z % 360.0f;
        if (RotationUtil.mc.field_71439_g.field_70177_z > 0.0f) {
            if (yaw > 180.0f) {
                yaw = -180.0f + (yaw - 180.0f);
            }
        } else if (yaw < -180.0f) {
            yaw = 180.0f + (yaw + 180.0f);
        }
        if (yaw < 0.0f) {
            return 180.0f + yaw;
        }
        return -180.0f + yaw;
    }

    public static boolean isInFov(Vec3d vec3d, Vec3d other) {
        if (RotationUtil.mc.field_71439_g.field_70125_A > 30.0f ? other.field_72448_b > RotationUtil.mc.field_71439_g.field_70163_u : RotationUtil.mc.field_71439_g.field_70125_A < -30.0f && other.field_72448_b < RotationUtil.mc.field_71439_g.field_70163_u) {
            return true;
        }
        float angle = BlockUtil.calcAngleNoY(vec3d, other)[0] - RotationUtil.transformYaw();
        if (angle < -270.0f) {
            return true;
        }
        float fov = RotationUtil.mc.field_71474_y.field_74334_X / 2.0f;
        return angle < fov + 10.0f && angle > -fov - 10.0f;
    }

    public static float getFov() {
        return RotationUtil.mc.field_71474_y.field_74334_X;
    }

    public static float getHalvedfov() {
        return RotationUtil.getFov() / 2.0f;
    }
}
