/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 */
package com.lemonclient.client.module.modules.misc;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;

@Module.Declaration(name="AntiHunger", category=Category.Misc, priority=999)
public class AntiHunger
extends Module {
    BooleanSetting cancelMove = this.registerBoolean("Cancel Spring", false);
    @EventHandler
    private final Listener<PacketEvent.Send> sendListener = new Listener<PacketEvent.Send>(event -> {
        CPacketEntityAction packet;
        if (AntiHunger.mc.field_71441_e == null || AntiHunger.mc.field_71439_g == null) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayer.Position) {
            this.onPacket((CPacketPlayer)((CPacketPlayer.Position)event.getPacket()));
        }
        if (event.getPacket() instanceof CPacketEntityAction && ((Boolean)this.cancelMove.getValue()).booleanValue() && ((packet = (CPacketEntityAction)event.getPacket()).func_180764_b() == CPacketEntityAction.Action.START_SPRINTING || packet.func_180764_b() == CPacketEntityAction.Action.STOP_SPRINTING)) {
            event.cancel();
        }
    }, new Predicate[0]);

    private void onPacket(CPacketPlayer packet) {
        packet.field_149474_g = (AntiHunger.mc.field_71439_g.field_70143_R <= 0.0f || AntiHunger.mc.field_71442_b.field_78778_j) && AntiHunger.mc.field_71439_g.func_184613_cA();
    }
}
