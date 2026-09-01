/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 */
package com.lemonclient.api.util.player;

import com.lemonclient.api.util.misc.MathUtil;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class RayTraceUtil {
    public static Minecraft mc = Minecraft.func_71410_x();

    public static float[] hitVecToPlaceVec(BlockPos pos, Vec3d hitVec) {
        float x = (float)(hitVec.field_72450_a - (double)pos.func_177958_n());
        float y = (float)(hitVec.field_72448_b - (double)pos.func_177956_o());
        float z = (float)(hitVec.field_72449_c - (double)pos.func_177952_p());
        return new float[]{x, y, z};
    }

    public static RayTraceResult getRayTraceResult(float yaw, float pitch) {
        return RayTraceUtil.getRayTraceResult(yaw, pitch, RayTraceUtil.mc.field_71442_b.func_78757_d());
    }

    public static RayTraceResult getRayTraceResultWithEntity(float yaw, float pitch, Entity from) {
        return RayTraceUtil.getRayTraceResult(yaw, pitch, RayTraceUtil.mc.field_71442_b.func_78757_d(), from);
    }

    public static RayTraceResult getRayTraceResult(float yaw, float pitch, float distance) {
        return RayTraceUtil.getRayTraceResult(yaw, pitch, distance, (Entity)RayTraceUtil.mc.field_71439_g);
    }

    public static RayTraceResult getRayTraceResult(float yaw, float pitch, float d, Entity from) {
        Vec3d vec3d = RayTraceUtil.getEyePos(from);
        Vec3d lookVec = RayTraceUtil.getVec3d(yaw, pitch);
        Vec3d rotations = vec3d.func_72441_c(lookVec.field_72450_a * (double)d, lookVec.field_72448_b * (double)d, lookVec.field_72449_c * (double)d);
        return Optional.ofNullable(RayTraceUtil.mc.field_71441_e.func_147447_a(vec3d, rotations, false, false, false)).orElseGet(() -> new RayTraceResult(RayTraceResult.Type.MISS, new Vec3d(0.5, 1.0, 0.5), EnumFacing.UP, BlockPos.field_177992_a));
    }

    public static Vec3d getVec3d(float yaw, float pitch) {
        float vx = -MathHelper.func_76126_a((float)MathUtil.rad(yaw)) * MathHelper.func_76134_b((float)MathUtil.rad(pitch));
        float vz = MathHelper.func_76134_b((float)MathUtil.rad(yaw)) * MathHelper.func_76134_b((float)MathUtil.rad(pitch));
        float vy = -MathHelper.func_76126_a((float)MathUtil.rad(pitch));
        return new Vec3d((double)vx, (double)vy, (double)vz);
    }

    public static Vec3d getEyePos(Entity entity) {
        return new Vec3d(entity.field_70165_t, RayTraceUtil.getEyeHeight(entity), entity.field_70161_v);
    }

    public static double getEyeHeight(Entity entity) {
        return entity.field_70163_u + (double)entity.func_70047_e();
    }

    public static boolean canBeSeen(double x, double y, double z, Entity by) {
        return RayTraceUtil.canBeSeen(new Vec3d(x, y, z), by.field_70165_t, by.field_70163_u, by.field_70161_v, by.func_70047_e());
    }

    public static boolean canBeSeen(Vec3d toSee, Entity by) {
        return RayTraceUtil.canBeSeen(toSee, by.field_70165_t, by.field_70163_u, by.field_70161_v, by.func_70047_e());
    }

    public static boolean canBeSeen(Vec3d toSee, double x, double y, double z, float eyeHeight) {
        Vec3d start = new Vec3d(x, y + (double)eyeHeight, z);
        return RayTraceUtil.mc.field_71441_e.func_147447_a(start, toSee, false, true, false) == null;
    }

    public static boolean canBeSeen(Entity toSee, EntityLivingBase by) {
        return by.func_70685_l(toSee);
    }

    public static boolean raytracePlaceCheck(Entity entity, BlockPos pos) {
        return RayTraceUtil.getFacing(entity, pos, false) != null;
    }

    public static EnumFacing getFacing(Entity entity, BlockPos pos, boolean verticals) {
        for (EnumFacing facing : EnumFacing.values()) {
            RayTraceResult result = RayTraceUtil.mc.field_71441_e.func_147447_a(RayTraceUtil.getEyePos(entity), new Vec3d((double)pos.func_177958_n() + 0.5 + (double)facing.func_176730_m().func_177958_n() * 1.0 / 2.0, (double)pos.func_177956_o() + 0.5 + (double)facing.func_176730_m().func_177956_o() * 1.0 / 2.0, (double)pos.func_177952_p() + 0.5 + (double)facing.func_176730_m().func_177952_p() * 1.0 / 2.0), false, true, false);
            if (result == null || result.field_72313_a != RayTraceResult.Type.BLOCK || !result.func_178782_a().equals((Object)pos)) continue;
            return facing;
        }
        if (verticals) {
            if ((double)pos.func_177956_o() > RayTraceUtil.mc.field_71439_g.field_70163_u + (double)RayTraceUtil.mc.field_71439_g.func_70047_e()) {
                return EnumFacing.DOWN;
            }
            return EnumFacing.UP;
        }
        return null;
    }
}
