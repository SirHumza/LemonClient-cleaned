/*
 * Decompiled with CFR 0.152.
 */
package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.mixin.transformer.MixinInfo;

static interface MixinConfig.IListener {
    public void onPrepare(MixinInfo var1);

    public void onInit(MixinInfo var1);
}
