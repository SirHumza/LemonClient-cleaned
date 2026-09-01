/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.render;

import com.lemonclient.api.util.render.Easing;

public static class Easing.BackIn
extends Easing.Back {
    public Easing.BackIn() {
    }

    public Easing.BackIn(float overshoot) {
        super(overshoot);
    }

    @Override
    public float ease(float time, float startTime, float change, float endTime) {
        float s = this.getOvershoot();
        return change * (time /= endTime) * time * ((s + 1.0f) * time - s) + startTime;
    }
}
