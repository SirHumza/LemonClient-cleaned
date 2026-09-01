/*
 * Decompiled with CFR 0.152.
 */
package org.spongepowered.asm.util;

import org.spongepowered.asm.util.ClassSignature;

static interface ClassSignature.IToken {
    public static final String WILDCARDS = "+-";

    public String asType();

    public String asBound();

    public ClassSignature.Token asToken();

    public ClassSignature.IToken setArray(boolean var1);

    public ClassSignature.IToken setWildcard(char var1);
}
