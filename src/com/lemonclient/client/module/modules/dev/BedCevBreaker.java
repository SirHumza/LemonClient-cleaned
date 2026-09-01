/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockBed
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Enchantments
 *  net.minecraft.init.Items
 *  net.minecraft.init.MobEffects
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemBed
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketClickWindow
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketVehicleMove
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.world.IBlockAccess
 */
package com.lemonclient.client.module.modules.dev;

import com.lemonclient.api.event.Phase;
import com.lemonclient.api.event.events.OnUpdateWalkingPlayerEvent;
import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.PlayerPacket;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.client.manager.managers.PlayerPacketManager;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.combat.AntiBurrow;
import com.lemonclient.client.module.modules.combat.AntiRegear;
import com.lemonclient.client.module.modules.exploits.PacketMine;
import com.lemonclient.client.module.modules.gui.ColorMain;
import com.lemonclient.mixin.mixins.accessor.AccessorCPacketVehicleMove;
import java.util.Comparator;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;

@Module.Declaration(name="BedCev", category=Category.Dev)
public class BedCevBreaker
extends Module {
    public static BedCevBreaker INSTANCE;
    IntegerSetting slotS = this.registerInteger("Slot", 1, 1, 9);
    IntegerSetting delay = this.registerInteger("Delay", 50, 0, 1000);
    BooleanSetting helpBlock = this.registerBoolean("Help Block", true);
    DoubleSetting maxRange = this.registerDouble("Max Range", 5.0, 0.0, 10.0, () -> (Boolean)this.helpBlock.getValue());
    BooleanSetting down = this.registerBoolean("Down Block", true, () -> (Boolean)this.helpBlock.getValue());
    BooleanSetting packet = this.registerBoolean("Packet Place", true);
    BooleanSetting rotate = this.registerBoolean("Rotate", false);
    BooleanSetting swing = this.registerBoolean("Swing", true);
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true);
    BooleanSetting instantMine = this.registerBoolean("Instant Mine", true);
    BooleanSetting pickBypass = this.registerBoolean("Pick Bypass", false);
    BooleanSetting strict = this.registerBoolean("Strict", false);
    public boolean working;
    boolean offhand;
    boolean start;
    boolean anyBed;
    int blockSlot;
    int bedSlot;
    int pickSlot;
    long time;
    EnumFacing facing;
    Vec2f rotation;
    Timing timer = new Timing();
    BlockPos[] side = new BlockPos[]{new BlockPos(0, 0, 1), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0)};
    @EventHandler
    private final Listener<OnUpdateWalkingPlayerEvent> onUpdateWalkingPlayerEventListener = new Listener<OnUpdateWalkingPlayerEvent>(event -> {
        if (this.rotation == null || event.getPhase() != Phase.PRE) {
            return;
        }
        PlayerPacket packet = new PlayerPacket((Module)this, new Vec2f(this.rotation.field_189982_i, PlayerPacketManager.INSTANCE.getServerSideRotation().field_189983_j));
        PlayerPacketManager.INSTANCE.addPacket(packet);
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.Send> sendListener = new Listener<PacketEvent.Send>(event -> {
        if (this.rotation != null) {
            if (event.getPacket() instanceof CPacketPlayer.Rotation) {
                ((CPacketPlayer.Rotation)event.getPacket()).field_149476_e = this.rotation.field_189982_i;
            }
            if (event.getPacket() instanceof CPacketPlayer.PositionRotation) {
                ((CPacketPlayer.PositionRotation)event.getPacket()).field_149476_e = this.rotation.field_189982_i;
            }
            if (event.getPacket() instanceof CPacketVehicleMove) {
                ((AccessorCPacketVehicleMove)event.getPacket()).setYaw(this.rotation.field_189982_i);
            }
        }
    }, new Predicate[0]);
    BlockPos placePos;
    int lastSlot;
    @EventHandler
    private final Listener<PacketEvent.PostSend> postSendListener = new Listener<PacketEvent.PostSend>(event -> {
        int slot;
        if (BedCevBreaker.mc.field_71441_e == null || BedCevBreaker.mc.field_71439_g == null) {
            return;
        }
        if (event.getPacket() instanceof CPacketHeldItemChange && (slot = ((CPacketHeldItemChange)event.getPacket()).func_149614_c()) != this.lastSlot) {
            this.lastSlot = slot;
            if (((Boolean)this.strict.getValue()).booleanValue()) {
                EnumFacing facing = BlockUtil.getRayTraceFacing(this.placePos, this.facing);
                BedCevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.placePos, facing));
                BedCevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.placePos, facing));
                if (((Boolean)this.swing.getValue()).booleanValue()) {
                    BedCevBreaker.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                }
                this.time = System.currentTimeMillis() + (long)this.calcBreakTime();
            }
        }
    }, new Predicate[0]);

    public BedCevBreaker() {
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        this.working = false;
    }

    public void refill_bed() {
        int airSlot;
        if ((!(BedCevBreaker.mc.field_71462_r instanceof GuiContainer) || BedCevBreaker.mc.field_71462_r instanceof GuiInventory) && (airSlot = this.isSpace()) != -1) {
            for (int i = 9; i < 36; ++i) {
                if (BedCevBreaker.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != Items.field_151104_aV) continue;
                BedCevBreaker.mc.field_71442_b.func_187098_a(0, i, airSlot, ClickType.SWAP, (EntityPlayer)BedCevBreaker.mc.field_71439_g);
            }
        }
    }

    private int isSpace() {
        int slot = -1;
        int slot1 = (Integer)this.slotS.getValue() - 1;
        if (BedCevBreaker.mc.field_71439_g.field_71071_by.func_70301_a(slot1).func_77973_b() != Items.field_151104_aV) {
            slot = slot1;
        }
        return slot;
    }

    @Override
    public void onEnable() {
        if (BedCevBreaker.mc.field_71476_x == null || BedCevBreaker.mc.field_71476_x.field_72313_a != RayTraceResult.Type.BLOCK || BedCevBreaker.mc.field_71441_e.func_180495_p(BedCevBreaker.mc.field_71476_x.func_178782_a()).func_177230_c() == Blocks.field_150357_h) {
            this.disable();
            return;
        }
        this.placePos = BedCevBreaker.mc.field_71476_x.func_178782_a();
        this.offhand = false;
        this.start = false;
        this.getItem();
        this.doBreak();
        this.timer.reset();
    }

    @Override
    public void fast() {
        this.working = false;
        if (BedCevBreaker.mc.field_71441_e == null || BedCevBreaker.mc.field_71439_g == null || this.placePos == null || BedCevBreaker.mc.field_71439_g.field_70128_L) {
            this.disable();
            return;
        }
        if (!this.canPlaceBedWithoutBase() || !this.space(this.placePos)) {
            this.disable();
            return;
        }
        this.refill_bed();
        this.getItem();
        if (!this.anyBed || this.blockSlot == -1 || this.pickSlot == -1) {
            this.disable();
            return;
        }
        if (this.bedSlot == -1) {
            return;
        }
        if (BedCevBreaker.mc.field_71441_e.func_175623_d(this.placePos.func_177978_c()) && BedCevBreaker.mc.field_71441_e.func_175623_d(this.placePos.func_177976_e()) && BedCevBreaker.mc.field_71441_e.func_175623_d(this.placePos.func_177974_f()) && BedCevBreaker.mc.field_71441_e.func_175623_d(this.placePos.func_177968_d())) {
            this.helpBlock(this.placePos);
            this.rotation = null;
            return;
        }
        if (AntiRegear.INSTANCE.working || AntiBurrow.INSTANCE.mining) {
            return;
        }
        BlockPos instantPos = null;
        if (ModuleManager.isModuleEnabled(PacketMine.class)) {
            instantPos = PacketMine.INSTANCE.packetPos;
        }
        if (instantPos != null && !this.isPos2(instantPos, this.placePos)) {
            if (instantPos.equals((Object)new BlockPos(BedCevBreaker.mc.field_71439_g.field_70165_t, BedCevBreaker.mc.field_71439_g.field_70163_u + 2.0, BedCevBreaker.mc.field_71439_g.field_70161_v))) {
                return;
            }
            if (instantPos.equals((Object)new BlockPos(BedCevBreaker.mc.field_71439_g.field_70165_t, BedCevBreaker.mc.field_71439_g.field_70163_u - 1.0, BedCevBreaker.mc.field_71439_g.field_70161_v))) {
                return;
            }
            if (BedCevBreaker.mc.field_71441_e.func_180495_p(instantPos).func_177230_c() == Blocks.field_150321_G) {
                return;
            }
            this.doBreak();
        }
        this.working = true;
        if (!this.start && BedCevBreaker.mc.field_71441_e.func_175623_d(this.placePos)) {
            this.time = System.currentTimeMillis() + (long)((Boolean)this.instantMine.getValue() != false ? 0 : this.calcBreakTime());
            this.start = true;
        }
        if (this.time > System.currentTimeMillis()) {
            return;
        }
        if (this.start && this.timer.passedMs(((Integer)this.delay.getValue()).intValue())) {
            BlockPos basePos;
            if (BlockUtil.isAir(this.placePos)) {
                this.run(this.blockSlot, false, () -> BurrowUtil.placeBlock(this.placePos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue()));
            }
            if (this.block(this.placePos.func_177974_f())) {
                this.rotation = new Vec2f(90.0f, 90.0f);
                basePos = this.placePos.func_177982_a(1, 0, 0);
            } else if (this.block(this.placePos.func_177978_c())) {
                this.rotation = new Vec2f(0.0f, 90.0f);
                basePos = this.placePos.func_177982_a(0, 0, -1);
            } else if (this.block(this.placePos.func_177976_e())) {
                this.rotation = new Vec2f(-90.0f, 90.0f);
                basePos = this.placePos.func_177982_a(-1, 0, 0);
            } else if (this.block(this.placePos.func_177968_d())) {
                this.rotation = new Vec2f(180.0f, 90.0f);
                basePos = this.placePos.func_177982_a(0, 0, 1);
            } else {
                this.rotation = null;
                return;
            }
            if (PlayerPacketManager.INSTANCE.getServerSideRotation().field_189982_i != this.rotation.field_189982_i) {
                return;
            }
            EnumHand hand = this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
            EnumFacing opposite = EnumFacing.DOWN.func_176734_d();
            Vec3d hitVec = new Vec3d((Vec3i)basePos).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
            if (BlockUtil.blackList.contains(BedCevBreaker.mc.field_71441_e.func_180495_p(basePos).func_177230_c()) && !ColorMain.INSTANCE.sneaking) {
                BedCevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BedCevBreaker.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            }
            this.run(this.bedSlot, false, () -> {
                if (((Boolean)this.packet.getValue()).booleanValue()) {
                    BedCevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(basePos, EnumFacing.UP, hand, 0.5f, 1.0f, 0.5f));
                } else {
                    BedCevBreaker.mc.field_71442_b.func_187099_a(BedCevBreaker.mc.field_71439_g, BedCevBreaker.mc.field_71441_e, basePos, EnumFacing.UP, hitVec, hand);
                }
                if (((Boolean)this.swing.getValue()).booleanValue()) {
                    BedCevBreaker.mc.field_71439_g.func_184609_a(hand);
                }
            });
            this.run(this.pickSlot, (Boolean)this.pickBypass.getValue(), () -> {
                this.facing = BlockUtil.getRayTraceFacing(this.placePos, EnumFacing.UP);
                BedCevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.placePos, this.facing));
                if (!((Boolean)this.instantMine.getValue()).booleanValue()) {
                    BedCevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.placePos, this.facing));
                    BedCevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.placePos, this.facing));
                    this.time = System.currentTimeMillis() + (long)this.calcBreakTime();
                }
                if (((Boolean)this.swing.getValue()).booleanValue()) {
                    BedCevBreaker.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                }
            });
            EnumFacing side = EnumFacing.UP;
            Vec3d vec = this.getHitVecOffset(side);
            BedCevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(this.placePos.func_177984_a(), side, hand, (float)vec.field_72450_a, (float)vec.field_72448_b, (float)vec.field_72449_c));
            if (((Boolean)this.swing.getValue()).booleanValue()) {
                BedCevBreaker.mc.field_71439_g.func_184609_a(hand);
            }
            this.timer.reset();
        }
    }

    private Vec3d getHitVecOffset(EnumFacing face) {
        Vec3i vec = face.func_176730_m();
        return new Vec3d((double)((float)vec.field_177962_a * 0.5f + 0.5f), (double)((float)vec.field_177960_b * 0.5f + 0.5f), (double)((float)vec.field_177961_c * 0.5f + 0.5f));
    }

    private void helpBlock(BlockPos pos) {
        NonNullList blocks = NonNullList.func_191196_a();
        for (BlockPos side : this.side) {
            blocks.add(pos.func_177971_a((Vec3i)side));
        }
        if (((Boolean)this.down.getValue()).booleanValue()) {
            blocks.add(pos.func_177977_b());
        }
        BlockPos finalPos = blocks.stream().filter(p -> BedCevBreaker.mc.field_71439_g.func_174818_b(p) <= (Double)this.maxRange.getValue() * (Double)this.maxRange.getValue()).filter(this::canPlaceBase).max(Comparator.comparing(p -> BedCevBreaker.mc.field_71439_g.func_174818_b(p))).orElse(null);
        this.run(this.blockSlot, false, () -> BurrowUtil.placeBlock(finalPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue()));
    }

    private boolean canPlaceBase(BlockPos pos) {
        if (ColorMain.INSTANCE.breakList.contains(pos)) {
            return false;
        }
        if (BurrowUtil.getBedFacing(pos) == null) {
            return false;
        }
        return this.space(pos) && !this.intersectsWithEntity(pos);
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : BedCevBreaker.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityItem || !new AxisAlignedBB(pos).func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    private boolean canPlaceBedWithoutBase() {
        return this.space(this.placePos) && (this.space(this.placePos.func_177974_f()) || this.space(this.placePos.func_177978_c()) || this.space(this.placePos.func_177976_e()) || this.space(this.placePos.func_177968_d()));
    }

    private boolean block(BlockPos pos) {
        if (BlockUtil.canReplace(pos)) {
            return false;
        }
        return this.space(pos) && this.solid(pos);
    }

    private boolean solid(BlockPos pos) {
        return !BlockUtil.isBlockUnSolid(pos) && !(BedCevBreaker.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockBed) && BedCevBreaker.mc.field_71441_e.func_180495_p(pos).isSideSolid((IBlockAccess)BedCevBreaker.mc.field_71441_e, pos, EnumFacing.UP) && BlockUtil.getBlock((BlockPos)pos).field_149787_q;
    }

    private boolean space(BlockPos pos) {
        return BedCevBreaker.mc.field_71441_e.func_180495_p(pos.func_177984_a()).func_177230_c() == Blocks.field_150324_C || BedCevBreaker.mc.field_71441_e.func_175623_d(pos.func_177984_a());
    }

    private void getItem() {
        this.pickSlot = -1;
        this.bedSlot = -1;
        this.blockSlot = -1;
        this.anyBed = false;
        if (BedCevBreaker.mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemBed) {
            this.bedSlot = 36;
            this.offhand = true;
        }
        for (int i = 0; i < 36; ++i) {
            ItemStack stack = BedCevBreaker.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a || !(stack.func_77973_b() instanceof ItemBed)) continue;
            this.anyBed = true;
            if (i >= 9) break;
            this.bedSlot = i;
            break;
        }
        this.blockSlot = BurrowUtil.findHotbarBlock(BlockObsidian.class);
        this.pickSlot = this.findItem();
    }

    private void doBreak() {
        if (this.placePos == null || BedCevBreaker.mc.field_71441_e.func_175623_d(this.placePos) || BedCevBreaker.mc.field_71441_e.func_180495_p(this.placePos).func_177230_c() == Blocks.field_150357_h) {
            return;
        }
        if (((Boolean)this.swing.getValue()).booleanValue()) {
            BedCevBreaker.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        }
        BedCevBreaker.mc.field_71442_b.func_180512_c(this.placePos, BlockUtil.getRayTraceFacing(this.placePos, EnumFacing.UP));
    }

    private boolean isPos2(BlockPos pos1, BlockPos pos2) {
        if (pos1 == null || pos2 == null) {
            return false;
        }
        return pos1.field_177962_a == pos2.field_177962_a && pos1.field_177960_b == pos2.field_177960_b && pos1.field_177961_c == pos2.field_177961_c;
    }

    private void run(int slot, boolean bypass, Runnable runnable) {
        int oldslot = BedCevBreaker.mc.field_71439_g.field_71071_by.field_70461_c;
        if (slot < 0 || slot == oldslot) {
            runnable.run();
            return;
        }
        if (bypass || slot > 8) {
            if (slot < 9) {
                slot += 36;
            }
            BedCevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketClickWindow(0, slot, BedCevBreaker.mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, ItemStack.field_190927_a, BedCevBreaker.mc.field_71439_g.field_71069_bz.func_75136_a(BedCevBreaker.mc.field_71439_g.field_71071_by)));
            runnable.run();
            BedCevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketClickWindow(0, slot, BedCevBreaker.mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, ItemStack.field_190927_a, BedCevBreaker.mc.field_71439_g.field_71069_bz.func_75136_a(BedCevBreaker.mc.field_71439_g.field_71071_by)));
        } else {
            if (((Boolean)this.packetSwitch.getValue()).booleanValue()) {
                BedCevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            } else {
                BedCevBreaker.mc.field_71439_g.field_71071_by.field_70461_c = slot;
            }
            runnable.run();
            if (((Boolean)this.packetSwitch.getValue()).booleanValue()) {
                BedCevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(oldslot));
            } else {
                BedCevBreaker.mc.field_71439_g.field_71071_by.field_70461_c = oldslot;
            }
        }
    }

    private int calcBreakTime() {
        return this.getBreakTime() * 70;
    }

    private int getBreakTime() {
        float hardness = 50.0f;
        float breakSpeed = this.getSpeed(Blocks.field_150343_Z.func_176194_O().func_177621_b());
        if (breakSpeed < 0.0f) {
            return -1;
        }
        float relativeDamage = this.getSpeed(Blocks.field_150343_Z.func_176194_O().func_177621_b()) / hardness / 30.0f;
        return (int)Math.ceil(0.7f / relativeDamage);
    }

    private int findItem() {
        int result = BedCevBreaker.mc.field_71439_g.field_71071_by.field_70461_c;
        double speed = this.getSpeed(Blocks.field_150343_Z.func_176194_O().func_177621_b(), BedCevBreaker.mc.field_71439_g.func_184614_ca());
        for (int i = 0; i < ((Boolean)this.pickBypass.getValue() != false ? 36 : 9); ++i) {
            ItemStack stack = BedCevBreaker.mc.field_71439_g.field_71071_by.func_70301_a(i);
            double stackSpeed = this.getSpeed(Blocks.field_150343_Z.func_176194_O().func_177621_b(), stack);
            if (!(stackSpeed > speed)) continue;
            speed = stackSpeed;
            result = i;
        }
        return result;
    }

    private double getSpeed(IBlockState state, ItemStack stack) {
        double str = stack.func_150997_a(state);
        int effect = EnchantmentHelper.func_77506_a((Enchantment)Enchantments.field_185305_q, (ItemStack)stack);
        return Math.max(str + (str > 1.0 ? (double)(effect * effect) + 1.0 : 0.0), 0.0);
    }

    private float getSpeed(IBlockState blockState) {
        int efficiencyModifier;
        ItemStack itemStack = BedCevBreaker.mc.field_71439_g.field_71071_by.func_70301_a(this.pickSlot);
        float digSpeed = BedCevBreaker.mc.field_71439_g.field_71071_by.func_70301_a(this.pickSlot).func_150997_a(blockState);
        if (!itemStack.func_190926_b() && (double)digSpeed > 1.0 && (efficiencyModifier = EnchantmentHelper.func_77506_a((Enchantment)Enchantments.field_185305_q, (ItemStack)itemStack)) > 0) {
            digSpeed += (float)(StrictMath.pow(efficiencyModifier, 2.0) + 1.0);
        }
        if (BedCevBreaker.mc.field_71439_g.func_70644_a(MobEffects.field_76422_e)) {
            digSpeed *= 1.0f + (float)(BedCevBreaker.mc.field_71439_g.func_70660_b(MobEffects.field_76422_e).func_76458_c() + 1) * 0.2f;
        }
        if (BedCevBreaker.mc.field_71439_g.func_70644_a(MobEffects.field_76419_f)) {
            float fatigueScale;
            switch (BedCevBreaker.mc.field_71439_g.func_70660_b(MobEffects.field_76419_f).func_76458_c()) {
                case 0: {
                    fatigueScale = 0.3f;
                    break;
                }
                case 1: {
                    fatigueScale = 0.09f;
                    break;
                }
                case 2: {
                    fatigueScale = 0.0027f;
                    break;
                }
                default: {
                    fatigueScale = 8.1E-4f;
                }
            }
            digSpeed *= fatigueScale;
        }
        if (BedCevBreaker.mc.field_71439_g.func_70055_a(Material.field_151586_h) && !EnchantmentHelper.func_185287_i((EntityLivingBase)BedCevBreaker.mc.field_71439_g)) {
            digSpeed /= 5.0f;
        }
        return digSpeed;
    }
}
