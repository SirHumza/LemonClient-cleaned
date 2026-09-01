/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 */
package org.spongepowered.asm.mixin.injection;

import com.google.common.base.Joiner;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

static abstract class InjectionPoint.CompositeInjectionPoint
extends InjectionPoint {
    protected final InjectionPoint[] components;

    protected InjectionPoint.CompositeInjectionPoint(InjectionPoint ... components) {
        if (components == null || components.length < 2) {
            throw new IllegalArgumentException("Must supply two or more component injection points for composite point!");
        }
        this.components = components;
    }

    @Override
    public String toString() {
        return "CompositeInjectionPoint(" + this.getClass().getSimpleName() + ")[" + Joiner.on((char)',').join((Object[])this.components) + "]";
    }
}
