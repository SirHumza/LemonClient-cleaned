/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Timer
 */
package com.lemonclient.api.util.world;

import com.lemonclient.api.util.misc.Mapping;
import com.lemonclient.mixin.mixins.accessor.AccessorMinecraft;
import com.lemonclient.mixin.mixins.accessor.AccessorTimer;
import java.lang.reflect.Field;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;

public class TimerUtils {
    private static int counter;
    private static final HashMap<Integer, Float> multipliers;

    public static void setTickLength(float speed) {
        Timer timer = ((AccessorMinecraft)Minecraft.func_71410_x()).getTimer();
        ((AccessorTimer)timer).setTickLength(speed);
    }

    public static float getTickLength() {
        Timer timer = ((AccessorMinecraft)Minecraft.func_71410_x()).getTimer();
        return ((AccessorTimer)timer).getTickLength();
    }

    public static void setSpeed(float speed) {
        Timer timer = ((AccessorMinecraft)Minecraft.func_71410_x()).getTimer();
        ((AccessorTimer)timer).setTickLength(50.0f / speed);
    }

    public static float getTimer() {
        Timer timer = ((AccessorMinecraft)Minecraft.func_71410_x()).getTimer();
        return 50.0f / ((AccessorTimer)timer).getTickLength();
    }

    public static void setTimerSpeed(float speed) {
        try {
            Field timer = Minecraft.class.getDeclaredField(Mapping.timer);
            timer.setAccessible(true);
            Field tickLength = Timer.class.getDeclaredField(Mapping.tickLength);
            tickLength.setAccessible(true);
            tickLength.setFloat(timer.get(Minecraft.func_71410_x()), 50.0f / speed);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static float getMultiplier() {
        float multiplier = 1.0f;
        for (float f : multipliers.values()) {
            multiplier *= f;
        }
        return multiplier;
    }

    public static int push(float multiplier) {
        multipliers.put(++counter, Float.valueOf(multiplier));
        TimerUtils.setSpeed(TimerUtils.getMultiplier());
        return counter;
    }

    public static void pop(int counter) {
        multipliers.remove(counter);
        TimerUtils.setSpeed(TimerUtils.getMultiplier());
    }

    static {
        multipliers = new HashMap();
    }
}
