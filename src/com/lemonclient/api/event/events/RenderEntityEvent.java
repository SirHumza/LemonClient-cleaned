/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package com.lemonclient.api.event.events;

import com.lemonclient.api.event.LemonClientEvent;
import net.minecraft.entity.Entity;

public class RenderEntityEvent
extends LemonClientEvent {
    private final Entity entity;
    private final Type type;

    public RenderEntityEvent(Entity entity, Type type) {
        this.entity = entity;
        this.type = type;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public Type getType() {
        return this.type;
    }

    public static class Return
    extends RenderEntityEvent {
        public Return(Entity entity, Type type) {
            super(entity, type);
        }
    }

    public static class Head
    extends RenderEntityEvent {
        public Head(Entity entity, Type type) {
            super(entity, type);
        }
    }

    public static enum Type {
        TEXTURE,
        COLOR;

    }
}
