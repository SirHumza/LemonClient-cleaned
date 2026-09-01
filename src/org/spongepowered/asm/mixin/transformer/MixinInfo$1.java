/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 */
package org.spongepowered.asm.mixin.transformer;

import com.google.common.base.Function;
import org.spongepowered.asm.lib.Type;

class MixinInfo.1
implements Function<Type, String> {
    MixinInfo.1() {
    }

    public String apply(Type input) {
        return input.getClassName();
    }
}
