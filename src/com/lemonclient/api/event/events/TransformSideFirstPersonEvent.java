/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumHandSide
 */
package com.lemonclient.api.event.events;

import com.lemonclient.api.event.LemonClientEvent;
import net.minecraft.util.EnumHandSide;

public class TransformSideFirstPersonEvent
extends LemonClientEvent {
    private final EnumHandSide enumHandSide;

    public TransformSideFirstPersonEvent(EnumHandSide enumHandSide) {
        this.enumHandSide = enumHandSide;
    }

    public EnumHandSide getEnumHandSide() {
        return this.enumHandSide;
    }
}
