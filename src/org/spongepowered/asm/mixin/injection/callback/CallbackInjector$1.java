/*
 * Decompiled with CFR 0.152.
 */
package org.spongepowered.asm.mixin.injection.callback;

import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

static class CallbackInjector.1 {
    static final /* synthetic */ int[] $SwitchMap$org$spongepowered$asm$mixin$injection$callback$LocalCapture;

    static {
        $SwitchMap$org$spongepowered$asm$mixin$injection$callback$LocalCapture = new int[LocalCapture.values().length];
        try {
            CallbackInjector.1.$SwitchMap$org$spongepowered$asm$mixin$injection$callback$LocalCapture[LocalCapture.CAPTURE_FAILEXCEPTION.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            CallbackInjector.1.$SwitchMap$org$spongepowered$asm$mixin$injection$callback$LocalCapture[LocalCapture.CAPTURE_FAILSOFT.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
