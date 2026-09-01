/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package com.lemonclient.api.util.player;

import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.client.module.modules.gui.ColorMain;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class BurrowUtil {
    public static final Minecraft mc = Minecraft.func_71410_x();
    static EnumFacing[] facing = new EnumFacing[]{EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.EAST};

    public static void placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking, boolean swing) {
        if (pos == null || !BlockUtil.isAir(pos)) {
            return;
        }
        EnumFacing side = BurrowUtil.getFirstFacing(pos);
        if (side == null) {
            return;
        }
        BlockPos neighbour = pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        boolean sneak = false;
        if (!ColorMain.INSTANCE.sneaking && BlockUtil.blackList.contains(BlockUtil.getBlock(neighbour))) {
            BurrowUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BurrowUtil.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            BurrowUtil.mc.field_71439_g.func_70095_a(true);
            sneak = true;
        }
        if (rotate) {
            BurrowUtil.faceVector(hitVec, true);
        }
        BurrowUtil.rightClickBlock(neighbour, hitVec, hand, opposite, packet, swing);
        if (sneak) {
            BurrowUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BurrowUtil.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
        }
        BurrowUtil.mc.field_71467_ac = 4;
    }

    public static void placeBlockDown(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking, boolean swing) {
        if (pos == null || !BlockUtil.isAir(pos)) {
            return;
        }
        EnumFacing side = EnumFacing.DOWN;
        BlockPos neighbour = pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        boolean sneak = false;
        if (!ColorMain.INSTANCE.sneaking && BlockUtil.blackList.contains(BlockUtil.getBlock(neighbour))) {
            BurrowUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BurrowUtil.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            BurrowUtil.mc.field_71439_g.func_70095_a(true);
            sneak = true;
        }
        if (rotate) {
            BurrowUtil.faceVector(hitVec, true);
        }
        BurrowUtil.rightClickBlock(neighbour, hitVec, hand, opposite, packet, swing);
        if (sneak) {
            BurrowUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BurrowUtil.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
        }
        BurrowUtil.mc.field_71467_ac = 4;
    }

    public static List<EnumFacing> getPossibleSides(BlockPos pos) {
        if (pos == null) {
            return null;
        }
        ArrayList<EnumFacing> facings = new ArrayList<EnumFacing>();
        for (EnumFacing side : EnumFacing.values()) {
            IBlockState blockState;
            BlockPos neighbour = pos.func_177972_a(side);
            if (!BurrowUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c().func_176209_a(BurrowUtil.mc.field_71441_e.func_180495_p(neighbour), false) || (blockState = BurrowUtil.mc.field_71441_e.func_180495_p(neighbour)).func_185904_a().func_76222_j()) continue;
            facings.add(side);
        }
        return facings;
    }

    public static List<EnumFacing> getTrapdoorPossibleSides(BlockPos pos) {
        if (pos == null) {
            return null;
        }
        ArrayList<EnumFacing> facings = new ArrayList<EnumFacing>();
        for (EnumFacing side : facing) {
            IBlockState blockState;
            BlockPos neighbour = pos.func_177972_a(side);
            if (!BurrowUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c().func_176209_a(BurrowUtil.mc.field_71441_e.func_180495_p(neighbour), false) || (blockState = BurrowUtil.mc.field_71441_e.func_180495_p(neighbour)).func_185904_a().func_76222_j()) continue;
            facings.add(side);
        }
        return facings;
    }

    public static EnumFacing getFirstFacing(BlockPos pos) {
        if (pos == null) {
            return null;
        }
        Iterator<EnumFacing> iterator = BurrowUtil.getPossibleSides(pos).iterator();
        if (iterator.hasNext()) {
            EnumFacing facing = iterator.next();
            return facing;
        }
        return null;
    }

    public static EnumFacing getBedFacing(BlockPos pos) {
        if (pos == null) {
            return null;
        }
        for (EnumFacing facing : BurrowUtil.getPossibleSides(pos)) {
            if (facing == EnumFacing.UP) continue;
            return facing;
        }
        return null;
    }

    public static EnumFacing getTrapdoorFacing(BlockPos pos) {
        if (pos == null) {
            return null;
        }
        Iterator<EnumFacing> iterator = BurrowUtil.getTrapdoorPossibleSides(pos).iterator();
        if (iterator.hasNext()) {
            EnumFacing facing = iterator.next();
            return facing;
        }
        return null;
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(BurrowUtil.mc.field_71439_g.field_70165_t, BurrowUtil.mc.field_71439_g.field_70163_u + (double)BurrowUtil.mc.field_71439_g.func_70047_e(), BurrowUtil.mc.field_71439_g.field_70161_v);
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = BurrowUtil.getEyesPos();
        double diffX = vec.field_72450_a - eyesPos.field_72450_a;
        double diffY = vec.field_72448_b - eyesPos.field_72448_b;
        double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{BurrowUtil.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g((float)(yaw - BurrowUtil.mc.field_71439_g.field_70177_z)), BurrowUtil.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g((float)(pitch - BurrowUtil.mc.field_71439_g.field_70125_A))};
    }

    public static void faceVector(Vec3d vec, boolean normalizeAngle) {
        float[] rotations = BurrowUtil.getLegitRotations(vec);
        BurrowUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? (float)MathHelper.func_180184_b((int)((int)rotations[1]), (int)360) : rotations[1], BurrowUtil.mc.field_71439_g.field_70122_E));
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet, boolean swing) {
        if (pos == null || vec == null || hand == null || direction == null) {
            return;
        }
        if (packet) {
            float f = (float)(vec.field_72450_a - (double)pos.func_177958_n());
            float f1 = (float)(vec.field_72448_b - (double)pos.func_177956_o());
            float f2 = (float)(vec.field_72449_c - (double)pos.func_177952_p());
            BurrowUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
        } else {
            BurrowUtil.mc.field_71442_b.func_187099_a(BurrowUtil.mc.field_71439_g, BurrowUtil.mc.field_71441_e, pos, direction, vec, hand);
        }
        if (swing) {
            BurrowUtil.mc.field_71439_g.func_184609_a(hand);
        }
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
        if (pos == null || vec == null || direction == null) {
            return;
        }
        if (packet) {
            BurrowUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, 0.5f, 1.0f, 0.5f));
        } else {
            BurrowUtil.mc.field_71442_b.func_187099_a(BurrowUtil.mc.field_71439_g, BurrowUtil.mc.field_71441_e, pos, direction, vec, hand);
        }
    }

    public static void rightClickBlock(BlockPos pos, EnumFacing facing, Vec3d hVec, boolean packet, boolean swing) {
        Vec3d hitVec = new Vec3d((Vec3i)pos).func_178787_e(hVec).func_178787_e(new Vec3d(facing.func_176730_m()).func_186678_a(0.5));
        if (packet) {
            BurrowUtil.rightClickBlock(pos, hitVec, EnumHand.MAIN_HAND, facing);
        } else {
            BurrowUtil.mc.field_71442_b.func_187099_a(BurrowUtil.mc.field_71439_g, BurrowUtil.mc.field_71441_e, pos, facing, hitVec, EnumHand.MAIN_HAND);
        }
        if (swing) {
            BurrowUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        }
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction) {
        float f = (float)(vec.field_72450_a - (double)pos.func_177958_n());
        float f1 = (float)(vec.field_72448_b - (double)pos.func_177956_o());
        float f2 = (float)(vec.field_72449_c - (double)pos.func_177952_p());
        BurrowUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
    }

    public static int findBlock(Class clazz, boolean inv) {
        int slot = BurrowUtil.findHotbarBlock(clazz);
        if (slot == -1 && inv) {
            slot = BurrowUtil.findInventoryBlock(clazz);
        }
        return slot;
    }

    public static int findHotbarBlock(Class clazz) {
        for (int i = 0; i < 9; ++i) {
            Block block;
            ItemStack stack = BurrowUtil.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a) continue;
            if (clazz.isInstance(stack.func_77973_b())) {
                return i;
            }
            if (!(stack.func_77973_b() instanceof ItemBlock) || !clazz.isInstance(block = ((ItemBlock)stack.func_77973_b()).func_179223_d())) continue;
            return i;
        }
        return -1;
    }

    public static int findHotbarBlock(Block blockIn) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = BurrowUtil.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a || !(stack.func_77973_b() instanceof ItemBlock) || ((ItemBlock)stack.func_77973_b()).func_179223_d() != blockIn) continue;
            return i;
        }
        return -1;
    }

    public static int findHotbarItem(Item input) {
        for (int i = 0; i < 9; ++i) {
            Item item = BurrowUtil.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
            if (Item.func_150891_b((Item)item) != Item.func_150891_b((Item)input)) continue;
            return i;
        }
        return -1;
    }

    public static int findInventoryItem(Item input) {
        for (int i = 0; i < 36; ++i) {
            Item item = BurrowUtil.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
            if (Item.func_150891_b((Item)item) != Item.func_150891_b((Item)input)) continue;
            return i;
        }
        return -1;
    }

    public static int findInventoryBlock(Class clazz) {
        for (int i = 9; i < 36; ++i) {
            Block block;
            ItemStack stack = BurrowUtil.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a) continue;
            if (clazz.isInstance(stack.func_77973_b())) {
                return i;
            }
            if (!(stack.func_77973_b() instanceof ItemBlock) || !clazz.isInstance(block = ((ItemBlock)stack.func_77973_b()).func_179223_d())) continue;
            return i;
        }
        return -1;
    }

    public static int getCount(Class clazz) {
        int count = 0;
        for (int i = 0; i < 36; ++i) {
            Block block;
            ItemStack stack = BurrowUtil.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a) continue;
            if (clazz.isInstance(stack.func_77973_b())) {
                count += stack.func_190916_E();
            }
            if (!(stack.func_77973_b() instanceof ItemBlock) || !clazz.isInstance(block = ((ItemBlock)stack.func_77973_b()).func_179223_d())) continue;
            count += stack.func_190916_E();
        }
        return count;
    }

    public static void switchToSlot(int slot) {
        BurrowUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
        BurrowUtil.mc.field_71439_g.field_71071_by.field_70461_c = slot;
        BurrowUtil.mc.field_71442_b.func_78765_e();
    }
}
