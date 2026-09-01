/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.misc;

public class EnumUtils {
    public static <T extends Enum<T>> T next(T value) {
        Enum[] enumValues = (Enum[])value.getDeclaringClass().getEnumConstants();
        return (T)enumValues[(value.ordinal() + 1) % enumValues.length];
    }
}
