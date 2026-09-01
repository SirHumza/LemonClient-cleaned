/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.render;

import com.lemonclient.api.util.render.Easing;

public static class Easing.BackOut
extends Easing.Back {
    public Easing.BackOut() {
    }

    public Easing.BackOut(float overshoot) {
        super(overshoot);
    }

    @Override
    public float ease(float time, float startTime, float change, float endTime) {
        float s = this.getOvershoot();
        time = time / endTime - 1.0f;
        return change * (time * time * ((s + 1.0f) * time + s) + 1.0f) + startTime;
    }
}
