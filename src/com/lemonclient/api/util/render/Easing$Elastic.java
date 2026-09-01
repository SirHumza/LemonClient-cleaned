/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.render;

import com.lemonclient.api.util.render.Easing;

public static abstract class Easing.Elastic
implements Easing {
    private float amplitude;
    private float period;

    public Easing.Elastic(float amplitude, float period) {
        this.amplitude = amplitude;
        this.period = period;
    }

    public Easing.Elastic() {
        this(-1.0f, 0.0f);
    }

    public float getPeriod() {
        return this.period;
    }

    public void setPeriod(float period) {
        this.period = period;
    }

    public float getAmplitude() {
        return this.amplitude;
    }

    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
    }
}
