/*
 * Decompiled with CFR 0.152.
 */
package org.spongepowered.asm.mixin;

import org.spongepowered.asm.mixin.MixinEnvironment;

static final class MixinEnvironment.CompatibilityLevel.3
extends MixinEnvironment.CompatibilityLevel {
    MixinEnvironment.CompatibilityLevel.3(int ver, int classVersion, boolean resolveMethodsInInterfaces) {
    }

    @Override
    boolean isSupported() {
        return false;
    }
}
