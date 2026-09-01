/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.render.animation;

import com.lemonclient.api.util.render.animation.AnimationMode;

static class TimeAnimation.1 {
    static final /* synthetic */ int[] $SwitchMap$com$lemonclient$api$util$render$animation$AnimationMode;

    static {
        $SwitchMap$com$lemonclient$api$util$render$animation$AnimationMode = new int[AnimationMode.values().length];
        try {
            TimeAnimation.1.$SwitchMap$com$lemonclient$api$util$render$animation$AnimationMode[AnimationMode.LINEAR.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            TimeAnimation.1.$SwitchMap$com$lemonclient$api$util$render$animation$AnimationMode[AnimationMode.EXPONENTIAL.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
