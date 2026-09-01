/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.render;

import com.lemonclient.api.util.render.Easing;

public static class Easing.BackInOut
extends Easing.Back {
    public Easing.BackInOut() {
    }

    public Easing.BackInOut(float overshoot) {
        super(overshoot);
    }

    @Override
    public float ease(float time, float startTime, float change, float endTime) {
        float f;
        float s = this.getOvershoot();
        time /= endTime / 2.0f;
        if (f < 1.0f) {
            s = (float)((double)s * 1.525);
            return change / 2.0f * (time * time * ((s + 1.0f) * time - s)) + startTime;
        }
        s = (float)((double)s * 1.525);
        return change / 2.0f * ((time -= 2.0f) * time * ((s + 1.0f) * time + s) + 2.0f) + startTime;
    }
}
