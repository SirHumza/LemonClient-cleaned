/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.common.io.LineProcessor
 */
package org.spongepowered.asm.mixin.refmap;

import com.google.common.base.Strings;
import com.google.common.io.LineProcessor;
import java.io.IOException;
import java.util.Map;

static final class RemappingReferenceMapper.1
implements LineProcessor<Object> {
    final /* synthetic */ Map val$map;

    RemappingReferenceMapper.1(Map map) {
        this.val$map = map;
    }

    public Object getResult() {
        return null;
    }

    public boolean processLine(String line) throws IOException {
        if (Strings.isNullOrEmpty((String)line) || line.startsWith("#")) {
            return true;
        }
        int fromPos = 0;
        int toPos = 0;
        if ((line.startsWith("MD: ") ? 2 : (toPos = line.startsWith("FD: ") ? 1 : 0)) > 0) {
            String[] entries = line.substring(4).split(" ", 4);
            this.val$map.put(entries[fromPos].substring(entries[fromPos].lastIndexOf(47) + 1), entries[toPos].substring(entries[toPos].lastIndexOf(47) + 1));
        }
        return true;
    }
}
