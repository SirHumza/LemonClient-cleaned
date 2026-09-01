/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 */
package org.spongepowered.asm.util;

import com.google.common.base.Strings;
import org.spongepowered.asm.util.PrettyPrinter;

class PrettyPrinter.HorizontalRule
implements PrettyPrinter.ISpecialEntry {
    private final char[] hrChars;

    public PrettyPrinter.HorizontalRule(char ... hrChars) {
        this.hrChars = hrChars;
    }

    public String toString() {
        return Strings.repeat((String)new String(this.hrChars), (int)(PrettyPrinter.this.width + 2));
    }
}
