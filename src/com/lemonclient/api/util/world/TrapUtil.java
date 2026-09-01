/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jdk.nashorn.internal.objects.NativeMath
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockDeadBush
 *  net.minecraft.block.BlockFire
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.BlockSnow
 *  net.minecraft.block.BlockTallGrass
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraft.world.chunk.Chunk
 */
package com.lemonclient.api.util.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jdk.nashorn.internal.objects.NativeMath;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class TrapUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();
    public static final Vec3d[] antiDropOffsetList = new Vec3d[]{new Vec3d(0.0, -2.0, 0.0)};
    public static final Vec3d[] platformOffsetList = new Vec3d[]{new Vec3d(0.0, -1.0, 0.0), new Vec3d(0.0, -1.0, -1.0), new Vec3d(0.0, -1.0, 1.0), new Vec3d(-1.0, -1.0, 0.0), new Vec3d(1.0, -1.0, 0.0)};
    public static final Vec3d[] legOffsetList = new Vec3d[]{new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0)};
    public static final Vec3d[] OffsetList = new Vec3d[]{new Vec3d(1.0, 1.0, 0.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(0.0, 2.0, 0.0)};
    public static final Vec3d[] antiStepOffsetList = new Vec3d[]{new Vec3d(-1.0, 2.0, 0.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(0.0, 2.0, -1.0)};
    public static final Vec3d[] antiScaffoldOffsetList = new Vec3d[]{new Vec3d(0.0, 3.0, 0.0)};

    public static void placeBlock(BlockPos pos) {
        for (EnumFacing side : EnumFacing.field_82609_l) {
            boolean sneak;
            BlockPos neighbor = pos.func_177972_a(side);
            IBlockState neighborState = TrapUtil.mc.field_71441_e.func_180495_p(neighbor);
            if (!neighborState.func_177230_c().func_176209_a(neighborState, false)) continue;
            boolean bl = sneak = !TrapUtil.mc.field_71439_g.func_70093_af() && neighborState.func_177230_c().func_180639_a((World)TrapUtil.mc.field_71441_e, pos, TrapUtil.mc.field_71441_e.func_180495_p(pos), (EntityPlayer)TrapUtil.mc.field_71439_g, EnumHand.MAIN_HAND, side, 0.5f, 0.5f, 0.5f);
            if (sneak) {
                mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)TrapUtil.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            }
            mc.func_147114_u().func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(neighbor, side.func_176734_d(), EnumHand.MAIN_HAND, 0.5f, 0.5f, 0.5f));
            mc.func_147114_u().func_147297_a((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
            if (!sneak) continue;
            mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)TrapUtil.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }

    public static boolean canPlaceCrystal(BlockPos pos, boolean checkSecond) {
        Chunk chunk = TrapUtil.mc.field_71441_e.func_175726_f(pos);
        Block block = chunk.func_177435_g(pos).func_177230_c();
        if (block != Blocks.field_150357_h && block != Blocks.field_150343_Z) {
            return false;
        }
        BlockPos boost = pos.func_177967_a(EnumFacing.UP, 1);
        if (chunk.func_177435_g(boost).func_177230_c() != Blocks.field_150350_a || chunk.func_177435_g(pos.func_177967_a(EnumFacing.UP, 2)).func_177230_c() != Blocks.field_150350_a) {
            return false;
        }
        return TrapUtil.mc.field_71441_e.func_175647_a(Entity.class, new AxisAlignedBB((double)boost.func_177958_n(), (double)boost.func_177956_o(), (double)boost.func_177952_p(), (double)(boost.func_177958_n() + 1), (double)(boost.func_177956_o() + (checkSecond ? 2 : 1)), (double)(boost.func_177952_p() + 1)), e -> !(e instanceof EntityEnderCrystal)).isEmpty();
    }

    public static List<BlockPos> getSphere(float radius) {
        ArrayList<BlockPos> sphere = new ArrayList<BlockPos>();
        BlockPos pos = new BlockPos(TrapUtil.mc.field_71439_g.field_70165_t, TrapUtil.mc.field_71439_g.field_70163_u, TrapUtil.mc.field_71439_g.field_70161_v);
        int posX = pos.func_177958_n();
        int posY = pos.func_177956_o();
        int posZ = pos.func_177952_p();
        int x = posX - (int)radius;
        while ((float)x <= (float)posX + radius) {
            int z = posZ - (int)radius;
            while ((float)z <= (float)posZ + radius) {
                int y = posY - (int)radius;
                while ((float)y < (float)posY + radius) {
                    if ((float)((posX - x) * (posX - x) + (posZ - z) * (posZ - z) + (posY - y) * (posY - y)) < radius * radius) {
                        sphere.add(new BlockPos(x, y, z));
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return sphere;
    }

    public static int isPositionPlaceable(BlockPos pos, boolean entityCheck) {
        try {
            Block block = TrapUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c();
            if (!(block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow)) {
                return 0;
            }
            if (entityCheck) {
                for (Entity entity : TrapUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
                    if (entity.field_70128_L || entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
                    return 1;
                }
            }
            for (EnumFacing side : TrapUtil.getPossibleSides(pos)) {
                if (!TrapUtil.canBeClicked(pos.func_177972_a(side))) continue;
                return 3;
            }
            return 2;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public static boolean canBeClicked(BlockPos pos) {
        return TrapUtil.getBlock(pos).func_176209_a(TrapUtil.getState(pos), false);
    }

    private static Block getBlock(BlockPos pos) {
        return TrapUtil.getState(pos).func_177230_c();
    }

    private static IBlockState getState(BlockPos pos) {
        return TrapUtil.mc.field_71441_e.func_180495_p(pos);
    }

    public static List<EnumFacing> getPossibleSides(BlockPos pos) {
        ArrayList<EnumFacing> facings = new ArrayList<EnumFacing>(6);
        for (EnumFacing side : EnumFacing.values()) {
            IBlockState blockState;
            BlockPos neighbour = pos.func_177972_a(side);
            if (!TrapUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c().func_176209_a(TrapUtil.mc.field_71441_e.func_180495_p(neighbour), false) || (blockState = TrapUtil.mc.field_71441_e.func_180495_p(neighbour)).func_185904_a().func_76222_j()) continue;
            facings.add(side);
        }
        return facings;
    }

    public static Vec3d[] getHelpingBlocks(Vec3d vec3d) {
        return new Vec3d[]{new Vec3d(vec3d.field_72450_a, vec3d.field_72448_b - 1.0, vec3d.field_72449_c), new Vec3d(vec3d.field_72450_a != 0.0 ? vec3d.field_72450_a * 2.0 : vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72450_a != 0.0 ? vec3d.field_72449_c : vec3d.field_72449_c * 2.0), new Vec3d(vec3d.field_72450_a == 0.0 ? vec3d.field_72450_a + 1.0 : vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72450_a == 0.0 ? vec3d.field_72449_c : vec3d.field_72449_c + 1.0), new Vec3d(vec3d.field_72450_a == 0.0 ? vec3d.field_72450_a - 1.0 : vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72450_a == 0.0 ? vec3d.field_72449_c : vec3d.field_72449_c - 1.0), new Vec3d(vec3d.field_72450_a, vec3d.field_72448_b + 1.0, vec3d.field_72449_c)};
    }

    public static List<Vec3d> getOffsetList(int y, boolean floor) {
        ArrayList<Vec3d> offsets = new ArrayList<Vec3d>(5);
        offsets.add(new Vec3d(-1.0, (double)y, 0.0));
        offsets.add(new Vec3d(1.0, (double)y, 0.0));
        offsets.add(new Vec3d(0.0, (double)y, -1.0));
        offsets.add(new Vec3d(0.0, (double)y, 1.0));
        if (floor) {
            offsets.add(new Vec3d(0.0, (double)(y - 1), 0.0));
        }
        return offsets;
    }

    public static Vec3d[] getOffsets(int y, boolean floor) {
        List<Vec3d> offsets = TrapUtil.getOffsetList(y, floor);
        Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray(array);
    }

    public static Vec3d[] getUnsafeBlockArray(Entity entity, int height, boolean floor) {
        List<Vec3d> list = TrapUtil.getUnsafeBlocks(entity, height, floor);
        Vec3d[] array = new Vec3d[list.size()];
        return list.toArray(array);
    }

    public static boolean isSafe(Entity entity, int height, boolean floor) {
        return TrapUtil.getUnsafeBlocks(entity, height, floor).size() == 0;
    }

    public static List<Vec3d> getUnsafeBlocks(Entity entity, int height, boolean floor) {
        return TrapUtil.getUnsafeBlocksFromVec3d(entity.func_174791_d(), height, floor);
    }

    public static List<Vec3d> getUnsafeBlocksFromVec3d(Vec3d pos, int height, boolean floor) {
        ArrayList<Vec3d> vec3ds = new ArrayList<Vec3d>(5);
        for (Vec3d vector : TrapUtil.getOffsets(height, floor)) {
            Block block = TrapUtil.mc.field_71441_e.func_180495_p(new BlockPos(pos).func_177963_a(vector.field_72450_a, vector.field_72448_b, vector.field_72449_c)).func_177230_c();
            if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid) && !(block instanceof BlockTallGrass) && !(block instanceof BlockFire) && !(block instanceof BlockDeadBush) && !(block instanceof BlockSnow)) continue;
            vec3ds.add(vector);
        }
        return vec3ds;
    }

    public static Vec3d[] getTrapOffsets(boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop) {
        List<Vec3d> offsets = TrapUtil.getTrapOffsetsList(antiScaffold, antiStep, legs, platform, antiDrop);
        Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray(array);
    }

    public static List<Vec3d> getTrapOffsetsList(boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop) {
        ArrayList<Vec3d> offsets = new ArrayList<Vec3d>(TrapUtil.getOffsetList(1, false));
        offsets.add(new Vec3d(0.0, 2.0, 0.0));
        if (antiScaffold) {
            offsets.add(new Vec3d(0.0, 3.0, 0.0));
        }
        if (antiStep) {
            offsets.addAll(TrapUtil.getOffsetList(2, false));
        }
        if (legs) {
            offsets.addAll(TrapUtil.getOffsetList(0, false));
        }
        if (platform) {
            offsets.addAll(TrapUtil.getOffsetList(-1, false));
            offsets.add(new Vec3d(0.0, -1.0, 0.0));
        }
        if (antiDrop) {
            offsets.add(new Vec3d(0.0, -2.0, 0.0));
        }
        return offsets;
    }

    public static boolean isTrapped(EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop) {
        return TrapUtil.getUntrappedBlocks(player, antiScaffold, antiStep, legs, platform, antiDrop).size() == 0;
    }

    public static boolean isTrappedExtended(int extension, EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace) {
        return TrapUtil.getUntrappedBlocksExtended(extension, player, antiScaffold, antiStep, legs, platform, antiDrop, raytrace).size() == 0;
    }

    public static List<Vec3d> getBlockBlocks(Entity entity) {
        ArrayList<Vec3d> vec3ds = new ArrayList<Vec3d>(8);
        AxisAlignedBB bb = entity.func_174813_aQ();
        double y = entity.field_70163_u;
        double minX = NativeMath.round((Object)bb.field_72340_a, (Object)0);
        double minZ = NativeMath.round((Object)bb.field_72339_c, (Object)0);
        double maxX = NativeMath.round((Object)bb.field_72336_d, (Object)0);
        double maxZ = NativeMath.round((Object)bb.field_72334_f, (Object)0);
        if (minX != maxX) {
            vec3ds.add(new Vec3d(minX, y, minZ));
            vec3ds.add(new Vec3d(maxX, y, minZ));
            if (minZ != maxZ) {
                vec3ds.add(new Vec3d(minX, y, maxZ));
                vec3ds.add(new Vec3d(maxX, y, maxZ));
                return vec3ds;
            }
        } else if (minZ != maxZ) {
            vec3ds.add(new Vec3d(minX, y, minZ));
            vec3ds.add(new Vec3d(minX, y, maxZ));
            return vec3ds;
        }
        vec3ds.add(entity.func_174791_d());
        return vec3ds;
    }

    public static List<Vec3d> getUntrappedBlocksExtended(int extension, EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace) {
        ArrayList<Vec3d> placeTargets = new ArrayList<Vec3d>();
        if (extension == 1) {
            placeTargets.addAll(TrapUtil.targets(player.func_174791_d(), antiScaffold, antiStep, legs, platform, antiDrop, raytrace));
        } else {
            int extend = 1;
            for (Vec3d vec3d : TrapUtil.getBlockBlocks((Entity)player)) {
                if (extend > extension) break;
                placeTargets.addAll(TrapUtil.targets(vec3d, antiScaffold, antiStep, legs, platform, antiDrop, raytrace));
                ++extend;
            }
        }
        ArrayList<Vec3d> removeList = new ArrayList<Vec3d>();
        for (Vec3d vec3d : placeTargets) {
            BlockPos pos = new BlockPos(vec3d);
            if (TrapUtil.isPositionPlaceable(pos, raytrace) != -1) continue;
            removeList.add(vec3d);
        }
        for (Vec3d vec3d : removeList) {
            placeTargets.remove(vec3d);
        }
        return placeTargets;
    }

    public static List<Vec3d> getUntrappedBlocks(EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop) {
        ArrayList<Vec3d> vec3ds = new ArrayList<Vec3d>();
        if (!antiStep && TrapUtil.getUnsafeBlocks((Entity)player, 2, false).size() == 4) {
            vec3ds.addAll(TrapUtil.getUnsafeBlocks((Entity)player, 2, false));
        }
        Vec3d[] trapOffsets = TrapUtil.getTrapOffsets(antiScaffold, antiStep, legs, platform, antiDrop);
        for (int i = 0; i < trapOffsets.length; ++i) {
            Vec3d vector = trapOffsets[i];
            BlockPos targetPos = new BlockPos(player.func_174791_d()).func_177963_a(vector.field_72450_a, vector.field_72448_b, vector.field_72449_c);
            Block block = TrapUtil.mc.field_71441_e.func_180495_p(targetPos).func_177230_c();
            if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid) && !(block instanceof BlockTallGrass) && !(block instanceof BlockFire) && !(block instanceof BlockDeadBush) && !(block instanceof BlockSnow)) continue;
            vec3ds.add(vector);
        }
        return vec3ds;
    }

    public static List<Vec3d> targets(Vec3d vec3d, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace) {
        ArrayList<Vec3d> placeTargets = new ArrayList<Vec3d>();
        if (antiDrop) {
            Collections.addAll(placeTargets, TrapUtil.convertVec3ds(vec3d, antiDropOffsetList));
        }
        if (platform) {
            Collections.addAll(placeTargets, TrapUtil.convertVec3ds(vec3d, platformOffsetList));
        }
        if (legs) {
            Collections.addAll(placeTargets, TrapUtil.convertVec3ds(vec3d, legOffsetList));
        }
        Collections.addAll(placeTargets, TrapUtil.convertVec3ds(vec3d, OffsetList));
        if (antiStep) {
            Collections.addAll(placeTargets, TrapUtil.convertVec3ds(vec3d, antiStepOffsetList));
        } else {
            List<Vec3d> vec3ds = TrapUtil.getUnsafeBlocksFromVec3d(vec3d, 2, false);
            if (vec3ds.size() == 4) {
                block5: for (Vec3d vector : vec3ds) {
                    BlockPos position = new BlockPos(vec3d).func_177963_a(vector.field_72450_a, vector.field_72448_b, vector.field_72449_c);
                    switch (TrapUtil.isPositionPlaceable(position, raytrace)) {
                        case 0: {
                            break block5;
                        }
                        case -1: 
                        case 1: 
                        case 2: {
                            continue block5;
                        }
                        case 3: {
                            placeTargets.add(vec3d.func_178787_e(vector));
                        }
                    }
                }
            }
        }
        if (antiScaffold) {
            Collections.addAll(placeTargets, TrapUtil.convertVec3ds(vec3d, antiScaffoldOffsetList));
        }
        return placeTargets;
    }

    public static Vec3d[] convertVec3ds(Vec3d vec3d, Vec3d[] input) {
        Vec3d[] output = new Vec3d[input.length];
        int length = input.length;
        for (int i = 0; i < length; ++i) {
            output[i] = vec3d.func_178787_e(input[i]);
        }
        return output;
    }
}
