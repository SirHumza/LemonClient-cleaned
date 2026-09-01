/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockDeadBush
 *  net.minecraft.block.BlockSoulSand
 *  net.minecraft.block.BlockTallGrass
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.boss.EntityWither
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemNameTag
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package com.lemonclient.client.module.modules.misc;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

@Module.Declaration(name="AutoSpawner", category=Category.Misc)
public class AutoSpawner
extends Module {
    ModeSetting useMode = this.registerMode("Use Mode", Arrays.asList("Single", "Spam"), "Spam");
    BooleanSetting party = this.registerBoolean("Wither Party", false);
    ModeSetting entityMode = this.registerMode("Entity Mode", Arrays.asList("Snow", "Iron", "Wither"), "Wither");
    BooleanSetting nametagWithers = this.registerBoolean("Nametag", true);
    DoubleSetting placeRange = this.registerDouble("Place Range", 3.5, 1.0, 10.0);
    IntegerSetting delay = this.registerInteger("Delay", 20, 0, 100);
    BooleanSetting rotate = this.registerBoolean("Rotate", true);
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true);
    BooleanSetting check = this.registerBoolean("Switch Check", true);
    BooleanSetting packet = this.registerBoolean("Packet Place", true);
    BooleanSetting swing = this.registerBoolean("Swing", true);
    private static boolean isSneaking;
    private BlockPos placeTarget;
    private boolean rotationPlaceableX;
    private boolean rotationPlaceableZ;
    private int bodySlot;
    private int headSlot;
    private int buildStage;
    private int delayStep;

    private void useNameTag() {
        int originalSlot = AutoSpawner.mc.field_71439_g.field_71071_by.field_70461_c;
        for (Entity w : AutoSpawner.mc.field_71441_e.func_72910_y()) {
            EntityWither wither;
            if (!(w instanceof EntityWither) || !w.func_145748_c_().func_150260_c().equalsIgnoreCase("Wither") || !((double)AutoSpawner.mc.field_71439_g.func_70032_d((Entity)(wither = (EntityWither)w)) <= (Double)this.placeRange.getValue())) continue;
            this.selectNameTags();
            AutoSpawner.mc.field_71442_b.func_187097_a((EntityPlayer)AutoSpawner.mc.field_71439_g, (Entity)wither, EnumHand.MAIN_HAND);
        }
        this.switchTo(originalSlot);
    }

    private void selectNameTags() {
        int tagSlot = -1;
        for (int i = 0; i < 9; ++i) {
            Item tag;
            ItemStack stack = AutoSpawner.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a || stack.func_77973_b() instanceof ItemBlock || !((tag = stack.func_77973_b()) instanceof ItemNameTag)) continue;
            tagSlot = i;
        }
        if (tagSlot == -1) {
            return;
        }
        this.switchTo(tagSlot);
    }

    private static EnumFacing getPlaceableSide(BlockPos pos) {
        for (EnumFacing side : EnumFacing.values()) {
            IBlockState blockState;
            BlockPos neighbour = pos.func_177972_a(side);
            if (!AutoSpawner.mc.field_71441_e.func_180495_p(neighbour).func_177230_c().func_176209_a(AutoSpawner.mc.field_71441_e.func_180495_p(neighbour), false) || (blockState = AutoSpawner.mc.field_71441_e.func_180495_p(neighbour)).func_185904_a().func_76222_j() || blockState.func_177230_c() instanceof BlockTallGrass || blockState.func_177230_c() instanceof BlockDeadBush) continue;
            return side;
        }
        return null;
    }

    @Override
    protected void onEnable() {
        this.buildStage = 1;
        this.delayStep = 1;
    }

    private boolean checkBlocksInHotbar() {
        this.headSlot = -1;
        this.bodySlot = -1;
        for (int i = 0; i < 9; ++i) {
            Block block;
            ItemStack stack = AutoSpawner.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a) continue;
            if (((String)this.entityMode.getValue()).equals("Wither")) {
                if (stack.func_77973_b() == Items.field_151144_bL && stack.func_77952_i() == 1) {
                    if (AutoSpawner.mc.field_71439_g.field_71071_by.func_70301_a((int)i).field_77994_a < 3) continue;
                    this.headSlot = i;
                    continue;
                }
                if (!(stack.func_77973_b() instanceof ItemBlock)) continue;
                block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
                if (block instanceof BlockSoulSand && AutoSpawner.mc.field_71439_g.field_71071_by.func_70301_a((int)i).field_77994_a >= 4) {
                    this.bodySlot = i;
                }
            }
            if (((String)this.entityMode.getValue()).equals("Iron")) {
                if (!(stack.func_77973_b() instanceof ItemBlock)) continue;
                block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
                if ((block == Blocks.field_150428_aP || block == Blocks.field_150423_aK) && AutoSpawner.mc.field_71439_g.field_71071_by.func_70301_a((int)i).field_77994_a >= 1) {
                    this.headSlot = i;
                }
                if (block == Blocks.field_150339_S && AutoSpawner.mc.field_71439_g.field_71071_by.func_70301_a((int)i).field_77994_a >= 4) {
                    this.bodySlot = i;
                }
            }
            if (!((String)this.entityMode.getValue()).equals("Snow") || !(stack.func_77973_b() instanceof ItemBlock)) continue;
            block = ((ItemBlock)stack.func_77973_b()).func_179223_d();
            if ((block == Blocks.field_150428_aP || block == Blocks.field_150423_aK) && AutoSpawner.mc.field_71439_g.field_71071_by.func_70301_a((int)i).field_77994_a >= 1) {
                this.headSlot = i;
            }
            if (block != Blocks.field_150433_aE || AutoSpawner.mc.field_71439_g.field_71071_by.func_70301_a((int)i).field_77994_a < 2) continue;
            this.bodySlot = i;
        }
        return this.bodySlot != -1 && this.headSlot != -1;
    }

    private boolean testStructure() {
        if (((String)this.entityMode.getValue()).equals("Wither")) {
            return this.testWitherStructure();
        }
        if (((String)this.entityMode.getValue()).equals("Iron")) {
            return this.testIronGolemStructure();
        }
        if (((String)this.entityMode.getValue()).equals("Snow")) {
            return this.testSnowGolemStructure();
        }
        return false;
    }

    private boolean testWitherStructure() {
        boolean noRotationPlaceable = true;
        this.rotationPlaceableX = true;
        this.rotationPlaceableZ = true;
        boolean isShitGrass = false;
        if (AutoSpawner.mc.field_71441_e.func_180495_p(this.placeTarget) == null) {
            return false;
        }
        Block block = AutoSpawner.mc.field_71441_e.func_180495_p(this.placeTarget).func_177230_c();
        if (block instanceof BlockTallGrass || block instanceof BlockDeadBush) {
            isShitGrass = true;
        }
        if (AutoSpawner.getPlaceableSide(this.placeTarget.func_177984_a()) == null) {
            return false;
        }
        for (BlockPos pos : BodyParts.bodyBase) {
            if (!this.placingIsBlocked(this.placeTarget.func_177971_a((Vec3i)pos))) continue;
            noRotationPlaceable = false;
        }
        for (BlockPos pos : BodyParts.ArmsX) {
            if (!this.placingIsBlocked(this.placeTarget.func_177971_a((Vec3i)pos)) && !this.placingIsBlocked(this.placeTarget.func_177971_a((Vec3i)pos.func_177977_b()))) continue;
            this.rotationPlaceableX = false;
        }
        for (BlockPos pos : BodyParts.ArmsZ) {
            if (!this.placingIsBlocked(this.placeTarget.func_177971_a((Vec3i)pos)) && !this.placingIsBlocked(this.placeTarget.func_177971_a((Vec3i)pos.func_177977_b()))) continue;
            this.rotationPlaceableZ = false;
        }
        for (BlockPos pos : BodyParts.headsX) {
            if (!this.placingIsBlocked(this.placeTarget.func_177971_a((Vec3i)pos))) continue;
            this.rotationPlaceableX = false;
        }
        for (BlockPos pos : BodyParts.headsZ) {
            if (!this.placingIsBlocked(this.placeTarget.func_177971_a((Vec3i)pos))) continue;
            this.rotationPlaceableZ = false;
        }
        return !isShitGrass && noRotationPlaceable && (this.rotationPlaceableX || this.rotationPlaceableZ);
    }

    private boolean testIronGolemStructure() {
        boolean noRotationPlaceable = true;
        this.rotationPlaceableX = true;
        this.rotationPlaceableZ = true;
        boolean isShitGrass = false;
        if (AutoSpawner.mc.field_71441_e.func_180495_p(this.placeTarget) == null) {
            return false;
        }
        Block block = AutoSpawner.mc.field_71441_e.func_180495_p(this.placeTarget).func_177230_c();
        if (block instanceof BlockTallGrass || block instanceof BlockDeadBush) {
            isShitGrass = true;
        }
        if (AutoSpawner.getPlaceableSide(this.placeTarget.func_177984_a()) == null) {
            return false;
        }
        for (BlockPos pos : BodyParts.bodyBase) {
            if (!this.placingIsBlocked(this.placeTarget.func_177971_a((Vec3i)pos))) continue;
            noRotationPlaceable = false;
        }
        for (BlockPos pos : BodyParts.ArmsX) {
            if (!this.placingIsBlocked(this.placeTarget.func_177971_a((Vec3i)pos)) && !this.placingIsBlocked(this.placeTarget.func_177971_a((Vec3i)pos.func_177977_b()))) continue;
            this.rotationPlaceableX = false;
        }
        for (BlockPos pos : BodyParts.ArmsZ) {
            if (!this.placingIsBlocked(this.placeTarget.func_177971_a((Vec3i)pos)) && !this.placingIsBlocked(this.placeTarget.func_177971_a((Vec3i)pos.func_177977_b()))) continue;
            this.rotationPlaceableZ = false;
        }
        for (BlockPos pos : BodyParts.head) {
            if (!this.placingIsBlocked(this.placeTarget.func_177971_a((Vec3i)pos))) continue;
            noRotationPlaceable = false;
        }
        return !isShitGrass && noRotationPlaceable && (this.rotationPlaceableX || this.rotationPlaceableZ);
    }

    private boolean testSnowGolemStructure() {
        boolean noRotationPlaceable = true;
        boolean isShitGrass = false;
        if (AutoSpawner.mc.field_71441_e.func_180495_p(this.placeTarget) == null) {
            return false;
        }
        Block block = AutoSpawner.mc.field_71441_e.func_180495_p(this.placeTarget).func_177230_c();
        if (block instanceof BlockTallGrass || block instanceof BlockDeadBush) {
            isShitGrass = true;
        }
        if (AutoSpawner.getPlaceableSide(this.placeTarget.func_177984_a()) == null) {
            return false;
        }
        for (BlockPos pos : BodyParts.bodyBase) {
            if (!this.placingIsBlocked(this.placeTarget.func_177971_a((Vec3i)pos))) continue;
            noRotationPlaceable = false;
        }
        for (BlockPos pos : BodyParts.head) {
            if (!this.placingIsBlocked(this.placeTarget.func_177971_a((Vec3i)pos))) continue;
            noRotationPlaceable = false;
        }
        return !isShitGrass && noRotationPlaceable;
    }

    private void switchTo(int slot) {
        if (!(slot <= -1 || slot >= 9 || ((Boolean)this.check.getValue()).booleanValue() && AutoSpawner.mc.field_71439_g.field_71071_by.field_70461_c == slot)) {
            if (((Boolean)this.packetSwitch.getValue()).booleanValue()) {
                AutoSpawner.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            } else {
                AutoSpawner.mc.field_71439_g.field_71071_by.field_70461_c = slot;
            }
            AutoSpawner.mc.field_71442_b.func_78765_e();
        }
    }

    @Override
    public void onUpdate() {
        if (AutoSpawner.mc.field_71441_e == null || AutoSpawner.mc.field_71439_g == null || AutoSpawner.mc.field_71439_g.field_70128_L) {
            return;
        }
        if (((Boolean)this.nametagWithers.getValue()).booleanValue() && (((Boolean)this.party.getValue()).booleanValue() || !((Boolean)this.party.getValue()).booleanValue() && ((String)this.entityMode.getValue()).equals("Wither"))) {
            this.useNameTag();
        }
        if (this.buildStage == 1) {
            isSneaking = false;
            this.rotationPlaceableX = false;
            this.rotationPlaceableZ = false;
            if (((Boolean)this.party.getValue()).booleanValue()) {
                this.entityMode.setValue("Wither");
            }
            if (!this.checkBlocksInHotbar()) {
                if (((String)this.useMode.getValue()).equals("Single")) {
                    this.disable();
                }
                return;
            }
            List<BlockPos> blockPosList = EntityUtil.getSphere(AutoSpawner.mc.field_71439_g.func_180425_c().func_177977_b(), (Double)this.placeRange.getValue(), (Double)this.placeRange.getValue(), false, true, 0);
            boolean noPositionInArea = true;
            for (BlockPos pos : blockPosList) {
                this.placeTarget = pos.func_177977_b();
                if (!this.testStructure()) continue;
                noPositionInArea = false;
                break;
            }
            if (noPositionInArea) {
                if (((String)this.useMode.getValue()).equals("Single")) {
                    this.disable();
                }
                return;
            }
            int oldslot = AutoSpawner.mc.field_71439_g.field_71071_by.field_70461_c;
            this.switchTo(this.bodySlot);
            for (BlockPos pos : BodyParts.bodyBase) {
                BurrowUtil.placeBlock(this.placeTarget.func_177971_a((Vec3i)pos), EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
            }
            if (((String)this.entityMode.getValue()).equals("Wither") || ((String)this.entityMode.getValue()).equals("Iron")) {
                if (this.rotationPlaceableX) {
                    for (BlockPos pos : BodyParts.ArmsX) {
                        BurrowUtil.placeBlock(this.placeTarget.func_177971_a((Vec3i)pos), EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
                    }
                } else if (this.rotationPlaceableZ) {
                    for (BlockPos pos : BodyParts.ArmsZ) {
                        BurrowUtil.placeBlock(this.placeTarget.func_177971_a((Vec3i)pos), EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
                    }
                }
            }
            this.switchTo(oldslot);
            this.buildStage = 2;
        } else if (this.buildStage == 2) {
            int oldslot = AutoSpawner.mc.field_71439_g.field_71071_by.field_70461_c;
            this.switchTo(this.headSlot);
            if (((String)this.entityMode.getValue()).equals("Wither")) {
                if (this.rotationPlaceableX) {
                    for (BlockPos pos : BodyParts.headsX) {
                        BurrowUtil.placeBlock(this.placeTarget.func_177971_a((Vec3i)pos), EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
                    }
                } else if (this.rotationPlaceableZ) {
                    for (BlockPos pos : BodyParts.headsZ) {
                        BurrowUtil.placeBlock(this.placeTarget.func_177971_a((Vec3i)pos), EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
                    }
                }
            }
            if (((String)this.entityMode.getValue()).equals("Iron") || ((String)this.entityMode.getValue()).equals("Snow")) {
                for (BlockPos pos : BodyParts.head) {
                    BurrowUtil.placeBlock(this.placeTarget.func_177971_a((Vec3i)pos), EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
                }
            }
            if (isSneaking) {
                AutoSpawner.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)AutoSpawner.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
                isSneaking = false;
            }
            if (((String)this.useMode.getValue()).equals("Single")) {
                this.disable();
            }
            this.switchTo(oldslot);
            this.buildStage = 3;
        } else if (this.buildStage == 3) {
            if (this.delayStep < (Integer)this.delay.getValue()) {
                ++this.delayStep;
            } else {
                this.delayStep = 1;
                this.buildStage = 1;
            }
        }
    }

    private boolean placingIsBlocked(BlockPos pos) {
        Block block = AutoSpawner.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        if (!(block instanceof BlockAir)) {
            return true;
        }
        for (Entity entity : AutoSpawner.mc.field_71441_e.func_72839_b(null, new AxisAlignedBB(pos))) {
            if (entity instanceof EntityItem || entity instanceof EntityXPOrb) continue;
            return true;
        }
        return false;
    }

    private static class BodyParts {
        private static final BlockPos[] bodyBase = new BlockPos[]{new BlockPos(0, 1, 0), new BlockPos(0, 2, 0)};
        private static final BlockPos[] ArmsX = new BlockPos[]{new BlockPos(-1, 2, 0), new BlockPos(1, 2, 0)};
        private static final BlockPos[] ArmsZ = new BlockPos[]{new BlockPos(0, 2, -1), new BlockPos(0, 2, 1)};
        private static final BlockPos[] headsX = new BlockPos[]{new BlockPos(0, 3, 0), new BlockPos(-1, 3, 0), new BlockPos(1, 3, 0)};
        private static final BlockPos[] headsZ = new BlockPos[]{new BlockPos(0, 3, 0), new BlockPos(0, 3, -1), new BlockPos(0, 3, 1)};
        private static final BlockPos[] head = new BlockPos[]{new BlockPos(0, 3, 0)};

        private BodyParts() {
        }
    }
}
