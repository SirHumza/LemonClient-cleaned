/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketExplosion
 *  net.minecraftforge.client.event.InputUpdateEvent
 *  org.lwjgl.input.Keyboard
 */
package com.lemonclient.client.module.modules.movement;

import com.lemonclient.api.event.events.EntityCollisionEvent;
import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.event.events.WaterPushEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.client.event.InputUpdateEvent;
import org.lwjgl.input.Keyboard;

@Module.Declaration(name="PlayerTweaks", category=Category.Movement)
public class PlayerTweaks
extends Module {
    public BooleanSetting guiMove = this.registerBoolean("Gui Move", false);
    BooleanSetting noPush = this.registerBoolean("No Push", false);
    BooleanSetting noFall = this.registerBoolean("No Fall", false);
    public BooleanSetting noSlow = this.registerBoolean("No Slow", false);
    BooleanSetting antiKnockBack = this.registerBoolean("Velocity", false);
    @EventHandler
    private final Listener<InputUpdateEvent> eventListener = new Listener<InputUpdateEvent>(event -> {
        if (((Boolean)this.noSlow.getValue()).booleanValue() && PlayerTweaks.mc.field_71439_g.func_184587_cr() && !PlayerTweaks.mc.field_71439_g.func_184218_aH()) {
            event.getMovementInput().field_78902_a *= 5.0f;
            event.getMovementInput().field_192832_b *= 5.0f;
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<EntityCollisionEvent> entityCollisionEventListener = new Listener<EntityCollisionEvent>(event -> {
        if (((Boolean)this.noPush.getValue()).booleanValue()) {
            event.cancel();
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener = new Listener<PacketEvent.Receive>(event -> {
        if (((Boolean)this.antiKnockBack.getValue()).booleanValue()) {
            if (event.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity)event.getPacket()).func_149412_c() == PlayerTweaks.mc.field_71439_g.func_145782_y()) {
                event.cancel();
            }
            if (event.getPacket() instanceof SPacketExplosion) {
                event.cancel();
            }
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<PacketEvent.Send> sendListener = new Listener<PacketEvent.Send>(event -> {
        if (((Boolean)this.noFall.getValue()).booleanValue() && event.getPacket() instanceof CPacketPlayer && (double)PlayerTweaks.mc.field_71439_g.field_70143_R >= 3.0) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            packet.field_149474_g = true;
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<WaterPushEvent> waterPushEventListener = new Listener<WaterPushEvent>(event -> {
        if (((Boolean)this.noPush.getValue()).booleanValue()) {
            event.cancel();
        }
    }, new Predicate[0]);

    @Override
    public void onUpdate() {
        if (((Boolean)this.guiMove.getValue()).booleanValue() && PlayerTweaks.mc.field_71462_r != null && !(PlayerTweaks.mc.field_71462_r instanceof GuiChat)) {
            if (Keyboard.isKeyDown((int)200)) {
                PlayerTweaks.mc.field_71439_g.field_70125_A -= 5.0f;
            }
            if (Keyboard.isKeyDown((int)208)) {
                PlayerTweaks.mc.field_71439_g.field_70125_A += 5.0f;
            }
            if (Keyboard.isKeyDown((int)205)) {
                PlayerTweaks.mc.field_71439_g.field_70177_z += 5.0f;
            }
            if (Keyboard.isKeyDown((int)203)) {
                PlayerTweaks.mc.field_71439_g.field_70177_z -= 5.0f;
            }
            if (PlayerTweaks.mc.field_71439_g.field_70125_A > 90.0f) {
                PlayerTweaks.mc.field_71439_g.field_70125_A = 90.0f;
            }
            if (PlayerTweaks.mc.field_71439_g.field_70125_A < -90.0f) {
                PlayerTweaks.mc.field_71439_g.field_70125_A = -90.0f;
            }
        }
    }
}
