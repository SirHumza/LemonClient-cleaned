/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.event.world.WorldEvent$Unload
 */
package com.lemonclient.client.module.modules.render;

import com.lemonclient.api.event.events.PlayerJoinEvent;
import com.lemonclient.api.event.events.PlayerLeaveEvent;
import com.lemonclient.api.event.events.RenderEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.api.setting.values.ColorSetting;
import com.lemonclient.api.setting.values.IntegerSetting;
import com.lemonclient.api.setting.values.ModeSetting;
import com.lemonclient.api.util.chat.Notification;
import com.lemonclient.api.util.misc.MessageBus;
import com.lemonclient.api.util.misc.Timer;
import com.lemonclient.api.util.render.GSColor;
import com.lemonclient.api.util.render.RenderUtil;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.WorldEvent;

@Module.Declaration(name="LogoutSpots", category=Category.Render)
public class LogoutSpots
extends Module {
    IntegerSetting range = this.registerInteger("Range", 100, 10, 260);
    BooleanSetting disconnectMsg = this.registerBoolean("Disconnect Msgs", true);
    BooleanSetting reconnectMsg = this.registerBoolean("Reconnect Msgs", true);
    BooleanSetting nameTag = this.registerBoolean("NameTag", true);
    IntegerSetting lineWidth = this.registerInteger("Width", 1, 1, 10);
    ModeSetting renderMode = this.registerMode("Render", Arrays.asList("Both", "Outline", "Fill", "None"), "Both");
    ColorSetting color = this.registerColor("Color", new GSColor(255, 0, 0, 255));
    Map<Entity, String> loggedPlayers = new ConcurrentHashMap<Entity, String>();
    Set<EntityPlayer> worldPlayers = ConcurrentHashMap.newKeySet();
    Timer timer = new Timer();
    Timer timer2 = new Timer();
    @EventHandler
    private final Listener<PlayerJoinEvent> playerJoinEventListener = new Listener<PlayerJoinEvent>(event -> {
        if (LogoutSpots.mc.field_71441_e != null) {
            this.loggedPlayers.keySet().removeIf(entity -> {
                if (entity.func_70005_c_().equalsIgnoreCase(event.getName())) {
                    if (((Boolean)this.reconnectMsg.getValue()).booleanValue() && this.timer2.getTimePassed() / 50L >= 5L) {
                        MessageBus.sendClientPrefixMessage(event.getName() + " reconnected.", Notification.Type.INFO);
                        this.timer2.reset();
                    }
                    return true;
                }
                return false;
            });
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PlayerLeaveEvent> playerLeaveEventListener = new Listener<PlayerLeaveEvent>(event -> {
        if (LogoutSpots.mc.field_71441_e != null) {
            this.worldPlayers.removeIf(entity -> {
                if (entity.func_70005_c_().equalsIgnoreCase(event.getName())) {
                    String date = new SimpleDateFormat("k:mm").format(new Date());
                    this.loggedPlayers.put((Entity)entity, date);
                    if (((Boolean)this.disconnectMsg.getValue()).booleanValue() && this.timer.getTimePassed() / 50L >= 5L) {
                        String location = "(" + (int)entity.field_70165_t + "," + (int)entity.field_70163_u + "," + (int)entity.field_70161_v + ")";
                        MessageBus.sendClientPrefixMessage(event.getName() + " disconnected at " + location + ".", Notification.Type.INFO);
                        this.timer.reset();
                    }
                    return true;
                }
                return false;
            });
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<WorldEvent.Unload> unloadListener = new Listener<WorldEvent.Unload>(event -> {
        this.worldPlayers.clear();
        if (LogoutSpots.mc.field_71439_g == null || LogoutSpots.mc.field_71441_e == null) {
            this.loggedPlayers.clear();
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<WorldEvent.Load> loadListener = new Listener<WorldEvent.Load>(event -> {
        this.worldPlayers.clear();
        if (LogoutSpots.mc.field_71439_g == null || LogoutSpots.mc.field_71441_e == null) {
            this.loggedPlayers.clear();
        }
    }, new Predicate[0]);

    @Override
    public void onUpdate() {
        LogoutSpots.mc.field_71441_e.field_73010_i.stream().filter(entityPlayer -> entityPlayer != LogoutSpots.mc.field_71439_g).filter(entityPlayer -> entityPlayer.func_70032_d((Entity)LogoutSpots.mc.field_71439_g) <= (float)((Integer)this.range.getValue()).intValue()).forEach(entityPlayer -> this.worldPlayers.add((EntityPlayer)entityPlayer));
    }

    @Override
    public void onWorldRender(RenderEvent event) {
        if (LogoutSpots.mc.field_71439_g != null && LogoutSpots.mc.field_71441_e != null) {
            this.loggedPlayers.forEach(this::startFunction);
        }
    }

    @Override
    public void onEnable() {
        this.loggedPlayers.clear();
        this.worldPlayers = ConcurrentHashMap.newKeySet();
    }

    @Override
    public void onDisable() {
        this.worldPlayers.clear();
    }

    private void startFunction(Entity entity, String string) {
        if (entity.func_70032_d((Entity)LogoutSpots.mc.field_71439_g) > (float)((Integer)this.range.getValue()).intValue()) {
            return;
        }
        int posX = (int)entity.field_70165_t;
        int posY = (int)entity.field_70163_u;
        int posZ = (int)entity.field_70161_v;
        String[] nameTagMessage = new String[]{entity.func_70005_c_() + " (" + string + ")", "(" + posX + "," + posY + "," + posZ + ")"};
        GlStateManager.func_179094_E();
        if (((Boolean)this.nameTag.getValue()).booleanValue()) {
            RenderUtil.drawNametag(entity, nameTagMessage, this.color.getValue(), 0);
        }
        switch ((String)this.renderMode.getValue()) {
            case "Both": {
                RenderUtil.drawBoundingBox(entity.func_184177_bl(), (double)((Integer)this.lineWidth.getValue()).intValue(), this.color.getValue());
                RenderUtil.drawBox(entity.func_184177_bl(), true, -0.4, new GSColor(this.color.getValue(), 50), 63);
                break;
            }
            case "Outline": {
                RenderUtil.drawBoundingBox(entity.func_184177_bl(), (double)((Integer)this.lineWidth.getValue()).intValue(), this.color.getValue());
                break;
            }
            case "Fill": {
                RenderUtil.drawBox(entity.func_184177_bl(), true, -0.4, new GSColor(this.color.getValue(), 50), 63);
            }
        }
        GlStateManager.func_179121_F();
    }
}
