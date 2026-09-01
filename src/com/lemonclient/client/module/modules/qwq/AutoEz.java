/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.world.World
 *  net.minecraftforge.event.entity.living.LivingDeathEvent
 */
package com.lemonclient.client.module.modules.qwq;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.StringSetting;
import com.lemonclient.api.util.misc.MessageBus;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

@Module.Declaration(name="AutoEz", category=Category.qwq)
public class AutoEz
extends Module {
    public static AutoEz INSTANCE;
    public BooleanSetting hi = this.registerBoolean("Use {name} for target name", true);
    StringSetting msg = this.registerString("Msg", ">Ez");
    IntegerSetting delay = this.registerInteger("Delay", 0, 0, 20);
    List<Target> targetedPlayers;
    int waited;
    @EventHandler
    private final Listener<PacketEvent.Send> sendListener = new Listener<PacketEvent.Send>(event -> {
        if (AutoEz.mc.field_71439_g != null) {
            Entity targetEntity;
            CPacketUseEntity cPacketUseEntity;
            if (this.targetedPlayers == null) {
                this.targetedPlayers = new ArrayList<Target>();
            }
            if (this.waited > 0) {
                return;
            }
            if (event.getPacket() instanceof CPacketUseEntity && (cPacketUseEntity = (CPacketUseEntity)event.getPacket()).func_149565_c().equals((Object)CPacketUseEntity.Action.ATTACK) && (targetEntity = cPacketUseEntity.func_149564_a((World)AutoEz.mc.field_71441_e)) instanceof EntityPlayer) {
                this.addTargetedPlayer(targetEntity.func_70005_c_());
            }
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<LivingDeathEvent> livingDeathEventListener = new Listener<LivingDeathEvent>(event -> {
        if (AutoEz.mc.field_71439_g != null) {
            EntityPlayer player;
            if (this.targetedPlayers == null) {
                this.targetedPlayers = new ArrayList<Target>();
            }
            if (this.waited > 0) {
                return;
            }
            EntityLivingBase entity = event.getEntityLiving();
            if (entity != null && entity instanceof EntityPlayer && (player = (EntityPlayer)entity).func_110143_aJ() <= 0.0f) {
                String name = player.func_70005_c_();
                this.doAnnounce(name);
            }
        }
    }, new Predicate[0]);

    public AutoEz() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        this.targetedPlayers = new ArrayList<Target>();
    }

    @Override
    public void onDisable() {
        this.targetedPlayers = null;
    }

    @Override
    public void onUpdate() {
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ArrayList<Target>();
        }
        --this.waited;
        if (this.waited > 0) {
            return;
        }
        ArrayList<String> nameList = new ArrayList<String>();
        for (EntityPlayer player : AutoEz.mc.field_71441_e.field_73010_i) {
            String name = player.func_70005_c_();
            nameList.add(name);
            if (!this.inList(name) || !(player.func_110143_aJ() <= 0.0f)) continue;
            this.doAnnounce(name);
        }
        this.targetedPlayers.removeIf(target -> {
            if (!nameList.contains(target.name) || target.name.equals("")) {
                return true;
            }
            target.updateTime();
            return target.time <= 0;
        });
    }

    private void doAnnounce(String name) {
        if (name.equals(AutoEz.mc.field_71439_g.func_70005_c_())) {
            return;
        }
        boolean in = false;
        for (Target target : this.targetedPlayers) {
            if (!target.name.equals(name)) continue;
            this.targetedPlayers.remove(target);
            in = true;
            break;
        }
        if (!in) {
            return;
        }
        String message = this.msg.getText();
        String messageSanitized = message.replace("{name}", name);
        if (messageSanitized.length() > 255) {
            messageSanitized = messageSanitized.substring(0, 255);
        }
        MessageBus.sendServerMessage(messageSanitized);
        this.waited = (Integer)this.delay.getValue();
    }

    public void addTargetedPlayer(String name) {
        if (!Objects.equals(name, AutoEz.mc.field_71439_g.func_70005_c_())) {
            if (this.targetedPlayers == null) {
                this.targetedPlayers = new ArrayList<Target>();
            }
            boolean added = false;
            for (Target target : this.targetedPlayers) {
                if (!target.name.equals(name)) continue;
                target.update();
                added = true;
                break;
            }
            if (!added) {
                this.targetedPlayers.add(new Target(name));
            }
        }
    }

    private boolean inList(String name) {
        for (Target target : this.targetedPlayers) {
            if (!target.name.equals(name)) continue;
            return true;
        }
        return false;
    }

    static class Target {
        String name;
        int time;

        public Target(String name) {
            this.name = name;
            this.time = 20;
        }

        void updateTime() {
            --this.time;
        }

        void update() {
            this.time = 20;
        }
    }
}
