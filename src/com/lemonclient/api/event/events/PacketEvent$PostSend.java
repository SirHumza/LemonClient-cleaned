/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 */
package com.lemonclient.api.event.events;

import com.lemonclient.api.event.events.PacketEvent;
import net.minecraft.network.Packet;

public static class PacketEvent.PostSend
extends PacketEvent {
    public PacketEvent.PostSend(Packet packet) {
        super(packet);
    }
}
