/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.init.MobEffects
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketClickWindow
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package com.lemonclient.api.util.misc;

import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.world.BlockUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class CrystalUtil {
    public static Minecraft mc = Minecraft.func_71410_x();
    private static final List<Block> valid = Arrays.asList(Blocks.field_150343_Z, Blocks.field_150357_h, Blocks.field_150477_bB, Blocks.field_150467_bQ);

    public static void placeCrystal(BlockPos pos, boolean rotate) {
        boolean offhand = CrystalUtil.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP;
        BlockPos obsPos = pos.func_177977_b();
        RayTraceResult result = CrystalUtil.mc.field_71441_e.func_72933_a(new Vec3d(CrystalUtil.mc.field_71439_g.field_70165_t, CrystalUtil.mc.field_71439_g.field_70163_u + (double)CrystalUtil.mc.field_71439_g.func_70047_e(), CrystalUtil.mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() - 0.5, (double)pos.func_177952_p() + 0.5));
        EnumFacing facing = result == null || result.field_178784_b == null ? EnumFacing.UP : result.field_178784_b;
        EnumFacing opposite = facing.func_176734_d();
        Vec3d vec = new Vec3d((Vec3i)obsPos).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()));
        if (rotate) {
            BlockUtil.faceVector(vec);
        }
        CrystalUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(obsPos, facing, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        CrystalUtil.mc.field_71439_g.func_184609_a(offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
    }

    public static boolean placeCrystal(BlockPos pos, EnumHand hand, boolean packet, boolean rotate, boolean swing) {
        EnumFacing facing = EnumFacing.UP;
        EnumFacing opposite = facing.func_176734_d();
        Vec3d vec = new Vec3d((Vec3i)pos).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()));
        if (rotate) {
            BlockUtil.faceVector(vec);
        }
        if (packet) {
            CrystalUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0f, 0.0f, 0.0f));
        } else {
            CrystalUtil.mc.field_71442_b.func_187099_a(CrystalUtil.mc.field_71439_g, CrystalUtil.mc.field_71441_e, pos, facing, vec, hand);
        }
        if (swing) {
            CrystalUtil.mc.field_71439_g.func_184609_a(hand);
        }
        return true;
    }

    public static boolean isNull(RayTraceResult result, Entity entity) {
        return result == null || result.field_178784_b == null || result.field_72308_g == entity;
    }

    public static boolean calculateRaytrace(Entity entity) {
        Vec3d vec3d;
        if (entity == null) {
            return true;
        }
        Vec3d vec = PlayerUtil.getEyeVec();
        RayTraceResult result = CrystalUtil.mc.field_71441_e.func_72933_a(vec, vec3d = entity.func_174791_d());
        if (CrystalUtil.isNull(result, entity)) {
            return true;
        }
        double x = entity.field_70121_D.field_72336_d - entity.field_70121_D.field_72340_a;
        double y = entity.field_70121_D.field_72337_e - entity.field_70121_D.field_72338_b;
        double z = entity.field_70121_D.field_72334_f - entity.field_70121_D.field_72339_c;
        for (double addX = -x; addX <= x; addX += x) {
            for (double addY = 0.0; addY <= y; addY += y) {
                for (double addZ = -z; addZ <= z; addZ += z) {
                    result = CrystalUtil.mc.field_71441_e.func_72933_a(vec, vec3d.func_72441_c(addX, addY, addZ));
                    if (!CrystalUtil.isNull(result, entity)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isNull(RayTraceResult result, BlockPos pos) {
        if (result == null || result.func_178782_a() == pos) {
            return true;
        }
        if (result.field_72313_a == RayTraceResult.Type.ENTITY) {
            double distance = CrystalUtil.mc.field_71439_g.func_70032_d(result.field_72308_g);
            return distance <= PlayerUtil.getDistanceI(pos);
        }
        return false;
    }

    public static boolean isNull(RayTraceResult result, Vec3d vec3d) {
        BlockPos pos = new BlockPos(vec3d);
        return CrystalUtil.isNull(result, pos);
    }

    public static boolean calculateRaytrace(BlockPos pos) {
        Vec3d vec3d;
        Vec3d vec = PlayerUtil.getEyeVec();
        RayTraceResult result = CrystalUtil.mc.field_71441_e.func_72933_a(vec, vec3d = new Vec3d((Vec3i)pos));
        if (CrystalUtil.isNull(result, pos)) {
            return true;
        }
        double x = 0.5;
        double y = 0.5;
        double z = 0.5;
        for (double addX = 0.0; addX <= 1.0; addX += x) {
            for (double addY = 0.0; addY <= 1.0; addY += y) {
                for (double addZ = 0.0; addZ <= 1.0; addZ += z) {
                    result = CrystalUtil.mc.field_71441_e.func_72933_a(vec, vec3d.func_72441_c(addX, addY, addZ));
                    if (!CrystalUtil.isNull(result, pos)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean calculateRaytrace(EntityPlayer player, Vec3d vec3d) {
        Vec3d vec = new Vec3d(player.field_70165_t, player.field_70163_u + (double)player.func_70047_e(), player.field_70161_v);
        RayTraceResult result = CrystalUtil.mc.field_71441_e.func_72933_a(vec, vec3d);
        if (CrystalUtil.isNull(result, vec3d)) {
            return true;
        }
        double x = 0.5;
        double y = 0.5;
        double z = 0.5;
        for (double addX = 0.0; addX <= 1.0; addX += x) {
            for (double addY = 0.0; addY <= 1.0; addY += y) {
                for (double addZ = 0.0; addZ <= 1.0; addZ += z) {
                    result = CrystalUtil.mc.field_71441_e.func_72933_a(vec, vec3d.func_72441_c(addX, addY, addZ));
                    if (!CrystalUtil.isNull(result, vec3d)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public static RayTraceResult rayTraceBlocks(Vec3d start, Vec3d end) {
        return CrystalUtil.rayTraceBlocks(start, end, false, false, false);
    }

    public static RayTraceResult rayTraceBlocks(Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUnCollidableBlock) {
        if (!(Double.isNaN(vec31.field_72450_a) || Double.isNaN(vec31.field_72448_b) || Double.isNaN(vec31.field_72449_c))) {
            if (!(Double.isNaN(vec32.field_72450_a) || Double.isNaN(vec32.field_72448_b) || Double.isNaN(vec32.field_72449_c))) {
                int j1;
                int i1;
                int i = MathHelper.func_76128_c((double)vec32.field_72450_a);
                int j = MathHelper.func_76128_c((double)vec32.field_72448_b);
                int k = MathHelper.func_76128_c((double)vec32.field_72449_c);
                int l = MathHelper.func_76128_c((double)vec31.field_72450_a);
                BlockPos blockpos = new BlockPos(l, i1 = MathHelper.func_76128_c((double)vec31.field_72448_b), j1 = MathHelper.func_76128_c((double)vec31.field_72449_c));
                IBlockState iblockstate = CrystalUtil.mc.field_71441_e.func_180495_p(blockpos);
                Block block = iblockstate.func_177230_c();
                if (!valid.contains(block)) {
                    block = Blocks.field_150350_a;
                    iblockstate = Blocks.field_150350_a.func_176194_O().func_177621_b();
                }
                if ((!ignoreBlockWithoutBoundingBox || iblockstate.func_185890_d((IBlockAccess)CrystalUtil.mc.field_71441_e, blockpos) != Block.field_185506_k) && block.func_176209_a(iblockstate, stopOnLiquid)) {
                    return iblockstate.func_185910_a((World)CrystalUtil.mc.field_71441_e, blockpos, vec31, vec32);
                }
                RayTraceResult raytraceresult2 = null;
                int k1 = 200;
                while (k1-- >= 0) {
                    EnumFacing enumfacing;
                    if (Double.isNaN(vec31.field_72450_a) || Double.isNaN(vec31.field_72448_b) || Double.isNaN(vec31.field_72449_c)) {
                        return null;
                    }
                    if (l == i && i1 == j && j1 == k) {
                        return returnLastUnCollidableBlock ? raytraceresult2 : null;
                    }
                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0;
                    double d1 = 999.0;
                    double d2 = 999.0;
                    if (i > l) {
                        d0 = (double)l + 1.0;
                    } else if (i < l) {
                        d0 = (double)l + 0.0;
                    } else {
                        flag2 = false;
                    }
                    if (j > i1) {
                        d1 = (double)i1 + 1.0;
                    } else if (j < i1) {
                        d1 = (double)i1 + 0.0;
                    } else {
                        flag = false;
                    }
                    if (k > j1) {
                        d2 = (double)j1 + 1.0;
                    } else if (k < j1) {
                        d2 = (double)j1 + 0.0;
                    } else {
                        flag1 = false;
                    }
                    double d3 = 999.0;
                    double d4 = 999.0;
                    double d5 = 999.0;
                    double d6 = vec32.field_72450_a - vec31.field_72450_a;
                    double d7 = vec32.field_72448_b - vec31.field_72448_b;
                    double d8 = vec32.field_72449_c - vec31.field_72449_c;
                    if (flag2) {
                        d3 = (d0 - vec31.field_72450_a) / d6;
                    }
                    if (flag) {
                        d4 = (d1 - vec31.field_72448_b) / d7;
                    }
                    if (flag1) {
                        d5 = (d2 - vec31.field_72449_c) / d8;
                    }
                    if (d3 == -0.0) {
                        d3 = -1.0E-4;
                    }
                    if (d4 == -0.0) {
                        d4 = -1.0E-4;
                    }
                    if (d5 == -0.0) {
                        d5 = -1.0E-4;
                    }
                    if (d3 < d4 && d3 < d5) {
                        enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                        vec31 = new Vec3d(d0, vec31.field_72448_b + d7 * d3, vec31.field_72449_c + d8 * d3);
                    } else if (d4 < d5) {
                        enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        vec31 = new Vec3d(vec31.field_72450_a + d6 * d4, d1, vec31.field_72449_c + d8 * d4);
                    } else {
                        enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vec31 = new Vec3d(vec31.field_72450_a + d6 * d5, vec31.field_72448_b + d7 * d5, d2);
                    }
                    l = MathHelper.func_76128_c((double)vec31.field_72450_a) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.func_76128_c((double)vec31.field_72448_b) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.func_76128_c((double)vec31.field_72449_c) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    blockpos = new BlockPos(l, i1, j1);
                    IBlockState iblockstate1 = CrystalUtil.mc.field_71441_e.func_180495_p(blockpos);
                    Block block1 = iblockstate1.func_177230_c();
                    if (!valid.contains(block1)) {
                        block1 = Blocks.field_150350_a;
                        iblockstate1 = Blocks.field_150350_a.func_176194_O().func_177621_b();
                    }
                    if (ignoreBlockWithoutBoundingBox && iblockstate1.func_185904_a() != Material.field_151567_E && iblockstate1.func_185890_d((IBlockAccess)CrystalUtil.mc.field_71441_e, blockpos) == Block.field_185506_k) continue;
                    if (block1.func_176209_a(iblockstate1, stopOnLiquid)) {
                        return iblockstate1.func_185910_a((World)CrystalUtil.mc.field_71441_e, blockpos, vec31, vec32);
                    }
                    raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
                }
                return returnLastUnCollidableBlock ? raytraceresult2 : null;
            }
            return null;
        }
        return null;
    }

    public static boolean canPlaceCrystal(BlockPos pos) {
        return BlockUtil.getBlock(pos.func_177982_a(0, 1, 0)) == Blocks.field_150350_a && BlockUtil.getBlock(pos.func_177982_a(0, 2, 0)) == Blocks.field_150350_a;
    }

    public static List<BlockPos> getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circleBlocks = new ArrayList<BlockPos>();
        int cx = pos.func_177958_n();
        int cy = pos.func_177956_o();
        int cz = pos.func_177952_p();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                int y = sphere ? cy - (int)r : cy;
                while (true) {
                    float f2;
                    float f = y;
                    float f3 = f2 = sphere ? (float)cy + r : (float)(cy + h);
                    if (!(f < f2)) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (!(!(dist < (double)(r * r)) || hollow && dist < (double)((r - 1.0f) * (r - 1.0f)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleBlocks.add(l);
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return circleBlocks;
    }

    public static boolean canPlaceCrystal(BlockPos blockPos, boolean specialEntityCheck, boolean onepointThirteen) {
        BlockPos boost = blockPos.func_177982_a(0, 1, 0);
        BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
        try {
            if (!onepointThirteen) {
                if (CrystalUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150357_h && CrystalUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150343_Z) {
                    return false;
                }
                if (CrystalUtil.mc.field_71441_e.func_180495_p(boost).func_177230_c() != Blocks.field_150350_a || CrystalUtil.mc.field_71441_e.func_180495_p(boost2).func_177230_c() != Blocks.field_150350_a) {
                    return false;
                }
                if (!specialEntityCheck) {
                    return CrystalUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty() && CrystalUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
                }
                for (Entity entity : CrystalUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
                for (Entity entity : CrystalUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
            } else {
                if (CrystalUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150357_h && CrystalUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150343_Z) {
                    return false;
                }
                if (CrystalUtil.mc.field_71441_e.func_180495_p(boost).func_177230_c() != Blocks.field_150350_a) {
                    return false;
                }
                if (!specialEntityCheck) {
                    return CrystalUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost)).isEmpty();
                }
                for (Entity entity : CrystalUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void breakCrystal(BlockPos pos, boolean swing) {
        if (pos == null) {
            return;
        }
        for (Entity entity : CrystalUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            CrystalUtil.breakCrystal(entity, swing);
            break;
        }
    }

    public static void breakCrystalPacket(BlockPos pos, boolean swing) {
        if (pos == null) {
            return;
        }
        for (Entity entity : CrystalUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            CrystalUtil.breakCrystalPacket(entity, swing);
            break;
        }
    }

    public static void breakCrystal(Entity crystal, boolean swing) {
        CrystalUtil.mc.field_71442_b.func_78764_a((EntityPlayer)CrystalUtil.mc.field_71439_g, crystal);
        if (swing) {
            CrystalUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        }
    }

    public static void breakCrystalPacket(Entity crystal, boolean swing) {
        CrystalUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity(crystal));
        if (swing) {
            CrystalUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        }
    }

    public static void breakCrystal(Entity crystal, boolean packet, boolean swing, boolean packetSwitch, boolean switchBack, boolean antiWeakness, boolean weaknessBypass) {
        int slot = -1;
        if (antiWeakness && CrystalUtil.mc.field_71439_g.func_70644_a(MobEffects.field_76437_t) && (!CrystalUtil.mc.field_71439_g.func_70644_a(MobEffects.field_76420_g) || Objects.requireNonNull(CrystalUtil.mc.field_71439_g.func_70660_b(MobEffects.field_76420_g)).func_76458_c() < 1)) {
            for (int b = 0; b < (weaknessBypass ? 36 : 9); ++b) {
                ItemStack stack = CrystalUtil.mc.field_71439_g.field_71071_by.func_70301_a(b);
                if (stack == ItemStack.field_190927_a) continue;
                if (stack.func_77973_b() instanceof ItemSword) {
                    slot = b;
                    break;
                }
                if (!(stack.func_77973_b() instanceof ItemTool)) continue;
                slot = b;
            }
        }
        CrystalUtil.switchTo(slot, weaknessBypass, packetSwitch, switchBack, () -> {
            if (packet) {
                CrystalUtil.breakCrystalPacket(crystal, swing);
            } else {
                CrystalUtil.breakCrystal(crystal, swing);
            }
        });
    }

    public static void windowClick(int windowId, int slotId, int mouseButton, ClickType type, EntityPlayer player) {
        short short1 = player.field_71070_bA.func_75136_a(player.field_71071_by);
        ItemStack itemStack = player.field_71070_bA.func_184996_a(slotId, mouseButton, type, player);
        CrystalUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketClickWindow(windowId, slotId, mouseButton, type, itemStack, short1));
        CrystalUtil.mc.field_71442_b.func_78765_e();
        CrystalUtil.mc.field_71439_g.field_71070_bA.func_75142_b();
    }

    private static void switchTo(int slot, boolean bypass, boolean packetSwitch, boolean switchBack, Runnable runnable) {
        int oldslot = CrystalUtil.mc.field_71439_g.field_71071_by.field_70461_c;
        if (slot < 0 || slot == oldslot) {
            runnable.run();
            return;
        }
        if (bypass) {
            if (slot < 9) {
                slot += 36;
            }
            int id = CrystalUtil.mc.field_71439_g.field_71069_bz.field_75152_c;
            CrystalUtil.windowClick(id, slot, oldslot, ClickType.SWAP, (EntityPlayer)CrystalUtil.mc.field_71439_g);
            CrystalUtil.mc.field_71439_g.field_71070_bA.func_75142_b();
            CrystalUtil.windowClick(id, slot, oldslot, ClickType.SWAP, (EntityPlayer)CrystalUtil.mc.field_71439_g);
        } else if (slot < 9) {
            if (!switchBack) {
                packetSwitch = false;
            }
            if (packetSwitch) {
                InventoryUtil.packetSwitch(slot);
            } else {
                InventoryUtil.switchSlot(slot);
            }
            runnable.run();
            if (switchBack) {
                if (packetSwitch) {
                    InventoryUtil.packetSwitch(oldslot);
                } else {
                    InventoryUtil.switchSlot(oldslot);
                }
            }
        }
    }
}
