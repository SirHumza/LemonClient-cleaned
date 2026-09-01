/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.render;

import com.lemonclient.api.util.render.Easing;

public static class Easing.ElasticOut
extends Easing.Elastic {
    public Easing.ElasticOut(float amplitude, float period) {
        super(amplitude, period);
    }

    public Easing.ElasticOut() {
    }

    @Override
    public float ease(float time, float startTime, float change, float endTime) {
        float s;
        float a = this.getAmplitude();
        float p = this.getPeriod();
        if (time == 0.0f) {
            return startTime;
        }
        if ((time /= endTime) == 1.0f) {
            return startTime + change;
        }
        if (p == 0.0f) {
            p = endTime * 0.3f;
        }
        if (a < Math.abs(change)) {
            a = change;
            s = p / 4.0f;
        } else {
            s = p / ((float)Math.PI * 2) * (float)Math.asin(change / a);
        }
        return a * (float)Math.pow(2.0, -10.0f * time) * (float)Math.sin((double)(time * endTime - s) * (Math.PI * 2) / (double)p) + change + startTime;
    }
}
