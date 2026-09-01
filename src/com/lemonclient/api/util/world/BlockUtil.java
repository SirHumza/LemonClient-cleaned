/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityArrow
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.world.IBlockAccess
 */
package com.lemonclient.api.util.world;

import com.lemonclient.api.util.misc.Wrapper;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.api.util.world.combat.CrystalUtil;
import com.lemonclient.client.module.modules.gui.ColorMain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;

public class BlockUtil {
    public static final List shulkerList = Arrays.asList(Blocks.field_190977_dl, Blocks.field_190978_dm, Blocks.field_190979_dn, Blocks.field_190980_do, Blocks.field_190981_dp, Blocks.field_190982_dq, Blocks.field_190983_dr, Blocks.field_190984_ds, Blocks.field_190985_dt, Blocks.field_190986_du, Blocks.field_190987_dv, Blocks.field_190988_dw, Blocks.field_190989_dx, Blocks.field_190990_dy, Blocks.field_190991_dz, Blocks.field_190975_dA);
    public static final List blackList = Arrays.asList(Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150477_bB, Blocks.field_150467_bQ, Blocks.field_150471_bO, Blocks.field_150430_aB, Blocks.field_150441_bU, Blocks.field_150413_aR, Blocks.field_150416_aS, Blocks.field_150455_bV, Blocks.field_180390_bo, Blocks.field_180391_bp, Blocks.field_180392_bq, Blocks.field_180386_br, Blocks.field_180385_bs, Blocks.field_180387_bt, Blocks.field_150382_bo, Blocks.field_150367_z, Blocks.field_150409_cd, Blocks.field_150442_at, Blocks.field_150323_B, Blocks.field_150421_aI, Blocks.field_150461_bJ, Blocks.field_150324_C, Blocks.field_150460_al, Blocks.field_180413_ao, Blocks.field_180414_ap, Blocks.field_180412_aq, Blocks.field_180411_ar, Blocks.field_180410_as, Blocks.field_180409_at, Blocks.field_150414_aQ, Blocks.field_150381_bn, Blocks.field_150380_bt, Blocks.field_150438_bZ, Blocks.field_185776_dc, Blocks.field_150483_bI, Blocks.field_185777_dd, Blocks.field_150462_ai, Blocks.field_150444_as, Blocks.field_150472_an, shulkerList);
    public static final List unSolidBlocks = Arrays.asList(Blocks.field_150433_aE, Blocks.field_150404_cg, Blocks.field_185764_cQ, Blocks.field_150465_bP, Blocks.field_150457_bL, Blocks.field_150473_bD, Blocks.field_150479_bC, Blocks.field_150468_ap, Blocks.field_150437_az, Blocks.field_150488_af, Blocks.field_150350_a, Blocks.field_150427_aO, Blocks.field_150384_bq, Blocks.field_150355_j, Blocks.field_150358_i, Blocks.field_150353_l, Blocks.field_150356_k, Blocks.field_150345_g, Blocks.field_150328_O, Blocks.field_150327_N, Blocks.field_150338_P, Blocks.field_150337_Q, Blocks.field_150464_aj, Blocks.field_150459_bM, Blocks.field_150469_bN, Blocks.field_185773_cZ, Blocks.field_150436_aH, Blocks.field_150393_bb, Blocks.field_150394_bc, Blocks.field_150392_bi, Blocks.field_150388_bm, Blocks.field_150375_by, Blocks.field_185766_cS, Blocks.field_185765_cR, Blocks.field_150329_H, Blocks.field_150330_I, Blocks.field_150395_bd, Blocks.field_150480_ab, Blocks.field_150448_aq, Blocks.field_150408_cc, Blocks.field_150319_E, Blocks.field_150318_D, Blocks.field_150478_aa, Blocks.field_150429_aA, Blocks.field_150321_G, Blocks.field_150332_K, Blocks.field_180384_M, Blocks.field_150331_J, Blocks.field_150320_F, Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150477_bB, Blocks.field_150467_bQ, Blocks.field_150471_bO, Blocks.field_150430_aB, Blocks.field_150441_bU, Blocks.field_150413_aR, Blocks.field_150416_aS, Blocks.field_150455_bV, Blocks.field_180390_bo, Blocks.field_180391_bp, Blocks.field_180392_bq, Blocks.field_180386_br, Blocks.field_180385_bs, Blocks.field_180387_bt, Blocks.field_150382_bo, Blocks.field_150367_z, Blocks.field_150409_cd, Blocks.field_150442_at, Blocks.field_150323_B, Blocks.field_150421_aI, Blocks.field_150461_bJ, Blocks.field_150324_C, Blocks.field_150460_al, Blocks.field_180413_ao, Blocks.field_180414_ap, Blocks.field_180412_aq, Blocks.field_180411_ar, Blocks.field_180410_as, Blocks.field_180409_at, Blocks.field_150414_aQ, Blocks.field_150381_bn, Blocks.field_150380_bt, shulkerList);
    public static final List airBlocks = Arrays.asList(Blocks.field_150350_a, Blocks.field_189877_df, Blocks.field_150353_l, Blocks.field_150356_k, Blocks.field_150355_j, Blocks.field_150358_i, Blocks.field_150480_ab, Blocks.field_150395_bd, Blocks.field_150431_aC, Blocks.field_150329_H);
    private static final Minecraft mc = Minecraft.func_71410_x();
    static EnumFacing[] facing = new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.EAST};

    public static AxisAlignedBB getBoundingBox(BlockPos pos) {
        if (pos == null) {
            return null;
        }
        AxisAlignedBB box = BlockUtil.getState(pos).func_185890_d((IBlockAccess)BlockUtil.mc.field_71441_e, pos);
        return box == null ? null : new AxisAlignedBB((double)pos.field_177962_a + box.field_72340_a, (double)pos.field_177960_b + box.field_72338_b, (double)pos.field_177961_c + box.field_72339_c, (double)pos.field_177962_a + box.field_72336_d, (double)pos.field_177960_b + box.field_72337_e, (double)pos.field_177961_c + box.field_72334_f);
    }

    public static Vec3d[] convertVec3ds(Vec3d vec3d, Vec3d[] input) {
        Vec3d[] output = new Vec3d[input.length];
        for (int i = 0; i < input.length; ++i) {
            output[i] = vec3d.func_178787_e(input[i]);
        }
        return output;
    }

    public static Vec3d[] convertVec3ds(EntityPlayer entity, Vec3d[] input) {
        return BlockUtil.convertVec3ds(entity.func_174791_d(), input);
    }

    public static NonNullList<BlockPos> getBox(float range) {
        NonNullList positions = NonNullList.func_191196_a();
        positions.addAll(EntityUtil.getSphere(new BlockPos(Math.floor(BlockUtil.mc.field_71439_g.field_70165_t), Math.floor(BlockUtil.mc.field_71439_g.field_70163_u), Math.floor(BlockUtil.mc.field_71439_g.field_70161_v)), Double.valueOf(range), 0.0, false, true, 0));
        return positions;
    }

    public static NonNullList<BlockPos> getBox(float range, BlockPos pos) {
        NonNullList positions = NonNullList.func_191196_a();
        positions.addAll(EntityUtil.getSphere(pos, Double.valueOf(range), 0.0, false, true, 0));
        return positions;
    }

    public static boolean isBlockUnSolid(BlockPos blockPos) {
        Block block = BlockUtil.getBlock(blockPos);
        return BlockUtil.isBlockUnSolid(block) || !block.field_149787_q;
    }

    public static boolean canOpen(BlockPos blockPos) {
        return BlockUtil.canOpen(BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c());
    }

    public static boolean isAir(BlockPos blockPos) {
        return BlockUtil.isAir(BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c());
    }

    public static boolean isAirBlock(BlockPos blockPos) {
        return BlockUtil.isAirBlock(BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c());
    }

    public static boolean raytraceCheck(BlockPos pos, float height) {
        return BlockUtil.mc.field_71441_e.func_147447_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + (double)BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n(), (double)((float)pos.func_177956_o() + height), (double)pos.func_177952_p()), false, true, false) == null;
    }

    public static boolean canBePlace(BlockPos pos) {
        return !BlockUtil.checkPlayer(pos) && BlockUtil.canReplace(pos);
    }

    public static boolean canBePlace(BlockPos pos, double distance) {
        if (BlockUtil.mc.field_71439_g.func_70011_f((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5) > distance) {
            return false;
        }
        return !BlockUtil.checkPlayer(pos) && BlockUtil.canReplace(pos);
    }

    public static boolean checkPlayer(BlockPos pos) {
        for (Entity entity : BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
            if (entity.field_70128_L || entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityExpBottle || entity instanceof EntityArrow || entity instanceof EntityEnderCrystal) continue;
            return true;
        }
        return false;
    }

    public static EnumFacing getBestNeighboring(BlockPos pos, EnumFacing facing) {
        for (EnumFacing i : EnumFacing.field_82609_l) {
            if (facing != null && pos.func_177972_a(i).equals((Object)pos.func_177967_a(facing, -1)) || i == EnumFacing.DOWN) continue;
            for (EnumFacing side : BlockUtil.getPlacableFacings(pos.func_177972_a(i), true, true)) {
                if (!BlockUtil.canClick(pos.func_177972_a(i).func_177972_a(side))) continue;
                return i;
            }
        }
        EnumFacing bestFacing = null;
        double distance = 0.0;
        for (EnumFacing i : EnumFacing.field_82609_l) {
            if (facing != null && pos.func_177972_a(i).equals((Object)pos.func_177967_a(facing, -1)) || i == EnumFacing.DOWN) continue;
            for (EnumFacing side : BlockUtil.getPlacableFacings(pos.func_177972_a(i), true, false)) {
                if (!BlockUtil.canClick(pos.func_177972_a(i).func_177972_a(side)) || bestFacing != null && !(BlockUtil.mc.field_71439_g.func_174818_b(pos.func_177972_a(i)) < distance)) continue;
                bestFacing = i;
                distance = BlockUtil.mc.field_71439_g.func_174818_b(pos.func_177972_a(i));
            }
        }
        return null;
    }

    public static double distanceToXZ(double x, double z) {
        double dx = BlockUtil.mc.field_71439_g.field_70165_t - x;
        double dz = BlockUtil.mc.field_71439_g.field_70161_v - z;
        return Math.sqrt(dx * dx + dz * dz);
    }

    public static void placeBlock(BlockPos pos, boolean rotate, boolean packet, boolean strict, boolean raytrace, boolean swing) {
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, rotate, packet, strict, raytrace, swing);
    }

    public static void placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean attackEntity, boolean strict, boolean raytrace, boolean swing) {
        if (attackEntity) {
            CrystalUtil.breakCrystal(pos, swing);
        }
        BlockUtil.placeBlock(pos, hand, rotate, packet, strict, raytrace, swing);
    }

    public static boolean canBlockFacing(BlockPos pos) {
        boolean airCheck = false;
        for (EnumFacing side : EnumFacing.values()) {
            if (!BlockUtil.canClick(pos.func_177972_a(side))) continue;
            airCheck = true;
        }
        return airCheck;
    }

    public static boolean canBlockFacing(BlockPos pos, BlockPos check) {
        boolean airCheck = false;
        for (EnumFacing side : EnumFacing.values()) {
            if (!BlockUtil.canClick(pos.func_177972_a(side)) || BlockUtil.isPos2(pos.func_177972_a(side), check)) continue;
            airCheck = true;
        }
        return airCheck;
    }

    public static boolean strictPlaceCheck(BlockPos pos, boolean strict, boolean raytrace) {
        if (!strict) {
            return true;
        }
        for (EnumFacing side : BlockUtil.getPlacableFacings(pos, true, raytrace)) {
            if (!BlockUtil.canClick(pos.func_177972_a(side))) continue;
            return true;
        }
        return false;
    }

    public static boolean strictPlaceCheck(BlockPos pos, boolean strict, boolean raytrace, BlockPos check) {
        if (!strict) {
            return true;
        }
        for (EnumFacing side : BlockUtil.getPlacableFacings(pos, true, raytrace)) {
            if (!BlockUtil.canClick(pos.func_177972_a(side)) || BlockUtil.isPos2(pos.func_177972_a(side), check)) continue;
            return true;
        }
        return false;
    }

    public static boolean isPos2(BlockPos pos1, BlockPos pos2) {
        if (pos1 == null || pos2 == null) {
            return false;
        }
        return pos1.field_177962_a == pos2.field_177962_a && pos1.field_177960_b == pos2.field_177960_b && pos1.field_177961_c == pos2.field_177961_c;
    }

    public static boolean canClick(BlockPos pos) {
        return BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c().func_176209_a(BlockUtil.mc.field_71441_e.func_180495_p(pos), false);
    }

    public static void placeCrystal(BlockPos pos, boolean rotate) {
        boolean offhand = BlockUtil.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP;
        BlockPos obsPos = pos.func_177977_b();
        RayTraceResult result = BlockUtil.mc.field_71441_e.func_72933_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + (double)BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() - 0.5, (double)pos.func_177952_p() + 0.5));
        EnumFacing facing = result == null || result.field_178784_b == null ? EnumFacing.UP : result.field_178784_b;
        EnumFacing opposite = facing.func_176734_d();
        Vec3d vec = new Vec3d((Vec3i)obsPos).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()));
        if (rotate) {
            EntityUtil.faceVector(vec);
        }
        BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(obsPos, facing, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        BlockUtil.mc.field_71439_g.func_184609_a(offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
    }

    public static boolean canPlaceCrystal(BlockPos pos, double distance) {
        if (BlockUtil.mc.field_71439_g.func_70011_f((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5) > distance) {
            return false;
        }
        BlockPos obsPos = pos.func_177977_b();
        BlockPos boost = obsPos.func_177984_a();
        BlockPos boost2 = obsPos.func_177981_b(2);
        return (BlockUtil.getBlock(obsPos) == Blocks.field_150357_h || BlockUtil.getBlock(obsPos) == Blocks.field_150343_Z) && BlockUtil.getBlock(boost) == Blocks.field_150350_a && BlockUtil.getBlock(boost2) == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty() && BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }

    public static boolean canPlaceCrystal(BlockPos pos) {
        BlockPos obsPos = pos.func_177977_b();
        BlockPos boost = obsPos.func_177984_a();
        BlockPos boost2 = obsPos.func_177981_b(2);
        return (BlockUtil.getBlock(obsPos) == Blocks.field_150357_h || BlockUtil.getBlock(obsPos) == Blocks.field_150343_Z) && BlockUtil.getBlock(boost) == Blocks.field_150350_a && BlockUtil.getBlock(boost2) == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty() && BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }

    public static List<EnumFacing> getPlacableFacings(BlockPos pos, boolean strictDirection, boolean rayTrace) {
        ArrayList<EnumFacing> validFacings = new ArrayList<EnumFacing>();
        for (EnumFacing side : EnumFacing.values()) {
            if (BlockUtil.getRaytrace(pos, side)) continue;
            BlockUtil.getPlaceFacing(pos, strictDirection, validFacings, side);
        }
        for (EnumFacing side : EnumFacing.values()) {
            if (rayTrace && BlockUtil.getRaytrace(pos, side)) continue;
            BlockUtil.getPlaceFacing(pos, strictDirection, validFacings, side);
        }
        return validFacings;
    }

    public static List<EnumFacing> getTrapPlacableFacings(BlockPos pos, boolean strictDirection, boolean rayTrace) {
        ArrayList<EnumFacing> validFacings = new ArrayList<EnumFacing>();
        for (EnumFacing side : facing) {
            if (BlockUtil.getRaytrace(pos, side)) continue;
            BlockUtil.getPlaceFacing(pos, strictDirection, validFacings, side);
        }
        for (EnumFacing side : facing) {
            if (rayTrace && BlockUtil.getRaytrace(pos, side)) continue;
            BlockUtil.getPlaceFacing(pos, strictDirection, validFacings, side);
        }
        return validFacings;
    }

    private static boolean getRaytrace(BlockPos pos, EnumFacing side) {
        Vec3d testVec = new Vec3d((Vec3i)pos).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(side.func_176730_m()).func_186678_a(0.5));
        RayTraceResult result = BlockUtil.mc.field_71441_e.func_72933_a(BlockUtil.mc.field_71439_g.func_174824_e(1.0f), testVec);
        return result != null && result.field_72313_a != RayTraceResult.Type.MISS;
    }

    private static void getPlaceFacing(BlockPos pos, boolean strictDirection, ArrayList<EnumFacing> validFacings, EnumFacing side) {
        IBlockState blockState;
        BlockPos neighbour = pos.func_177972_a(side);
        if (strictDirection) {
            Vec3d eyePos = BlockUtil.mc.field_71439_g.func_174824_e(1.0f);
            Vec3d blockCenter = new Vec3d((double)neighbour.func_177958_n() + 0.5, (double)neighbour.func_177956_o() + 0.5, (double)neighbour.func_177952_p() + 0.5);
            IBlockState blockState2 = BlockUtil.mc.field_71441_e.func_180495_p(neighbour);
            boolean isFullBox = blockState2.func_177230_c() == Blocks.field_150350_a || blockState2.func_185913_b();
            ArrayList<EnumFacing> validAxis = new ArrayList<EnumFacing>();
            validAxis.addAll(BlockUtil.checkAxis(eyePos.field_72450_a - blockCenter.field_72450_a, EnumFacing.WEST, EnumFacing.EAST, !isFullBox));
            validAxis.addAll(BlockUtil.checkAxis(eyePos.field_72448_b - blockCenter.field_72448_b, EnumFacing.DOWN, EnumFacing.UP, true));
            validAxis.addAll(BlockUtil.checkAxis(eyePos.field_72449_c - blockCenter.field_72449_c, EnumFacing.NORTH, EnumFacing.SOUTH, !isFullBox));
            if (!validAxis.contains(side.func_176734_d())) {
                return;
            }
        }
        if (!(blockState = BlockUtil.mc.field_71441_e.func_180495_p(neighbour)).func_177230_c().func_176209_a(blockState, false) || blockState.func_185904_a().func_76222_j()) {
            return;
        }
        validFacings.add(side);
    }

    public static ArrayList<EnumFacing> checkAxis(double diff, EnumFacing negativeSide, EnumFacing positiveSide, boolean bothIfInRange) {
        ArrayList<EnumFacing> valid = new ArrayList<EnumFacing>();
        if (diff < -0.5) {
            valid.add(negativeSide);
        }
        if (diff > 0.5) {
            valid.add(positiveSide);
        }
        if (bothIfInRange) {
            if (!valid.contains(negativeSide)) {
                valid.add(negativeSide);
            }
            if (!valid.contains(positiveSide)) {
                valid.add(positiveSide);
            }
        }
        return valid;
    }

    public static boolean canPlaceEnum(BlockPos pos, boolean strict, boolean raytrace) {
        if (!BlockUtil.canBlockFacing(pos)) {
            return false;
        }
        return BlockUtil.strictPlaceCheck(pos, strict, raytrace);
    }

    public static boolean checkEntity(BlockPos pos) {
        for (Entity entity : BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
            if (entity.field_70128_L || entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityExpBottle || entity instanceof EntityArrow) continue;
            return true;
        }
        return false;
    }

    public static boolean canPlace(BlockPos pos, double distance, boolean strict, boolean raytrace) {
        if (BlockUtil.mc.field_71439_g.func_70011_f((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5) > distance) {
            return false;
        }
        if (!BlockUtil.canBlockFacing(pos)) {
            return false;
        }
        if (!BlockUtil.canReplace(pos)) {
            return false;
        }
        if (!BlockUtil.strictPlaceCheck(pos, strict, raytrace)) {
            return false;
        }
        return !BlockUtil.checkEntity(pos);
    }

    public static boolean canPlace(BlockPos pos, boolean strict, boolean raytrace) {
        if (BlockUtil.mc.field_71439_g.func_70011_f((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 0.5, (double)pos.func_177952_p() + 0.5) > 6.0) {
            return false;
        }
        if (!BlockUtil.canBlockFacing(pos)) {
            return false;
        }
        if (!BlockUtil.canReplace(pos)) {
            return false;
        }
        if (!BlockUtil.strictPlaceCheck(pos, strict, raytrace)) {
            return false;
        }
        return !BlockUtil.checkEntity(pos);
    }

    public static boolean canPlaceWithoutBase(BlockPos pos, boolean strict, boolean raytrace, boolean base) {
        if (!base && !BlockUtil.canBlockFacing(pos)) {
            return false;
        }
        if (!BlockUtil.canReplace(pos)) {
            return false;
        }
        if (!base && !BlockUtil.strictPlaceCheck(pos, strict, raytrace)) {
            return false;
        }
        return !BlockUtil.checkEntity(pos);
    }

    public static boolean canPlaceWithoutBase(BlockPos pos, boolean strict, boolean raytrace, boolean base, BlockPos check) {
        if (!base && !BlockUtil.canBlockFacing(pos, check)) {
            return false;
        }
        if (!BlockUtil.canReplace(pos)) {
            return false;
        }
        return base || BlockUtil.strictPlaceCheck(pos, strict, raytrace, check);
    }

    public static void placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean strict, boolean raytrace, boolean swing) {
        EnumFacing side = BlockUtil.getFirstFacing(pos, strict, raytrace);
        if (side == null) {
            return;
        }
        BlockPos neighbour = pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        boolean sneaking = false;
        if (!ColorMain.INSTANCE.sneaking && blackList.contains(BlockUtil.getBlock(neighbour))) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            sneaking = true;
        }
        if (rotate) {
            BlockUtil.faceVector(hitVec);
        }
        BlockUtil.rightClickBlock(neighbour, hitVec, hand, opposite, packet, swing);
        if (sneaking) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }

    public static boolean placeBlockBoolean(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean strict, boolean raytrace, boolean swing) {
        EnumFacing side = BlockUtil.getFirstFacing(pos, strict, raytrace);
        if (side == null) {
            return false;
        }
        BlockPos neighbour = pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        boolean sneaking = false;
        if (!ColorMain.INSTANCE.sneaking && blackList.contains(BlockUtil.getBlock(neighbour))) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            sneaking = true;
        }
        if (rotate) {
            BlockUtil.faceVector(hitVec);
        }
        BlockUtil.rightClickBlock(neighbour, hitVec, hand, opposite, packet, swing);
        if (sneaking) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
        }
        return true;
    }

    public static void faceVector(Vec3d vec) {
        float[] rotations = EntityUtil.getLegitRotations(vec);
        EntityUtil.sendPlayerRot(rotations[0], rotations[1], BlockUtil.mc.field_71439_g.field_70122_E);
    }

    public static boolean posHasCrystal(BlockPos pos) {
        for (Entity entity : BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityEnderCrystal) || !new BlockPos(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v).equals((Object)pos)) continue;
            return true;
        }
        return false;
    }

    public static boolean canReplace(BlockPos pos) {
        if (pos == null) {
            return false;
        }
        return BlockUtil.getState(pos).func_185904_a().func_76222_j() || BlockUtil.isAir(pos);
    }

    public static boolean canReplace(Vec3d vec3d) {
        if (vec3d == null) {
            return false;
        }
        BlockPos pos = new BlockPos(vec3d);
        return BlockUtil.getState(pos).func_185904_a().func_76222_j() || BlockUtil.isAir(pos);
    }

    public static boolean isBlockUnSolid(Block block) {
        return unSolidBlocks.contains(block);
    }

    public static boolean canOpen(Block block) {
        return blackList.contains(block);
    }

    public static boolean isAir(Block block) {
        return airBlocks.contains(block);
    }

    public static boolean isAirBlock(Block block) {
        return block == Blocks.field_150350_a;
    }

    public static double blockDistance2d(double blockposx, double blockposz, Entity owo) {
        double deltaX = owo.field_70165_t - blockposx;
        double deltaZ = owo.field_70161_v - blockposz;
        return Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
    }

    public static EnumFacing getRayTraceFacing(BlockPos pos) {
        RayTraceResult result = BlockUtil.mc.field_71441_e.func_72933_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + (double)BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() - 0.5, (double)pos.func_177952_p() + 0.5));
        if (result == null || result.field_178784_b == null) {
            return EnumFacing.UP;
        }
        return result.field_178784_b;
    }

    public static EnumFacing getRayTraceFacing(BlockPos pos, EnumFacing facing) {
        RayTraceResult result = BlockUtil.mc.field_71441_e.func_72933_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + (double)BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() - 0.5, (double)pos.func_177952_p() + 0.5));
        if (result == null || result.field_178784_b == null) {
            return facing;
        }
        return result.field_178784_b;
    }

    public static IBlockState getState(BlockPos pos) {
        return BlockUtil.mc.field_71441_e.func_180495_p(pos);
    }

    public static float[] calcAngle(Vec3d from, Vec3d to) {
        double difX = to.field_72450_a - from.field_72450_a;
        double difY = (to.field_72448_b - from.field_72448_b) * -1.0;
        double difZ = to.field_72449_c - from.field_72449_c;
        double dist = MathHelper.func_76133_a((double)(difX * difX + difZ * difZ));
        return new float[]{(float)MathHelper.func_76138_g((double)(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0)), (float)MathHelper.func_76138_g((double)Math.toDegrees(Math.atan2(difY, dist)))};
    }

    public static CPacketPlayer.Rotation getFaceVectorPacket(Vec3d vec, Boolean roundAngles) {
        float[] rotations = BlockUtil.getNeededRotations2(vec);
        CPacketPlayer.Rotation e = new CPacketPlayer.Rotation(rotations[0], roundAngles != false ? (float)MathHelper.func_180184_b((int)((int)rotations[1]), (int)360) : rotations[1], BlockUtil.mc.field_71439_g.field_70122_E);
        BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)e);
        return e;
    }

    public static float[] calcAngleNoY(Vec3d from, Vec3d to) {
        double difX = to.field_72450_a - from.field_72450_a;
        double difZ = to.field_72449_c - from.field_72449_c;
        return new float[]{(float)MathHelper.func_76138_g((double)(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0))};
    }

    public static BlockPos[] toBlockPos(Vec3d[] vec3ds) {
        BlockPos[] list = new BlockPos[vec3ds.length];
        for (int i = 0; i < vec3ds.length; ++i) {
            list[i] = new BlockPos(vec3ds[i]);
        }
        return list;
    }

    public static boolean hasNeighbour(BlockPos blockPos) {
        boolean canPlace = false;
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbour = blockPos.func_177972_a(side);
            if (!BlockUtil.mc.field_71441_e.func_180495_p(neighbour).func_185904_a().func_76222_j()) continue;
            canPlace = true;
        }
        return canPlace;
    }

    public static boolean canPlaceBlock(BlockPos pos) {
        return (BlockUtil.getBlock(pos) == Blocks.field_150350_a || BlockUtil.getBlock(pos) instanceof BlockLiquid) && BlockUtil.hasNeighbour(pos) && !blackList.contains(BlockUtil.getBlock(pos));
    }

    public static boolean canPlaceBlockFuture(BlockPos pos) {
        return (BlockUtil.getBlock(pos) == Blocks.field_150350_a || BlockUtil.getBlock(pos) instanceof BlockLiquid) && !blackList.contains(BlockUtil.getBlock(pos));
    }

    public static void rightClickBlock(BlockPos pos, EnumFacing facing, boolean packet) {
        Vec3d hitVec = new Vec3d((Vec3i)pos).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(facing.func_176730_m()).func_186678_a(0.5));
        if (packet) {
            BlockUtil.rightClickBlock(pos, hitVec, EnumHand.MAIN_HAND, facing);
        } else {
            BlockUtil.mc.field_71442_b.func_187099_a(BlockUtil.mc.field_71439_g, BlockUtil.mc.field_71441_e, pos, facing, hitVec, EnumHand.MAIN_HAND);
            BlockUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        }
    }

    public static void rightClickBlock(BlockPos pos, EnumFacing facing, Vec3d hVec, boolean packet) {
        Vec3d hitVec = new Vec3d((Vec3i)pos).func_178787_e(hVec).func_178787_e(new Vec3d(facing.func_176730_m()).func_186678_a(0.5));
        if (packet) {
            BlockUtil.rightClickBlock(pos, hitVec, EnumHand.MAIN_HAND, facing);
        } else {
            BlockUtil.mc.field_71442_b.func_187099_a(BlockUtil.mc.field_71439_g, BlockUtil.mc.field_71441_e, pos, facing, hitVec, EnumHand.MAIN_HAND);
            BlockUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        }
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction) {
        float f = (float)(vec.field_72450_a - (double)pos.func_177958_n());
        float f1 = (float)(vec.field_72448_b - (double)pos.func_177956_o());
        float f2 = (float)(vec.field_72449_c - (double)pos.func_177952_p());
        BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
        BlockUtil.mc.field_71467_ac = 4;
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet, boolean swing) {
        if (packet) {
            float f = (float)(vec.field_72450_a - (double)pos.func_177958_n());
            float f1 = (float)(vec.field_72448_b - (double)pos.func_177956_o());
            float f2 = (float)(vec.field_72449_c - (double)pos.func_177952_p());
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
        } else {
            BlockUtil.mc.field_71442_b.func_187099_a(BlockUtil.mc.field_71439_g, BlockUtil.mc.field_71441_e, pos, direction, vec, hand);
        }
        if (swing) {
            BlockUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        }
        BlockUtil.mc.field_71467_ac = 4;
    }

    public static int isPositionPlaceable(BlockPos pos, boolean rayTrace) {
        return BlockUtil.isPositionPlaceable(pos, rayTrace, true);
    }

    public static EnumFacing getFirstFacing(BlockPos pos, boolean strict, boolean raytrace) {
        if (!strict) {
            Iterator<EnumFacing> iterator = BlockUtil.getPossibleSides(pos).iterator();
            if (iterator.hasNext()) {
                return iterator.next();
            }
        } else {
            for (EnumFacing side : BlockUtil.getPlacableFacings(pos, true, raytrace)) {
                if (!BlockUtil.canClick(pos.func_177972_a(side))) continue;
                return side;
            }
        }
        return null;
    }

    public static EnumFacing getTrapFirstFacing(BlockPos pos, boolean strict, boolean raytrace) {
        if (!strict) {
            Iterator<EnumFacing> iterator = BlockUtil.getTrapPossibleSides(pos).iterator();
            if (iterator.hasNext()) {
                return iterator.next();
            }
        } else {
            for (EnumFacing side : BlockUtil.getTrapPlacableFacings(pos, true, raytrace)) {
                if (!BlockUtil.canClick(pos.func_177972_a(side))) continue;
                return side;
            }
        }
        return null;
    }

    public static int isPositionPlaceable(BlockPos pos, boolean rayTrace, boolean entityCheck) {
        Block block = BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        if (!(block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow)) {
            return 0;
        }
        if (!BlockUtil.rayTracePlaceCheck(pos, rayTrace, 0.0f)) {
            return -1;
        }
        if (entityCheck) {
            for (Entity entity : BlockUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
                if (entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
                return 1;
            }
        }
        for (EnumFacing side : BlockUtil.getPossibleSides(pos)) {
            if (!BlockUtil.canBeClicked(pos.func_177972_a(side))) continue;
            return 3;
        }
        return 2;
    }

    public static List<EnumFacing> getPossibleSides(BlockPos pos) {
        ArrayList<EnumFacing> facings = new ArrayList<EnumFacing>();
        if (BlockUtil.mc.field_71441_e == null || pos == null) {
            return facings;
        }
        for (EnumFacing side : EnumFacing.field_82609_l) {
            BlockPos neighbour = pos.func_177972_a(side);
            IBlockState blockState = BlockUtil.mc.field_71441_e.func_180495_p(neighbour);
            if (!blockState.func_177230_c().func_176209_a(blockState, false) || blockState.func_185904_a().func_76222_j() || !BlockUtil.canBeClicked(neighbour)) continue;
            facings.add(side);
        }
        return facings;
    }

    public static List<EnumFacing> getTrapPossibleSides(BlockPos pos) {
        ArrayList<EnumFacing> facings = new ArrayList<EnumFacing>();
        if (BlockUtil.mc.field_71441_e == null || pos == null) {
            return facings;
        }
        for (EnumFacing side : facing) {
            BlockPos neighbour = pos.func_177972_a(side);
            IBlockState blockState = BlockUtil.mc.field_71441_e.func_180495_p(neighbour);
            if (blockState == null || !blockState.func_177230_c().func_176209_a(blockState, false) || blockState.func_185904_a().func_76222_j()) continue;
            facings.add(side);
        }
        return facings;
    }

    public static boolean rayTracePlaceCheck(BlockPos pos, boolean shouldCheck, float height) {
        return !shouldCheck || BlockUtil.mc.field_71441_e.func_147447_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + (double)BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n(), (double)((float)pos.func_177956_o() + height), (double)pos.func_177952_p()), false, true, false) == null;
    }

    public static boolean rayTracePlaceCheck(BlockPos pos, boolean shouldCheck) {
        return BlockUtil.rayTracePlaceCheck(pos, shouldCheck, 1.0f);
    }

    public static boolean rayTracePlaceCheck(BlockPos pos) {
        return BlockUtil.rayTracePlaceCheck(pos, true);
    }

    public static Block getBlock(BlockPos pos) {
        return BlockUtil.getState(pos).func_177230_c();
    }

    public static Block getBlock(double x, double y, double z) {
        return BlockUtil.mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
    }

    public static boolean canBeClicked(BlockPos pos) {
        return BlockUtil.getBlock(pos).func_176209_a(BlockUtil.getState(pos), false);
    }

    public static boolean canBeClicked(Vec3d vec3d) {
        return BlockUtil.getBlock(new BlockPos(vec3d)).func_176209_a(BlockUtil.getState(new BlockPos(vec3d)), false);
    }

    public static void faceVectorPacketInstant(Vec3d vec, Boolean roundAngles) {
        float[] rotations = BlockUtil.getNeededRotations2(vec);
        BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(rotations[0], roundAngles != false ? (float)MathHelper.func_180184_b((int)((int)rotations[1]), (int)360) : rotations[1], BlockUtil.mc.field_71439_g.field_70122_E));
    }

    public static void faceVectorPacketInstant2(Vec3d vec) {
        float[] rotations = BlockUtil.getLegitRotations(vec);
        Wrapper.getPlayer().field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], Wrapper.getPlayer().field_70122_E));
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = BlockUtil.getEyesPos();
        double diffX = vec.field_72450_a - eyesPos.field_72450_a;
        double diffY = vec.field_72448_b - eyesPos.field_72448_b;
        double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{Wrapper.getPlayer().field_70177_z + MathHelper.func_76142_g((float)(yaw - Wrapper.getPlayer().field_70177_z)), Wrapper.getPlayer().field_70125_A + MathHelper.func_76142_g((float)(pitch - Wrapper.getPlayer().field_70125_A))};
    }

    private static float[] getNeededRotations2(Vec3d vec) {
        Vec3d eyesPos = BlockUtil.getEyesPos();
        double diffX = vec.field_72450_a - eyesPos.field_72450_a;
        double diffY = vec.field_72448_b - eyesPos.field_72448_b;
        double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{BlockUtil.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g((float)(yaw - BlockUtil.mc.field_71439_g.field_70177_z)), BlockUtil.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g((float)(pitch - BlockUtil.mc.field_71439_g.field_70125_A))};
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + (double)BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v);
    }

    public static double blockDistance(double blockposx, double blockposy, double blockposz, Entity owo) {
        double deltaX = owo.field_70165_t - blockposx;
        double deltaY = owo.field_70163_u - blockposy;
        double deltaZ = owo.field_70161_v - blockposz;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }

    public static List<BlockPos> getCircle(BlockPos loc, int y, float r, boolean hollow) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = loc.func_177958_n();
        int cz = loc.func_177952_p();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z);
                if (dist < (double)(r * r) && (!hollow || dist >= (double)((r - 1.0f) * (r - 1.0f)))) {
                    BlockPos l = new BlockPos(x, y, z);
                    circleblocks.add(l);
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    public static EnumFacing getPlaceableSide(BlockPos pos) {
        for (EnumFacing side : EnumFacing.values()) {
            IBlockState blockState;
            BlockPos neighbour = pos.func_177972_a(side);
            if (!BlockUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c().func_176209_a(BlockUtil.mc.field_71441_e.func_180495_p(neighbour), false) || (blockState = BlockUtil.mc.field_71441_e.func_180495_p(neighbour)).func_185904_a().func_76222_j()) continue;
            return side;
        }
        return null;
    }

    public static EnumFacing getPlaceableSideExlude(BlockPos pos, ArrayList<EnumFacing> excluding) {
        for (EnumFacing side : EnumFacing.values()) {
            IBlockState blockState;
            BlockPos neighbour;
            if (excluding.contains(side) || !BlockUtil.mc.field_71441_e.func_180495_p(neighbour = pos.func_177972_a(side)).func_177230_c().func_176209_a(BlockUtil.mc.field_71441_e.func_180495_p(neighbour), false) || (blockState = BlockUtil.mc.field_71441_e.func_180495_p(neighbour)).func_185904_a().func_76222_j()) continue;
            return side;
        }
        return null;
    }

    public static Vec3d getCenterOfBlock(double playerX, double playerY, double playerZ) {
        double newX = Math.floor(playerX) + 0.5;
        double newY = Math.floor(playerY);
        double newZ = Math.floor(playerZ) + 0.5;
        return new Vec3d(newX, newY, newZ);
    }
}
