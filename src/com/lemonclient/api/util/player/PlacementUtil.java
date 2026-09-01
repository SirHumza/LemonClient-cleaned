/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.item.Item
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.util.EnumActionResult
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package com.lemonclient.api.util.player;

import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.api.util.world.BlockUtil;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class PlacementUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();
    private static int placementConnections = 0;
    private static boolean isSneaking = false;

    public static void onEnable() {
        ++placementConnections;
    }

    public static void onDisable() {
        if (--placementConnections == 0 && isSneaking) {
            PlacementUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)PlacementUtil.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
            isSneaking = false;
        }
    }

    public static void stopSneaking() {
        if (isSneaking) {
            isSneaking = false;
            PlacementUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)PlacementUtil.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }

    public static boolean placeBlock(BlockPos blockPos, EnumHand hand, boolean rotate, Class<? extends Block> blockToPlace) {
        int oldSlot = PlacementUtil.mc.field_71439_g.field_71071_by.field_70461_c;
        int newSlot = InventoryUtil.findFirstBlockSlot(blockToPlace, 0, 8);
        if (newSlot == -1) {
            return false;
        }
        PlacementUtil.mc.field_71439_g.field_71071_by.field_70461_c = newSlot;
        boolean output = PlacementUtil.place(blockPos, hand, rotate);
        PlacementUtil.mc.field_71439_g.field_71071_by.field_70461_c = oldSlot;
        return output;
    }

    public static boolean placeItem(BlockPos blockPos, EnumHand hand, boolean rotate, Class<? extends Item> itemToPlace) {
        int oldSlot = PlacementUtil.mc.field_71439_g.field_71071_by.field_70461_c;
        int newSlot = InventoryUtil.findFirstItemSlot(itemToPlace, 0, 8);
        if (newSlot == -1) {
            return false;
        }
        PlacementUtil.mc.field_71439_g.field_71071_by.field_70461_c = newSlot;
        boolean output = PlacementUtil.place(blockPos, hand, rotate);
        PlacementUtil.mc.field_71439_g.field_71071_by.field_70461_c = oldSlot;
        return output;
    }

    public static boolean place(BlockPos blockPos, EnumHand hand, boolean rotate) {
        return PlacementUtil.placeBlock(blockPos, hand, rotate, true, null);
    }

    public static boolean place(BlockPos blockPos, EnumHand hand, boolean rotate, ArrayList<EnumFacing> forceSide) {
        return PlacementUtil.placeBlock(blockPos, hand, rotate, true, forceSide);
    }

    public static boolean holeFill(BlockPos blockPos, EnumHand hand, boolean rotate, boolean swing, ArrayList<EnumFacing> forceSide) {
        return PlacementUtil.holeFillBlock(blockPos, hand, rotate, swing, forceSide);
    }

    public static boolean holeFillawa(BlockPos blockPos, EnumHand hand, boolean rotate, boolean swing) {
        return PlacementUtil.holeFillBlockawa(blockPos, hand, rotate, swing);
    }

    public static boolean place(BlockPos blockPos, EnumHand hand, boolean rotate, boolean checkAction) {
        return PlacementUtil.placeBlock(blockPos, hand, rotate, checkAction, null);
    }

    public static boolean holeFill(BlockPos blockPos, EnumHand hand, boolean rotate, boolean swing) {
        return PlacementUtil.holeFillBlock(blockPos, hand, rotate, swing, null);
    }

    public static CPacketPlayer.Rotation placeBlockGetRotate(BlockPos blockPos, EnumHand hand, boolean checkAction, ArrayList<EnumFacing> forceSide, boolean swingArm) {
        EnumFacing side;
        EntityPlayerSP player = PlacementUtil.mc.field_71439_g;
        WorldClient world = PlacementUtil.mc.field_71441_e;
        PlayerControllerMP playerController = PlacementUtil.mc.field_71442_b;
        if (player == null || world == null || playerController == null) {
            return null;
        }
        if (!world.func_180495_p(blockPos).func_185904_a().func_76222_j()) {
            return null;
        }
        EnumFacing enumFacing = side = forceSide != null ? BlockUtil.getPlaceableSideExlude(blockPos, forceSide) : BlockUtil.getPlaceableSide(blockPos);
        if (side == null) {
            return null;
        }
        BlockPos neighbour = blockPos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        if (!BlockUtil.canBeClicked(neighbour)) {
            return null;
        }
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        Block neighbourBlock = world.func_180495_p(neighbour).func_177230_c();
        if (!isSneaking && BlockUtil.blackList.contains(neighbourBlock) || BlockUtil.shulkerList.contains(neighbourBlock)) {
            player.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)player, CPacketEntityAction.Action.START_SNEAKING));
            isSneaking = true;
        }
        EnumActionResult action = playerController.func_187099_a(player, world, neighbour, opposite, hitVec, hand);
        if (!checkAction || action == EnumActionResult.SUCCESS) {
            if (swingArm) {
                player.func_184609_a(hand);
                PlacementUtil.mc.field_71467_ac = 4;
            } else {
                player.field_71174_a.func_147297_a((Packet)new CPacketAnimation(hand));
            }
        }
        return BlockUtil.getFaceVectorPacket(hitVec, true);
    }

    public static boolean placeBlock(BlockPos blockPos, EnumHand hand, boolean rotate, boolean checkAction, ArrayList<EnumFacing> forceSide) {
        EnumFacing side;
        EntityPlayerSP player = PlacementUtil.mc.field_71439_g;
        WorldClient world = PlacementUtil.mc.field_71441_e;
        PlayerControllerMP playerController = PlacementUtil.mc.field_71442_b;
        if (player == null || world == null || playerController == null) {
            return false;
        }
        if (!world.func_180495_p(blockPos).func_185904_a().func_76222_j()) {
            return false;
        }
        EnumFacing enumFacing = side = forceSide != null ? BlockUtil.getPlaceableSideExlude(blockPos, forceSide) : BlockUtil.getPlaceableSide(blockPos);
        if (side == null) {
            return false;
        }
        BlockPos neighbour = blockPos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        if (!BlockUtil.canBeClicked(neighbour)) {
            return false;
        }
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        Block neighbourBlock = world.func_180495_p(neighbour).func_177230_c();
        if (!isSneaking && BlockUtil.blackList.contains(neighbourBlock) || BlockUtil.shulkerList.contains(neighbourBlock)) {
            player.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)player, CPacketEntityAction.Action.START_SNEAKING));
            isSneaking = true;
        }
        if (rotate) {
            BlockUtil.faceVectorPacketInstant(hitVec, true);
        }
        EnumActionResult action = playerController.func_187099_a(player, world, neighbour, opposite, hitVec, hand);
        if (!checkAction || action == EnumActionResult.SUCCESS) {
            player.func_184609_a(hand);
            PlacementUtil.mc.field_71467_ac = 4;
        }
        return action == EnumActionResult.SUCCESS;
    }

    public static boolean holeFillBlock(BlockPos blockPos, EnumHand hand, boolean rotate, boolean swing, ArrayList<EnumFacing> forceSide) {
        EnumFacing side;
        EntityPlayerSP player = PlacementUtil.mc.field_71439_g;
        WorldClient world = PlacementUtil.mc.field_71441_e;
        PlayerControllerMP playerController = PlacementUtil.mc.field_71442_b;
        if (player == null || world == null || playerController == null) {
            return false;
        }
        if (!world.func_180495_p(blockPos).func_185904_a().func_76222_j()) {
            return false;
        }
        EnumFacing enumFacing = side = forceSide != null ? BlockUtil.getPlaceableSideExlude(blockPos, forceSide) : BlockUtil.getPlaceableSide(blockPos);
        if (side == null) {
            return false;
        }
        BlockPos neighbour = blockPos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        if (!BlockUtil.canBeClicked(neighbour)) {
            return false;
        }
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        Block neighbourBlock = world.func_180495_p(neighbour).func_177230_c();
        if (!isSneaking && BlockUtil.blackList.contains(neighbourBlock) || BlockUtil.shulkerList.contains(neighbourBlock)) {
            player.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)player, CPacketEntityAction.Action.START_SNEAKING));
            isSneaking = true;
        }
        if (rotate) {
            BlockUtil.faceVectorPacketInstant(hitVec, true);
        }
        EnumActionResult action = playerController.func_187099_a(player, world, neighbour, opposite, hitVec, hand);
        if (swing) {
            player.func_184609_a(hand);
        }
        return action == EnumActionResult.SUCCESS;
    }

    public static boolean holeFillBlockawa(BlockPos blockPos, EnumHand hand, boolean rotate, boolean swing) {
        EnumFacing opposite;
        BlockPos neighbour;
        EntityPlayerSP player = PlacementUtil.mc.field_71439_g;
        WorldClient world = PlacementUtil.mc.field_71441_e;
        PlayerControllerMP playerController = PlacementUtil.mc.field_71442_b;
        if (player == null || world == null || playerController == null) {
            return false;
        }
        if (!world.func_180495_p(blockPos).func_185904_a().func_76222_j()) {
            return false;
        }
        if (!PlacementUtil.mc.field_71441_e.func_175623_d(blockPos.func_177968_d())) {
            neighbour = blockPos.func_177972_a(EnumFacing.SOUTH);
            opposite = EnumFacing.SOUTH.func_176734_d();
        } else if (!PlacementUtil.mc.field_71441_e.func_175623_d(blockPos.func_177978_c())) {
            neighbour = blockPos.func_177972_a(EnumFacing.NORTH);
            opposite = EnumFacing.NORTH.func_176734_d();
        } else if (!PlacementUtil.mc.field_71441_e.func_175623_d(blockPos.func_177974_f())) {
            neighbour = blockPos.func_177972_a(EnumFacing.EAST);
            opposite = EnumFacing.EAST.func_176734_d();
        } else if (!PlacementUtil.mc.field_71441_e.func_175623_d(blockPos.func_177976_e())) {
            neighbour = blockPos.func_177972_a(EnumFacing.WEST);
            opposite = EnumFacing.WEST.func_176734_d();
        } else {
            return false;
        }
        if (!BlockUtil.canBeClicked(neighbour)) {
            return false;
        }
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_178787_e(new Vec3d(0.5, 0.8, 0.5)).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        Block neighbourBlock = world.func_180495_p(neighbour).func_177230_c();
        if (!isSneaking && BlockUtil.blackList.contains(neighbourBlock) || BlockUtil.shulkerList.contains(neighbourBlock)) {
            player.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)player, CPacketEntityAction.Action.START_SNEAKING));
            isSneaking = true;
        }
        if (rotate) {
            BlockUtil.faceVectorPacketInstant(hitVec, true);
        }
        EnumActionResult action = playerController.func_187099_a(player, world, neighbour, opposite, hitVec, hand);
        if (swing) {
            player.func_184609_a(hand);
        }
        return action == EnumActionResult.SUCCESS;
    }

    public static boolean placePrecise(BlockPos blockPos, EnumHand hand, boolean rotate, Vec3d precise, EnumFacing forceSide, boolean onlyRotation, boolean support) {
        EnumFacing side;
        EntityPlayerSP player = PlacementUtil.mc.field_71439_g;
        WorldClient world = PlacementUtil.mc.field_71441_e;
        PlayerControllerMP playerController = PlacementUtil.mc.field_71442_b;
        if (player == null || world == null || playerController == null) {
            return false;
        }
        if (!world.func_180495_p(blockPos).func_185904_a().func_76222_j()) {
            return false;
        }
        EnumFacing enumFacing = side = forceSide == null ? BlockUtil.getPlaceableSide(blockPos) : forceSide;
        if (side == null) {
            return false;
        }
        BlockPos neighbour = blockPos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        if (!BlockUtil.canBeClicked(neighbour)) {
            return false;
        }
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        Block neighbourBlock = world.func_180495_p(neighbour).func_177230_c();
        if (!isSneaking && BlockUtil.blackList.contains(neighbourBlock) || BlockUtil.shulkerList.contains(neighbourBlock)) {
            player.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)player, CPacketEntityAction.Action.START_SNEAKING));
            isSneaking = true;
        }
        if (rotate && !support) {
            BlockUtil.faceVectorPacketInstant(precise == null ? hitVec : precise, true);
        }
        if (!onlyRotation) {
            EnumActionResult action = playerController.func_187099_a(player, world, neighbour, opposite, precise == null ? hitVec : precise, hand);
            if (action == EnumActionResult.SUCCESS) {
                player.func_184609_a(hand);
                PlacementUtil.mc.field_71467_ac = 4;
            }
            return action == EnumActionResult.SUCCESS;
        }
        return true;
    }
}
