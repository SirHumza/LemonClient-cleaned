/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityPotion
 *  net.minecraft.init.MobEffects
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.network.play.server.SPacketDestroyEntities
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.potion.PotionUtils
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package com.lemonclient.client.module.modules.dev;

import com.lemonclient.api.event.Phase;
import com.lemonclient.api.event.events.EntityRemovedEvent;
import com.lemonclient.api.event.events.OnUpdateWalkingPlayerEvent;
import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.chat.Notification;
import com.lemonclient.api.util.misc.MathUtil;
import com.lemonclient.api.util.misc.MessageBus;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.api.util.player.PlayerPacket;
import com.lemonclient.api.util.world.BlockUtil;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.client.manager.managers.PlayerPacketManager;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Module.Declaration(name="AutoPot", category=Category.Dev, priority=1001)
public class AutoPot
extends Module {
    ModeSetting page = this.registerMode("Page", Arrays.asList("General", "BadPot"), "General");
    BooleanSetting hp = this.registerBoolean("Health Potion", false, () -> ((String)this.page.getValue()).equals("General"));
    IntegerSetting health = this.registerInteger("Health", 16, 0, 20, () -> (Boolean)this.hp.getValue() != false && ((String)this.page.getValue()).equals("General"));
    BooleanSetting equal = this.registerBoolean("Equal", false, () -> (Boolean)this.hp.getValue() != false && ((String)this.page.getValue()).equals("General"));
    BooleanSetting predict = this.registerBoolean("Predict", false, () -> (Boolean)this.hp.getValue() != false && ((String)this.page.getValue()).equals("General"));
    DoubleSetting times = this.registerDouble("Time(s)", 1.0, 0.0, 5.0, () -> (Boolean)this.hp.getValue() != false && (Boolean)this.predict.getValue() != false && ((String)this.page.getValue()).equals("General"));
    IntegerSetting predictHpDelay = this.registerInteger("Predict Health Delay", 50, 0, 1000, () -> (Boolean)this.hp.getValue() != false && (Boolean)this.predict.getValue() != false && ((String)this.page.getValue()).equals("General"));
    IntegerSetting healthSlot = this.registerInteger("Health Slot", 1, 1, 9, () -> (Boolean)this.hp.getValue() != false && ((String)this.page.getValue()).equals("General"));
    IntegerSetting hpDelay = this.registerInteger("Health Delay", 50, 0, 1000, () -> (Boolean)this.hp.getValue() != false && ((String)this.page.getValue()).equals("General"));
    BooleanSetting speed = this.registerBoolean("Swiftness", false, () -> ((String)this.page.getValue()).equals("General"));
    IntegerSetting time = this.registerInteger("Time Left", 5, 0, 30, () -> (Boolean)this.speed.getValue() != false && ((String)this.page.getValue()).equals("General"));
    IntegerSetting swiftnessSlot = this.registerInteger("Swiftness Slot", 1, 1, 9, () -> (Boolean)this.speed.getValue() != false && ((String)this.page.getValue()).equals("General"));
    IntegerSetting speedDelay = this.registerInteger("Swiftness Delay", 50, 0, 1000, () -> (Boolean)this.speed.getValue() != false && ((String)this.page.getValue()).equals("General"));
    BooleanSetting only = this.registerBoolean("On GroundOnly", true, () -> ((String)this.page.getValue()).equals("General"));
    BooleanSetting silentSwitch = this.registerBoolean("Packet Switch", true, () -> ((String)this.page.getValue()).equals("General"));
    IntegerSetting delay = this.registerInteger("Delay", 10, 0, 30, () -> ((String)this.page.getValue()).equals("BadPot"));
    DoubleSetting factor = this.registerDouble("Factor", 0.75, 0.0, 1.5, () -> ((String)this.page.getValue()).equals("BadPot"));
    DoubleSetting range = this.registerDouble("Range", 4.0, 0.0, 10.0, () -> ((String)this.page.getValue()).equals("BadPot"));
    IntegerSetting badSlot = this.registerInteger("BadPot Slot", 1, 1, 9, () -> ((String)this.page.getValue()).equals("BadPot"));
    BooleanSetting weak = this.registerBoolean("Weakness", false, () -> ((String)this.page.getValue()).equals("BadPot"));
    BooleanSetting jump = this.registerBoolean("JumpBoost", false, () -> ((String)this.page.getValue()).equals("BadPot"));
    BooleanSetting poison = this.registerBoolean("Poison", false, () -> ((String)this.page.getValue()).equals("BadPot"));
    BooleanSetting slow = this.registerBoolean("Slowness", false, () -> ((String)this.page.getValue()).equals("BadPot"));
    BooleanSetting debug = this.registerBoolean("Debug", false, () -> ((String)this.page.getValue()).equals("BadPot"));
    HashMap<Integer, Long> weaknessTime = new HashMap();
    HashMap<Integer, Long> jumpBoostTime = new HashMap();
    HashMap<Integer, Long> poisonTime = new HashMap();
    HashMap<Integer, Long> slownessTime = new HashMap();
    Timing hpTimer = new Timing();
    Timing hpPredictTimer = new Timing();
    Timing speedTimer = new Timing();
    Timing badPotTimer = new Timing();
    int potionSlot;
    int potSlot;
    double lastHealth = 36.0;
    boolean working = false;
    boolean preHp;
    @EventHandler
    private final Listener<OnUpdateWalkingPlayerEvent> onUpdateWalkingPlayerEventListener = new Listener<OnUpdateWalkingPlayerEvent>(event -> {
        if (AutoPot.mc.field_71441_e == null || AutoPot.mc.field_71439_g == null || AutoPot.mc.field_71439_g.field_70128_L) {
            return;
        }
        this.working = false;
        if (((Boolean)this.only.getValue()).booleanValue() || AutoPot.mc.field_71439_g.func_180799_ab() || AutoPot.mc.field_71439_g.func_70090_H()) {
            ArrayList<BlockPos> posList = new ArrayList<BlockPos>();
            Vec3d floorPos = new Vec3d(AutoPot.mc.field_71439_g.field_70165_t, AutoPot.mc.field_71439_g.field_70163_u - 2.0, AutoPot.mc.field_71439_g.field_70161_v);
            AxisAlignedBB potBox = new AxisAlignedBB(AutoPot.mc.field_71439_g.field_70165_t - 0.125, AutoPot.mc.field_71439_g.field_70163_u - 2.0, AutoPot.mc.field_71439_g.field_70161_v - 0.125, AutoPot.mc.field_71439_g.field_70165_t + 0.125, AutoPot.mc.field_71439_g.field_70163_u + (double)AutoPot.mc.field_71439_g.eyeHeight + 0.125, AutoPot.mc.field_71439_g.field_70161_v + 0.125);
            int i = 0;
            while ((double)i < AutoPot.mc.field_71439_g.field_70163_u + (double)AutoPot.mc.field_71439_g.eyeHeight + 1.125 && (int)(floorPos.field_72448_b + (double)i) <= (int)(AutoPot.mc.field_71439_g.field_70163_u + (double)AutoPot.mc.field_71439_g.eyeHeight + 0.125)) {
                for (Vec3d vec3d : new Vec3d[]{new Vec3d(0.125, 0.0, 0.125), new Vec3d(0.125, 0.0, -0.125), new Vec3d(-0.125, 0.0, 0.125), new Vec3d(-0.125, 0.0, -0.125)}) {
                    BlockPos pos = new BlockPos(floorPos.field_72450_a + vec3d.field_72450_a, floorPos.field_72448_b + (double)i, floorPos.field_72449_c + vec3d.field_72449_c);
                    if (BlockUtil.isAir(pos)) continue;
                    posList.add(pos);
                }
                ++i;
            }
            boolean can = false;
            for (BlockPos pos : posList) {
                AxisAlignedBB box = BlockUtil.getBoundingBox(pos);
                if (box == null || !MathUtil.isIntersect(potBox, box)) continue;
                can = true;
                break;
            }
            if (!can) {
                return;
            }
        }
        if (this.potionSlot == -1) {
            this.potionSlot = this.getPotion();
        }
        if (this.potSlot == -1) {
            this.potSlot = this.getBadPot();
        }
        if (this.potionSlot == -1 && this.potSlot == -1) {
            return;
        }
        this.working = true;
        if (this.potionSlot > 8) {
            if (AutoPot.mc.field_71462_r instanceof GuiContainer && !(AutoPot.mc.field_71462_r instanceof GuiInventory)) {
                return;
            }
            int finalSlot = this.potionSlot == InventoryUtil.getPotion("swiftness") ? ((Integer)this.swiftnessSlot.getValue()).intValue() : ((Integer)this.healthSlot.getValue()).intValue();
            AutoPot.mc.field_71442_b.func_187098_a(0, this.potionSlot, finalSlot - 1, ClickType.SWAP, (EntityPlayer)AutoPot.mc.field_71439_g);
            AutoPot.mc.field_71442_b.func_78765_e();
            this.potionSlot = finalSlot - 1;
        }
        if (this.potSlot > 8) {
            if (AutoPot.mc.field_71462_r instanceof GuiContainer && !(AutoPot.mc.field_71462_r instanceof GuiInventory)) {
                return;
            }
            AutoPot.mc.field_71442_b.func_187098_a(0, this.potSlot, (Integer)this.badSlot.getValue() - 1, ClickType.SWAP, (EntityPlayer)AutoPot.mc.field_71439_g);
            AutoPot.mc.field_71442_b.func_78765_e();
            this.potSlot = (Integer)this.badSlot.getValue() - 1;
        }
        if (event.getPhase() == Phase.PRE) {
            PlayerPacket packet = new PlayerPacket((Module)this, new Vec2f(PlayerPacketManager.INSTANCE.getServerSideRotation().field_189982_i, 90.0f));
            PlayerPacketManager.INSTANCE.addPacket(packet);
        }
        if (event.getPhase() == Phase.POST && (PlayerPacketManager.INSTANCE.getPrevServerSideRotation().field_189983_j > 85.0f || PlayerPacketManager.INSTANCE.getServerSideRotation().field_189983_j > 85.0f)) {
            int slot = this.potionSlot == -1 ? this.potSlot : this.potionSlot;
            InventoryUtil.run(slot, (Boolean)this.silentSwitch.getValue(), () -> AutoPot.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND)));
            this.potSlot = -1;
            this.potionSlot = -1;
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.Send> sendListener = new Listener<PacketEvent.Send>(event -> {
        if (this.working) {
            if (event.getPacket() instanceof CPacketPlayer.Rotation) {
                ((CPacketPlayer.Rotation)event.getPacket()).field_149473_f = 90.0f;
            }
            if (event.getPacket() instanceof CPacketPlayer.PositionRotation) {
                ((CPacketPlayer.PositionRotation)event.getPacket()).field_149473_f = 90.0f;
            }
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.PostReceive> receiveListener = new Listener<PacketEvent.PostReceive>(event -> {
        if (event.getPacket() instanceof SPacketDestroyEntities) {
            Arrays.stream(((SPacketDestroyEntities)event.getPacket()).func_149098_c()).forEach(this.weaknessTime::remove);
            Arrays.stream(((SPacketDestroyEntities)event.getPacket()).func_149098_c()).forEach(this.jumpBoostTime::remove);
            Arrays.stream(((SPacketDestroyEntities)event.getPacket()).func_149098_c()).forEach(this.poisonTime::remove);
            Arrays.stream(((SPacketDestroyEntities)event.getPacket()).func_149098_c()).forEach(this.slownessTime::remove);
        }
        if (event.getPacket() instanceof SPacketEntityStatus && ((SPacketEntityStatus)event.getPacket()).func_149160_c() == 35) {
            this.weaknessTime.remove(((SPacketEntityStatus)event.getPacket()).func_149161_a((World)AutoPot.mc.field_71441_e).field_145783_c);
            this.jumpBoostTime.remove(((SPacketEntityStatus)event.getPacket()).func_149161_a((World)AutoPot.mc.field_71441_e).field_145783_c);
            this.poisonTime.remove(((SPacketEntityStatus)event.getPacket()).func_149161_a((World)AutoPot.mc.field_71441_e).field_145783_c);
            this.slownessTime.remove(((SPacketEntityStatus)event.getPacket()).func_149161_a((World)AutoPot.mc.field_71441_e).field_145783_c);
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<EntityRemovedEvent> entityRemovedEventListener = new Listener<EntityRemovedEvent>(event -> {
        if (event.getEntity() instanceof EntityPotion) {
            List effectList = PotionUtils.func_185189_a((ItemStack)((EntityPotion)event.getEntity()).func_184543_l());
            PotionEffect weakness = null;
            PotionEffect jumpBoost = null;
            PotionEffect poison = null;
            PotionEffect slowness = null;
            for (PotionEffect effect : effectList) {
                if (effect.func_188419_a() == MobEffects.field_76437_t) {
                    weakness = effect;
                }
                if (effect.func_188419_a() == MobEffects.field_76430_j) {
                    jumpBoost = effect;
                }
                if (effect.func_188419_a() == MobEffects.field_76436_u) {
                    poison = effect;
                }
                if (effect.func_188419_a() != MobEffects.field_76421_d) continue;
                slowness = effect;
            }
            AxisAlignedBB box = event.getEntity().field_70121_D.func_72314_b(4.0, 2.0, 4.0);
            PotionEffect finalWeakness = weakness;
            PotionEffect finalJumpBoost = jumpBoost;
            PotionEffect finalPoison = poison;
            PotionEffect finalSlowness = slowness;
            AutoPot.mc.field_71441_e.field_73010_i.stream().filter(p -> AutoPot.mc.field_71439_g.field_71174_a.func_175104_a(p.func_70005_c_()) != null).filter(EntityUtil::isAlive).filter(p -> box.func_72326_a(p.field_70121_D)).forEach(p -> {
                double distanceSq = event.getEntity().func_70068_e((Entity)p);
                if (distanceSq < 16.0) {
                    double duration;
                    double factor = Math.sqrt(distanceSq) * (Double)this.factor.getValue();
                    if (finalWeakness != null) {
                        duration = factor * (double)finalWeakness.func_76459_b();
                        this.weaknessTime.put(p.func_145782_y(), (long)((double)System.currentTimeMillis() + duration * 50.0));
                    }
                    if (finalJumpBoost != null) {
                        duration = factor * (double)finalJumpBoost.func_76459_b();
                        this.jumpBoostTime.put(p.func_145782_y(), (long)((double)System.currentTimeMillis() + duration * 50.0));
                    }
                    if (finalPoison != null) {
                        duration = factor * (double)finalPoison.func_76459_b();
                        this.poisonTime.put(p.func_145782_y(), (long)((double)System.currentTimeMillis() + duration * 50.0));
                    }
                    if (finalSlowness != null) {
                        duration = factor * (double)finalSlowness.func_76459_b();
                        this.slownessTime.put(p.func_145782_y(), (long)((double)System.currentTimeMillis() + duration * 50.0));
                    }
                }
            });
        }
    }, new Predicate[0]);

    @Override
    public void onEnable() {
        this.slownessTime = new HashMap();
        this.poisonTime = this.slownessTime;
        this.jumpBoostTime = this.slownessTime;
        this.weaknessTime = this.slownessTime;
    }

    @Override
    public void fast() {
        for (EntityPlayer player : AutoPot.mc.field_71441_e.field_73010_i) {
            int id = player.func_145782_y();
            long time = System.currentTimeMillis();
            if (this.weaknessTime.containsKey(id) && this.weaknessTime.get(id) <= time) {
                this.weaknessTime.remove(id);
            }
            if (this.jumpBoostTime.containsKey(id) && this.jumpBoostTime.get(id) <= time) {
                this.jumpBoostTime.remove(id);
            }
            if (this.poisonTime.containsKey(id) && this.poisonTime.get(id) <= time) {
                this.poisonTime.remove(id);
            }
            if (!this.slownessTime.containsKey(id) || this.slownessTime.get(id) > time) continue;
            this.slownessTime.remove(id);
        }
        if (!((Boolean)this.debug.getValue()).booleanValue()) {
            return;
        }
        StringBuilder weak = new StringBuilder("Weakness");
        for (EntityPlayer player : AutoPot.mc.field_71441_e.field_73010_i) {
            if (!this.weaknessTime.containsKey(player.func_145782_y())) continue;
            weak.append(player.func_70005_c_()).append(" ").append(this.weaknessTime.get(player.func_145782_y()) - System.currentTimeMillis()).append(", ");
        }
        if (!weak.toString().equals("Weakness")) {
            MessageBus.sendClientDeleteMessage(weak.toString(), Notification.Type.DISABLE, "Weakness", 0);
        }
    }

    private int getPotion() {
        if (((Boolean)this.hp.getValue()).booleanValue()) {
            int slot;
            if (this.healthCheck(((Integer)this.health.getValue()).intValue()) && this.hpTimer.passedMs(((Integer)this.hpDelay.getValue()).intValue())) {
                this.preHp = false;
                this.hpTimer.reset();
                slot = InventoryUtil.getPotion("healing");
                if (slot != -1) {
                    return slot;
                }
            }
            if (((Boolean)this.predict.getValue()).booleanValue()) {
                this.healthPredict();
            }
            if (this.preHp && this.hpPredictTimer.passedMs(((Integer)this.predictHpDelay.getValue()).intValue())) {
                this.preHp = false;
                this.hpPredictTimer.reset();
                slot = InventoryUtil.getPotion("healing");
                if (slot != -1) {
                    return slot;
                }
            }
        }
        if (((Boolean)this.speed.getValue()).booleanValue() && (!AutoPot.mc.field_71439_g.func_70644_a(MobEffects.field_76424_c) || Objects.requireNonNull(AutoPot.mc.field_71439_g.func_70660_b(MobEffects.field_76424_c)).func_76459_b() <= (Integer)this.time.getValue() * 20) && this.speedTimer.passedMs(((Integer)this.speedDelay.getValue()).intValue())) {
            this.speedTimer.reset();
            return InventoryUtil.getPotion("swiftness");
        }
        return -1;
    }

    private int getBadPot() {
        if (this.badPotTimer.passedS(((Integer)this.delay.getValue()).intValue())) {
            this.badPotTimer.reset();
            for (EntityPlayer player : AutoPot.mc.field_71441_e.field_73010_i) {
                int slot;
                if (AutoPot.mc.field_71439_g.field_71174_a.func_175104_a(player.func_70005_c_()) == null || EntityUtil.basicChecksEntity(player) || (double)AutoPot.mc.field_71439_g.func_70032_d((Entity)player) > (Double)this.range.getValue()) continue;
                if (((Boolean)this.weak.getValue()).booleanValue() && !this.weaknessTime.containsKey(player.func_145782_y()) && (slot = InventoryUtil.getPotion("weakness")) != -1) {
                    return slot;
                }
                if (((Boolean)this.jump.getValue()).booleanValue() && !this.jumpBoostTime.containsKey(player.func_145782_y()) && (slot = InventoryUtil.getPotion("leaping")) != -1) {
                    return slot;
                }
                if (((Boolean)this.poison.getValue()).booleanValue() && !this.poisonTime.containsKey(player.func_145782_y()) && (slot = InventoryUtil.getPotion("poison")) != -1) {
                    return slot;
                }
                if (!((Boolean)this.slow.getValue()).booleanValue() || this.slownessTime.containsKey(player.func_145782_y())) continue;
                return InventoryUtil.getPotion("slowness");
            }
        }
        return -1;
    }

    private boolean healthCheck(double value) {
        return (double)AutoPot.mc.field_71439_g.func_110143_aJ() < value || (Boolean)this.equal.getValue() != false && (double)AutoPot.mc.field_71439_g.func_110143_aJ() == value;
    }

    private void healthPredict() {
        double change;
        double health = AutoPot.mc.field_71439_g.func_110143_aJ() + AutoPot.mc.field_71439_g.func_110139_bj();
        if (health == 36.0) {
            this.lastHealth = 36.0;
        }
        if ((change = health - this.lastHealth) >= 0.0) {
            return;
        }
        this.lastHealth = health;
        this.preHp = (health += change * (Double)this.times.getValue()) < (double)((Integer)this.health.getValue()).intValue() || (Boolean)this.equal.getValue() != false && health == (double)((Integer)this.health.getValue()).intValue();
    }
}
