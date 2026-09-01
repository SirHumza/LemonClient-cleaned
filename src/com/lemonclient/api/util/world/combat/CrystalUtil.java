/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.api.util.world.combat;

import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.world.EntityUtil;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class CrystalUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();

    public static boolean canPlaceCrystal(BlockPos blockPos, boolean newPlacement) {
        if (CrystalUtil.notValidBlock(CrystalUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c())) {
            return false;
        }
        BlockPos posUp = blockPos.func_177984_a();
        if (newPlacement ? !CrystalUtil.mc.field_71441_e.func_175623_d(posUp) : CrystalUtil.notValidMaterial(CrystalUtil.mc.field_71441_e.func_180495_p(posUp).func_185904_a()) || CrystalUtil.notValidMaterial(CrystalUtil.mc.field_71441_e.func_180495_p(posUp.func_177984_a()).func_185904_a())) {
            return false;
        }
        AxisAlignedBB box = new AxisAlignedBB((double)posUp.func_177958_n(), (double)posUp.func_177956_o(), (double)posUp.func_177952_p(), (double)posUp.func_177958_n() + 1.0, (double)posUp.func_177956_o() + 2.0, (double)posUp.func_177952_p() + 1.0);
        return CrystalUtil.mc.field_71441_e.func_175647_a(Entity.class, box, Entity::func_70089_S).isEmpty();
    }

    public static boolean canPlaceCrystalExcludingCrystals(BlockPos blockPos, boolean newPlacement) {
        if (CrystalUtil.notValidBlock(CrystalUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c())) {
            return false;
        }
        BlockPos posUp = blockPos.func_177984_a();
        if (newPlacement ? !CrystalUtil.mc.field_71441_e.func_175623_d(posUp) : CrystalUtil.notValidMaterial(CrystalUtil.mc.field_71441_e.func_180495_p(posUp).func_185904_a()) || CrystalUtil.notValidMaterial(CrystalUtil.mc.field_71441_e.func_180495_p(posUp.func_177984_a()).func_185904_a())) {
            return false;
        }
        AxisAlignedBB box = new AxisAlignedBB((double)posUp.func_177958_n(), (double)posUp.func_177956_o(), (double)posUp.func_177952_p(), (double)posUp.func_177958_n() + 1.0, (double)posUp.func_177956_o() + 2.0, (double)posUp.func_177952_p() + 1.0);
        return CrystalUtil.mc.field_71441_e.func_175647_a(Entity.class, box, entity -> !entity.field_70128_L && !(entity instanceof EntityEnderCrystal)).isEmpty();
    }

    public static boolean notValidBlock(Block block) {
        return block != Blocks.field_150357_h && block != Blocks.field_150343_Z;
    }

    public static boolean notValidMaterial(Material material) {
        return material.func_76224_d() || !material.func_76222_j();
    }

    public static List<BlockPos> findCrystalBlocks(float placeRange, boolean mode) {
        return EntityUtil.getSphere(PlayerUtil.getPlayerPos(), Double.valueOf(placeRange), Double.valueOf(placeRange), false, true, 0).stream().filter(pos -> CrystalUtil.canPlaceCrystal(pos, mode)).collect(Collectors.toList());
    }

    public static List<BlockPos> findCrystalBlocksExcludingCrystals(float placeRange, boolean mode) {
        return EntityUtil.getSphere(PlayerUtil.getPlayerPos(), Double.valueOf(placeRange), Double.valueOf(placeRange), false, true, 0).stream().filter(pos -> CrystalUtil.canPlaceCrystalExcludingCrystals(pos, mode)).collect(Collectors.toList());
    }

    public static void breakCrystal(BlockPos pos, boolean swing) {
        for (Entity entity : CrystalUtil.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            CrystalUtil.mc.field_71442_b.func_78764_a((EntityPlayer)CrystalUtil.mc.field_71439_g, entity);
            if (!swing) break;
            CrystalUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
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
}
