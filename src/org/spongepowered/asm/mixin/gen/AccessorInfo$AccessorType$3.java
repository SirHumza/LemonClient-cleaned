/*
 * Decompiled with CFR 0.152.
 */
package org.spongepowered.asm.mixin.gen;

import java.util.Set;
import org.spongepowered.asm.mixin.gen.AccessorGenerator;
import org.spongepowered.asm.mixin.gen.AccessorGeneratorMethodProxy;
import org.spongepowered.asm.mixin.gen.AccessorInfo;

static final class AccessorInfo.AccessorType.3
extends AccessorInfo.AccessorType {
    AccessorInfo.AccessorType.3(Set expectedPrefixes) {
    }

    @Override
    AccessorGenerator getGenerator(AccessorInfo info) {
        return new AccessorGeneratorMethodProxy(info);
    }
}
