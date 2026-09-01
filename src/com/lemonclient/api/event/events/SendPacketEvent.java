/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.lemonclient.api.event.events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SendPacketEvent
extends Event {
    private final Packet packet;

    public SendPacketEvent(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public static class Send
    extends SendPacketEvent {
        public Send(Packet packet) {
            super(packet);
        }
    }

    public static class Receive
    extends SendPacketEvent {
        public Receive(Packet packet) {
            super(packet);
        }
    }
}
