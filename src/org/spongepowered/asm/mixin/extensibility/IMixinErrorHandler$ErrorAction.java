/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.Level
 */
package org.spongepowered.asm.mixin.extensibility;

import org.apache.logging.log4j.Level;

public static enum IMixinErrorHandler.ErrorAction {
    NONE(Level.INFO),
    WARN(Level.WARN),
    ERROR(Level.FATAL);

    public final Level logLevel;

    private IMixinErrorHandler.ErrorAction(Level logLevel) {
        this.logLevel = logLevel;
    }
}
