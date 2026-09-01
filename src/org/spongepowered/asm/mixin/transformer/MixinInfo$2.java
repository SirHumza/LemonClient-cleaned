/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 */
package org.spongepowered.asm.mixin.transformer;

import com.google.common.base.Function;

class MixinInfo.2
implements Function<String, String> {
    MixinInfo.2() {
    }

    public String apply(String input) {
        return MixinInfo.this.getParent().remapClassName(MixinInfo.this.getClassRef(), input);
    }
}
