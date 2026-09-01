/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.lemonclient.api.event.events;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ClickBlockEvent
extends Event {
    private final BlockPos pos;
    private final EnumFacing side;
    private boolean damage = false;

    public ClickBlockEvent(BlockPos pos, EnumFacing side) {
        this.pos = pos;
        this.side = side;
    }

    public ClickBlockEvent(BlockPos pos, EnumFacing side, boolean damage) {
        this.pos = pos;
        this.side = side;
        this.damage = damage;
    }

    public boolean isDamage() {
        return this.damage;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public EnumFacing getSide() {
        return this.side;
    }
}
