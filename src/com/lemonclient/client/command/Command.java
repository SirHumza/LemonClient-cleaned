/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package com.lemonclient.client.command;

import com.lemonclient.client.command.CommandManager;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.minecraft.client.Minecraft;

public abstract class Command {
    protected static final Minecraft mc = Minecraft.func_71410_x();
    private final String name = this.getDeclaration().name();
    private final String[] alias = this.getDeclaration().alias();
    private final String syntax = this.getDeclaration().syntax();

    private Declaration getDeclaration() {
        return this.getClass().getAnnotation(Declaration.class);
    }

    public String getName() {
        return this.name;
    }

    public String getSyntax() {
        return CommandManager.getCommandPrefix() + this.syntax;
    }

    public String[] getAlias() {
        return this.alias;
    }

    public abstract void onCommand(String var1, String[] var2, boolean var3);

    @Retention(value=RetentionPolicy.RUNTIME)
    @Target(value={ElementType.TYPE})
    public static @interface Declaration {
        public String name();

        public String syntax();

        public String[] alias();
    }
}
