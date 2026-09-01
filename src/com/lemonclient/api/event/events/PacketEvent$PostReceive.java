/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 */
package com.lemonclient.api.event.events;

import com.lemonclient.api.event.events.PacketEvent;
import net.minecraft.network.Packet;

public static class PacketEvent.PostReceive
extends PacketEvent {
    public PacketEvent.PostReceive(Packet packet) {
        super(packet);
    }
}
