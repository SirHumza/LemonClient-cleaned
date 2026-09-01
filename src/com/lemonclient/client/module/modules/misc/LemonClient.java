/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketChatMessage
 */
package com.lemonclient.client.module.modules.misc;

import com.lemonclient.api.event.events.PacketEvent;
import com.lemonclient.api.setting.values.BooleanSetting;
import com.lemonclient.client.module.Category;
import com.lemonclient.client.module.Module;
import java.util.function.Predicate;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;

@Module.Declaration(name="LemonClient", category=Category.Misc)
public class LemonClient
extends Module {
    BooleanSetting commands = this.registerBoolean("Commands", false);
    String SUFFIX = " \u23d0 \u2113\u0454\u043c\u2134\u0e20";
    @EventHandler
    public Listener<PacketEvent.Send> listener = new Listener<PacketEvent.Send>(event -> {
        if (event.getPacket() instanceof CPacketChatMessage) {
            String s = ((CPacketChatMessage)event.getPacket()).func_149439_c();
            if (s.startsWith("/") && !((Boolean)this.commands.getValue()).booleanValue()) {
                return;
            }
            if (s.contains(this.SUFFIX) || s.isEmpty()) {
                return;
            }
            if ((s = s + this.SUFFIX).length() >= 256) {
                s = s.substring(0, 256);
            }
            ((CPacketChatMessage)event.getPacket()).field_149440_a = s;
        }
    }, new Predicate[0]);
}
