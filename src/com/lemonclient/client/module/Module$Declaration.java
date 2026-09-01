/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.module;

import com.lemonclient.client.module.Category;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public static @interface Module.Declaration {
    public String name();

    public Category category();

    public int priority() default 0;

    public int bind() default 0;

    public boolean enabled() default false;

    public boolean drawn() default true;

    public boolean toggleMsg() default false;
}
