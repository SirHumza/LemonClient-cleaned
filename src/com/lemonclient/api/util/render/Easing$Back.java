/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.render;

import com.lemonclient.api.util.render.Easing;

public static abstract class Easing.Back
implements Easing {
    public static final float DEFAULT_OVERSHOOT = 1.70158f;
    private float overshoot;

    public Easing.Back() {
        this(1.70158f);
    }

    public Easing.Back(float overshoot) {
        this.overshoot = overshoot;
    }

    public float getOvershoot() {
        return this.overshoot;
    }

    public void setOvershoot(float overshoot) {
        this.overshoot = overshoot;
    }
}
