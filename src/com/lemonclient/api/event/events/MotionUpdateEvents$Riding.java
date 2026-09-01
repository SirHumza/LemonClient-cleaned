/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 */
package com.lemonclient.api.event.events;

import com.lemonclient.api.event.events.MotionUpdateEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public static class MotionUpdateEvents.Riding
extends MotionUpdateEvents {
    private float moveStrafing;
    private float moveForward;
    private boolean jump;
    private boolean sneak;

    public MotionUpdateEvents.Riding(double x, double y, double z, float rotationYaw, float rotationPitch, boolean onGround, float moveStrafing, float moveForward, boolean jump, boolean sneak) {
        super(x, y, z, rotationYaw, rotationPitch, onGround);
        this.moveStrafing = moveStrafing;
        this.moveForward = moveForward;
        this.jump = jump;
        this.sneak = sneak;
    }

    public MotionUpdateEvents.Riding(MotionUpdateEvents.Riding event) {
        this(event.getX(), event.getY(), event.getZ(), event.getYaw(), event.getPitch(), event.isOnGround(), event.moveStrafing, event.moveForward, event.jump, event.sneak);
    }

    public Entity getEntity() {
        return Minecraft.func_71410_x().field_71439_g.func_184208_bv();
    }

    public float getMoveStrafing() {
        return this.moveStrafing;
    }

    public void setMoveStrafing(float moveStrafing) {
        this.modified = true;
        this.moveStrafing = moveStrafing;
    }

    public float getMoveForward() {
        return this.moveForward;
    }

    public void setMoveForward(float moveForward) {
        this.modified = true;
        this.moveForward = moveForward;
    }

    public boolean getJump() {
        return this.jump;
    }

    public void setJump(boolean jump) {
        this.modified = true;
        this.jump = jump;
    }

    public boolean getSneak() {
        return this.sneak;
    }

    public void setSneak(boolean sneak) {
        this.modified = true;
        this.sneak = sneak;
    }
}
