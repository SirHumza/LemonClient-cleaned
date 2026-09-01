/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public static @interface Command.Declaration {
    public String name();

    public String syntax();

    public String[] alias();
}
