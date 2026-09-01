/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.world.World
 */
package com.lemonclient.api.util.world.combat;

import com.lemonclient.api.util.misc.MathUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class RotationUtil {
    public static Minecraft mc = Minecraft.func_71410_x();

    public static float getYawChangeGiven(double posX, double posZ, float yaw) {
        double deltaX = posX - Minecraft.func_71410_x().field_71439_g.field_70165_t;
        double deltaZ = posZ - Minecraft.func_71410_x().field_71439_g.field_70161_v;
        double yawToEntity = deltaZ < 0.0 && deltaX < 0.0 ? 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : (deltaZ < 0.0 && deltaX > 0.0 ? -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : Math.toDegrees(-Math.atan(deltaX / deltaZ)));
        return MathHelper.func_76142_g((float)(-(yaw - (float)yawToEntity)));
    }

    public static float[] getRotations(BlockPos pos, EnumFacing facing) {
        return RotationUtil.getRotations(pos, facing, (Entity)RotationUtil.getRotationPlayer());
    }

    public static float[] getRotations(BlockPos pos, EnumFacing facing, Entity from) {
        return RotationUtil.getRotations(pos, facing, from, (World)RotationUtil.mc.field_71441_e, RotationUtil.mc.field_71441_e.func_180495_p(pos));
    }

    public static float[] getRotations(BlockPos pos, EnumFacing facing, Entity from, World world, IBlockState state) {
        AxisAlignedBB bb = state.func_185918_c(world, pos);
        double x = (double)pos.func_177958_n() + (bb.field_72340_a + bb.field_72336_d) / 2.0;
        double y = (double)pos.func_177956_o() + (bb.field_72338_b + bb.field_72337_e) / 2.0;
        double z = (double)pos.func_177952_p() + (bb.field_72339_c + bb.field_72334_f) / 2.0;
        if (facing != null) {
            x += (double)facing.func_176730_m().func_177958_n() * ((bb.field_72340_a + bb.field_72336_d) / 2.0);
            y += (double)facing.func_176730_m().func_177956_o() * ((bb.field_72338_b + bb.field_72337_e) / 2.0);
            z += (double)facing.func_176730_m().func_177952_p() * ((bb.field_72339_c + bb.field_72334_f) / 2.0);
        }
        return RotationUtil.getRotations(x, y, z, from);
    }

    public static float[] getRotations(double x, double y, double z, double fromX, double fromY, double fromZ, float fromHeight) {
        double xDiff = x - fromX;
        double yDiff = y - (fromY + (double)fromHeight);
        double zDiff = z - fromZ;
        double dist = MathHelper.func_76133_a((double)(xDiff * xDiff + zDiff * zDiff));
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
        float prevYaw = RotationUtil.mc.field_71439_g.field_70126_B;
        float diff = yaw - prevYaw;
        if (diff < -180.0f || diff > 180.0f) {
            float round = Math.round(Math.abs(diff / 360.0f));
            diff = diff < 0.0f ? diff + 360.0f * round : diff - 360.0f * round;
        }
        return new float[]{prevYaw + diff, pitch};
    }

    public static float[] getRotations(double x, double y, double z, Entity f) {
        return RotationUtil.getRotations(x, y, z, f.field_70165_t, f.field_70163_u, f.field_70161_v, f.func_70047_e());
    }

    public static Vec3d getVec3d(float yaw, float pitch) {
        float vx = -MathHelper.func_76126_a((float)MathUtil.rad(yaw)) * MathHelper.func_76134_b((float)MathUtil.rad(pitch));
        float vz = MathHelper.func_76134_b((float)MathUtil.rad(yaw)) * MathHelper.func_76134_b((float)MathUtil.rad(pitch));
        float vy = -MathHelper.func_76126_a((float)MathUtil.rad(pitch));
        return new Vec3d((double)vx, (double)vy, (double)vz);
    }

    public static EntityPlayer getRotationPlayer() {
        EntityPlayerSP rotationEntity = RotationUtil.mc.field_71439_g;
        return rotationEntity == null ? RotationUtil.mc.field_71439_g : rotationEntity;
    }

    public static float[] getNeededRotations(Vec3d vec) {
        Vec3d playerVector = new Vec3d(RotationUtil.mc.field_71439_g.field_70165_t, RotationUtil.mc.field_71439_g.field_70163_u + (double)RotationUtil.mc.field_71439_g.func_70047_e(), RotationUtil.mc.field_71439_g.field_70161_v);
        double y = vec.field_72448_b - playerVector.field_72448_b;
        double x = vec.field_72450_a - playerVector.field_72450_a;
        double z = vec.field_72449_c - playerVector.field_72449_c;
        double dff = Math.sqrt(x * x + z * z);
        float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(y, dff)));
        return new float[]{MathHelper.func_76142_g((float)yaw), MathHelper.func_76142_g((float)pitch)};
    }

    public static float[] getNeededFacing(Vec3d target, Vec3d from) {
        double diffX = target.field_72450_a - from.field_72450_a;
        double diffY = target.field_72448_b - from.field_72448_b;
        double diffZ = target.field_72449_c - from.field_72449_c;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{MathHelper.func_76142_g((float)yaw), MathHelper.func_76142_g((float)pitch)};
    }

    public static float[] getRotations(Vec3d from, Vec3d to) {
        double difX = to.field_72450_a - from.field_72450_a;
        double difY = (to.field_72448_b - from.field_72448_b) * -1.0;
        double difZ = to.field_72449_c - from.field_72449_c;
        double dist = MathHelper.func_76133_a((double)(difX * difX + difZ * difZ));
        return new float[]{(float)MathHelper.func_76138_g((double)(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0)), (float)MathHelper.func_76138_g((double)Math.toDegrees(Math.atan2(difY, dist)))};
    }

    public static boolean isInFov(BlockPos pos) {
        return pos != null && (RotationUtil.mc.field_71439_g.func_174818_b(pos) < 4.0 || RotationUtil.isInFov(new Vec3d((Vec3i)pos), RotationUtil.mc.field_71439_g.func_174791_d()));
    }

    public static float[] getRotations(EntityLivingBase ent) {
        double x = ent.field_70165_t;
        double z = ent.field_70161_v;
        double y = ent.field_70163_u + (double)(ent.func_70047_e() / 2.0f);
        return RotationUtil.getRotationFromPosition(x, z, y);
    }

    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.func_71410_x().field_71439_g.field_70165_t;
        double zDiff = z - Minecraft.func_71410_x().field_71439_g.field_70161_v;
        double yDiff = y - Minecraft.func_71410_x().field_71439_g.field_70163_u - 1.2;
        double dist = MathHelper.func_76133_a((double)(xDiff * xDiff + zDiff * zDiff));
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-Math.atan2(yDiff, dist) * 180.0 / Math.PI);
        return new float[]{yaw, pitch};
    }

    public static boolean isInFov(Vec3d vec3d, Vec3d other) {
        if (RotationUtil.mc.field_71439_g.field_70125_A > 30.0f ? other.field_72448_b > RotationUtil.mc.field_71439_g.field_70163_u : RotationUtil.mc.field_71439_g.field_70125_A < -30.0f && other.field_72448_b < RotationUtil.mc.field_71439_g.field_70163_u) {
            return true;
        }
        float angle = MathUtil.calcAngleNoY(vec3d, other)[0] - RotationUtil.transformYaw();
        if (angle < -270.0f) {
            return true;
        }
        float fov = RotationUtil.mc.field_71474_y.field_74334_X / 2.0f;
        return angle < fov + 10.0f && angle > -fov - 10.0f;
    }

    public static float transformYaw() {
        float yaw = RotationUtil.mc.field_71439_g.field_70177_z % 360.0f;
        if (RotationUtil.mc.field_71439_g.field_70177_z > 0.0f && yaw > 180.0f) {
            yaw = -180.0f + (yaw - 180.0f);
        }
        return yaw;
    }

    public static float[] getRotationsBlock(BlockPos block, EnumFacing face, boolean Legit) {
        double x = (double)block.func_177958_n() + 0.5 - RotationUtil.mc.field_71439_g.field_70165_t + (double)face.func_82601_c() / 2.0;
        double z = (double)block.func_177952_p() + 0.5 - RotationUtil.mc.field_71439_g.field_70161_v + (double)face.func_82599_e() / 2.0;
        double y = (double)block.func_177956_o() + 0.5;
        if (Legit) {
            y += 0.5;
        }
        double d1 = RotationUtil.mc.field_71439_g.field_70163_u + (double)RotationUtil.mc.field_71439_g.func_70047_e() - y;
        double d3 = MathHelper.func_76133_a((double)(x * x + z * z));
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(Math.atan2(d1, d3) * 180.0 / Math.PI);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[]{yaw, pitch};
    }
}
