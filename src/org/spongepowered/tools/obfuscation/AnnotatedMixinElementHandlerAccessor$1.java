/*
 * Decompiled with CFR 0.152.
 */
package org.spongepowered.tools.obfuscation;

import org.spongepowered.asm.mixin.gen.AccessorInfo;

static class AnnotatedMixinElementHandlerAccessor.1 {
    static final /* synthetic */ int[] $SwitchMap$org$spongepowered$asm$mixin$gen$AccessorInfo$AccessorType;

    static {
        $SwitchMap$org$spongepowered$asm$mixin$gen$AccessorInfo$AccessorType = new int[AccessorInfo.AccessorType.values().length];
        try {
            AnnotatedMixinElementHandlerAccessor.1.$SwitchMap$org$spongepowered$asm$mixin$gen$AccessorInfo$AccessorType[AccessorInfo.AccessorType.FIELD_GETTER.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            AnnotatedMixinElementHandlerAccessor.1.$SwitchMap$org$spongepowered$asm$mixin$gen$AccessorInfo$AccessorType[AccessorInfo.AccessorType.FIELD_SETTER.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
