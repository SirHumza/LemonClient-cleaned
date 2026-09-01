/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 */
package org.spongepowered.asm.util;

import com.google.common.base.Function;
import org.spongepowered.asm.lib.tree.AnnotationNode;

static final class Annotations.1
implements Function<AnnotationNode, String> {
    Annotations.1() {
    }

    public String apply(AnnotationNode input) {
        return input.desc;
    }
}
