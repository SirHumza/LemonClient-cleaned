/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 */
package com.lemonclient.client.module.modules.qwq;

import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.Arrays;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

@Module.Declaration(name="NoteSpam", category=Category.qwq)
public class NoteSpam
extends Module {
    ModeSetting timeMode = this.registerMode("Time Mode", Arrays.asList("onUpdate", "Tick", "Fast"), "Fast");
    DoubleSetting range = this.registerDouble("Range", 5.5, 1.0, 10.0);
    IntegerSetting max = this.registerInteger("MaxBlocks", 30, 1, 150);

    @Override
    public void onUpdate() {
        if (((String)this.timeMode.getValue()).equalsIgnoreCase("onUpdate")) {
            this.doNoteSpam();
        }
    }

    @Override
    public void onTick() {
        if (((String)this.timeMode.getValue()).equalsIgnoreCase("Tick")) {
            this.doNoteSpam();
        }
    }

    @Override
    public void fast() {
        if (((String)this.timeMode.getValue()).equalsIgnoreCase("Fast")) {
            this.doNoteSpam();
        }
    }

    private void doNoteSpam() {
        if (NoteSpam.mc.field_71441_e == null || NoteSpam.mc.field_71439_g == null || NoteSpam.mc.field_71439_g.field_70128_L) {
            return;
        }
        int counter = 0;
        List<BlockPos> posList = EntityUtil.getSphere(PlayerUtil.getPlayerPos(), (Double)this.range.getValue(), (Double)this.range.getValue(), false, true, 0);
        for (BlockPos b : posList) {
            if (BlockUtil.getBlock(b) != Blocks.field_150323_B) continue;
            NoteSpam.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, b, EnumFacing.UP));
            if (++counter <= (Integer)this.max.getValue()) continue;
            return;
        }
    }
}
