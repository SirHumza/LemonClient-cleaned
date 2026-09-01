/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.BlockPistonBase
 *  net.minecraft.block.BlockPistonMoving
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.dev;

import com.google.common.collect.ImmutableMap;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

@Module.Declaration(name="AntiHolePush", category=Category.Dev)
public class AntiHolePush
extends Module {
    ModeSetting timeMode = this.registerMode("Time Mode", Arrays.asList("onUpdate", "Tick", "Both", "Fast"), "Fast");
    BooleanSetting packet = this.registerBoolean("Packet Place", false);
    BooleanSetting swing = this.registerBoolean("Swing", false);
    BooleanSetting rotate = this.registerBoolean("Rotate", true);
    BooleanSetting strict = this.registerBoolean("Strict", true);
    BooleanSetting raytrace = this.registerBoolean("RayTrace", true);
    BooleanSetting trap = this.registerBoolean("Trap", true);
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", false);
    BooleanSetting entityCheck = this.registerBoolean("Entity Check", true);
    BooleanSetting breakPiston = this.registerBoolean("Break Piston", false);

    private void switchTo(int slot, Runnable runnable) {
        int oldslot = AntiHolePush.mc.field_71439_g.field_71071_by.field_70461_c;
        if (slot < 0 || slot == oldslot) {
            runnable.run();
            return;
        }
        if (slot < 9) {
            boolean packetSwitch = (Boolean)this.packetSwitch.getValue();
            if (packetSwitch) {
                AntiHolePush.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            } else {
                AntiHolePush.mc.field_71439_g.field_71071_by.field_70461_c = slot;
            }
            runnable.run();
            if (packetSwitch) {
                AntiHolePush.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(oldslot));
            } else {
                AntiHolePush.mc.field_71439_g.field_71071_by.field_70461_c = oldslot;
            }
        }
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : AntiHolePush.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityItem || !new AxisAlignedBB(pos).func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    @Override
    public void onUpdate() {
        if (((String)this.timeMode.getValue()).equalsIgnoreCase("onUpdate") || ((String)this.timeMode.getValue()).equalsIgnoreCase("Both")) {
            this.block();
        }
    }

    @Override
    public void onTick() {
        if (((String)this.timeMode.getValue()).equalsIgnoreCase("Tick") || ((String)this.timeMode.getValue()).equalsIgnoreCase("Both")) {
            this.block();
        }
    }

    @Override
    public void fast() {
        if (((String)this.timeMode.getValue()).equalsIgnoreCase("Fast")) {
            this.block();
        }
    }

    private void block() {
        if (AntiHolePush.mc.field_71439_g == null || AntiHolePush.mc.field_71441_e == null) {
            return;
        }
        BlockPos pos = new BlockPos(AntiHolePush.mc.field_71439_g.field_70165_t, AntiHolePush.mc.field_71439_g.field_70163_u, AntiHolePush.mc.field_71439_g.field_70161_v);
        int obsidian = BurrowUtil.findHotbarBlock(BlockObsidian.class);
        if (obsidian == -1) {
            return;
        }
        BlockPos head = pos.func_177982_a(0, 2, 0);
        BlockPos pos1 = pos.func_177982_a(1, 1, 0);
        BlockPos pos2 = pos.func_177982_a(-1, 1, 0);
        BlockPos pos3 = pos.func_177982_a(0, 1, 1);
        BlockPos pos4 = pos.func_177982_a(0, 1, -1);
        if (!this.airBlock(head)) {
            return;
        }
        ArrayList<BlockPos> posList = new ArrayList<BlockPos>();
        if (this.isPiston(pos1) && AntiHolePush.isFacing(pos1, EnumFacing.WEST)) {
            BlockPos pos5 = pos.func_177982_a(-1, 2, 0);
            if (this.airBlock(pos2) && this.airBlock(pos5)) {
                posList.add(pos2);
            }
            if (((Boolean)this.trap.getValue()).booleanValue() && this.airBlock(head)) {
                posList.add(pos2.func_177984_a());
                posList.add(head);
            }
            if (((Boolean)this.breakPiston.getValue()).booleanValue()) {
                AntiHolePush.mc.field_71442_b.func_180512_c(pos1, BlockUtil.getRayTraceFacing(pos3));
            }
        }
        if (this.isPiston(pos2) && AntiHolePush.isFacing(pos2, EnumFacing.EAST)) {
            BlockPos pos6 = pos.func_177982_a(1, 2, 0);
            if (this.airBlock(pos1) && this.airBlock(pos6)) {
                posList.add(pos1);
            }
            if (((Boolean)this.trap.getValue()).booleanValue() && this.airBlock(head)) {
                posList.add(pos1.func_177984_a());
                posList.add(head);
            }
            if (((Boolean)this.breakPiston.getValue()).booleanValue()) {
                AntiHolePush.mc.field_71442_b.func_180512_c(pos2, BlockUtil.getRayTraceFacing(pos3));
            }
        }
        if (this.isPiston(pos3) && AntiHolePush.isFacing(pos3, EnumFacing.NORTH)) {
            BlockPos pos7 = pos.func_177982_a(0, 2, -1);
            if (this.airBlock(pos4) && this.airBlock(pos7)) {
                posList.add(pos4);
            }
            if (((Boolean)this.trap.getValue()).booleanValue() && this.airBlock(head)) {
                posList.add(pos4.func_177984_a());
                posList.add(head);
            }
            if (((Boolean)this.breakPiston.getValue()).booleanValue()) {
                AntiHolePush.mc.field_71442_b.func_180512_c(pos3, BlockUtil.getRayTraceFacing(pos3));
            }
        }
        if (this.isPiston(pos4) && AntiHolePush.isFacing(pos4, EnumFacing.SOUTH)) {
            BlockPos pos8 = pos.func_177982_a(0, 2, 1);
            if (this.airBlock(pos3) && this.airBlock(pos8)) {
                posList.add(pos3);
            }
            if (((Boolean)this.trap.getValue()).booleanValue() && this.airBlock(head)) {
                posList.add(pos3.func_177984_a());
                posList.add(head);
            }
            if (((Boolean)this.breakPiston.getValue()).booleanValue()) {
                AntiHolePush.mc.field_71442_b.func_180512_c(pos4, BlockUtil.getRayTraceFacing(pos3));
            }
        }
        if (!posList.isEmpty()) {
            this.switchTo(obsidian, () -> {
                for (BlockPos placePos : posList) {
                    this.perform(placePos);
                }
            });
        }
    }

    private IBlockState getBlock(BlockPos block) {
        return AntiHolePush.mc.field_71441_e.func_180495_p(block);
    }

    private boolean airBlock(BlockPos pos) {
        return BlockUtil.airBlocks.contains(this.getBlock(pos).func_177230_c());
    }

    private void perform(BlockPos pos) {
        if (((Boolean)this.entityCheck.getValue()).booleanValue() && this.intersectsWithEntity(pos) || !BlockUtil.canPlace(pos, (Boolean)this.strict.getValue(), (Boolean)this.raytrace.getValue())) {
            return;
        }
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), (Boolean)this.strict.getValue(), (Boolean)this.raytrace.getValue(), (Boolean)this.swing.getValue());
    }

    public static boolean isFacing(BlockPos pos, EnumFacing enumFacing) {
        ImmutableMap properties = AntiHolePush.mc.field_71441_e.func_180495_p(pos).func_177228_b();
        for (IProperty prop : properties.keySet()) {
            if (prop.func_177699_b() != EnumFacing.class || !prop.func_177701_a().equals("facing") && !prop.func_177701_a().equals("rotation") || properties.get((Object)prop) != enumFacing) continue;
            return true;
        }
        return false;
    }

    private boolean isPiston(BlockPos pos) {
        return AntiHolePush.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockPistonMoving || AntiHolePush.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockPistonBase || AntiHolePush.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150331_J || AntiHolePush.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150320_F;
    }
}
