/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.Vec2f
 */
package com.lemonclient.client.module.modules.combat;

import com.lemonclient.api.event.Phase;
import com.lemonclient.api.event.events.OnUpdateWalkingPlayerEvent;
import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.DoubleSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.util.misc.Timing;
import com.lemonclient.api.util.player.InventoryUtil;
import com.lemonclient.api.util.player.PlayerPacket;
import com.lemonclient.api.util.world.EntityUtil;
import com.lemonclient.api.util.world.combat.DamageUtil;
import com.lemonclient.client.LemonClient;
import com.lemonclient.client.manager.managers.PlayerPacketManager;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec2f;

@Module.Declaration(name="AutoMend", category=Category.Combat)
public class AutoMend
extends Module {
    BooleanSetting rotate = this.registerBoolean("Rotate", true);
    BooleanSetting packetSwitch = this.registerBoolean("Packet Switch", true);
    IntegerSetting delay = this.registerInteger("Delay", 0, 0, 1000);
    IntegerSetting minDamage = this.registerInteger("Min Damage", 50, 1, 100);
    IntegerSetting maxHeal = this.registerInteger("Repair To", 90, 1, 100);
    BooleanSetting takeOff = this.registerBoolean("TakeOff", true);
    IntegerSetting takeOffDelay = this.registerInteger("TakeOff Delay", 0, 0, 1000);
    BooleanSetting predict = this.registerBoolean("Predict", true);
    BooleanSetting crystal = this.registerBoolean("Crystal Check", true);
    DoubleSetting biasDamage = this.registerDouble("Bias Damage", 1.0, 0.0, 3.0);
    BooleanSetting health = this.registerBoolean("Health Check", true);
    IntegerSetting minHealth = this.registerInteger("Min Health", 16, 0, 36, () -> (Boolean)this.health.getValue());
    BooleanSetting player = this.registerBoolean("Enemy Check", true);
    DoubleSetting maxSpeed = this.registerDouble("Max Speed", 10.0, 0.0, 50.0, () -> (Boolean)this.player.getValue());
    int tookOff;
    Timing timer = new Timing();
    Timing takeOffTimer = new Timing();
    char toMend = '\u0000';
    @EventHandler
    private final Listener<OnUpdateWalkingPlayerEvent> onUpdateWalkingPlayerEventListener = new Listener<OnUpdateWalkingPlayerEvent>(event -> {
        if (!((Boolean)this.rotate.getValue()).booleanValue()) {
            return;
        }
        if (event.getPhase() != Phase.PRE) {
            return;
        }
        PlayerPacket packet = new PlayerPacket((Module)this, new Vec2f(PlayerPacketManager.INSTANCE.getServerSideRotation().field_189982_i, 90.0f));
        PlayerPacketManager.INSTANCE.addPacket(packet);
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.Send> sendListener = new Listener<PacketEvent.Send>(event -> {
        if (!((Boolean)this.rotate.getValue()).booleanValue()) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayer.Rotation) {
            ((CPacketPlayer.Rotation)event.getPacket()).field_149476_e = PlayerPacketManager.INSTANCE.getServerSideRotation().field_189982_i;
        }
        if (event.getPacket() instanceof CPacketPlayer.PositionRotation) {
            ((CPacketPlayer.PositionRotation)event.getPacket()).field_149476_e = PlayerPacketManager.INSTANCE.getServerSideRotation().field_189982_i;
        }
    }, new Predicate[0]);

    @Override
    public void onEnable() {
        this.tookOff = 0;
    }

    @Override
    public void onTick() {
        if (AutoMend.mc.field_71439_g == null || AutoMend.mc.field_71441_e == null || AutoMend.mc.field_71439_g.field_70128_L || AutoMend.mc.field_71439_g.field_70173_aa < 10) {
            this.disable();
            return;
        }
        if (((Boolean)this.crystal.getValue()).booleanValue() && this.crystalDamage()) {
            this.setDisabledMessage("Lethal crystal nearby");
            this.disable();
            return;
        }
        if (((Boolean)this.health.getValue()).booleanValue() && AutoMend.mc.field_71439_g.func_110143_aJ() + AutoMend.mc.field_71439_g.func_110139_bj() < (float)((Integer)this.minHealth.getValue()).intValue()) {
            this.setDisabledMessage("Low health");
            this.disable();
            return;
        }
        if (((Boolean)this.player.getValue()).booleanValue() && this.checkNearbyPlayers()) {
            this.setDisabledMessage("Players nearby");
            this.disable();
            return;
        }
        if (this.findXPSlot() == -1) {
            this.setDisabledMessage("No xp bottle found in hotbar");
            this.disable();
            return;
        }
        if (this.checkFinished()) {
            this.setDisabledMessage("Finished mending armors");
            this.disable();
            return;
        }
        if (!this.timer.passedMs(((Integer)this.delay.getValue()).intValue())) {
            return;
        }
        this.timer.reset();
        int sumOfDamage = 0;
        NonNullList armour = AutoMend.mc.field_71439_g.field_71071_by.field_70460_b;
        for (int i = 0; i < armour.size(); ++i) {
            ItemStack itemStack = (ItemStack)armour.get(i);
            if (itemStack.field_190928_g) continue;
            float damageOnArmor = itemStack.func_77958_k() - itemStack.func_77952_i();
            float damagePercent = 100.0f - 100.0f * (1.0f - damageOnArmor / (float)itemStack.func_77958_k());
            if (damagePercent <= (float)((Integer)this.maxHeal.getValue()).intValue()) {
                if (damagePercent <= (float)((Integer)this.minDamage.getValue()).intValue()) {
                    this.toMend = (char)(this.toMend | (char)(1 << i));
                }
                if (!((Boolean)this.predict.getValue()).booleanValue()) continue;
                sumOfDamage += (int)((float)(itemStack.func_77958_k() * (Integer)this.maxHeal.getValue()) / 100.0f - (float)(itemStack.func_77958_k() - itemStack.func_77952_i()));
                continue;
            }
            this.toMend = (char)(this.toMend & (char)(~(1 << i)));
        }
        if (this.toMend > '\u0000') {
            if (((Boolean)this.predict.getValue()).booleanValue()) {
                int totalXp = AutoMend.mc.field_71441_e.field_72996_f.stream().filter(entity -> entity instanceof EntityXPOrb).filter(entity -> entity.func_70068_e((Entity)AutoMend.mc.field_71439_g) <= 1.0).mapToInt(entity -> ((EntityXPOrb)entity).field_70530_e).sum();
                if (totalXp * 2 < sumOfDamage) {
                    this.mendArmor();
                }
            } else {
                this.mendArmor();
            }
        }
    }

    private void mendArmor() {
        int newSlot = this.findXPSlot();
        if (newSlot == -1) {
            return;
        }
        InventoryUtil.run(newSlot, (Boolean)this.packetSwitch.getValue(), () -> AutoMend.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND)));
        if (((Boolean)this.takeOff.getValue()).booleanValue()) {
            this.takeArmorOff();
        }
    }

    private void takeArmorOff() {
        for (int slot = 5; slot <= 8; ++slot) {
            ItemStack item = this.getArmor(slot);
            double max_dam = item.func_77958_k();
            double dam_left = item.func_77958_k() - item.func_77952_i();
            double percent = dam_left / max_dam * 100.0;
            if (!(percent >= (double)((Integer)this.maxHeal.getValue()).intValue()) || item.func_77973_b() == Items.field_190931_a) continue;
            if (!this.notInInv(Items.field_190931_a).booleanValue()) {
                return;
            }
            if (!this.takeOffTimer.passedMs(((Integer)this.takeOffDelay.getValue()).intValue())) {
                return;
            }
            this.takeOffTimer.reset();
            boolean hasEmpty = false;
            for (int l_I = 0; l_I < 36; ++l_I) {
                ItemStack l_Stack = AutoMend.mc.field_71439_g.field_71071_by.func_70301_a(l_I);
                if (!l_Stack.field_190928_g) continue;
                hasEmpty = true;
                break;
            }
            if (hasEmpty) {
                AutoMend.mc.field_71442_b.func_187098_a(0, slot, 0, ClickType.QUICK_MOVE, (EntityPlayer)AutoMend.mc.field_71439_g);
                continue;
            }
            for (int l_l = 1; l_l < 5; ++l_l) {
                if (!AutoMend.mc.field_71439_g.field_71069_bz.func_75139_a((int)l_l).func_75211_c().field_190928_g) continue;
                AutoMend.mc.field_71442_b.func_187098_a(AutoMend.mc.field_71439_g.field_71069_bz.field_75152_c, slot, 0, ClickType.PICKUP, (EntityPlayer)AutoMend.mc.field_71439_g);
                AutoMend.mc.field_71442_b.func_187098_a(AutoMend.mc.field_71439_g.field_71069_bz.field_75152_c, l_l, 0, ClickType.PICKUP, (EntityPlayer)AutoMend.mc.field_71439_g);
            }
        }
    }

    private ItemStack getArmor(int first) {
        return (ItemStack)AutoMend.mc.field_71439_g.field_71069_bz.func_75138_a().get(first);
    }

    public Boolean notInInv(Item itemOfChoice) {
        int n = 0;
        if (itemOfChoice == AutoMend.mc.field_71439_g.func_184592_cb().func_77973_b()) {
            return true;
        }
        for (int i = 35; i >= 0; --i) {
            Item item = AutoMend.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
            if (item == itemOfChoice) {
                return true;
            }
            ++n;
        }
        return n <= 35;
    }

    private int findXPSlot() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            if (AutoMend.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != Items.field_151062_by) continue;
            slot = i;
            break;
        }
        return slot;
    }

    private boolean crystalDamage() {
        for (Entity t : AutoMend.mc.field_71441_e.field_72996_f) {
            if (!(t instanceof EntityEnderCrystal) || !(AutoMend.mc.field_71439_g.func_70032_d(t) <= 12.0f) || !((double)DamageUtil.calculateDamage((EntityLivingBase)AutoMend.mc.field_71439_g, AutoMend.mc.field_71439_g.func_174791_d(), AutoMend.mc.field_71439_g.func_174813_aQ(), (EntityEnderCrystal)t) * (Double)this.biasDamage.getValue() >= (double)AutoMend.mc.field_71439_g.func_110143_aJ())) continue;
            return true;
        }
        return false;
    }

    private boolean checkNearbyPlayers() {
        AxisAlignedBB box = new AxisAlignedBB(AutoMend.mc.field_71439_g.field_70165_t - 0.5, AutoMend.mc.field_71439_g.field_70163_u - 0.5, AutoMend.mc.field_71439_g.field_70161_v - 0.5, AutoMend.mc.field_71439_g.field_70165_t + 0.5, AutoMend.mc.field_71439_g.field_70163_u + 2.5, AutoMend.mc.field_71439_g.field_70161_v + 0.5);
        for (EntityPlayer entity : AutoMend.mc.field_71441_e.field_73010_i) {
            if (EntityUtil.basicChecksEntity(entity) || AutoMend.mc.field_71439_g.field_71174_a.func_175104_a(entity.func_70005_c_()) == null || LemonClient.speedUtil.getPlayerSpeed(entity) >= (Double)this.maxSpeed.getValue() || !box.func_72326_a(entity.func_174813_aQ())) continue;
            return true;
        }
        return false;
    }

    private boolean checkFinished() {
        int finished = 0;
        for (int slot = 5; slot <= 8; ++slot) {
            ItemStack item = this.getArmor(slot);
            if (this.getItemDamage(slot) < (Integer)this.maxHeal.getValue() && item != ItemStack.field_190927_a) continue;
            ++finished;
        }
        return finished >= 4;
    }

    private int getItemDamage(int slot) {
        ItemStack itemStack = AutoMend.mc.field_71439_g.field_71069_bz.func_75139_a(slot).func_75211_c();
        float green = ((float)itemStack.func_77958_k() - (float)itemStack.func_77952_i()) / (float)itemStack.func_77958_k();
        float red = 1.0f - green;
        return 100 - (int)(red * 100.0f);
    }
}
