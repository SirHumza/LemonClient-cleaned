/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package com.lemonclient.api.util.world.combat.raytrace;

import com.lemonclient.api.util.world.combat.RotationUtil;
import com.lemonclient.api.util.world.combat.raytrace.Ray;
import com.lemonclient.api.util.world.combat.raytrace.RayTracer;
import java.util.HashSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RayTraceFactory {
    private static final EnumFacing[] T = new EnumFacing[]{EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN};
    private static final EnumFacing[] B = new EnumFacing[]{EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.UP};
    private static final EnumFacing[] S = new EnumFacing[]{EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.UP, EnumFacing.DOWN};
    public static Minecraft mc = Minecraft.func_71410_x();

    private RayTraceFactory() {
        throw new AssertionError();
    }

    public static Ray fullTrace(Entity from, World world, BlockPos pos, double resolution) {
        Ray dumbRay = null;
        double closest = Double.MAX_VALUE;
        for (EnumFacing facing : RayTraceFactory.getOptimalFacings(from, pos)) {
            BlockPos offset = pos.func_177972_a(facing);
            IBlockState state = world.func_180495_p(offset);
            if (state.func_185904_a().func_76222_j()) continue;
            Ray ray = RayTraceFactory.rayTrace(from, offset, facing.func_176734_d(), world, state, resolution);
            if (ray.isLegit()) {
                return ray;
            }
            double dist = from.func_70092_e((double)offset.field_177962_a + 0.5, (double)offset.field_177960_b + 0.5, (double)offset.field_177961_c + 0.5);
            if (dumbRay != null && !(dist < closest)) continue;
            closest = dist;
            dumbRay = ray;
        }
        return dumbRay;
    }

    public static Ray rayTrace(Entity from, BlockPos on, EnumFacing facing, World access, IBlockState state, double res) {
        boolean zEq;
        Vec3d start = new Vec3d(from.field_70165_t, from.field_70163_u + (double)from.func_70047_e(), from.field_70161_v);
        AxisAlignedBB bb = state.func_185900_c((IBlockAccess)access, on);
        if (res >= 1.0) {
            float[] r = RayTraceFactory.rots(on, facing, from, access, state);
            Vec3d look = RotationUtil.getVec3d(r[0], r[1]);
            double d = RayTraceFactory.mc.field_71442_b.func_78757_d();
            Vec3d rotations = start.func_72441_c(look.field_72450_a * d, look.field_72448_b * d, look.field_72449_c * d);
            RayTraceResult result = RayTracer.trace((World)RayTraceFactory.mc.field_71441_e, (IBlockAccess)access, start, rotations, false, false, true);
            if (result == null || result.field_178784_b != facing || !on.equals((Object)result.func_178782_a())) {
                return RayTraceFactory.dumbRay(on, facing, r);
            }
            return new Ray(result, r, on, facing, null).setLegit(true);
        }
        Vec3i dirVec = facing.func_176730_m();
        double dirX = dirVec.func_177958_n() < 0 ? bb.field_72340_a : (double)dirVec.func_177958_n() * bb.field_72336_d;
        double dirY = dirVec.func_177956_o() < 0 ? bb.field_72338_b : (double)dirVec.func_177956_o() * bb.field_72337_e;
        double dirZ = dirVec.func_177952_p() < 0 ? bb.field_72339_c : (double)dirVec.func_177952_p() * bb.field_72334_f;
        double minX = (double)on.func_177958_n() + dirX + (dirVec.func_177958_n() == 0 ? bb.field_72340_a : 0.0);
        double minY = (double)on.func_177956_o() + dirY + (dirVec.func_177956_o() == 0 ? bb.field_72338_b : 0.0);
        double minZ = (double)on.func_177952_p() + dirZ + (dirVec.func_177952_p() == 0 ? bb.field_72339_c : 0.0);
        double maxX = (double)on.func_177958_n() + dirX + (dirVec.func_177958_n() == 0 ? bb.field_72336_d : 0.0);
        double maxY = (double)on.func_177956_o() + dirY + (dirVec.func_177956_o() == 0 ? bb.field_72337_e : 0.0);
        double maxZ = (double)on.func_177952_p() + dirZ + (dirVec.func_177952_p() == 0 ? bb.field_72334_f : 0.0);
        boolean xEq = Double.compare(minX, maxX) == 0;
        boolean yEq = Double.compare(minY, maxY) == 0;
        boolean bl = zEq = Double.compare(minZ, maxZ) == 0;
        if (xEq) {
            maxX = minX -= (double)dirVec.func_177958_n() * 5.0E-4;
        }
        if (yEq) {
            maxY = minY -= (double)dirVec.func_177956_o() * 5.0E-4;
        }
        if (zEq) {
            maxZ = minZ -= (double)dirVec.func_177952_p() * 5.0E-4;
        }
        double endX = Math.max(minX, maxX) - (xEq ? 0.0 : 5.0E-4);
        double endY = Math.max(minY, maxY) - (yEq ? 0.0 : 5.0E-4);
        double endZ = Math.max(minZ, maxZ) - (zEq ? 0.0 : 5.0E-4);
        if (res <= 0.0) {
            double staX = Math.min(minX, maxX) + (xEq ? 0.0 : 5.0E-4);
            double staY = Math.min(minY, maxY) + (yEq ? 0.0 : 5.0E-4);
            double staZ = Math.min(minZ, maxZ) + (zEq ? 0.0 : 5.0E-4);
            HashSet<Vec3d> vectors = new HashSet<Vec3d>();
            vectors.add(new Vec3d(staX, staY, staZ));
            vectors.add(new Vec3d(staX, staY, endZ));
            vectors.add(new Vec3d(staX, endY, staZ));
            vectors.add(new Vec3d(staX, endY, endZ));
            vectors.add(new Vec3d(endX, staY, staZ));
            vectors.add(new Vec3d(endX, staY, endZ));
            vectors.add(new Vec3d(endX, endY, staZ));
            vectors.add(new Vec3d(endX, endY, endZ));
            double x = (endX - staX) / 2.0 + staX;
            double y = (endY - staY) / 2.0 + staY;
            double z = (endZ - staZ) / 2.0 + staZ;
            vectors.add(new Vec3d(x, y, z));
            for (Vec3d vec : vectors) {
                RayTraceResult ray = RayTracer.trace((World)RayTraceFactory.mc.field_71441_e, (IBlockAccess)access, start, vec, false, false, true);
                if (ray == null || !on.equals((Object)ray.func_178782_a()) || facing != ray.field_178784_b) continue;
                return new Ray(ray, RayTraceFactory.rots(from, vec), on, facing, vec).setLegit(true);
            }
            return RayTraceFactory.dumbRay(on, facing, RayTraceFactory.rots(on, facing, from, access, state));
        }
        for (double x = Math.min(minX, maxX); x <= endX; x += res) {
            for (double y = Math.min(minY, maxY); y <= endY; y += res) {
                for (double z = Math.min(minZ, maxZ); z <= endZ; z += res) {
                    Vec3d vector = new Vec3d(x, y, z);
                    RayTraceResult ray = RayTracer.trace((World)RayTraceFactory.mc.field_71441_e, (IBlockAccess)access, start, vector, false, false, true);
                    if (ray == null || facing != ray.field_178784_b || !on.equals((Object)ray.func_178782_a())) continue;
                    return new Ray(ray, RayTraceFactory.rots(from, vector), on, facing, vector).setLegit(true);
                }
            }
        }
        return RayTraceFactory.dumbRay(on, facing, RayTraceFactory.rots(on, facing, from, access, state));
    }

    public static Ray dumbRay(BlockPos on, EnumFacing offset, float[] rotations) {
        return RayTraceFactory.newRay(new RayTraceResult(RayTraceResult.Type.MISS, new Vec3d(0.5, 1.0, 0.5), EnumFacing.UP, BlockPos.field_177992_a), on, offset, rotations);
    }

    public static Ray newRay(RayTraceResult result, BlockPos on, EnumFacing offset, float[] rotations) {
        return new Ray(result, rotations, on, offset, null);
    }

    static float[] rots(Entity from, Vec3d vec3d) {
        return RotationUtil.getRotations(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c, from);
    }

    private static float[] rots(BlockPos pos, EnumFacing facing, Entity from, World world, IBlockState state) {
        return RotationUtil.getRotations(pos, facing, from, world, state);
    }

    private static EnumFacing[] getOptimalFacings(Entity player, BlockPos pos) {
        if ((double)pos.func_177956_o() > player.field_70163_u + 2.0) {
            return T;
        }
        if ((double)pos.func_177956_o() < player.field_70163_u) {
            return B;
        }
        return S;
    }
}
