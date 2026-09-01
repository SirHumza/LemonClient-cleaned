/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockShulkerBox
 *  net.minecraft.client.gui.inventory.GuiShulkerBox
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package com.lemonclient.client.module.modules.dev;

import com.lemonclient.api.event.events.DeathEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.modules.gui.ColorMain;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

@Module.Declaration(name="AutoShulker", category=Category.Dev)
public class AutoShulker
extends Module {
    BooleanSetting once = this.registerBoolean("Once", false);
    IntegerSetting counts = this.registerInteger("EmptySlots", 6, 1, 36, () -> (Boolean)this.once.getValue() == false);
    BooleanSetting disable = this.registerBoolean("Disable After Death", true, () -> (Boolean)this.once.getValue() == false);
    DoubleSetting range = this.registerDouble("Range", 5.0, 0.0, 10.0);
    DoubleSetting yRange = this.registerDouble("YRange", 5.0, 0.0, 10.0);
    DoubleSetting targetRange = this.registerDouble("Target Range", 8.0, 0.0, 16.0);
    IntegerSetting tickDelay = this.registerInteger("Tick Delay", 5, 0, 10);
    IntegerSetting openDelay = this.registerInteger("Open Delay", 5, 0, 10);
    BooleanSetting inventory = this.registerBoolean("Inventory", true);
    IntegerSetting Slot = this.registerInteger("Slot", 1, 1, 9);
    BooleanSetting packetPlace = this.registerBoolean("Packet Place", true);
    BooleanSetting placeSwing = this.registerBoolean("Place Swing", true);
    BooleanSetting packetSwing = this.registerBoolean("Packet Swing", true);
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true);
    private int delayTimeTicks;
    BlockPos playerPos;
    ShulkerPos blockAim;
    List<BlockPos> list = new ArrayList<BlockPos>();
    int slot;
    boolean swapped = false;
    int tick = 0;
    @EventHandler
    private final Listener<DeathEvent> deathEventListener = new Listener<DeathEvent>(event -> {
        if (event.player == AutoShulker.mc.field_71439_g && ((Boolean)this.disable.getValue()).booleanValue()) {
            this.disable();
        }
    }, new Predicate[0]);

    private void switchTo(int slot, Runnable runnable) {
        int oldslot = AutoShulker.mc.field_71439_g.field_71071_by.field_70461_c;
        if (slot < 0 || slot == oldslot) {
            runnable.run();
            return;
        }
        if (slot < 9) {
            boolean packetSwitch = (Boolean)this.packetSwitch.getValue();
            if (packetSwitch) {
                AutoShulker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            } else {
                AutoShulker.mc.field_71439_g.field_71071_by.field_70461_c = slot;
            }
            runnable.run();
            if (packetSwitch) {
                AutoShulker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(oldslot));
            } else {
                AutoShulker.mc.field_71439_g.field_71071_by.field_70461_c = oldslot;
            }
        }
    }

    private int getShulkerSlot() {
        for (int i = 0; i < AutoShulker.mc.field_71439_g.field_71071_by.field_70462_a.size(); ++i) {
            if (!(AutoShulker.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() instanceof ItemBlock) || !(((ItemBlock)AutoShulker.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b()).func_179223_d() instanceof BlockShulkerBox)) continue;
            return i;
        }
        return -1;
    }

    private boolean isPos2(BlockPos pos1, BlockPos pos2) {
        if (pos1 == null || pos2 == null) {
            return false;
        }
        return pos1.field_177962_a == pos2.field_177962_a && pos1.field_177960_b == pos2.field_177960_b && pos1.field_177961_c == pos2.field_177961_c;
    }

    private void initValues() {
        List<BlockPos> blocks = EntityUtil.getSphere(PlayerUtil.getEyesPos(), (Double)this.range.getValue() + 1.0, (Double)this.yRange.getValue() + 1.0, false, true, 0);
        blocks.removeIf(p -> ColorMain.INSTANCE.breakList.contains(p) || this.list.contains(p));
        ArrayList posList = new ArrayList();
        blocks.forEach(pos -> {
            EnumFacing facing = this.getFacing((BlockPos)pos);
            if (facing == null) {
                return;
            }
            BlockPos neighbour = pos.func_177972_a(facing);
            EnumFacing opposite = facing.func_176734_d();
            Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
            if (this.inRange(hitVec)) {
                posList.add(new ShulkerPos((BlockPos)pos, facing, neighbour, opposite, hitVec));
            }
        });
        EntityPlayer target = PlayerUtil.getNearestPlayer(12.0);
        this.blockAim = target == null ? (ShulkerPos)posList.stream().min(Comparator.comparing(p -> p.getRange((EntityPlayer)AutoShulker.mc.field_71439_g))).orElse(null) : (ShulkerPos)posList.stream().max(Comparator.comparing(p -> this.getWeight((ShulkerPos)p, target))).orElse(null);
        if (this.blockAim == null) {
            return;
        }
        this.list.add(this.blockAim.pos);
    }

    private double getWeight(ShulkerPos pos, EntityPlayer target) {
        double range = pos.getRange(target);
        if (range >= (Double)this.targetRange.getValue()) {
            int y = 256 - pos.pos.func_177956_o();
            range += (double)(y * 100);
        }
        return range;
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : AutoShulker.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityItem || !new AxisAlignedBB(pos).func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    private EnumFacing getFacing(BlockPos pos) {
        if (this.intersectsWithEntity(pos) || !BlockUtil.canReplace(pos) && !(BlockUtil.getBlock(pos) instanceof BlockShulkerBox)) {
            return null;
        }
        for (EnumFacing facing : EnumFacing.field_82609_l) {
            if (!BlockUtil.canBeClicked(pos.func_177972_a(facing)) || !BlockUtil.airBlocks.contains(AutoShulker.mc.field_71441_e.func_180495_p(pos.func_177967_a(facing, -1)).func_177230_c())) continue;
            return facing;
        }
        return null;
    }

    private boolean inRange(Vec3d vec) {
        double x = vec.field_72450_a - AutoShulker.mc.field_71439_g.field_70165_t;
        double z = vec.field_72449_c - AutoShulker.mc.field_71439_g.field_70161_v;
        double y = vec.field_72448_b - (double)PlayerUtil.getEyesPos().field_177960_b;
        double add = Math.sqrt(y * y) / 2.0;
        return x * x + z * z <= ((Double)this.range.getValue() - add) * ((Double)this.range.getValue() - add) && y * y <= (Double)this.yRange.getValue() * (Double)this.yRange.getValue();
    }

    private boolean inRange(BlockPos pos) {
        double x = (double)pos.field_177962_a + 0.5 - AutoShulker.mc.field_71439_g.field_70165_t;
        double z = (double)pos.field_177961_c + 0.5 - AutoShulker.mc.field_71439_g.field_70161_v;
        double y = (double)pos.field_177960_b + 0.5 - (double)PlayerUtil.getEyesPos().field_177960_b;
        double add = Math.sqrt(y * y) / 2.0;
        return x * x + z * z <= ((Double)this.range.getValue() - add) * ((Double)this.range.getValue() - add) && y * y <= (Double)this.yRange.getValue() * (Double)this.yRange.getValue();
    }

    @Override
    public void onUpdate() {
        if (AutoShulker.mc.field_71439_g == null) {
            return;
        }
        if (this.tick++ >= (Integer)this.openDelay.getValue()) {
            if (this.blockAim != null && !BlockUtil.isAir(this.blockAim.pos) && !BlockUtil.canReplace(this.blockAim.pos)) {
                this.openBlock();
            }
            this.tick = 0;
        }
        if (AutoShulker.mc.field_71462_r instanceof GuiShulkerBox) {
            if (((Boolean)this.once.getValue()).booleanValue()) {
                this.disable();
            }
            this.blockAim = null;
            return;
        }
        if (this.delayTimeTicks++ < (Integer)this.tickDelay.getValue()) {
            return;
        }
        this.delayTimeTicks = 0;
        this.slot = this.getShulkerSlot();
        if (this.slot == -1) {
            return;
        }
        if (((Boolean)this.once.getValue()).booleanValue() || InventoryUtil.getEmptyCounts() >= (Integer)this.counts.getValue()) {
            if (this.blockAim == null) {
                this.initValues();
            }
        } else {
            this.checkPos();
        }
        if (this.blockAim != null) {
            if (!this.inRange(this.blockAim.pos)) {
                this.blockAim = null;
                return;
            }
        } else {
            if (((Boolean)this.once.getValue()).booleanValue()) {
                this.disable();
            }
            return;
        }
        if (this.slot > 8 && !this.swapped) {
            if (!((Boolean)this.inventory.getValue()).booleanValue()) {
                return;
            }
            AutoShulker.mc.field_71442_b.func_187098_a(0, this.slot, ((Integer)this.Slot.getValue()).intValue(), ClickType.SWAP, (EntityPlayer)AutoShulker.mc.field_71439_g);
            AutoShulker.mc.field_71442_b.func_78765_e();
            this.swapped = true;
            if ((Integer)this.tickDelay.getValue() != 0) {
                return;
            }
        }
        if (BlockUtil.isAir(this.blockAim.pos) || BlockUtil.canReplace(this.blockAim.pos)) {
            this.switchTo(this.slot, () -> {
                boolean sneak = false;
                if (BlockUtil.blackList.contains(AutoShulker.mc.field_71441_e.func_180495_p(this.blockAim.neighbour).func_177230_c()) && !AutoShulker.mc.field_71439_g.func_70093_af()) {
                    AutoShulker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)AutoShulker.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
                    sneak = true;
                }
                BurrowUtil.rightClickBlock(this.blockAim.neighbour, this.blockAim.vec, EnumHand.MAIN_HAND, this.blockAim.opposite, (boolean)((Boolean)this.packetPlace.getValue()));
                if (sneak) {
                    AutoShulker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)AutoShulker.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
                }
                if (((Boolean)this.placeSwing.getValue()).booleanValue()) {
                    this.swing();
                }
                this.tick = 0;
            });
            if ((Integer)this.tickDelay.getValue() == 0) {
                this.openBlock();
            }
        } else {
            this.openBlock();
        }
    }

    private void checkPos() {
        if (!this.isPos2(PlayerUtil.getPlayerPos(), this.playerPos)) {
            this.list = new ArrayList<BlockPos>();
            this.playerPos = PlayerUtil.getPlayerPos();
        }
    }

    private void swing() {
        if (((Boolean)this.packetSwing.getValue()).booleanValue()) {
            AutoShulker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
        } else {
            AutoShulker.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        }
    }

    private void openBlock() {
        EnumFacing side = EnumFacing.func_190914_a((BlockPos)this.blockAim.pos, (EntityLivingBase)AutoShulker.mc.field_71439_g);
        BlockPos neighbour = this.blockAim.pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        AutoShulker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)AutoShulker.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
        AutoShulker.mc.field_71442_b.func_187099_a(AutoShulker.mc.field_71439_g, AutoShulker.mc.field_71441_e, this.blockAim.pos, opposite, hitVec, EnumHand.MAIN_HAND);
    }

    @Override
    public void onEnable() {
        this.blockAim = null;
        this.checkPos();
    }

    static class ShulkerPos {
        BlockPos pos;
        EnumFacing facing;
        Vec3d vec;
        BlockPos neighbour;
        EnumFacing opposite;

        public ShulkerPos(BlockPos pos, EnumFacing facing, BlockPos neighbour, EnumFacing opposite, Vec3d vec3d) {
            this.pos = pos;
            this.facing = facing;
            this.neighbour = neighbour;
            this.opposite = opposite;
            this.vec = vec3d;
        }

        public double getRange(EntityPlayer player) {
            return player.func_70011_f((double)this.pos.field_177962_a + 0.5, (double)this.pos.field_177960_b + 0.5, (double)this.pos.field_177961_c + 0.5);
        }
    }
}
