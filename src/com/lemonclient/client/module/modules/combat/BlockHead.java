/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityArmorStand
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.api.util.world.HoleUtil;
import com.lemonclient.client.LemonClient;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.modules.dev.PistonAura;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

@Module.Declaration(name="BlockHead", category=Category.Combat)
public class BlockHead
extends Module {
    IntegerSetting delay = this.registerInteger("Delay", 0, 0, 20);
    DoubleSetting range = this.registerDouble("Range", 5.0, 0.0, 10.0);
    IntegerSetting maxTarget = this.registerInteger("Max Target", 1, 1, 10);
    DoubleSetting maxSpeed = this.registerDouble("Max Target Speed", 10.0, 0.0, 50.0);
    IntegerSetting bpt = this.registerInteger("BlocksPerTick", 4, 0, 20);
    BooleanSetting rotate = this.registerBoolean("Rotate", false);
    BooleanSetting packet = this.registerBoolean("Packet Place", false);
    BooleanSetting swing = this.registerBoolean("Swing", false);
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true);
    BooleanSetting pause = this.registerBoolean("BedrockHole", true);
    int ob;
    int waited;
    int placed;
    BlockPos[] block = new BlockPos[]{new BlockPos(0, 0, 0), new BlockPos(0, 1, 0)};
    BlockPos[] sides = new BlockPos[]{new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1)};

    public static boolean isPlayerInHole(EntityPlayer target) {
        BlockPos blockPos = BlockHead.getLocalPlayerPosFloored(target);
        HoleUtil.HoleInfo holeInfo = HoleUtil.isHole(blockPos, true, true, false);
        HoleUtil.HoleType holeType = holeInfo.getType();
        return holeType == HoleUtil.HoleType.SINGLE;
    }

    public static BlockPos getLocalPlayerPosFloored(EntityPlayer target) {
        return new BlockPos(target.func_174791_d());
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : BlockHead.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityItem || entity instanceof EntityArmorStand || !new AxisAlignedBB(pos).func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    @Override
    public void onUpdate() {
        if (BlockHead.mc.field_71441_e == null || BlockHead.mc.field_71439_g == null || BlockHead.mc.field_71439_g.field_70128_L) {
            return;
        }
        this.placed = 0;
        if (this.waited++ < (Integer)this.delay.getValue()) {
            return;
        }
        this.waited = 0;
        this.ob = BurrowUtil.findHotbarBlock(BlockObsidian.class);
        if (this.ob == -1) {
            return;
        }
        for (EntityPlayer target : PlayerUtil.getNearPlayers((Double)this.range.getValue(), (Integer)this.maxTarget.getValue())) {
            BlockPos placePos;
            if (target == null || EntityUtil.isDead((Entity)target) || LemonClient.speedUtil.getPlayerSpeed(target) > (Double)this.maxSpeed.getValue() || !BlockHead.isPlayerInHole(target)) continue;
            BlockPos pos = new BlockPos(target.field_70165_t, target.field_70163_u + 0.5, target.field_70161_v);
            int bedrock = 0;
            for (BlockPos side2 : this.sides) {
                if (BlockHead.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)side2)).func_177230_c() != Blocks.field_150357_h) continue;
                ++bedrock;
            }
            if (bedrock >= 4 && !((Boolean)this.pause.getValue()).booleanValue() || !BlockUtil.isAir(placePos = pos.func_177981_b(2)) || this.intersectsWithEntity(placePos)) continue;
            if (BurrowUtil.getFirstFacing(pos.func_177981_b(2)) == null) {
                BlockPos crystalPos;
                BlockPos side;
                int n;
                BlockPos side2;
                ArrayList<Object> posList = new ArrayList<Object>();
                ArrayList<BlockPos> list = new ArrayList<BlockPos>();
                side2 = this.sides;
                int n2 = ((BlockPos[])side2).length;
                for (n = 0; n < n2; ++n) {
                    side = side2[n];
                    crystalPos = pos.func_177971_a((Vec3i)side);
                    if (!PistonAura.INSTANCE.canPistonCrystal(crystalPos, pos)) {
                        posList.add(crystalPos);
                    }
                    list.add(crystalPos);
                }
                if (posList.isEmpty()) {
                    side2 = this.sides;
                    n2 = ((BlockPos[])side2).length;
                    for (n = 0; n < n2; ++n) {
                        side = side2[n];
                        crystalPos = pos.func_177971_a((Vec3i)side);
                        if (!PistonAura.INSTANCE.canPistonCrystal(crystalPos.func_177984_a(), pos)) {
                            posList.add(crystalPos);
                        }
                        list.add(crystalPos);
                    }
                }
                if (posList.isEmpty()) {
                    posList.addAll(list);
                }
                if ((side2 = (BlockPos)posList.stream().max(Comparator.comparing(PlayerUtil::getDistance)).orElse(null)) == null) continue;
                BlockPos[] blockPosArray = this.block;
                n = blockPosArray.length;
                for (int i = 0; i < n; ++i) {
                    BlockPos add = blockPosArray[i];
                    if (this.placed > (Integer)this.bpt.getValue()) {
                        return;
                    }
                    BlockPos obsi = side2.func_177984_a().func_177971_a((Vec3i)add);
                    if (this.intersectsWithEntity(obsi) || !BlockUtil.canReplace(obsi)) continue;
                    InventoryUtil.run(this.ob, (Boolean)this.packetSwitch.getValue(), () -> BurrowUtil.placeBlock(obsi, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue()));
                    ++this.placed;
                }
            }
            if (this.placed > (Integer)this.bpt.getValue()) {
                return;
            }
            InventoryUtil.run(this.ob, (Boolean)this.packetSwitch.getValue(), () -> BurrowUtil.placeBlock(placePos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue()));
            ++this.placed;
        }
    }
}
