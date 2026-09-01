/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityEnderPearl
 *  net.minecraft.entity.item.EntityExpBottle
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityArrow
 *  net.minecraft.entity.projectile.EntityEgg
 *  net.minecraft.entity.projectile.EntitySnowball
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.init.MobEffects
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketClickWindow
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.network.play.server.SPacketSpawnObject
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.world.World
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.event.Phase;
import com.lemonclient.api.event.events.EntityRemovedEvent;
import com.lemonclient.api.event.events.OnUpdateWalkingPlayerEvent;
import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.event.events.RenderEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.misc.CrystalUtil;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.player.BurrowUtil;
import com.lemonclient.api.util.player.Locks;
import com.lemonclient.api.util.player.PlayerPacket;
import com.lemonclient.api.util.player.PlayerUtil;
import com.lemonclient.api.util.player.PredictUtil;
import com.lemonclient.api.util.player.RotationUtil;
import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.api.util.render.RenderUtil;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.api.util.world.combat.DamageUtil;
import com.lemonclient.client.LemonClient;
import com.lemonclient.client.manager.managers.PlayerPacketManager;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.dev.OffHand;
import com.lemonclient.client.module.modules.dev.PistonAura;
import com.lemonclient.client.module.modules.dev.PullCrystal;
import com.lemonclient.client.module.modules.qwq.AutoEz;
import com.lemonclient.mixin.mixins.accessor.AccessorCPacketUseEntity;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

