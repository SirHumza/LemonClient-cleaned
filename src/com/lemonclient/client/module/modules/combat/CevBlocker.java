/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

@Module.Declaration(name="CevBlocker", category=Category.Combat)
public class CevBlocker
extends Module {
    ModeSetting time = this.registerMode("Time Mode", Arrays.asList("Tick", "onUpdate", "Both", "Fast"), "Tick");
    BooleanSetting high = this.registerBoolean("High Cev", true);
    BooleanSetting pa = this.registerBoolean("Ignore Bedrock", true);
    BooleanSetting bevel = this.registerBoolean("Bevel", true);
    BooleanSetting packet = this.registerBoolean("Packet Place", true);
    BooleanSetting swing = this.registerBoolean("Swing", true);
    BooleanSetting rotate = this.registerBoolean("Rotate", true);
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true);
    private List<BlockPos> cevPositions = new ArrayList<BlockPos>();

    private void switchTo(int slot, Runnable runnable) {
        int oldslot = CevBlocker.mc.field_71439_g.field_71071_by.field_70461_c;
        if (slot < 0 || slot == oldslot) {
            runnable.run();
            return;
        }
        if (slot < 9) {
            boolean packetSwitch = (Boolean)this.packetSwitch.getValue();
            if (packetSwitch) {
                CevBlocker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            } else {
                CevBlocker.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                CevBlocker.mc.field_71442_b.func_78765_e();
            }
            runnable.run();
            if (packetSwitch) {
                CevBlocker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(oldslot));
            } else {
                CevBlocker.mc.field_71439_g.field_71071_by.field_70461_c = oldslot;
                CevBlocker.mc.field_71442_b.func_78765_e();
            }
        }
    }

    @Override
    public void onUpdate() {
        if (((String)this.time.getValue()).equals("onUpdate") || ((String)this.time.getValue()).equals("Both")) {
            this.doBlock();
        }
    }

    @Override
    public void onTick() {
        if (((String)this.time.getValue()).equals("Tick") || ((String)this.time.getValue()).equals("Both")) {
            this.doBlock();
        }
    }

    @Override
    public void fast() {
        if (((String)this.time.getValue()).equals("Fast")) {
            this.doBlock();
        }
    }

    private void doBlock() {
        if (CevBlocker.mc.field_71441_e == null || CevBlocker.mc.field_71439_g == null) {
            return;
        }
        BlockPos[] highpos = new BlockPos[]{new BlockPos(0, 3, 0), new BlockPos(0, 4, 0), new BlockPos(1, 2, 0), new BlockPos(-1, 2, 0), new BlockPos(0, 2, 1), new BlockPos(0, 2, -1)};
        BlockPos[] hight2 = new BlockPos[]{new BlockPos(1, 2, 1), new BlockPos(1, 2, -1), new BlockPos(-1, 2, 1), new BlockPos(-1, 2, -1)};
        BlockPos[] offsets = new BlockPos[]{new BlockPos(0, 2, 0), new BlockPos(1, 1, 0), new BlockPos(-1, 1, 0), new BlockPos(0, 1, 1), new BlockPos(0, 1, -1)};
        BlockPos[] offsets2 = new BlockPos[]{new BlockPos(1, 1, 1), new BlockPos(1, 1, -1), new BlockPos(-1, 1, 1), new BlockPos(-1, 1, -1)};
        for (BlockPos offset : offsets) {
            this.check(offset);
        }
        if (((Boolean)this.high.getValue()).booleanValue()) {
            for (BlockPos offset : highpos) {
                this.check(offset);
            }
        }
        if (((Boolean)this.bevel.getValue()).booleanValue()) {
            for (BlockPos offset : offsets2) {
                this.check(offset);
            }
            if (((Boolean)this.high.getValue()).booleanValue()) {
                for (BlockPos offset : hight2) {
                    this.check(offset);
                }
            }
        }
        Iterator<BlockPos> iterator = this.cevPositions.iterator();
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            if (!Objects.isNull(this.getCrystal(pos))) continue;
            int obby = BurrowUtil.findHotbarBlock(BlockObsidian.class);
            if (obby == -1) {
                return;
            }
            this.switchTo(obby, () -> {
                if (CevBlocker.mc.field_71441_e.func_175623_d(pos)) {
                    BurrowUtil.placeBlock(pos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
                    BurrowUtil.placeBlock(pos.func_177984_a(), EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
                } else {
                    BurrowUtil.placeBlock(pos.func_177984_a(), EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue());
                }
            });
            iterator.remove();
        }
    }

    public void check(BlockPos offset) {
        BlockPos playerPos = PlayerUtil.getPlayerPos();
        BlockPos offsetPos = playerPos.func_177971_a((Vec3i)offset);
        Entity crystal = this.getCrystal(offsetPos);
        if (Objects.isNull(crystal)) {
            return;
        }
        BlockPos crystalPos = EntityUtil.getEntityPos(crystal).func_177977_b();
        if (((Boolean)this.pa.getValue()).booleanValue() && !CevBlocker.mc.field_71441_e.func_175623_d(crystalPos) && CevBlocker.mc.field_71441_e.func_180495_p(crystalPos).func_177230_c() != Blocks.field_150343_Z) {
            return;
        }
        if (!CevBlocker.mc.field_71441_e.func_175623_d(playerPos.func_177984_a().func_177984_a())) {
            CevBlocker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(CevBlocker.mc.field_71439_g.field_70165_t, (double)playerPos.func_177956_o() + 0.2, CevBlocker.mc.field_71439_g.field_70161_v, false));
        }
        CevBlocker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity(crystal));
        CevBlocker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
        if (!this.cevPositions.contains(crystalPos)) {
            this.cevPositions.add(crystalPos);
        }
    }

    private Entity getCrystal(BlockPos pos) {
        return CevBlocker.mc.field_71441_e.field_72996_f.stream().filter(e -> e instanceof EntityEnderCrystal).filter(e -> EntityUtil.getEntityPos(e).func_177977_b().equals((Object)pos)).min(Comparator.comparing(this::getDistance)).orElse(null);
    }

    public double getDistance(Entity e) {
        return CevBlocker.mc.field_71439_g.func_70032_d(e);
    }

    @Override
    public void onDisable() {
        this.cevPositions = new ArrayList<BlockPos>();
    }
}
