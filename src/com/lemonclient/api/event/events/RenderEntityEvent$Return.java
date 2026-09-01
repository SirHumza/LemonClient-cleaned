/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package com.lemonclient.api.event.events;

import com.lemonclient.api.event.events.RenderEntityEvent;
import net.minecraft.entity.Entity;

public static class RenderEntityEvent.Return
extends RenderEntityEvent {
    public RenderEntityEvent.Return(Entity entity, RenderEntityEvent.Type type) {
        super(entity, type);
    }
}
