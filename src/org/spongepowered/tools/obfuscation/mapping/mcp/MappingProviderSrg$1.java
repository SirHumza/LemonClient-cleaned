/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.common.collect.BiMap
 *  com.google.common.io.LineProcessor
 */
package org.spongepowered.tools.obfuscation.mapping.mcp;

import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import com.google.common.io.LineProcessor;
import java.io.File;
import java.io.IOException;
import org.spongepowered.asm.mixin.throwables.MixinException;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.obfuscation.mapping.mcp.MappingFieldSrg;

class MappingProviderSrg.1
implements LineProcessor<String> {
    final /* synthetic */ BiMap val$packageMap;
    final /* synthetic */ BiMap val$classMap;
    final /* synthetic */ BiMap val$fieldMap;
    final /* synthetic */ BiMap val$methodMap;
    final /* synthetic */ File val$input;

    MappingProviderSrg.1(BiMap biMap, BiMap biMap2, BiMap biMap3, BiMap biMap4, File file) {
        this.val$packageMap = biMap;
        this.val$classMap = biMap2;
        this.val$fieldMap = biMap3;
        this.val$methodMap = biMap4;
        this.val$input = file;
    }

    public String getResult() {
        return null;
    }

    public boolean processLine(String line) throws IOException {
        if (Strings.isNullOrEmpty((String)line) || line.startsWith("#")) {
            return true;
        }
        String type = line.substring(0, 2);
        String[] args = line.substring(4).split(" ");
        if (type.equals("PK")) {
            this.val$packageMap.forcePut((Object)args[0], (Object)args[1]);
        } else if (type.equals("CL")) {
            this.val$classMap.forcePut((Object)args[0], (Object)args[1]);
        } else if (type.equals("FD")) {
            this.val$fieldMap.forcePut((Object)new MappingFieldSrg(args[0]).copy(), (Object)new MappingFieldSrg(args[1]).copy());
        } else if (type.equals("MD")) {
            this.val$methodMap.forcePut((Object)new MappingMethod(args[0], args[1]), (Object)new MappingMethod(args[2], args[3]));
        } else {
            throw new MixinException("Invalid SRG file: " + this.val$input);
        }
        return true;
    }
}
