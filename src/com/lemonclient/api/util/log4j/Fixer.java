/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.Logger
 *  org.apache.logging.log4j.core.appender.AbstractManager
 *  org.apache.logging.log4j.core.net.JndiManager
 */
package com.lemonclient.api.util.log4j;

import com.lemonclient.api.util.log4j.EmptyJndiContext;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Map;
import javax.naming.Context;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.net.JndiManager;

public class Fixer {
    public static void doRuntimeTest(Logger logger) {
        logger.info("Fix4Log4J loaded.");
        logger.info("If you see stacktrace below, CLOSE EVERYTHING IMMEDIATELY!");
        String someRandomUri = Fixer.randomUri();
        logger.info("Exploit Test: ${jndi:ldap://" + someRandomUri + "}");
    }

    private static String randomUri() {
        char[] buf = new char[81];
        SecureRandom rng = new SecureRandom();
        for (int i = 0; i < buf.length; ++i) {
            buf[i] = (char)(97 + rng.nextInt(26));
        }
        buf[40] = 58;
        return new String(buf);
    }

    public static void disableJndiManager() {
    }

    private static void disableJndiManager0() {
        JndiManager.getDefaultManager();
        Class<AbstractManager> mapHolder = AbstractManager.class;
        Arrays.stream(mapHolder.getDeclaredFields()).filter(f -> Modifier.isStatic(f.getModifiers())).filter(f -> Map.class.isAssignableFrom(f.getType())).map(f -> {
            try {
                f.setAccessible(true);
                return (Map)f.get(null);
            }
            catch (IllegalAccessException e) {
                throw new ExceptionInInitializerError(e);
            }
        }).forEach(map -> {
            if (map == null) {
                return;
            }
            map.forEach((k, v) -> {
                if (v instanceof JndiManager) {
                    try {
                        Fixer.fixJndiManager((JndiManager)v);
                    }
                    catch (ReflectiveOperationException e) {
                        throw new ExceptionInInitializerError(e);
                    }
                }
            });
        });
    }

    private static void fixJndiManager(JndiManager jndiManager) throws ReflectiveOperationException {
        Arrays.stream(jndiManager.getClass().getDeclaredFields()).filter(f -> Context.class.isAssignableFrom(f.getType())).forEach(f -> {
            try {
                f.setAccessible(true);
                Fixer.removeFinalModifier(f);
                f.set(jndiManager, EmptyJndiContext.INSTANCE);
            }
            catch (IllegalAccessException e) {
                throw new ExceptionInInitializerError(e);
            }
        });
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void removeFinalModifier(Field field) throws IllegalAccessException {
        block7: {
            try {
                if (!Modifier.isFinal(field.getModifiers())) break block7;
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                boolean doForceAccess = !modifiersField.isAccessible();
                boolean bl = doForceAccess;
                if (doForceAccess) {
                    modifiersField.setAccessible(true);
                }
                try {
                    modifiersField.setInt(field, field.getModifiers() & 0xFFFFFFEF);
                }
                finally {
                    if (doForceAccess) {
                        modifiersField.setAccessible(false);
                    }
                }
            }
            catch (NoSuchFieldException noSuchFieldException) {
                // empty catch block
            }
        }
    }

    private Fixer() {
    }
}
