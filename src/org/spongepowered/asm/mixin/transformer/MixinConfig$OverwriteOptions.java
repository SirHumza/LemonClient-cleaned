/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 */
package org.spongepowered.asm.mixin.transformer;

import com.google.gson.annotations.SerializedName;

static class MixinConfig.OverwriteOptions {
    @SerializedName(value="conformVisibility")
    boolean conformAccessModifiers;
    @SerializedName(value="requireAnnotations")
    boolean requireOverwriteAnnotations;

    MixinConfig.OverwriteOptions() {
    }
}
