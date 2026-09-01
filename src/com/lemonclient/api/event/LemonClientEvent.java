/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package com.lemonclient.api.event;

import me.zero.alpine.event.type.Cancellable;
import net.minecraft.client.Minecraft;

public class LemonClientEvent
extends Cancellable {
    private final Era era = Era.PRE;
    private final float partialTicks = Minecraft.func_71410_x().func_184121_ak();

    public Era getEra() {
        return this.era;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public static enum Era {
        PRE,
        PERI,
        POST;

    }
}
