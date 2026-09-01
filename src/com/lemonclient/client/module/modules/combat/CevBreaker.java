/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Enchantments
 *  net.minecraft.init.MobEffects
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemEndCrystal
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketClickWindow
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.misc.Wrapper;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.combat.CrystalUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.combat.AntiBurrow;
import com.lemonclient.client.module.modules.combat.AntiRegear;
import com.lemonclient.client.module.modules.exploits.PacketMine;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

@Module.Declaration(name="CevBreaker", category=Category.Combat)
public class CevBreaker
extends Module {
    public static CevBreaker INSTANCE;
    ModeSetting page = this.registerMode("Page", Arrays.asList("General", "Place"), "General");
    IntegerSetting delay = this.registerInteger("Delay", 50, 0, 1000, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting helpBlock = this.registerBoolean("Help Block", true, () -> ((String)this.page.getValue()).equals("General"));
    DoubleSetting maxRange = this.registerDouble("Max Range", 5.0, 0.0, 10.0, () -> (Boolean)this.helpBlock.getValue() != false && ((String)this.page.getValue()).equals("General"));
    BooleanSetting down = this.registerBoolean("Down Block", true, () -> (Boolean)this.helpBlock.getValue() != false && ((String)this.page.getValue()).equals("General"));
    BooleanSetting packet = this.registerBoolean("Packet Place", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting rotate = this.registerBoolean("Rotate", false, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting strictFacing = this.registerBoolean("Strict Facing", false, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting swing = this.registerBoolean("Swing", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting bypassSwitch = this.registerBoolean("Bypass Switch", false, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting instantMine = this.registerBoolean("Instant Mine", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting pickBypass = this.registerBoolean("Pick Bypass", false, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting strict = this.registerBoolean("Strict", false, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting packetCrystal = this.registerBoolean("Packet Crystal", false, () -> ((String)this.page.getValue()).equals("Place"));
    BooleanSetting crystalBypass = this.registerBoolean("Crystal Bypass", false, () -> ((String)this.page.getValue()).equals("Place"));
    IntegerSetting breakDelay = this.registerInteger("Break Delay", 50, 0, 1000, () -> ((String)this.page.getValue()).equals("Place"));
    ModeSetting breakCrystal = this.registerMode("Break Crystal", Arrays.asList("Vanilla", "Packet"), "Packet", () -> ((String)this.page.getValue()).equals("Place"));
    BooleanSetting airCheck = this.registerBoolean("Air Check", true, () -> ((String)this.page.getValue()).equals("Place"));
    BooleanSetting antiWeakness = this.registerBoolean("AntiWeakness", true, () -> ((String)this.page.getValue()).equals("Place"));
    public boolean working;
    boolean offhand;
    boolean start;
    boolean anyCrystal;
    int blockSlot;
    int crystalSlot;
    int pickSlot;
    long time;
    EnumFacing facing;
    Timing timer = new Timing();
    Timing breakTimer = new Timing();
    BlockPos[] side = new BlockPos[]{new BlockPos(0, 0, 1), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0)};
    BlockPos placePos;
    int lastSlot;
    @EventHandler
    private final Listener<PacketEvent.PostSend> postSendListener = new Listener<PacketEvent.PostSend>(event -> {
        int slot;
        if (CevBreaker.mc.field_71441_e == null || CevBreaker.mc.field_71439_g == null) {
            return;
        }
        if (event.getPacket() instanceof CPacketHeldItemChange && (slot = ((CPacketHeldItemChange)event.getPacket()).func_149614_c()) != this.lastSlot) {
            this.lastSlot = slot;
            if (((Boolean)this.strict.getValue()).booleanValue()) {
                EnumFacing facing = BlockUtil.getRayTraceFacing(this.placePos, this.facing);
                CevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.placePos, facing));
                CevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.placePos, facing));
                if (((Boolean)this.swing.getValue()).booleanValue()) {
                    CevBreaker.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                }
                this.time = System.currentTimeMillis() + (long)this.calcBreakTime();
            }
        }
    }, new Predicate[0]);

    public CevBreaker() {
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        this.working = false;
    }

    @Override
    public void onEnable() {
        if (CevBreaker.mc.field_71476_x == null || CevBreaker.mc.field_71476_x.field_72313_a != RayTraceResult.Type.BLOCK || CevBreaker.mc.field_71441_e.func_180495_p(CevBreaker.mc.field_71476_x.func_178782_a()).func_177230_c() == Blocks.field_150357_h) {
            this.disable();
            return;
        }
        this.placePos = CevBreaker.mc.field_71476_x.func_178782_a();
        this.offhand = false;
        this.start = false;
        this.getItem();
        this.doBreak();
        this.timer.reset();
    }

    @Override
    public void fast() {
        this.working = false;
        if (CevBreaker.mc.field_71441_e == null || CevBreaker.mc.field_71439_g == null || this.placePos == null || CevBreaker.mc.field_71439_g.field_70128_L) {
            this.disable();
            return;
        }
        if (!CevBreaker.mc.field_71441_e.func_175623_d(this.placePos.func_177984_a()) || !CevBreaker.mc.field_71441_e.func_175623_d(this.placePos.func_177984_a().func_177984_a())) {
            this.disable();
            return;
        }
        this.getItem();
        if (!this.anyCrystal || this.blockSlot == -1 || this.pickSlot == -1) {
            this.disable();
            return;
        }
        if (this.crystalSlot == -1) {
            return;
        }
        if (CevBreaker.mc.field_71441_e.func_175623_d(this.placePos.func_177977_b()) && CevBreaker.mc.field_71441_e.func_175623_d(this.placePos.func_177978_c()) && CevBreaker.mc.field_71441_e.func_175623_d(this.placePos.func_177976_e()) && CevBreaker.mc.field_71441_e.func_175623_d(this.placePos.func_177974_f()) && CevBreaker.mc.field_71441_e.func_175623_d(this.placePos.func_177968_d())) {
            this.helpBlock(this.placePos);
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
            if (instantPos.equals((Object)new BlockPos(CevBreaker.mc.field_71439_g.field_70165_t, CevBreaker.mc.field_71439_g.field_70163_u + 2.0, CevBreaker.mc.field_71439_g.field_70161_v))) {
                return;
            }
            if (instantPos.equals((Object)new BlockPos(CevBreaker.mc.field_71439_g.field_70165_t, CevBreaker.mc.field_71439_g.field_70163_u - 1.0, CevBreaker.mc.field_71439_g.field_70161_v))) {
                return;
            }
            if (CevBreaker.mc.field_71441_e.func_180495_p(instantPos).func_177230_c() == Blocks.field_150321_G) {
                return;
            }
            this.doBreak();
        }
        this.working = true;
        if (!this.start && CevBreaker.mc.field_71441_e.func_175623_d(this.placePos)) {
            this.time = System.currentTimeMillis() + (long)((Boolean)this.instantMine.getValue() != false ? 0 : this.calcBreakTime());
            this.start = true;
        }
        Entity crystal = this.getCrystal();
        if (CevBreaker.mc.field_71441_e.func_180495_p(this.placePos).func_177230_c() instanceof BlockAir) {
            this.breakCrystalPiston(crystal);
            this.breakTimer.reset();
        }
        if (this.time > System.currentTimeMillis()) {
            return;
        }
        if (this.start && this.timer.passedMs(((Integer)this.delay.getValue()).intValue())) {
            this.run(this.blockSlot, (Boolean)this.bypassSwitch.getValue(), false, () -> BurrowUtil.placeBlock(this.placePos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue()));
            this.run(this.crystalSlot, (Boolean)this.crystalBypass.getValue(), true, () -> this.placeCrystal(this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
            this.run(this.pickSlot, (Boolean)this.pickBypass.getValue(), false, () -> {
                this.facing = EnumFacing.UP;
                if (((Boolean)this.strictFacing.getValue()).booleanValue()) {
                    this.facing = BlockUtil.getRayTraceFacing(this.placePos, this.facing);
                }
                CevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.placePos, this.facing));
                if (!((Boolean)this.instantMine.getValue()).booleanValue()) {
                    CevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.placePos, this.facing));
                    CevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.placePos, this.facing));
                    this.time = System.currentTimeMillis() + (long)this.calcBreakTime();
                }
                if (((Boolean)this.swing.getValue()).booleanValue()) {
                    CevBreaker.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                }
            });
            if (!((Boolean)this.airCheck.getValue()).booleanValue() || BlockUtil.isAir(this.placePos)) {
                this.breakCrystalPiston(this.getCrystal());
            }
            this.timer.reset();
        }
    }

    private void helpBlock(BlockPos pos) {
        NonNullList blocks = NonNullList.func_191196_a();
        for (BlockPos side : this.side) {
            blocks.add(pos.func_177971_a((Vec3i)side));
        }
        if (((Boolean)this.down.getValue()).booleanValue()) {
            blocks.add(pos.func_177977_b());
        }
        BlockPos finalPos = blocks.stream().filter(p -> CevBreaker.mc.field_71439_g.func_174818_b(p) <= (Double)this.maxRange.getValue() * (Double)this.maxRange.getValue()).max(Comparator.comparing(p -> CevBreaker.mc.field_71439_g.func_174818_b(p))).orElse(null);
        this.run(this.blockSlot, (Boolean)this.bypassSwitch.getValue(), false, () -> BurrowUtil.placeBlock(finalPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packet.getValue(), false, (Boolean)this.swing.getValue()));
    }

    private void getItem() {
        this.pickSlot = -1;
        this.crystalSlot = -1;
        this.blockSlot = -1;
        this.anyCrystal = false;
        if (CevBreaker.mc.field_71439_g.func_184592_cb().func_77973_b() instanceof ItemEndCrystal) {
            this.crystalSlot = 11;
            this.offhand = true;
        }
        for (int i = 0; i < 36; ++i) {
            ItemStack stack = CevBreaker.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a || !(stack.func_77973_b() instanceof ItemEndCrystal)) continue;
            this.anyCrystal = true;
            if (!((Boolean)this.crystalBypass.getValue()).booleanValue() && i >= 9) break;
            this.crystalSlot = i;
            break;
        }
        this.blockSlot = BurrowUtil.findHotbarBlock(BlockObsidian.class);
        this.pickSlot = this.findItem();
    }

    private void breakCrystalPiston(Entity crystal) {
        if (crystal == null) {
            return;
        }
        if (!this.breakTimer.passedMs(((Integer)this.breakDelay.getValue()).intValue())) {
            return;
        }
        this.breakTimer.reset();
        int newSlot = -1;
        if (((Boolean)this.antiWeakness.getValue()).booleanValue() && CevBreaker.mc.field_71439_g.func_70644_a(MobEffects.field_76437_t)) {
            for (int i = 0; i < 9; ++i) {
                ItemStack stack = Wrapper.getPlayer().field_71071_by.func_70301_a(i);
                if (stack == ItemStack.field_190927_a) continue;
                if (stack.func_77973_b() instanceof ItemSword) {
                    newSlot = i;
                    break;
                }
                if (!(stack.func_77973_b() instanceof ItemTool)) continue;
                newSlot = i;
            }
        }
        this.run(newSlot, (Boolean)this.pickBypass.getValue(), false, () -> {
            if (((String)this.breakCrystal.getValue()).equalsIgnoreCase("Vanilla")) {
                CrystalUtil.breakCrystal(crystal, (boolean)((Boolean)this.swing.getValue()));
            } else if (((String)this.breakCrystal.getValue()).equalsIgnoreCase("Packet")) {
                CrystalUtil.breakCrystalPacket(crystal, (Boolean)this.swing.getValue());
            }
        });
    }

    private Entity getCrystal() {
        for (Entity t : CevBreaker.mc.field_71441_e.field_72996_f) {
            if (!(t instanceof EntityEnderCrystal) || !(t.func_70011_f((double)this.placePos.field_177962_a + 0.5, (double)this.placePos.field_177960_b + 1.5, (double)this.placePos.field_177961_c + 0.5) < 3.0)) continue;
            return t;
        }
        return null;
    }

    private void doBreak() {
        if (this.placePos == null || CevBreaker.mc.field_71441_e.func_175623_d(this.placePos) || CevBreaker.mc.field_71441_e.func_180495_p(this.placePos).func_177230_c() == Blocks.field_150357_h) {
            return;
        }
        if (((Boolean)this.swing.getValue()).booleanValue()) {
            CevBreaker.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        }
        CevBreaker.mc.field_71442_b.func_180512_c(this.placePos, BlockUtil.getRayTraceFacing(this.placePos, EnumFacing.UP));
    }

    private boolean isPos2(BlockPos pos1, BlockPos pos2) {
        if (pos1 == null || pos2 == null) {
            return false;
        }
        return pos1.field_177962_a == pos2.field_177962_a && pos1.field_177960_b == pos2.field_177960_b && pos1.field_177961_c == pos2.field_177961_c;
    }

    private void placeCrystal(EnumHand hand) {
        if (((Boolean)this.packetCrystal.getValue()).booleanValue()) {
            CevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(this.placePos, EnumFacing.UP, hand, 0.0f, 0.0f, 0.0f));
        } else {
            CevBreaker.mc.field_71442_b.func_187099_a(CevBreaker.mc.field_71439_g, CevBreaker.mc.field_71441_e, this.placePos, EnumFacing.UP, new Vec3d((Vec3i)this.placePos).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(EnumFacing.UP.func_176730_m())), hand);
        }
        if (((Boolean)this.swing.getValue()).booleanValue()) {
            CevBreaker.mc.field_71439_g.func_184609_a(hand);
        }
    }

    private void run(int slot, boolean bypass, boolean update, Runnable runnable) {
        int oldslot = CevBreaker.mc.field_71439_g.field_71071_by.field_70461_c;
        if (slot < 0 || slot == oldslot) {
            runnable.run();
            return;
        }
        if (bypass || slot > 8) {
            ItemStack itemStack = CevBreaker.mc.field_71439_g.field_71071_by.func_70301_a(slot);
            if (slot < 9) {
                slot += 36;
            }
            CevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketClickWindow(0, slot, CevBreaker.mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, ItemStack.field_190927_a, CevBreaker.mc.field_71439_g.field_71069_bz.func_75136_a(CevBreaker.mc.field_71439_g.field_71071_by)));
            runnable.run();
            CevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketClickWindow(0, slot, CevBreaker.mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, update ? itemStack : ItemStack.field_190927_a, CevBreaker.mc.field_71439_g.field_71069_bz.func_75136_a(CevBreaker.mc.field_71439_g.field_71071_by)));
        } else {
            if (((Boolean)this.packetSwitch.getValue()).booleanValue()) {
                CevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            } else {
                CevBreaker.mc.field_71439_g.field_71071_by.field_70461_c = slot;
            }
            runnable.run();
            if (((Boolean)this.packetSwitch.getValue()).booleanValue()) {
                CevBreaker.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(oldslot));
            } else {
                CevBreaker.mc.field_71439_g.field_71071_by.field_70461_c = oldslot;
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
        int result = CevBreaker.mc.field_71439_g.field_71071_by.field_70461_c;
        double speed = this.getSpeed(Blocks.field_150343_Z.func_176194_O().func_177621_b(), CevBreaker.mc.field_71439_g.func_184614_ca());
        for (int i = 0; i < ((Boolean)this.pickBypass.getValue() != false ? 36 : 9); ++i) {
            ItemStack stack = CevBreaker.mc.field_71439_g.field_71071_by.func_70301_a(i);
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
        ItemStack itemStack = CevBreaker.mc.field_71439_g.field_71071_by.func_70301_a(this.pickSlot);
        float digSpeed = CevBreaker.mc.field_71439_g.field_71071_by.func_70301_a(this.pickSlot).func_150997_a(blockState);
        if (!itemStack.func_190926_b() && (double)digSpeed > 1.0 && (efficiencyModifier = EnchantmentHelper.func_77506_a((Enchantment)Enchantments.field_185305_q, (ItemStack)itemStack)) > 0) {
            digSpeed += (float)(StrictMath.pow(efficiencyModifier, 2.0) + 1.0);
        }
        if (CevBreaker.mc.field_71439_g.func_70644_a(MobEffects.field_76422_e)) {
            digSpeed *= 1.0f + (float)(CevBreaker.mc.field_71439_g.func_70660_b(MobEffects.field_76422_e).func_76458_c() + 1) * 0.2f;
        }
        if (CevBreaker.mc.field_71439_g.func_70644_a(MobEffects.field_76419_f)) {
            float fatigueScale;
            switch (CevBreaker.mc.field_71439_g.func_70660_b(MobEffects.field_76419_f).func_76458_c()) {
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
        if (CevBreaker.mc.field_71439_g.func_70055_a(Material.field_151586_h) && !EnchantmentHelper.func_185287_i((EntityLivingBase)CevBreaker.mc.field_71439_g)) {
            digSpeed /= 5.0f;
        }
        return digSpeed;
    }
}
