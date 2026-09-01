/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.render;

import com.lemonclient.api.util.render.Easing;

public static class Easing.ElasticInOut
extends Easing.Elastic {
    public Easing.ElasticInOut(float amplitude, float period) {
        super(amplitude, period);
    }

    public Easing.ElasticInOut() {
    }

    @Override
    public float ease(float time, float startTime, float change, float endTime) {
        float s;
        float a = this.getAmplitude();
        float p = this.getPeriod();
        if (time == 0.0f) {
            return startTime;
        }
        if ((time /= endTime / 2.0f) == 2.0f) {
            return startTime + change;
        }
        if (p == 0.0f) {
            p = endTime * 0.45000002f;
        }
        if (a < Math.abs(change)) {
            a = change;
            s = p / 4.0f;
        } else {
            s = p / ((float)Math.PI * 2) * (float)Math.asin(change / a);
        }
        if (time < 1.0f) {
            return -0.5f * (a * (float)Math.pow(2.0, 10.0f * (time -= 1.0f)) * (float)Math.sin((double)(time * endTime - s) * (Math.PI * 2) / (double)p)) + startTime;
        }
        return a * (float)Math.pow(2.0, -10.0f * (time -= 1.0f)) * (float)Math.sin((double)(time * endTime - s) * (Math.PI * 2) / (double)p) * 0.5f + change + startTime;
    }
}
