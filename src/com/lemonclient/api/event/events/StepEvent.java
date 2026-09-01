/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.AxisAlignedBB
 */
package com.lemonclient.api.event.events;

import com.lemonclient.api.event.LemonClientEvent;
import net.minecraft.util.math.AxisAlignedBB;

public class StepEvent
extends LemonClientEvent {
    AxisAlignedBB BB;

    public StepEvent(AxisAlignedBB bb) {
        this.BB = bb;
    }

    public AxisAlignedBB getBB() {
        return this.BB;
    }
}
