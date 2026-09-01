/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.event.events;

import com.lemonclient.api.event.LemonClientEvent;

public class PlayerJoinEvent
extends LemonClientEvent {
    private final String name;

    public PlayerJoinEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
