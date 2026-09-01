/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.lemonclient.api.event.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class UpdateEvent
extends Event {
    private final Stage stage;

    public UpdateEvent(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return this.stage;
    }

    public static enum Stage {
        PRE,
        POST;

    }
}
