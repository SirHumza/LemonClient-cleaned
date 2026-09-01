/*
 * Decompiled with CFR 0.152.
 */
package org.spongepowered.tools.obfuscation;

public static class ReferenceManager.ReferenceConflictException
extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String oldReference;
    private final String newReference;

    public ReferenceManager.ReferenceConflictException(String oldReference, String newReference) {
        this.oldReference = oldReference;
        this.newReference = newReference;
    }

    public String getOld() {
        return this.oldReference;
    }

    public String getNew() {
        return this.newReference;
    }
}
