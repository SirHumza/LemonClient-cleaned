/*
 * Decompiled with CFR 0.152.
 */
package org.spongepowered.asm.mixin.injection;

public static enum InjectionPoint.Selector {
    FIRST,
    LAST,
    ONE;

    public static final InjectionPoint.Selector DEFAULT;

    static {
        DEFAULT = FIRST;
    }
}
