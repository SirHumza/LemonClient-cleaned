/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 */
package org.spongepowered.asm.mixin.gen;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.spongepowered.asm.mixin.gen.AccessorGenerator;
import org.spongepowered.asm.mixin.gen.AccessorGeneratorFieldGetter;
import org.spongepowered.asm.mixin.gen.AccessorGeneratorFieldSetter;
import org.spongepowered.asm.mixin.gen.AccessorGeneratorMethodProxy;
import org.spongepowered.asm.mixin.gen.AccessorInfo;

public static enum AccessorInfo.AccessorType {
    FIELD_GETTER((Set)ImmutableSet.of((Object)"get", (Object)"is")){

        @Override
        AccessorGenerator getGenerator(AccessorInfo info) {
            return new AccessorGeneratorFieldGetter(info);
        }
    }
    ,
    FIELD_SETTER((Set)ImmutableSet.of((Object)"set")){

        @Override
        AccessorGenerator getGenerator(AccessorInfo info) {
            return new AccessorGeneratorFieldSetter(info);
        }
    }
    ,
    METHOD_PROXY((Set)ImmutableSet.of((Object)"call", (Object)"invoke")){

        @Override
        AccessorGenerator getGenerator(AccessorInfo info) {
            return new AccessorGeneratorMethodProxy(info);
        }
    };

    private final Set<String> expectedPrefixes;

    private AccessorInfo.AccessorType(Set<String> expectedPrefixes) {
        this.expectedPrefixes = expectedPrefixes;
    }

    public boolean isExpectedPrefix(String prefix) {
        return this.expectedPrefixes.contains(prefix);
    }

    public String getExpectedPrefixes() {
        return this.expectedPrefixes.toString();
    }

    abstract AccessorGenerator getGenerator(AccessorInfo var1);
}
