/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAnvil
 *  net.minecraft.block.BlockConcretePowder
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package com.lemonclient.client.module.modules.dev;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.client.LemonClient;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockConcretePowder;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

@Module.Declaration(name="AutoConcrete", category=Category.Dev)
public class AutoConcrete
extends Module {
    DoubleSetting range = this.registerDouble("Range", 5.5, 0.0, 10.0);
    BooleanSetting packet = this.registerBoolean("Packet Place", true);
    BooleanSetting swing = this.registerBoolean("Swing", true);
    BooleanSetting rotate = this.registerBoolean("Rotate", false);
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true);
    BooleanSetting air = this.registerBoolean("Air Check", true);
    BooleanSetting disable = this.registerBoolean("Disable", true);
    IntegerSetting delay = this.registerInteger("Delay", 5, 0, 100, () -> (Boolean)this.disable.getValue() == false);
    DoubleSetting maxTargetSpeed = this.registerDouble("Max Target Speed", 10.0, 0.0, 50.0);
    int waited;
    BlockPos[] sides = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1)};

    private void switchTo(int slot, Runnable runnable) {
        int oldslot = AutoConcrete.mc.field_71439_g.field_71071_by.field_70461_c;
        if (slot < 0 || slot == oldslot) {
            runnable.run();
            return;
        }
        if (slot < 9) {
            boolean packetSwitch = (Boolean)this.packetSwitch.getValue();
            if (packetSwitch) {
                AutoConcrete.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            } else {
                AutoConcrete.mc.field_71439_g.field_71071_by.field_70461_c = slot;
            }
            runnable.run();
            if (packetSwitch) {
                AutoConcrete.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(oldslot));
            } else {
                AutoConcrete.mc.field_71439_g.field_71071_by.field_70461_c = oldslot;
            }
        }
    }

    @Override
    public void onEnable() {
        this.waited = 100;
    }

    @Override
    public void onUpdate() {
        if (AutoConcrete.mc.field_71441_e == null || AutoConcrete.mc.field_71439_g == null || AutoConcrete.mc.field_71439_g.field_70128_L) {
            if (((Boolean)this.disable.getValue()).booleanValue()) {
                this.disable();
            }
            return;
        }
        if (this.waited++ < (Integer)this.delay.getValue()) {
            return;
        }
        this.waited = 0;
        int slot = BurrowUtil.findHotbarBlock(BlockAnvil.class);
        if (slot == -1 && (slot = BurrowUtil.findHotbarBlock(BlockConcretePowder.class)) == -1) {
            return;
        }
        EntityPlayer player = PlayerUtil.getNearestPlayer((Double)this.range.getValue());
        if (LemonClient.speedUtil.getPlayerSpeed(player) > (Double)this.maxTargetSpeed.getValue()) {
            return;
        }
        if (player == null) {
            if (((Boolean)this.disable.getValue()).booleanValue()) {
                this.disable();
            }
            return;
        }
        BlockPos pos = new BlockPos(player.field_70165_t, player.field_70163_u, player.field_70161_v);
        if (!BlockUtil.airBlocks.contains(AutoConcrete.mc.field_71441_e.func_180495_p(pos).func_177230_c()) && ((Boolean)this.air.getValue()).booleanValue()) {
            if (((Boolean)this.disable.getValue()).booleanValue()) {
                this.disable();
            }
            return;
        }
        BlockPos placePos = pos.func_177981_b(2);
        if (this.intersectsWithEntity(placePos)) {
            return;
        }
        if (BurrowUtil.getFirstFacing(placePos) == null) {
            int obby = BurrowUtil.findHotbarBlock(BlockObsidian.class);
            if (obby == -1) {
                return;
            }
            boolean helped = false;
            for (BlockPos side : this.sides) {
                BlockPos helpingBlock = placePos.func_177971_a((Vec3i)side);
                if (this.intersectsWithEntity(helpingBlock)) continue;
                if (BurrowUtil.getFirstFacing(helpingBlock) != null) {
                    this.switchTo(obby, () -> BurrowUtil.placeBlock(helpingBlock, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue()));
                    helped = true;
                    break;
                }
                if (this.intersectsWithEntity(helpingBlock.func_177977_b())) continue;
                if (BurrowUtil.getFirstFacing(helpingBlock.func_177977_b()) != null) {
                    this.switchTo(obby, () -> {
                        BurrowUtil.placeBlock(helpingBlock.func_177977_b(), EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
                        BurrowUtil.placeBlock(helpingBlock, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
                    });
                    helped = true;
                    break;
                }
                if (this.intersectsWithEntity(helpingBlock.func_177979_c(2)) || BurrowUtil.getFirstFacing(helpingBlock.func_177979_c(2)) == null) continue;
                this.switchTo(obby, () -> {
                    BurrowUtil.placeBlock(helpingBlock.func_177979_c(2), EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
                    BurrowUtil.placeBlock(helpingBlock.func_177977_b(), EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
                    BurrowUtil.placeBlock(helpingBlock, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
                });
                helped = true;
                break;
            }
            if (!helped) {
                return;
            }
        }
        this.switchTo(slot, () -> BurrowUtil.placeBlock(placePos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue()));
        if (((Boolean)this.disable.getValue()).booleanValue()) {
            this.disable();
        }
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : AutoConcrete.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityItem || !new AxisAlignedBB(pos).func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }
}
