/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Enchantments
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.server.SPacketBlockBreakAnim
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package com.lemonclient.client.module.modules.render;

import com.lemonclient.api.event.events.DrawBlockDamageEvent;
import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.event.events.RenderEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.api.util.render.RenderUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Module.Declaration(name="BreakHighlight", category=Category.Render)
public class BreakHighlight
extends Module {
    public static BreakHighlight INSTANCE;
    BooleanSetting cancelAnimation = this.registerBoolean("No Animation", true);
    IntegerSetting range = this.registerInteger("Range", 64, 0, 256);
    IntegerSetting playerRange = this.registerInteger("Player Range", 16, 0, 64);
    BooleanSetting showProgress = this.registerBoolean("Show Progress", false);
    IntegerSetting decimal = this.registerInteger("Decimal", 2, 0, 2, () -> (Boolean)this.showProgress.getValue());
    BooleanSetting doubleMine = this.registerBoolean("Double Mine", true);
    ColorSetting nameColor = this.registerColor("Name Color", new GSColor(255, 255, 255));
    ModeSetting renderType = this.registerMode("Render", Arrays.asList("Outline", "Fill", "Both"), "Both");
    ColorSetting color = this.registerColor("Color", new GSColor(0, 255, 0, 255));
    ColorSetting dColor = this.registerColor("Double Color", new GSColor(0, 255, 0, 255), () -> (Boolean)this.doubleMine.getValue());
    IntegerSetting alpha = this.registerInteger("Alpha", 100, 0, 255);
    IntegerSetting outAlpha = this.registerInteger("Outline Alpha", 255, 0, 255);
    IntegerSetting width = this.registerInteger("Width", 1, 0, 5);
    DoubleSetting scale = this.registerDouble("Text Scale", 0.025, 0.01, 0.05);
    HashMap<EntityPlayer, renderBlock> list = new HashMap();
    BlockPos lastBreak;
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener = new Listener<PacketEvent.Receive>(event -> {
        if (BreakHighlight.mc.field_71441_e == null || BreakHighlight.mc.field_71439_g == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketBlockBreakAnim) {
            SPacketBlockBreakAnim packet = (SPacketBlockBreakAnim)event.getPacket();
            BlockPos blockPos = packet.func_179821_b();
            if (BreakHighlight.mc.field_71439_g.func_174818_b(blockPos) > (double)((Integer)this.range.getValue() * (Integer)this.range.getValue())) {
                return;
            }
            EntityPlayer entityPlayer = (EntityPlayer)BreakHighlight.mc.field_71441_e.func_73045_a(packet.func_148845_c());
            if (entityPlayer == null) {
                return;
            }
            if (this.list.containsKey(entityPlayer)) {
                if (this.isPos2(this.list.get(entityPlayer).pos.pos, blockPos)) {
                    return;
                }
                this.list.get(entityPlayer).pos.updatePos(blockPos);
            } else {
                this.list.put(entityPlayer, new renderBlock(new breakPos(blockPos), entityPlayer));
            }
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<DrawBlockDamageEvent> drawBlockDamageEventListener = new Listener<DrawBlockDamageEvent>(event -> {
        if (((Boolean)this.cancelAnimation.getValue()).booleanValue()) {
            event.cancel();
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.PostSend> listener = new Listener<PacketEvent.PostSend>(event -> {
        CPacketPlayerDigging packet;
        if (event.getPacket() instanceof CPacketPlayerDigging && (packet = (CPacketPlayerDigging)event.getPacket()).func_180762_c() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
            this.lastBreak = packet.func_179715_a();
        }
    }, new Predicate[0]);

    public BreakHighlight() {
        INSTANCE = this;
    }

    private boolean isPos2(BlockPos pos1, BlockPos pos2) {
        if (pos1 == null || pos2 == null) {
            return false;
        }
        return pos1.field_177962_a == pos2.field_177962_a && pos1.field_177960_b == pos2.field_177960_b && pos1.field_177961_c == pos2.field_177961_c;
    }

    @Override
    public void onWorldRender(RenderEvent event) {
        if (BreakHighlight.mc.field_71439_g == null || BreakHighlight.mc.field_71441_e == null) {
            this.list.clear();
            return;
        }
        List playerList = BreakHighlight.mc.field_71441_e.field_73010_i;
        for (EntityPlayer player : playerList) {
            if (!this.list.containsKey(player)) continue;
            BlockPos pos = this.list.get(player).pos.pos;
            BlockPos dPos = this.list.get(player).pos.dPos;
            if (pos != null && BreakHighlight.mc.field_71441_e.func_180495_p(pos).func_185887_b((World)BreakHighlight.mc.field_71441_e, pos) < 0.0f) {
                this.list.get(player).pos.remove();
            }
            if (dPos != null && BreakHighlight.mc.field_71441_e.func_180495_p(dPos).func_185887_b((World)BreakHighlight.mc.field_71441_e, dPos) < 0.0f) {
                this.list.get(player).pos.removeDouble();
            }
            if (this.isPos2(pos, dPos)) {
                dPos = null;
            }
            if (pos == null && dPos == null) continue;
            int rangeSq = (Integer)this.range.getValue() * (Integer)this.range.getValue();
            int playerSq = (Integer)this.playerRange.getValue() * (Integer)this.playerRange.getValue();
            if (pos != null && BreakHighlight.mc.field_71439_g.func_174818_b(pos) > (double)rangeSq && dPos != null && BreakHighlight.mc.field_71439_g.func_174818_b(dPos) > (double)rangeSq) continue;
            if (pos != null && player.func_174818_b(pos) > (double)playerSq && dPos != null && player.func_174818_b(dPos) > (double)playerSq) {
                this.list.remove(player);
                continue;
            }
            this.list.get(player).update();
        }
    }

    public static GSColor getRainbowColor(int damage) {
        return GSColor.fromHSB((float)((1 + damage * 32) % 11520) / 11520.0f, 1.0f, 1.0f);
    }

    private void renderBox(breakPos pos, EntityPlayer player) {
        String[] name = new String[]{player.func_70005_c_()};
        BlockPos blockPos = pos.pos;
        if (blockPos != null) {
            float mineDamage = (float)(System.currentTimeMillis() - pos.start) / (float)pos.time;
            if (mineDamage > 1.0f) {
                mineDamage = 1.0f;
            }
            AxisAlignedBB getSelectedBoundingBox = new AxisAlignedBB(blockPos);
            Vec3d getCenter = getSelectedBoundingBox.func_189972_c();
            float prognum = mineDamage * 100.0f;
            if (((Boolean)this.showProgress.getValue()).booleanValue()) {
                String[] progress = new String[]{String.format("%.0f", Float.valueOf(prognum))};
                if ((Integer)this.decimal.getValue() == 1) {
                    progress = new String[]{String.format("%.1f", Float.valueOf(prognum))};
                } else if ((Integer)this.decimal.getValue() == 2) {
                    progress = new String[]{String.format("%.2f", Float.valueOf(prognum))};
                }
                RenderUtil.drawNametag((double)blockPos.func_177958_n() + 0.5, (double)blockPos.func_177956_o() + 0.39, (double)blockPos.func_177952_p() + 0.5, progress, BreakHighlight.getRainbowColor((int)prognum), 1, (Double)this.scale.getValue(), 0.0);
                RenderUtil.drawNametag((double)blockPos.func_177958_n() + 0.5, (double)blockPos.func_177956_o() + 0.61, (double)blockPos.func_177952_p() + 0.5, name, new GSColor(this.nameColor.getColor(), 255), 1, (Double)this.scale.getValue(), 0.0);
            } else {
                RenderUtil.drawNametag((double)blockPos.func_177958_n() + 0.5, (double)blockPos.func_177956_o() + 0.5, (double)blockPos.func_177952_p() + 0.5, name, new GSColor(this.nameColor.getColor(), 255), 1, (Double)this.scale.getValue(), 0.0);
            }
            this.renderESP(new AxisAlignedBB(getCenter.field_72450_a, getCenter.field_72448_b, getCenter.field_72449_c, getCenter.field_72450_a, getCenter.field_72448_b, getCenter.field_72449_c).func_72314_b((getSelectedBoundingBox.field_72340_a - getSelectedBoundingBox.field_72336_d) * 0.5 * (double)MathHelper.func_76131_a((float)mineDamage, (float)0.0f, (float)1.0f), (getSelectedBoundingBox.field_72338_b - getSelectedBoundingBox.field_72337_e) * 0.5 * (double)MathHelper.func_76131_a((float)mineDamage, (float)0.0f, (float)1.0f), (getSelectedBoundingBox.field_72339_c - getSelectedBoundingBox.field_72334_f) * 0.5 * (double)MathHelper.func_76131_a((float)mineDamage, (float)0.0f, (float)1.0f)), false);
        }
        if (!((Boolean)this.doubleMine.getValue()).booleanValue()) {
            return;
        }
        BlockPos doubleBlockPos = pos.dPos;
        if (doubleBlockPos != null) {
            float doubleMineDamage = (float)(System.currentTimeMillis() - pos.dStart) / (float)pos.dTime;
            if (doubleMineDamage > 1.0f) {
                doubleMineDamage = 1.0f;
            }
            AxisAlignedBB getDoubleSelectedBoundingBox = new AxisAlignedBB(doubleBlockPos);
            Vec3d getDoubleCenter = getDoubleSelectedBoundingBox.func_189972_c();
            float doublePrognum = doubleMineDamage * 100.0f;
            if (((Boolean)this.showProgress.getValue()).booleanValue()) {
                String[] progress = new String[]{String.format("%.0f", Float.valueOf(doublePrognum))};
                if ((Integer)this.decimal.getValue() == 1) {
                    progress = new String[]{String.format("%.1f", Float.valueOf(doublePrognum))};
                } else if ((Integer)this.decimal.getValue() == 2) {
                    progress = new String[]{String.format("%.2f", Float.valueOf(doublePrognum))};
                }
                RenderUtil.drawNametag((double)doubleBlockPos.func_177958_n() + 0.5, (double)doubleBlockPos.func_177956_o() + 0.39, (double)doubleBlockPos.func_177952_p() + 0.5, progress, BreakHighlight.getRainbowColor((int)doublePrognum), 1, (Double)this.scale.getValue(), 0.0);
                RenderUtil.drawNametag((double)doubleBlockPos.func_177958_n() + 0.5, (double)doubleBlockPos.func_177956_o() + 0.61, (double)doubleBlockPos.func_177952_p() + 0.5, name, new GSColor(this.nameColor.getColor(), 255), 1, (Double)this.scale.getValue(), 0.0);
            } else {
                RenderUtil.drawNametag((double)doubleBlockPos.func_177958_n() + 0.5, (double)doubleBlockPos.func_177956_o() + 0.5, (double)doubleBlockPos.func_177952_p() + 0.5, name, new GSColor(this.nameColor.getColor(), 255), 1, (Double)this.scale.getValue(), 0.0);
            }
            this.renderESP(new AxisAlignedBB(getDoubleCenter.field_72450_a, getDoubleCenter.field_72448_b, getDoubleCenter.field_72449_c, getDoubleCenter.field_72450_a, getDoubleCenter.field_72448_b, getDoubleCenter.field_72449_c).func_72314_b((getDoubleSelectedBoundingBox.field_72340_a - getDoubleSelectedBoundingBox.field_72336_d) * 0.5 * (double)MathHelper.func_76131_a((float)doubleMineDamage, (float)0.0f, (float)1.0f), (getDoubleSelectedBoundingBox.field_72338_b - getDoubleSelectedBoundingBox.field_72337_e) * 0.5 * (double)MathHelper.func_76131_a((float)doubleMineDamage, (float)0.0f, (float)1.0f), (getDoubleSelectedBoundingBox.field_72339_c - getDoubleSelectedBoundingBox.field_72334_f) * 0.5 * (double)MathHelper.func_76131_a((float)doubleMineDamage, (float)0.0f, (float)1.0f)), true);
        }
    }

    private void renderESP(AxisAlignedBB axisAlignedBB, boolean dm) {
        GSColor fillColor = new GSColor(dm ? this.dColor.getValue() : this.color.getValue(), (Integer)this.alpha.getValue());
        GSColor outlineColor = new GSColor(dm ? this.dColor.getValue() : this.color.getValue(), (Integer)this.outAlpha.getValue());
        switch ((String)this.renderType.getValue()) {
            case "Fill": {
                RenderUtil.drawBox(axisAlignedBB, true, 0.0, fillColor, 63);
                break;
            }
            case "Outline": {
                RenderUtil.drawBoundingBox(axisAlignedBB, (double)((Integer)this.width.getValue()).intValue(), outlineColor);
                break;
            }
            default: {
                RenderUtil.drawBox(axisAlignedBB, true, 0.0, fillColor, 63);
                RenderUtil.drawBoundingBox(axisAlignedBB, (double)((Integer)this.width.getValue()).intValue(), outlineColor);
            }
        }
    }

    private int calcBreakTime(BlockPos pos) {
        if (pos == null) {
            return -1;
        }
        IBlockState blockState = BreakHighlight.mc.field_71441_e.func_180495_p(pos);
        float hardness = blockState.func_185887_b((World)BreakHighlight.mc.field_71441_e, pos);
        float breakSpeed = this.getBreakSpeed(pos, blockState);
        if (breakSpeed == -1.0f) {
            return -1;
        }
        float relativeDamage = breakSpeed / hardness / 30.0f;
        int ticks = (int)Math.ceil(0.7f / relativeDamage);
        return ticks * 50;
    }

    private float getBreakSpeed(BlockPos pos, IBlockState blockState) {
        float maxSpeed = 1.0f;
        int slot = this.findItem(pos);
        float speed = BreakHighlight.mc.field_71439_g.field_71071_by.func_70301_a(slot).func_150997_a(blockState);
        if (speed <= 1.0f) {
            return maxSpeed;
        }
        int efficiency = EnchantmentHelper.func_77506_a((Enchantment)Enchantments.field_185305_q, (ItemStack)BreakHighlight.mc.field_71439_g.field_71071_by.func_70301_a(slot));
        if (efficiency > 0) {
            speed += (float)(efficiency * efficiency) + 1.0f;
        }
        if (speed > maxSpeed) {
            maxSpeed = speed;
        }
        return maxSpeed;
    }

    public int findItem(BlockPos pos) {
        if (pos == null) {
            return BreakHighlight.mc.field_71439_g.field_71071_by.field_70461_c;
        }
        return BreakHighlight.findBestTool(pos, BreakHighlight.mc.field_71441_e.func_180495_p(pos));
    }

    public static int findBestTool(BlockPos pos, IBlockState state) {
        int result = BreakHighlight.mc.field_71439_g.field_71071_by.field_70461_c;
        if (state.func_185887_b((World)BreakHighlight.mc.field_71441_e, pos) > 0.0f) {
            double speed = BreakHighlight.getSpeed(state, BreakHighlight.mc.field_71439_g.func_184614_ca());
            for (int i = 0; i < 36; ++i) {
                ItemStack stack = BreakHighlight.mc.field_71439_g.field_71071_by.func_70301_a(i);
                double stackSpeed = BreakHighlight.getSpeed(state, stack);
                if (!(stackSpeed > speed)) continue;
                speed = stackSpeed;
                result = i;
            }
        }
        return result;
    }

    public static double getSpeed(IBlockState state, ItemStack stack) {
        double str = stack.func_150997_a(state);
        int effect = EnchantmentHelper.func_77506_a((Enchantment)Enchantments.field_185305_q, (ItemStack)stack);
        return Math.max(str + (str > 1.0 ? (double)(effect * effect) + 1.0 : 0.0), 0.0);
    }

    public static class breakPos {
        private BlockPos pos;
        private BlockPos dPos = null;
        private long start;
        private long dStart;
        private long time;
        private long dTime;

        public breakPos(BlockPos pos) {
            this.pos = pos;
            this.start = System.currentTimeMillis();
            this.time = INSTANCE.calcBreakTime(pos);
        }

        public void updatePos(BlockPos pos) {
            if (this.dPos == null) {
                this.dPos = this.pos;
                this.dStart = this.start;
                this.dTime = (long)((double)this.time * 1.4);
            }
            this.pos = pos;
            this.start = System.currentTimeMillis();
            this.time = INSTANCE.calcBreakTime(pos);
        }

        public long getEnd() {
            return this.start + this.time;
        }

        public void update() {
            this.time = INSTANCE.calcBreakTime(this.pos);
            if (this.dPos != null && BlockUtil.airBlocks.contains(mc.field_71441_e.func_180495_p(this.dPos).func_177230_c())) {
                this.removeDouble();
            }
        }

        public void remove() {
            this.pos = null;
        }

        public void removeDouble() {
            this.dPos = null;
        }
    }

    class renderBlock {
        private final breakPos pos;
        private final EntityPlayer player;

        public renderBlock(breakPos pos, EntityPlayer player) {
            this.pos = pos;
            this.player = player;
        }

        void update() {
            this.pos.update();
            BreakHighlight.this.renderBox(this.pos, this.player);
        }
    }
}