@Module.Declaration(name="AutoCrystal", category=Category.Combat, priority=999)
public class AutoCrystal
extends Module {
    public static CopyOnWriteArrayList<CPacketUseEntity> packetList = new CopyOnWriteArrayList();
    public static AutoCrystal INSTANCE = new AutoCrystal();
    ModeSetting page = this.registerMode("Page", Arrays.asList("General", "Place", "Break", "Combat", "Switch", "Base", "Predict", "Dev", "Render"), "General");
    ModeSetting logic = this.registerMode("Logic", Arrays.asList("PlaceBreak", "BreakPlace"), "BreakPlace", () -> ((String)this.page.getValue()).equals("General"));
    IntegerSetting updateDelay = this.registerInteger("CalcDelay", 25, 0, 1000, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting wall = this.registerBoolean("WallCheck", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting wallAI = this.registerBoolean("WallAI", true, () -> (Boolean)this.wall.getValue() != false && ((String)this.page.getValue()).equals("General"));
    IntegerSetting enemyRange = this.registerInteger("EnemyRange", 7, 1, 16, () -> ((String)this.page.getValue()).equals("General"));
    IntegerSetting maxTarget = this.registerInteger("MaxTargets", 1, 1, 10, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting highVersion = this.registerBoolean("1.13", false, () -> ((String)this.page.getValue()).equals("General"));
    ModeSetting godMode = this.registerMode("SelfDamage", Arrays.asList("Auto", "GodMode", "NoGodMode"), "Auto", () -> ((String)this.page.getValue()).equals("General"));
    DoubleSetting maxSelfDMG = this.registerDouble("MaxSelfDmg", 12.0, 0.0, 36.0, () -> !((String)this.godMode.getValue()).equals("GodMode") && ((String)this.page.getValue()).equals("General"));
    DoubleSetting balance = this.registerDouble("HealthBalance", 1.5, 0.0, 10.0, () -> !((String)this.godMode.getValue()).equals("GodMode") && ((String)this.page.getValue()).equals("General"));
    BooleanSetting eat = this.registerBoolean("WhileEating", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting place = this.registerBoolean("Place", true, () -> ((String)this.page.getValue()).equals("Place"));
    BooleanSetting multiPlace = this.registerBoolean("MultiPlace", false, () -> (Boolean)this.place.getValue() != false && ((String)this.page.getValue()).equals("Place"));
    BooleanSetting packet = this.registerBoolean("PacketCrystal", true, () -> (Boolean)this.place.getValue() != false && ((String)this.page.getValue()).equals("Place"));
    IntegerSetting placeDelay = this.registerInteger("PlaceDelay", 50, 0, 1000, () -> (Boolean)this.place.getValue() != false && ((String)this.page.getValue()).equals("Place"));
    DoubleSetting placeRange = this.registerDouble("PlaceRange", 5.5, 0.0, 6.0, () -> (Boolean)this.place.getValue() != false && ((String)this.page.getValue()).equals("Place"));
    DoubleSetting placeWallRange = this.registerDouble("PlaceWallRange", 3.0, 0.1, 6.0, () -> (Boolean)this.place.getValue() != false && (Boolean)this.wall.getValue() != false && (Boolean)this.wallAI.getValue() == false && ((String)this.page.getValue()).equals("Place"));
    DoubleSetting minDamage = this.registerDouble("MinDmg", 4.0, 0.0, 36.0, () -> (Boolean)this.place.getValue() != false && ((String)this.page.getValue()).equals("Place"));
    BooleanSetting forcePlace = this.registerBoolean("OverridePlace", false, () -> !((String)this.godMode.getValue()).equals("GodMode") && (Boolean)this.place.getValue() != false && ((String)this.page.getValue()).equals("Place"));
    BooleanSetting crystalCheck = this.registerBoolean("CrystalCheck", false, () -> (Boolean)this.place.getValue() != false && ((String)this.page.getValue()).equals("Place"));
    BooleanSetting placeAfter = this.registerBoolean("PlaceAfterBreak", true, () -> (Boolean)this.place.getValue() != false && ((String)this.page.getValue()).equals("Place"));
    BooleanSetting post = this.registerBoolean("Posted", true, () -> (Boolean)this.place.getValue() != false && (Boolean)this.placeAfter.getValue() != false && ((String)this.page.getValue()).equals("Place"));
    BooleanSetting placeOnRemove = this.registerBoolean("PlaceOnRemove", true, () -> (Boolean)this.place.getValue() != false && ((String)this.page.getValue()).equals("Place"));
    BooleanSetting explode = this.registerBoolean("Break", true, () -> ((String)this.page.getValue()).equals("Break"));
    BooleanSetting PacketExplode = this.registerBoolean("PacketExplode", true, () -> (Boolean)this.explode.getValue() != false && ((String)this.page.getValue()).equals("Break"));
    IntegerSetting hitDelay = this.registerInteger("BreakDelay", 50, 0, 1000, () -> (Boolean)this.explode.getValue() != false && ((String)this.page.getValue()).equals("Break"));
    IntegerSetting PacketExplodeDelay = this.registerInteger("PacketExplodeDelay", 45, 0, 500, () -> (Boolean)this.PacketExplode.getValue() != false && ((String)this.page.getValue()).equals("Break"));
    DoubleSetting breakRange = this.registerDouble("BreakRange", 5.5, 0.0, 6.0, () -> (Boolean)this.explode.getValue() != false && ((String)this.page.getValue()).equals("Break"));
    DoubleSetting breakWallRange = this.registerDouble("BreakWallRange", 3.0, 0.1, 6.0, () -> (Boolean)this.explode.getValue() != false && (Boolean)this.wall.getValue() != false && (Boolean)this.wallAI.getValue() == false && ((String)this.page.getValue()).equals("Break"));
    IntegerSetting breakMinDmg = this.registerInteger("BreakMinDmg", 2, 0, 36, () -> (Boolean)this.explode.getValue() != false && ((String)this.page.getValue()).equals("Break"));
    BooleanSetting forceBreak = this.registerBoolean("OverrideBreak", false, () -> !((String)this.godMode.getValue()).equals("GodMode") && (Boolean)this.explode.getValue() != false && ((String)this.page.getValue()).equals("Break"));
    BooleanSetting antiWeakness = this.registerBoolean("AntiWeakness", false, () -> (Boolean)this.explode.getValue() != false && ((String)this.page.getValue()).equals("Break"));
    ModeSetting antiWeakMode = this.registerMode("SwitchMode", Arrays.asList("Normal", "Silent", "Bypass"), "Normal", () -> (Boolean)this.explode.getValue() != false && (Boolean)this.antiWeakness.getValue() != false && ((String)this.page.getValue()).equals("Break"));
    BooleanSetting PredictHit = this.registerBoolean("PredictHit", false, () -> (Boolean)this.explode.getValue() != false && ((String)this.page.getValue()).equals("Break"));
    IntegerSetting PredictHitFactor = this.registerInteger("PredictHitFactor", 2, 1, 20, () -> (Boolean)this.explode.getValue() != false && (Boolean)this.PredictHit.getValue() != false && ((String)this.page.getValue()).equals("Break"));
    BooleanSetting rotate = this.registerBoolean("Rotate", true, () -> ((String)this.page.getValue()).equals("Combat"));
    BooleanSetting swing = this.registerBoolean("Swing", true, () -> ((String)this.page.getValue()).equals("Combat"));
    BooleanSetting packetSwing = this.registerBoolean("PacketSwing", false, () -> (Boolean)this.swing.getValue() != false && ((String)this.page.getValue()).equals("Combat"));
    BooleanSetting facePlace = this.registerBoolean("FacePlace", true, () -> ((String)this.page.getValue()).equals("Combat"));
    IntegerSetting BlastHealth = this.registerInteger("BlastHealth", 10, 0, 20, () -> (Boolean)this.facePlace.getValue() != false && ((String)this.page.getValue()).equals("Combat"));
    IntegerSetting armorCount = this.registerInteger("ArmorCount", 1, 0, 64, () -> (Boolean)this.facePlace.getValue() != false && ((String)this.page.getValue()).equals("Combat"));
    IntegerSetting armorRate = this.registerInteger("ArmorDamage", 15, 0, 100, () -> (Boolean)this.facePlace.getValue() != false && (Integer)this.armorCount.getValue() > 0 && ((String)this.page.getValue()).equals("Combat"));
    DoubleSetting fpMinDmg = this.registerDouble("FpMinDmg", 1.0, 0.0, 36.0, () -> (Boolean)this.facePlace.getValue() != false && ((String)this.page.getValue()).equals("Combat"));
    BooleanSetting ClientSide = this.registerBoolean("ClientSide", false, () -> ((String)this.page.getValue()).equals("Combat"));
    BooleanSetting autoSwitch = this.registerBoolean("AutoSwitch", true, () -> ((String)this.page.getValue()).equals("Switch"));
    BooleanSetting offhand = this.registerBoolean("Offhand", false, () -> (Boolean)this.autoSwitch.getValue() != false && ((String)this.page.getValue()).equals("Switch"));
    BooleanSetting switchBack = this.registerBoolean("SwitchBack", true, () -> (Boolean)this.autoSwitch.getValue() != false && (Boolean)this.offhand.getValue() == false && ((String)this.page.getValue()).equals("Switch"));
    BooleanSetting bypass = this.registerBoolean("Bypass", false, () -> (Boolean)this.autoSwitch.getValue() != false && (Boolean)this.offhand.getValue() == false && (Boolean)this.switchBack.getValue() != false && ((String)this.page.getValue()).equals("Switch"));
    BooleanSetting packetSwitch = this.registerBoolean("PacketSwitch", false, () -> (Boolean)this.autoSwitch.getValue() != false && (Boolean)this.offhand.getValue() == false && (Boolean)this.switchBack.getValue() != false && ((String)this.page.getValue()).equals("Switch"));
    BooleanSetting forceUpdate = this.registerBoolean("ForceUpdate", false, () -> (Boolean)this.autoSwitch.getValue() != false && (Boolean)this.offhand.getValue() == false && (Boolean)this.switchBack.getValue() != false && (Boolean)this.bypass.getValue() != false && ((String)this.page.getValue()).equals("Switch"));
    BooleanSetting base = this.registerBoolean("Base", false, () -> ((String)this.page.getValue()).equals("Base"));
    IntegerSetting baseDelay = this.registerInteger("BaseDelay", 100, 0, 200, () -> (Boolean)this.base.getValue() != false && ((String)this.page.getValue()).equals("Base"));
    IntegerSetting toggleDamage = this.registerInteger("ToggleMaxDmg", 12, 0, 36, () -> (Boolean)this.base.getValue() != false && ((String)this.page.getValue()).equals("Base"));
    IntegerSetting baseMinDamage = this.registerInteger("BaseMinDmg", 6, 0, 36, () -> (Boolean)this.base.getValue() != false && ((String)this.page.getValue()).equals("Base"));
    DoubleSetting maxSpeed = this.registerDouble("MaxSpeed", 10.0, 0.0, 50.0, () -> (Boolean)this.base.getValue() != false && ((String)this.page.getValue()).equals("Base"));
    BooleanSetting baseBypass = this.registerBoolean("BaseBypassSwitch", false, () -> (Boolean)this.base.getValue() != false && ((String)this.page.getValue()).equals("Base"));
    BooleanSetting packetPlace = this.registerBoolean("PacketPlace", false, () -> (Boolean)this.base.getValue() != false && ((String)this.page.getValue()).equals("Base"));
    BooleanSetting target = this.registerBoolean("Target", true, () -> ((String)this.page.getValue()).equals("Predict"));
    BooleanSetting self = this.registerBoolean("Self", true, () -> ((String)this.page.getValue()).equals("Predict"));
    IntegerSetting tickPredict = this.registerInteger("TickPredict", 8, 0, 30, () -> ((String)this.page.getValue()).equals("Predict"));
    BooleanSetting calculateYPredict = this.registerBoolean("CalculateYPredict", true, () -> ((String)this.page.getValue()).equals("Predict"));
    IntegerSetting startDecrease = this.registerInteger("StartDecrease", 39, 0, 200, () -> ((String)this.page.getValue()).equals("Predict") && (Boolean)this.calculateYPredict.getValue() != false);
    IntegerSetting exponentStartDecrease = this.registerInteger("ExponentStart", 2, 1, 5, () -> ((String)this.page.getValue()).equals("Predict") && (Boolean)this.calculateYPredict.getValue() != false);
    IntegerSetting decreaseY = this.registerInteger("DecreaseY", 2, 1, 5, () -> ((String)this.page.getValue()).equals("Predict") && (Boolean)this.calculateYPredict.getValue() != false);
    IntegerSetting exponentDecreaseY = this.registerInteger("ExponentDecreaseY", 1, 1, 3, () -> ((String)this.page.getValue()).equals("Predict") && (Boolean)this.calculateYPredict.getValue() != false);
    BooleanSetting splitXZ = this.registerBoolean("SplitXZ", true, () -> ((String)this.page.getValue()).equals("Predict"));
    BooleanSetting manualOutHole = this.registerBoolean("ManualOutHole", false, () -> ((String)this.page.getValue()).equals("Predict"));
    BooleanSetting aboveHoleManual = this.registerBoolean("AboveHoleManual", false, () -> ((String)this.page.getValue()).equals("Predict") && (Boolean)this.manualOutHole.getValue() != false);
    BooleanSetting stairPredict = this.registerBoolean("StairPredict", false, () -> ((String)this.page.getValue()).equals("Predict"));
    IntegerSetting nStair = this.registerInteger("NStair", 2, 1, 4, () -> ((String)this.page.getValue()).equals("Predict") && (Boolean)this.stairPredict.getValue() != false);
    DoubleSetting speedActivationStair = this.registerDouble("SpeedActivationStair", 0.11, 0.0, 1.0, () -> ((String)this.page.getValue()).equals("Predict") && (Boolean)this.stairPredict.getValue() != false);
    IntegerSetting cooldown = this.registerInteger("Cooldown", 500, 0, 2000, () -> ((String)this.page.getValue()).equals("Dev"));
    BooleanSetting MineDetect = this.registerBoolean("MineDetect", false, () -> ((String)this.page.getValue()).equals("Dev"));
    public BooleanSetting civ = this.registerBoolean("AllowCiv", false, () -> (Boolean)this.MineDetect.getValue() != false && ((String)this.page.getValue()).equals("Dev"));
    public BooleanSetting rangeCheck = this.registerBoolean("RangeCheck", false, () -> (Boolean)this.MineDetect.getValue() != false && ((String)this.page.getValue()).equals("Dev"));
    BooleanSetting packetOptimize = this.registerBoolean("PacketOptimize", true, () -> ((String)this.page.getValue()).equals("Dev"));
    IntegerSetting limit = this.registerInteger("Limit", 40, 1, 100, () -> (Boolean)this.packetOptimize.getValue() != false && ((String)this.page.getValue()).equals("Dev"));
    BooleanSetting pause = this.registerBoolean("PausePistonAura", true, () -> ((String)this.page.getValue()).equals("Dev"));
    BooleanSetting showBreakDelay = this.registerBoolean("ShowBreakDelay", true, () -> ((String)this.page.getValue()).equals("Dev"));
    BooleanSetting speedDebug = this.registerBoolean("SpeedDebug", true, () -> ((String)this.page.getValue()).equals("Dev"));
    ModeSetting mode = this.registerMode("Mode", Arrays.asList("Solid", "Both", "Outline"), "Both", () -> ((String)this.page.getValue()).equals("Render"));
    BooleanSetting showDamage = this.registerBoolean("ShowDamage", false, () -> ((String)this.page.getValue()).equals("Render"));
    BooleanSetting showSelfDamage = this.registerBoolean("ShowSelfDamage", false, () -> (Boolean)this.showDamage.getValue() != false && ((String)this.page.getValue()).equals("Render"));
    BooleanSetting flat = this.registerBoolean("Flat", false, () -> ((String)this.page.getValue()).equals("Render"));
    IntegerSetting width = this.registerInteger("Width", 1, 0, 10, () -> ((String)this.page.getValue()).equals("Render"));
    ColorSetting color = this.registerColor("Color", new GSColor(255, 255, 255), () -> ((String)this.page.getValue()).equals("Render"));
    IntegerSetting alpha = this.registerInteger("Alpha", 50, 0, 255, () -> ((String)this.page.getValue()).equals("Render"));
    IntegerSetting outAlpha = this.registerInteger("OutlineAlpha", 125, 0, 255, () -> ((String)this.page.getValue()).equals("Render"));
    IntegerSetting movingTime = this.registerInteger("MovingTime", 0, 0, 500, () -> ((String)this.page.getValue()).equals("Render"));
    IntegerSetting lifeTime = this.registerInteger("FadeTime", 100, 0, 500, () -> ((String)this.page.getValue()).equals("Render"));
    BooleanSetting scale = this.registerBoolean("Scale", false, () -> ((String)this.page.getValue()).equals("Render"));
    PredictUtil.PredictSettings settings;
    Timing PacketExplodeTimer = new Timing();
    Timing ExplodeTimer = new Timing();
    Timing UpdateTimer = new Timing();
    Timing PlaceTimer = new Timing();
    Timing CalcTimer = new Timing();
    Timing cooldownTimer = new Timing();
    EntityEnderCrystal lastCrystal;
    EntityEnderCrystal crystal;
    Vec3d movingPlaceNow = new Vec3d(-1.0, -1.0, -1.0);
    BlockPos lastBestPlace = null;
    PlaceInfo placeInfo;
    boolean ShouldInfoLastBreak = false;
    boolean afterAttacking = false;
    boolean canPredictHit = false;
    boolean calculated;
    boolean canBase;
    long infoBreakTime = 0L;
    long lastBreakTime = 0L;
    long updateTime;
    long startTime;
    int lastEntityID = -1;
    int placements = 0;
    int StuckTimes = 0;
    int crystals = 0;
    int waited;
    int crystalSlot;
    int crystalId;
    int lastSlot;
    Vec3d lastHitVec;
    @EventHandler
    private final Listener<OnUpdateWalkingPlayerEvent> onUpdateWalkingPlayerEventListener = new Listener<OnUpdateWalkingPlayerEvent>(event -> {
        if (event.getPhase() != Phase.PRE || this.lastHitVec == null || !((Boolean)this.rotate.getValue()).booleanValue()) {
            return;
        }
        PlayerPacket packet = new PlayerPacket((Module)this, RotationUtil.getRotationTo(this.lastHitVec));
        PlayerPacketManager.INSTANCE.addPacket(packet);
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.PostSend> postSendListener = new Listener<PacketEvent.PostSend>(event -> {
        Entity attacked;
        if (AutoCrystal.mc.field_71441_e == null || AutoCrystal.mc.field_71439_g == null || AutoCrystal.mc.field_71439_g.field_70128_L) {
            return;
        }
        if (event.getPacket() instanceof CPacketUseEntity && ((Boolean)this.placeAfter.getValue()).booleanValue() && ((Boolean)this.post.getValue()).booleanValue() && ((CPacketUseEntity)event.getPacket()).func_149565_c() == CPacketUseEntity.Action.ATTACK && (attacked = ((CPacketUseEntity)event.getPacket()).func_149564_a((World)AutoCrystal.mc.field_71441_e)) instanceof EntityEnderCrystal) {
            long passed = this.PlaceTimer.getTime();
            this.PlaceTimer.setMs((Integer)this.placeDelay.getValue() + 1);
            this.place(false);
            this.PlaceTimer.setTime(passed);
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.PostReceive> postReceiveListener = new Listener<PacketEvent.PostReceive>(event -> {
        SPacketSpawnObject packet;
        if (AutoCrystal.mc.field_71441_e == null || AutoCrystal.mc.field_71439_g == null || AutoCrystal.mc.field_71439_g.field_70128_L) {
            return;
        }
        if (event.getPacket() instanceof SPacketSpawnObject) {
            packet = (SPacketSpawnObject)event.getPacket();
            if (((Boolean)this.PredictHit.getValue()).booleanValue()) {
                for (Entity e : AutoCrystal.mc.field_71441_e.field_72996_f) {
                    if (!(e instanceof EntityItem) && !(e instanceof EntityArrow) && !(e instanceof EntityEnderPearl) && !(e instanceof EntitySnowball) && !(e instanceof EntityEgg) || !(e.func_70011_f(packet.func_186880_c(), packet.func_186882_d(), packet.func_186881_e()) <= 6.0)) continue;
                    this.lastEntityID = -1;
                    this.canPredictHit = false;
                    event.cancel();
                }
            }
            if (packet.func_148993_l() == 51) {
                EntityEnderCrystal crystal;
                this.lastEntityID = packet.func_149001_c();
                if (((Boolean)this.explode.getValue()).booleanValue() && this.check() && (crystal = (EntityEnderCrystal)AutoCrystal.mc.field_71441_e.func_73045_a(this.lastEntityID)) != null && ((Boolean)this.PacketExplode.getValue()).booleanValue() && this.PacketExplodeTimer.passedMs(((Integer)this.PacketExplodeDelay.getValue()).intValue()) && this.canHitCrystal(crystal)) {
                    this.PacketExplode(this.lastEntityID);
                    this.PacketExplodeTimer.reset();
                }
            }
        }
        if (event.getPacket() instanceof SPacketSoundEffect) {
            packet = (SPacketSoundEffect)event.getPacket();
            if (packet.func_186978_a().equals(SoundEvents.field_187601_be) || packet.func_186978_a().equals(SoundEvents.field_187635_cQ)) {
                this.canPredictHit = false;
            }
            if (packet.func_186978_a().equals(SoundEvents.field_187539_bB)) {
                this.ShouldInfoLastBreak = true;
                ++this.crystals;
            }
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.Send> sendListener = new Listener<PacketEvent.Send>(event -> {
        int slot;
        Entity attacked;
        if (AutoCrystal.mc.field_71441_e == null || AutoCrystal.mc.field_71439_g == null || AutoCrystal.mc.field_71439_g.field_70128_L) {
            return;
        }
        if (((Boolean)this.packetOptimize.getValue()).booleanValue() && event.getPacket() instanceof CPacketUseEntity && packetList.size() > (Integer)this.limit.getValue()) {
            event.cancel();
            packetList.clear();
        }
        if (event.getPacket() instanceof CPacketUseEntity && ((Boolean)this.placeAfter.getValue()).booleanValue() && !((Boolean)this.post.getValue()).booleanValue() && ((CPacketUseEntity)event.getPacket()).func_149565_c() == CPacketUseEntity.Action.ATTACK && (attacked = ((CPacketUseEntity)event.getPacket()).func_149564_a((World)AutoCrystal.mc.field_71441_e)) instanceof EntityEnderCrystal) {
            long passed = this.PlaceTimer.getTime();
            this.PlaceTimer.setMs((Integer)this.placeDelay.getValue() + 1);
            this.place(false);
            this.PlaceTimer.setTime(passed);
        }
        if (((Boolean)this.rotate.getValue()).booleanValue() && this.lastHitVec != null) {
            Vec2f vec = RotationUtil.getRotationTo(this.lastHitVec);
            if (event.getPacket() instanceof CPacketPlayer.Rotation) {
                ((CPacketPlayer.Rotation)event.getPacket()).field_149476_e = vec.field_189982_i;
                ((CPacketPlayer.Rotation)event.getPacket()).field_149473_f = vec.field_189983_j;
            }
            if (event.getPacket() instanceof CPacketPlayer.PositionRotation) {
                ((CPacketPlayer.PositionRotation)event.getPacket()).field_149476_e = vec.field_189982_i;
                ((CPacketPlayer.PositionRotation)event.getPacket()).field_149473_f = vec.field_189983_j;
            }
        }
        if (event.getPacket() instanceof CPacketHeldItemChange && (slot = ((CPacketHeldItemChange)event.getPacket()).func_149614_c()) != this.lastSlot) {
            this.lastSlot = slot;
            this.cooldownTimer.reset();
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<EntityRemovedEvent> entityRemovedEventListener = new Listener<EntityRemovedEvent>(event -> {
        if (event.getEntity().field_145783_c == this.crystalId && ((Boolean)this.placeOnRemove.getValue()).booleanValue()) {
            long passed = this.PlaceTimer.getTime();
            this.PlaceTimer.setMs((Integer)this.placeDelay.getValue() + 1);
            this.place(false);
            this.PlaceTimer.setTime(passed);
        }
    }, new Predicate[0]);
    boolean tryCalc;
    int c = 0;

    public void windowClick(int windowId, int slotId, int mouseButton, ClickType type, EntityPlayer player, boolean back) {
        short short1 = player.field_71070_bA.func_75136_a(player.field_71071_by);
        ItemStack itemStack = ItemStack.field_190927_a;
        if (!((Boolean)this.packetSwitch.getValue()).booleanValue()) {
            itemStack = player.field_71070_bA.func_184996_a(slotId, mouseButton, type, player);
        }
        AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketClickWindow(windowId, slotId, mouseButton, type, back && (Boolean)this.forceUpdate.getValue() != false ? Items.field_185158_cP.func_190903_i() : itemStack, short1));
        AutoCrystal.mc.field_71442_b.func_78765_e();
        AutoCrystal.mc.field_71439_g.field_71070_bA.func_75142_b();
    }

    private void switchToCrystal(int slot, boolean bypass, boolean shouldSwitch, boolean back, Runnable runnable) {
        int oldslot = AutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c;
        if (!shouldSwitch || slot < 0 || slot == oldslot) {
            runnable.run();
            return;
        }
        if (bypass) {
            if (slot < 9) {
                slot += 36;
            }
            int id = AutoCrystal.mc.field_71439_g.field_71069_bz.field_75152_c;
            int finalSlot = slot;
            Locks.acquire(Locks.PLACE_SWITCH_LOCK, () -> {
                Locks.acquire(Locks.WINDOW_CLICK_LOCK, () -> this.windowClick(id, finalSlot, oldslot, ClickType.SWAP, (EntityPlayer)AutoCrystal.mc.field_71439_g, false));
                runnable.run();
                AutoCrystal.mc.field_71442_b.func_78765_e();
                AutoCrystal.mc.field_71439_g.field_71070_bA.func_75142_b();
                Locks.acquire(Locks.WINDOW_CLICK_LOCK, () -> this.windowClick(id, finalSlot, oldslot, ClickType.SWAP, (EntityPlayer)AutoCrystal.mc.field_71439_g, true));
            });
        } else if (slot < 9) {
            boolean packetSwitch;
            boolean bl = packetSwitch = back && (Boolean)this.packetSwitch.getValue() != false;
            if (packetSwitch) {
                AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            } else {
                AutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                AutoCrystal.mc.field_71442_b.func_78765_e();
            }
            runnable.run();
            if (back) {
                if (packetSwitch) {
                    AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(oldslot));
                } else {
                    AutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c = oldslot;
                    AutoCrystal.mc.field_71442_b.func_78765_e();
                }
            }
        }
    }

    private void switchTo(int slot, boolean bypass, boolean back, Runnable runnable) {
        int oldslot = AutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c;
        if (slot < 0 || slot == oldslot) {
            runnable.run();
            return;
        }
        if (bypass) {
            if (slot < 9) {
                slot += 36;
            }
            int id = AutoCrystal.mc.field_71439_g.field_71069_bz.field_75152_c;
            int finalSlot = slot;
            Locks.acquire(Locks.PLACE_SWITCH_LOCK, () -> {
                Locks.acquire(Locks.WINDOW_CLICK_LOCK, () -> this.windowClick(id, finalSlot, oldslot, ClickType.SWAP, (EntityPlayer)AutoCrystal.mc.field_71439_g, false));
                runnable.run();
                Locks.acquire(Locks.WINDOW_CLICK_LOCK, () -> this.windowClick(id, finalSlot, oldslot, ClickType.SWAP, (EntityPlayer)AutoCrystal.mc.field_71439_g, false));
            });
        } else if (slot < 9) {
            boolean packetSwitch;
            boolean bl = packetSwitch = back && (Boolean)this.packetSwitch.getValue() != false;
            if (packetSwitch) {
                AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            } else {
                AutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                AutoCrystal.mc.field_71442_b.func_78765_e();
            }
            runnable.run();
            if (back) {
                if (packetSwitch) {
                    AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(oldslot));
                } else {
                    AutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c = oldslot;
                    AutoCrystal.mc.field_71442_b.func_78765_e();
                }
            }
        }
    }

    public static double getRange(Vec3d a, double x, double y, double z) {
        double xl = a.field_72450_a - x;
        double yl = a.field_72448_b - y;
        double zl = a.field_72449_c - z;
        return Math.sqrt(xl * xl + yl * yl + zl * zl);
    }

    private boolean check() {
        return this.placeInfo != null && this.placeInfo.target != null && this.placeInfo.target.player != null;
    }

    @Override
    public void onTick() {
        if (!this.tryCalc) {
            return;
        }
        if (this.UpdateTimer.passedMs(((Integer)this.updateDelay.getValue()).intValue())) {
            if (!(this.crystalSlot != -1 || AutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP || ((Boolean)this.autoSwitch.getValue()).booleanValue() && ((Boolean)this.offhand.getValue()).booleanValue())) {
                return;
            }
            this.placeInfo = this.Calc();
            if (!this.check()) {
                this.lastBreakTime = System.currentTimeMillis();
                this.switchOffhand(false);
                this.pausePA(false);
                this.lastHitVec = null;
                this.placeInfo = null;
                this.crystal = null;
                return;
            }
            if (this.placeInfo.blockPos == null || this.placeInfo.dmg == 0.0) {
                this.placeInfo.blockPos = null;
                this.placeInfo.dmg = 0.0;
                this.switchOffhand(false);
                this.pausePA(false);
                this.lastHitVec = null;
                this.crystal = null;
            }
            AutoEz.INSTANCE.addTargetedPlayer(this.placeInfo.target.player.func_70005_c_());
            this.UpdateTimer.reset();
        }
    }

    @Override
    public void fast() {
        if (AutoCrystal.mc.field_71441_e == null || AutoCrystal.mc.field_71439_g == null || AutoCrystal.mc.field_71439_g.field_70128_L) {
            return;
        }
        if (this.CalcTimer.passedMs(1000L)) {
            this.CalcTimer.reset();
            this.calculated = true;
        }
        this.crystalSlot = this.getItemHotbar();
        if (!(this.crystalSlot != -1 || AutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP || ((Boolean)this.autoSwitch.getValue()).booleanValue() && ((Boolean)this.offhand.getValue()).booleanValue())) {
            this.lastBreakTime = System.currentTimeMillis();
            this.placeInfo = null;
            this.switchOffhand(false);
            this.pausePA(false);
            this.lastHitVec = null;
            this.tryCalc = false;
            return;
        }
        this.tryCalc = true;
        if (((Boolean)this.base.getValue()).booleanValue()) {
            if (this.waited++ >= (Integer)this.baseDelay.getValue()) {
                this.canBase = true;
                this.waited = 0;
            }
        } else {
            this.canBase = false;
        }
        if (!this.check()) {
            return;
        }
        this.pausePA((Boolean)this.pause.getValue());
        if (!((Boolean)this.eat.getValue()).booleanValue() && EntityUtil.isEating() || (Integer)this.cooldown.getValue() != 0 && !this.cooldownTimer.passedMs(((Integer)this.cooldown.getValue()).intValue())) {
            this.lastHitVec = null;
            return;
        }
        if (((String)this.logic.getValue()).equals("BreakPlace")) {
            this.explode();
            this.place((Boolean)this.crystalCheck.getValue());
        } else {
            this.place((Boolean)this.crystalCheck.getValue());
            this.explode();
        }
    }

    private void place(boolean check) {
        Block block;
        boolean useOffhand;
        if (!((Boolean)this.place.getValue()).booleanValue()) {
            return;
        }
        if (this.placeInfo == null || this.placeInfo.blockPos == null) {
            this.crystal = null;
            return;
        }
        boolean detected = true;
        for (Entity entity : AutoCrystal.mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityEnderCrystal) || !this.crystalPlaceBoxIntersectsCrystalBox(this.placeInfo.blockPos, entity)) continue;
            detected = false;
            this.crystal = (EntityEnderCrystal)entity;
            break;
        }
        if (detected) {
            this.crystal = null;
        }
        boolean bl = useOffhand = AutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP;
        if (AutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c != this.crystalSlot && !useOffhand) {
            if (!((Boolean)this.autoSwitch.getValue()).booleanValue()) {
                return;
            }
            if (((Boolean)this.offhand.getValue()).booleanValue()) {
                this.switchOffhand(true);
                return;
            }
        }
        if ((block = BlockUtil.getBlock(this.placeInfo.blockPos)) != Blocks.field_150357_h && block != Blocks.field_150343_Z && ((Boolean)this.base.getValue()).booleanValue()) {
            int obby = BurrowUtil.findBlock(BlockObsidian.class, this.findInventory());
            if (obby == -1) {
                return;
            }
            this.switchTo(obby, (Boolean)this.baseBypass.getValue(), true, () -> BurrowUtil.placeBlock(this.placeInfo.blockPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), (Boolean)this.packetPlace.getValue(), false, (Boolean)this.swing.getValue()));
            this.canBase = false;
        }
        if (this.PlaceTimer.passedMs(((Integer)this.placeDelay.getValue()).intValue()) && (detected || !check)) {
            Vec3d vec;
            EnumHand hand = useOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
            EnumFacing facing = this.placeInfo.blockPos.func_177956_o() == 255 ? EnumFacing.DOWN : EnumFacing.UP;
            Vec3d add = new Vec3d(0.5, (double)(facing == EnumFacing.UP ? 1 : 0), 0.5);
            this.lastHitVec = vec = new Vec3d((double)this.placeInfo.blockPos.field_177962_a, (double)this.placeInfo.blockPos.field_177960_b, (double)this.placeInfo.blockPos.field_177961_c).func_178787_e(add);
            this.switchToCrystal(this.crystalSlot, this.findInventory(), !useOffhand && (Boolean)this.autoSwitch.getValue() != false, (Boolean)this.switchBack.getValue(), () -> {
                if (((Boolean)this.packet.getValue()).booleanValue()) {
                    AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(this.placeInfo.blockPos, facing, hand, (float)add.field_72450_a, (float)add.field_72448_b, (float)add.field_72449_c));
                } else {
                    AutoCrystal.mc.field_71442_b.func_187099_a(AutoCrystal.mc.field_71439_g, AutoCrystal.mc.field_71441_e, this.placeInfo.blockPos, facing, vec, hand);
                }
            });
            if (((Boolean)this.swing.getValue()).booleanValue()) {
                if (((Boolean)this.packetSwing.getValue()).booleanValue()) {
                    AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(hand));
                } else {
                    AutoCrystal.mc.field_71439_g.func_184609_a(hand);
                }
            }
            ++this.placements;
            this.PlaceTimer.reset();
        }
        if (((Boolean)this.PredictHit.getValue()).booleanValue() && DamageUtil.calculateCrystalDamage((EntityLivingBase)this.placeInfo.target.player, this.placeInfo.target.position, this.placeInfo.target.boundingBox, (double)this.placeInfo.blockPos.field_177962_a + 0.5, this.placeInfo.blockPos.field_177960_b + 1, (double)this.placeInfo.blockPos.field_177961_c + 0.5) > (float)((Integer)this.breakMinDmg.getValue()).intValue()) {
            try {
                if (!this.canPredictHit) {
                    this.PlaceTimer.reset();
                    return;
                }
                if ((double)(AutoCrystal.mc.field_71439_g.func_110143_aJ() + AutoCrystal.mc.field_71439_g.func_110139_bj()) > (Double)this.maxSelfDMG.getValue() && this.lastEntityID != -1 && this.lastCrystal != null && this.canPredictHit) {
                    for (int i = 0; i < (Integer)this.PredictHitFactor.getValue(); ++i) {
                        this.PacketExplode(this.lastEntityID + i + 2);
                    }
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public List<EntityPlayer> getTargets() {
        return PlayerUtil.getNearPlayers(((Integer)this.enemyRange.getValue()).intValue(), (Integer)this.maxTarget.getValue());
    }

    public PlaceInfo Calc() {
        List<BlockPos> default_blocks;
        PlaceInfo best = new PlaceInfo(new PlayerInfo(PlayerUtil.getNearestPlayer(((Integer)this.enemyRange.getValue()).intValue())), null, 0.0, 0.0);
        if (((Boolean)this.wall.getValue()).booleanValue() && ((Boolean)this.wallAI.getValue()).booleanValue()) {
            double TempRange = (Double)this.placeRange.getValue();
            double temp2 = TempRange - (double)this.StuckTimes * 0.5;
            if (this.StuckTimes > 0) {
                TempRange = (Double)this.placeRange.getValue();
                if (temp2 > (Double)this.placeWallRange.getValue()) {
                    TempRange = temp2;
                } else if ((Double)this.placeWallRange.getValue() < (Double)this.placeRange.getValue()) {
                    TempRange = 3.0;
                }
            }
            default_blocks = this.renditions(TempRange);
        } else {
            default_blocks = this.renditions((Double)this.placeRange.getValue());
        }
        this.settings = new PredictUtil.PredictSettings((Integer)this.tickPredict.getValue(), (Boolean)this.calculateYPredict.getValue(), (Integer)this.startDecrease.getValue(), (Integer)this.exponentStartDecrease.getValue(), (Integer)this.decreaseY.getValue(), (Integer)this.exponentDecreaseY.getValue(), (Boolean)this.splitXZ.getValue(), (Boolean)this.manualOutHole.getValue(), (Boolean)this.aboveHoleManual.getValue(), (Boolean)this.stairPredict.getValue(), (Integer)this.nStair.getValue(), (Double)this.speedActivationStair.getValue());
        EntityPlayerSP player = AutoCrystal.mc.field_71439_g;
        if (((Boolean)this.self.getValue()).booleanValue()) {
            player = PredictUtil.predictPlayer((EntityLivingBase)player, this.settings);
        }
        PlayerInfo self = new PlayerInfo((EntityPlayer)AutoCrystal.mc.field_71439_g, player.func_174791_d(), player.func_174813_aQ());
        boolean calcBase = true;
        Iterator<EntityPlayer> iterator = this.getTargets().iterator();
        while (iterator.hasNext()) {
            EntityPlayer target;
            EntityPlayer origin = target = iterator.next();
            if (((Boolean)this.target.getValue()).booleanValue()) {
                target = PredictUtil.predictPlayer((EntityLivingBase)target, this.settings);
            }
            PlayerInfo targetPlayer = new PlayerInfo(origin, target.func_174791_d(), target.func_174813_aQ());
            this.canPredictHit = ((Boolean)this.PredictHit.getValue() == false || !targetPlayer.player.func_184614_ca().func_77973_b().equals(Items.field_151062_by)) && !targetPlayer.player.func_184592_cb().func_77973_b().equals(Items.field_151062_by) || !ModuleManager.getModule("AutoMend").isEnabled();
            for (BlockPos blockPos : default_blocks) {
                double dmg;
                boolean shouldBase;
                boolean bl = shouldBase = AutoCrystal.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150357_h && AutoCrystal.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150343_Z;
                if (shouldBase && (!this.canBase || !calcBase || blockPos.field_177960_b >= (int)(targetPlayer.player.field_70163_u + 0.5) || BurrowUtil.findHotbarBlock(BlockObsidian.class) == -1 || LemonClient.speedUtil.getPlayerSpeed(targetPlayer.player) > (Double)this.maxSpeed.getValue()) || (dmg = (Boolean)this.MineDetect.getValue() != false ? (double)DamageUtil.calculateCrystalDamageMine((EntityLivingBase)targetPlayer.player, targetPlayer.position, targetPlayer.boundingBox, (double)blockPos.func_177958_n() + 0.5, blockPos.func_177956_o() + 1, (double)blockPos.func_177952_p() + 0.5) : (double)DamageUtil.calculateCrystalDamage((EntityLivingBase)targetPlayer.player, targetPlayer.position, targetPlayer.boundingBox, (double)blockPos.func_177958_n() + 0.5, blockPos.func_177956_o() + 1, (double)blockPos.func_177952_p() + 0.5)) == 0.0 || dmg < best.dmg) continue;
                if (shouldBase) {
                    if ((int)dmg == (int)best.dmg || dmg < (double)((Integer)this.baseMinDamage.getValue()).intValue()) {
                        continue;
                    }
                } else if (dmg >= (double)((Integer)this.toggleDamage.getValue()).intValue()) {
                    calcBase = false;
                }
                double selfDmg = 0.0;
                if (((String)this.godMode.getValue()).equals("NoGodMode") || ((String)this.godMode.getValue()).equals("Auto") && !AutoCrystal.mc.field_71439_g.func_184812_l_()) {
                    selfDmg = DamageUtil.calculateCrystalDamage((EntityLivingBase)self.player, self.position, self.boundingBox, (double)blockPos.func_177958_n() + 0.5, blockPos.func_177956_o() + 1, (double)blockPos.func_177952_p() + 0.5);
                }
                if (selfDmg != 0.0 && (selfDmg + (Double)this.balance.getValue() >= self.health || selfDmg + (Double)this.balance.getValue() > (Double)this.maxSelfDMG.getValue()) && (!((Boolean)this.forcePlace.getValue()).booleanValue() || dmg <= targetPlayer.health)) continue;
                double minDamage = (Double)this.minDamage.getValue();
                if (this.canFacePlace(targetPlayer)) {
                    minDamage = (Double)this.fpMinDmg.getValue();
                }
                if (!(dmg >= minDamage)) continue;
                best = new PlaceInfo(targetPlayer, blockPos, dmg, selfDmg);
            }
        }
        return best;
    }

    public void explode() {
        EntityEnderCrystal crystal;
        if (!((Boolean)this.explode.getValue()).booleanValue()) {
            return;
        }
        EntityEnderCrystal entityEnderCrystal = crystal = this.crystal == null ? (EntityEnderCrystal)AutoCrystal.mc.field_71441_e.field_72996_f.stream().filter(e -> e instanceof EntityEnderCrystal && this.canHitCrystal((EntityEnderCrystal)e)).map(e -> (EntityEnderCrystal)e).min(Comparator.comparing(e -> Float.valueOf(AutoCrystal.mc.field_71439_g.func_70032_d((Entity)e)))).orElse(null) : this.crystal;
        if (crystal != null) {
            this.lastCrystal = crystal;
            if (this.StuckTimes > 0) {
                this.StuckTimes = 0;
            }
            this.lastHitVec = new Vec3d(crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v);
            this.ExplodeCrystal((Entity)this.lastCrystal);
            if (this.lastBreakTime == 0L) {
                this.lastBreakTime = System.currentTimeMillis();
            }
            this.afterAttacking = true;
        } else {
            this.lastBreakTime = System.currentTimeMillis();
            this.afterAttacking = false;
            ++this.StuckTimes;
        }
    }

    public void ExplodeCrystal(Entity crystal) {
        if (crystal != null && this.ExplodeTimer.passedMs(((Integer)this.hitDelay.getValue()).intValue()) && mc.func_147114_u() != null) {
            this.PacketExplode(crystal.func_145782_y());
            this.ExplodeTimer.reset();
            if (((Boolean)this.ClientSide.getValue()).booleanValue()) {
                for (Entity o : AutoCrystal.mc.field_71441_e.func_72910_y()) {
                    if (!(o instanceof EntityEnderCrystal) || !(o.func_70011_f(o.field_70165_t, o.field_70163_u, o.field_70161_v) <= 6.0)) continue;
                    o.func_70106_y();
                }
                AutoCrystal.mc.field_71441_e.func_73022_a();
            }
            if (((Boolean)this.multiPlace.getValue()).booleanValue() && this.placements >= 3) {
                this.placements = 0;
                this.afterAttacking = true;
            }
        }
    }

    public void PacketExplode(int i) {
        if (this.check() && (this.lastCrystal != null && this.canHitCrystal(this.lastCrystal) || AutoCrystal.mc.field_71441_e.func_73045_a(i) == null)) {
            this.crystalId = i;
            try {
                EnumHand hand;
                int slot = -1;
                if (((Boolean)this.antiWeakness.getValue()).booleanValue() && AutoCrystal.mc.field_71439_g.func_70644_a(MobEffects.field_76437_t) && (!AutoCrystal.mc.field_71439_g.func_70644_a(MobEffects.field_76420_g) || Objects.requireNonNull(AutoCrystal.mc.field_71439_g.func_70660_b(MobEffects.field_76420_g)).func_76458_c() < 1)) {
                    for (int b = 0; b < (this.findInventory() ? 36 : 9); ++b) {
                        ItemStack stack = AutoCrystal.mc.field_71439_g.field_71071_by.func_70301_a(b);
                        if (stack == ItemStack.field_190927_a) continue;
                        if (stack.func_77973_b() instanceof ItemSword) {
                            slot = b;
                            break;
                        }
                        if (!(stack.func_77973_b() instanceof ItemTool)) continue;
                        slot = b;
                    }
                }
                this.switchTo(slot, ((String)this.antiWeakMode.getValue()).equals("Bypass"), !((String)this.antiWeakMode.getValue()).equals("Normal"), () -> {
                    CPacketUseEntity crystal = new CPacketUseEntity();
                    AutoCrystal.setEntityId(crystal, i);
                    AutoCrystal.setAction(crystal, CPacketUseEntity.Action.ATTACK);
                    AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)crystal);
                    if (((Boolean)this.packetOptimize.getValue()).booleanValue()) {
                        packetList.add(crystal);
                    }
                });
                EnumHand enumHand = hand = AutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
                if (((Boolean)this.swing.getValue()).booleanValue()) {
                    if (((Boolean)this.packetSwing.getValue()).booleanValue()) {
                        AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(hand));
                    } else {
                        AutoCrystal.mc.field_71439_g.func_184609_a(hand);
                    }
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public static void setEntityId(CPacketUseEntity packet, int entityId) {
        ((AccessorCPacketUseEntity)packet).setId(entityId);
    }

    public static void setAction(CPacketUseEntity packet, CPacketUseEntity.Action action) {
        ((AccessorCPacketUseEntity)packet).setAction(action);
    }

    public CrystalInfo getBestDmg(EntityEnderCrystal crystal) {
        CrystalInfo best = new CrystalInfo(crystal, null, 0.0);
        for (EntityPlayer entityPlayer : this.getTargets()) {
            EntityPlayer player = (Boolean)this.target.getValue() != false ? PredictUtil.predictPlayer((EntityLivingBase)entityPlayer, this.settings) : entityPlayer;
            PlayerInfo target = new PlayerInfo(entityPlayer, player.func_174791_d(), player.func_174813_aQ());
            double dmg = DamageUtil.calculateCrystalDamage((EntityLivingBase)target.player, target.position, target.boundingBox, crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v);
            if (dmg == 0.0) continue;
            CrystalInfo get = new CrystalInfo(crystal, target, dmg);
            if (dmg >= target.health) {
                return get;
            }
            if (!(dmg > best.damage)) continue;
            best = get;
        }
        return best;
    }

    public List<BlockPos> renditions(double range) {
        NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)EntityUtil.getSphere(PlayerUtil.getEyesPos(), range, range, false, true, 0).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
        return positions;
    }

    public boolean canPlaceCrystal(BlockPos blockPos) {
        if (PlayerUtil.getDistanceI(blockPos) > (Double)this.placeRange.getValue()) {
            return false;
        }
        if (((Boolean)this.wall.getValue()).booleanValue() && PlayerUtil.getDistanceI(blockPos) > (Double)this.placeWallRange.getValue() && !CrystalUtil.calculateRaytrace(blockPos)) {
            return false;
        }
        BlockPos boost = blockPos.func_177982_a(0, 1, 0);
        BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
        if (!BlockUtil.isAirBlock(boost)) {
            return false;
        }
        if (!((Boolean)this.highVersion.getValue()).booleanValue() && !BlockUtil.isAirBlock(boost2)) {
            return false;
        }
        if (AutoCrystal.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150357_h && AutoCrystal.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150343_Z) {
            if (!this.canBase || ((Boolean)this.base.getValue()).booleanValue()) {
                return false;
            }
            if (!BlockUtil.isAirBlock(blockPos) || this.intersectsWithEntity(blockPos)) {
                return false;
            }
            if (BurrowUtil.getFirstFacing(blockPos) == null) {
                return false;
            }
        }
        boolean recall = false;
        for (Entity entity : AutoCrystal.mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityEnderCrystal) || !this.crystalPlaceBoxIntersectsCrystalBox(blockPos, entity)) continue;
            recall = true;
            break;
        }
        for (Entity entity : AutoCrystal.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost))) {
            if (entity instanceof EntityEnderCrystal || recall && (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityExpBottle)) continue;
            return false;
        }
        if (!((Boolean)this.highVersion.getValue()).booleanValue()) {
            for (Entity entity : AutoCrystal.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(boost2))) {
                if (entity instanceof EntityEnderCrystal || recall && (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityExpBottle)) continue;
                return false;
            }
        }
        if (this.afterAttacking && this.lastCrystal != null) {
            for (Entity entity : AutoCrystal.mc.field_71441_e.field_72996_f) {
                double d2;
                if (!(entity instanceof EntityEnderCrystal)) continue;
                EntityEnderCrystal enderCrystal = (EntityEnderCrystal)entity;
                if (Math.abs(enderCrystal.field_70163_u - (double)(blockPos.func_177956_o() + 1)) >= 2.0 || (d2 = this.lastCrystal.func_70011_f((double)blockPos.func_177958_n() + 0.5, (double)(blockPos.func_177956_o() + 1), (double)blockPos.func_177952_p() + 0.5)) <= 6.0 || AutoCrystal.getRange(enderCrystal.func_174791_d(), (double)blockPos.func_177958_n() + 0.5, blockPos.func_177956_o() + 1, (double)blockPos.func_177952_p() + 0.5) >= 2.0) continue;
                return false;
            }
        }
        return true;
    }

    public boolean canHitCrystal(EntityEnderCrystal crystal) {
        if (crystal == null) {
            return false;
        }
        if ((double)AutoCrystal.mc.field_71439_g.func_70032_d((Entity)crystal) > (Double)this.breakRange.getValue()) {
            return false;
        }
        if (((Boolean)this.wall.getValue()).booleanValue() && (double)AutoCrystal.mc.field_71439_g.func_70032_d((Entity)crystal) > (Double)this.breakWallRange.getValue() && !CrystalUtil.calculateRaytrace((Entity)crystal)) {
            return false;
        }
        if (crystal == this.crystal && DamageUtil.calculateCrystalDamage((EntityLivingBase)this.placeInfo.target.player, this.placeInfo.target.position, this.placeInfo.target.boundingBox, (double)this.placeInfo.blockPos.field_177962_a + 0.5, this.placeInfo.blockPos.field_177960_b + 1, (double)this.placeInfo.blockPos.field_177961_c + 0.5) >= (float)((Integer)this.breakMinDmg.getValue()).intValue()) {
            return true;
        }
        float healthSelf = AutoCrystal.mc.field_71439_g.func_110143_aJ() + AutoCrystal.mc.field_71439_g.func_110139_bj();
        float selfDamage = 0.0f;
        if (((String)this.godMode.getValue()).equals("NoGodMode") || ((String)this.godMode.getValue()).equals("Auto") && !AutoCrystal.mc.field_71439_g.func_184812_l_()) {
            EntityPlayerSP player = (Boolean)this.self.getValue() != false ? PredictUtil.predictPlayer((EntityLivingBase)AutoCrystal.mc.field_71439_g, this.settings) : AutoCrystal.mc.field_71439_g;
            PlayerInfo self = new PlayerInfo((EntityPlayer)AutoCrystal.mc.field_71439_g, player.func_174791_d(), player.func_174813_aQ());
            selfDamage = DamageUtil.calculateCrystalDamage((EntityLivingBase)self.player, self.position, self.boundingBox, crystal.field_70165_t, crystal.field_70163_u, crystal.field_70161_v);
        }
        CrystalInfo bestTarget = this.getBestDmg(crystal);
        if (bestTarget.player == null) {
            return false;
        }
        if (selfDamage != 0.0f && ((double)selfDamage + (Double)this.balance.getValue() >= (double)healthSelf || (double)selfDamage + (Double)this.balance.getValue() > (Double)this.maxSelfDMG.getValue())) {
            if (!((Boolean)this.forceBreak.getValue()).booleanValue()) {
                return false;
            }
            return bestTarget.player.health <= bestTarget.damage;
        }
        double minDamage = ((Integer)this.breakMinDmg.getValue()).intValue();
        if (this.canFacePlace(bestTarget.player)) {
            minDamage = (Double)this.fpMinDmg.getValue();
        }
        return bestTarget.damage >= minDamage;
    }

    public boolean canFacePlace(PlayerInfo target) {
        if (target == null || target.player == null || !((Boolean)this.facePlace.getValue()).booleanValue()) {
            return false;
        }
        if (target.health < (double)((Integer)this.BlastHealth.getValue()).intValue()) {
            return true;
        }
        for (ItemStack itemStack : target.player.func_184193_aE()) {
            float dmg;
            if (itemStack.func_190926_b() || itemStack.func_190916_E() > (Integer)this.armorCount.getValue() || !((dmg = ((float)itemStack.func_77958_k() - (float)itemStack.func_77952_i()) / (float)itemStack.func_77958_k()) < (float)((Integer)this.armorRate.getValue()).intValue() / 100.0f)) continue;
            return true;
        }
        return false;
    }

    private int getItemHotbar() {
        for (int i = 0; i < (this.findInventory() ? 36 : 9); ++i) {
            Item item = AutoCrystal.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
            if (Item.func_150891_b((Item)item) != Item.func_150891_b((Item)Items.field_185158_cP)) continue;
            return i;
        }
        return -1;
    }

    private boolean findInventory() {
        return (Boolean)this.bypass.getValue() != false && (Boolean)this.switchBack.getValue() != false;
    }

    private void switchOffhand(boolean value) {
        if (ModuleManager.isModuleEnabled(OffHand.class)) {
            OffHand.INSTANCE.autoCrystal = value;
        }
    }

    private void pausePA(boolean value) {
        if (ModuleManager.isModuleEnabled(PistonAura.class)) {
            PistonAura.INSTANCE.autoCrystal = value;
        }
        if (ModuleManager.isModuleEnabled(PullCrystal.class)) {
            PullCrystal.INSTANCE.autoCrystal = value;
        }
    }

    private boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : AutoCrystal.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityExpBottle || !new AxisAlignedBB(pos).func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    private boolean crystalPlaceBoxIntersectsCrystalBox(BlockPos placePos, Entity entity) {
        return entity.field_70121_D.func_72326_a(new AxisAlignedBB((double)placePos.field_177962_a - 0.5, (double)placePos.field_177960_b, (double)placePos.field_177961_c - 0.5, (double)placePos.field_177962_a + 1.5, (double)(placePos.field_177960_b + ((Boolean)this.highVersion.getValue() != false ? 1 : 2)), (double)placePos.field_177961_c + 1.5));
    }

    @Override
    public void onEnable() {
        this.lastBreakTime = System.currentTimeMillis();
        this.lastEntityID = -1;
        this.c = 0;
        this.crystals = 0;
        this.updateTime = System.currentTimeMillis();
        this.startTime = System.currentTimeMillis();
        this.ShouldInfoLastBreak = false;
        this.afterAttacking = false;
        this.canPredictHit = true;
        this.PlaceTimer.reset();
        this.ExplodeTimer.reset();
        this.PacketExplodeTimer.reset();
        this.UpdateTimer.reset();
        this.CalcTimer.reset();
        packetList.clear();
        this.lastSlot = AutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c;
        this.lastHitVec = null;
        this.placeInfo = null;
        this.movingPlaceNow = new Vec3d(-1.0, -1.0, -1.0);
    }

    @Override
    public void onDisable() {
        this.switchOffhand(false);
        this.pausePA(false);
        this.lastHitVec = null;
        this.StuckTimes = 0;
        packetList.clear();
    }

    @Override
    public String getHudInfo() {
        if (!this.check()) {
            return "";
        }
        if (this.ShouldInfoLastBreak) {
            this.infoBreakTime = System.currentTimeMillis() - this.lastBreakTime;
            this.lastBreakTime = 0L;
            this.ShouldInfoLastBreak = false;
        }
        if (this.calculated) {
            this.c = this.crystals;
            this.calculated = false;
            this.crystals = 0;
        }
        String text = "[" + ChatFormatting.WHITE + this.placeInfo.target.player.func_70005_c_() + ((Boolean)this.showBreakDelay.getValue() != false ? ", " + this.infoBreakTime + "ms" : "") + ((Boolean)this.speedDebug.getValue() != false ? ", " + this.c + "c/s" : "") + ChatFormatting.GRAY + "]";
        return text;
    }

    @Override
    public void onWorldRender(RenderEvent event) {
        BlockPos placing;
        if (AutoCrystal.mc.field_71441_e == null || AutoCrystal.mc.field_71439_g == null) {
            return;
        }
        BlockPos blockPos = placing = this.placeInfo == null ? null : this.placeInfo.blockPos;
        if (placing != this.lastBestPlace) {
            if (placing != null && this.lastBestPlace == null) {
                this.movingPlaceNow = new Vec3d((double)this.placeInfo.blockPos.func_177958_n(), (double)this.placeInfo.blockPos.func_177956_o(), (double)this.placeInfo.blockPos.func_177952_p());
            }
            this.updateTime = System.currentTimeMillis();
            if (placing == null) {
                this.startTime = System.currentTimeMillis();
            } else if (this.lastBestPlace == null) {
                this.startTime = System.currentTimeMillis();
            }
            this.lastBestPlace = placing;
        }
        if (this.lastBestPlace != null) {
            if (this.movingPlaceNow.field_72450_a == -1.0 && this.movingPlaceNow.field_72448_b == -1.0 && this.movingPlaceNow.field_72449_c == -1.0) {
                this.movingPlaceNow = new Vec3d((double)this.lastBestPlace.func_177958_n(), (double)this.lastBestPlace.func_177956_o(), (double)this.lastBestPlace.func_177952_p());
            }
            this.movingPlaceNow = (Integer)this.movingTime.getValue() == 0 ? new Vec3d((Vec3i)this.lastBestPlace) : new Vec3d(this.movingPlaceNow.field_72450_a + ((double)this.lastBestPlace.func_177958_n() - this.movingPlaceNow.field_72450_a) * (double)this.toDelta(this.updateTime, ((Integer)this.movingTime.getValue()).intValue()), this.movingPlaceNow.field_72448_b + ((double)this.lastBestPlace.func_177956_o() - this.movingPlaceNow.field_72448_b) * (double)this.toDelta(this.updateTime, ((Integer)this.movingTime.getValue()).intValue()), this.movingPlaceNow.field_72449_c + ((double)this.lastBestPlace.func_177952_p() - this.movingPlaceNow.field_72449_c) * (double)this.toDelta(this.updateTime, ((Integer)this.movingTime.getValue()).intValue()));
        }
        if (this.movingPlaceNow.field_72450_a != -1.0 || this.movingPlaceNow.field_72448_b != -1.0 || this.movingPlaceNow.field_72449_c != -1.0) {
            this.drawBoxMain(this.movingPlaceNow.field_72450_a, this.movingPlaceNow.field_72448_b, this.movingPlaceNow.field_72449_c);
        }
    }

    AxisAlignedBB getBox(double x, double y, double z) {
        double maxX = x + 1.0;
        double maxZ = z + 1.0;
        return new AxisAlignedBB(x, y, z, maxX, y + 1.0, maxZ);
    }

    float toDelta(long start, float length) {
        float value = (float)this.toDelta(start) / length;
        if (value > 1.0f) {
            value = 1.0f;
        }
        if (value < 0.0f) {
            value = 0.0f;
        }
        return value;
    }

    long toDelta(long start) {
        return System.currentTimeMillis() - start;
    }

    void drawBoxMain(double x, double y, double z) {
        AxisAlignedBB box = this.getBox(x, y, z);
        float size = !this.check() || this.placeInfo.blockPos == null ? 1.0f - this.toDelta(this.startTime, ((Integer)this.lifeTime.getValue()).intValue()) : this.toDelta(this.startTime, ((Integer)this.lifeTime.getValue()).intValue());
        if (((Boolean)this.scale.getValue()).booleanValue()) {
            box = box.func_186662_g((double)((1.0f - size) * (1.0f - size) / 2.0f - 1.0f));
        }
        if (((Boolean)this.flat.getValue()).booleanValue()) {
            box = new AxisAlignedBB(box.field_72340_a, box.field_72337_e, box.field_72339_c, box.field_72336_d, box.field_72337_e, box.field_72334_f);
        }
        int alpha = (int)((float)((Integer)this.alpha.getValue()).intValue() * size);
        int outAlpha = (int)((float)((Integer)this.outAlpha.getValue()).intValue() * size);
        switch ((String)this.mode.getValue()) {
            case "Outline": {
                RenderUtil.drawBoundingBox(box, (double)((Integer)this.width.getValue()).intValue(), new GSColor(this.color.getValue(), outAlpha));
                break;
            }
            case "Solid": {
                RenderUtil.drawBox(box, true, (Boolean)this.flat.getValue() != false ? 0.0 : 1.0, new GSColor(this.color.getValue(), alpha), 63);
                break;
            }
            case "Both": {
                RenderUtil.drawBox(box, true, (Boolean)this.flat.getValue() != false ? 0.0 : 1.0, new GSColor(this.color.getValue(), alpha), 63);
                RenderUtil.drawBoundingBox(box, (double)((Integer)this.width.getValue()).intValue(), new GSColor(this.color.getValue(), outAlpha));
            }
        }
        if (((Boolean)this.showDamage.getValue()).booleanValue() && this.check() && this.placeInfo.blockPos != null) {
            box = this.getBox(x, y, z);
            String[] damageText = new String[]{String.format("%.1f", this.placeInfo.dmg)};
            if (((Boolean)this.showSelfDamage.getValue()).booleanValue()) {
                damageText = new String[]{String.format("%.1f", this.placeInfo.dmg) + "/" + String.format("%.1f", this.placeInfo.selfDmg)};
            }
            RenderUtil.drawNametag(box.field_72340_a + 0.5, box.field_72338_b + 0.5, box.field_72339_c + 0.5, damageText, new GSColor(255, 255, 255), 1, 0.02666666666666667, 0.0);
        }
    }

    public static class CrystalInfo {
        EntityEnderCrystal crystal;
        PlayerInfo player;
        double damage;

        public CrystalInfo(EntityEnderCrystal crystal, PlayerInfo player, double damage) {
            this.crystal = crystal;
            this.player = player;
            this.damage = damage;
        }
    }

    public static class PlayerInfo {
        EntityPlayer player;
        Vec3d position;
        AxisAlignedBB boundingBox;
        double health;

        public PlayerInfo(EntityPlayer player) {
            this.player = player;
            if (player != null) {
                this.position = player.func_174791_d();
                this.boundingBox = player.func_174813_aQ();
                this.health = player.func_110143_aJ() + player.func_110139_bj();
            }
        }

        public PlayerInfo(EntityPlayer player, Vec3d position, AxisAlignedBB boundingBox) {
            this.player = player;
            this.position = position;
            this.boundingBox = boundingBox;
            this.health = player.func_110143_aJ() + player.func_110139_bj();
        }
    }

    public static class PlaceInfo {
        public BlockPos blockPos;
        public PlayerInfo target;
        public double dmg;
        public double selfDmg;

        public PlaceInfo(PlayerInfo target, BlockPos block, double dmg, double selfDmg) {
            this.blockPos = block;
            this.target = target;
            this.dmg = dmg;
            this.selfDmg = selfDmg;
        }
    }
}
