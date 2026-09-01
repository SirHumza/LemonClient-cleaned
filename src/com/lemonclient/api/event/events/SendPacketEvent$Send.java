/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 */
package com.lemonclient.api.event.events;

import com.lemonclient.api.event.events.SendPacketEvent;
import net.minecraft.network.Packet;

public static class SendPacketEvent.Send
extends SendPacketEvent {
    public SendPacketEvent.Send(Packet packet) {
        super(packet);
    }
}
