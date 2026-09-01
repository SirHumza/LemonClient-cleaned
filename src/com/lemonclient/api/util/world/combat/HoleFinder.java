/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package com.lemonclient.api.util.world.combat;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class HoleFinder {
    private static final Minecraft mc = Minecraft.func_71410_x();
    private static final Vec3i[] OFFSETS_2x2 = new Vec3i[]{new Vec3i(0, 0, 0), new Vec3i(1, 0, 0), new Vec3i(0, 0, 1), new Vec3i(1, 0, 1)};
    public static final Set<Block> NO_BLAST = Sets.newHashSet((Object[])new Block[]{Blocks.field_150357_h, Blocks.field_150343_Z, Blocks.field_150467_bQ, Blocks.field_150477_bB});
    public static final Set<Block> UNSAFE = Sets.newHashSet((Object[])new Block[]{Blocks.field_150343_Z, Blocks.field_150467_bQ, Blocks.field_150477_bB});

    public static boolean isAir(BlockPos pos) {
        return HoleFinder.mc.field_71441_e.func_175623_d(pos);
    }

    public static boolean[] isHole(BlockPos pos, boolean above) {
        boolean[] result = new boolean[]{false, true};
        if (!HoleFinder.isAir(pos) || !HoleFinder.isAir(pos.func_177984_a()) || above && !HoleFinder.isAir(pos.func_177981_b(2))) {
            return result;
        }
        return HoleFinder.is1x1(pos, result);
    }

    public static boolean[] is1x1(BlockPos pos) {
        return HoleFinder.is1x1(pos, new boolean[]{false, true});
    }

    public static boolean[] is1x1(BlockPos pos, boolean[] result) {
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos offset;
            IBlockState state;
            if (facing == EnumFacing.UP || (state = HoleFinder.mc.field_71441_e.func_180495_p(offset = pos.func_177972_a(facing))).func_177230_c() == Blocks.field_150357_h) continue;
            if (!NO_BLAST.contains(state.func_177230_c())) {
                return result;
            }
            result[1] = false;
        }
        result[0] = true;
        return result;
    }

    public static boolean is2x1(BlockPos pos) {
        return HoleFinder.is2x1(pos, true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean is2x1(BlockPos pos, boolean upper) {
        if (upper) {
            if (!HoleFinder.isAir(pos)) return false;
            if (!HoleFinder.isAir(pos.func_177984_a())) return false;
            if (HoleFinder.isAir(pos.func_177977_b())) {
                return false;
            }
        }
        int airBlocks = 0;
        for (EnumFacing facing : EnumFacing.field_176754_o) {
            BlockPos offset = pos.func_177972_a(facing);
            if (HoleFinder.isAir(offset)) {
                if (!HoleFinder.isAir(offset.func_177984_a())) return false;
                if (HoleFinder.isAir(offset.func_177977_b())) return false;
                for (EnumFacing offsetFacing : EnumFacing.field_176754_o) {
                    IBlockState state;
                    if (offsetFacing == facing.func_176734_d() || NO_BLAST.contains((state = HoleFinder.mc.field_71441_e.func_180495_p(offset.func_177972_a(offsetFacing))).func_177230_c())) continue;
                    return false;
                }
                ++airBlocks;
            }
            if (airBlocks <= true) continue;
            return false;
        }
        if (airBlocks != true) return false;
        return true;
    }

    public static boolean is2x2Partial(BlockPos pos) {
        HashSet<BlockPos> positions = new HashSet<BlockPos>();
        for (Vec3i vec : OFFSETS_2x2) {
            positions.add(pos.func_177971_a(vec));
        }
        boolean airBlock = false;
        for (BlockPos holePos : positions) {
            if (HoleFinder.isAir(holePos) && HoleFinder.isAir(holePos.func_177984_a()) && !HoleFinder.isAir(holePos.func_177977_b())) {
                if (HoleFinder.isAir(holePos.func_177981_b(2))) {
                    airBlock = true;
                }
                for (EnumFacing facing : EnumFacing.field_176754_o) {
                    IBlockState state;
                    BlockPos offset = holePos.func_177972_a(facing);
                    if (positions.contains(offset) || NO_BLAST.contains((state = HoleFinder.mc.field_71441_e.func_180495_p(offset)).func_177230_c())) continue;
                    return false;
                }
                continue;
            }
            return false;
        }
        return airBlock;
    }

    public static boolean is2x2(BlockPos pos) {
        return HoleFinder.is2x2(pos, true);
    }

    public static boolean is2x2(BlockPos pos, boolean upper) {
        if (upper && !HoleFinder.isAir(pos)) {
            return false;
        }
        if (HoleFinder.is2x2Partial(pos)) {
            return true;
        }
        BlockPos l = pos.func_177982_a(-1, 0, 0);
        boolean airL = HoleFinder.isAir(l);
        if (airL && HoleFinder.is2x2Partial(l)) {
            return true;
        }
        BlockPos r = pos.func_177982_a(0, 0, -1);
        boolean airR = HoleFinder.isAir(r);
        if (airR && HoleFinder.is2x2Partial(r)) {
            return true;
        }
        return (airL || airR) && HoleFinder.is2x2Partial(pos.func_177982_a(-1, 0, -1));
    }

    public static boolean is2x2single(BlockPos pos, boolean upper) {
        if (upper && !HoleFinder.isAir(pos)) {
            return false;
        }
        return HoleFinder.is2x2Partial(pos);
    }
}
