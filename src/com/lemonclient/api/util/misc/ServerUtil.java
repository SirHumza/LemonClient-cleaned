/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package com.lemonclient.api.util.misc;

import com.lemonclient.api.util.misc.Timing;
import java.util.Arrays;
import java.util.Objects;
import net.minecraft.client.Minecraft;

public class ServerUtil {
    static Minecraft mc = Minecraft.func_71410_x();
    private static String serverBrand;
    private static Timing timer;
    private static float[] tpsCounts;
    private static long lastUpdate;
    private static String format;
    private static float TPS;

    public static float getTpsFactor() {
        return 20.0f / TPS;
    }

    public static long serverRespondingTime() {
        return timer.getPassedTimeMs();
    }

    public static String getServerBrand() {
        return serverBrand;
    }

    public boolean isServerNotResponding() {
        return timer.passedMs(500L);
    }

    public ServerUtil() {
        tpsCounts = new float[10];
        format = "%.3f";
        timer = new Timing();
        TPS = 20.0f;
        lastUpdate = -1L;
        serverBrand = "";
    }

    public void update() {
        double d;
        float f;
        long currentTimeMillis = System.currentTimeMillis();
        if (lastUpdate == -1L) {
            lastUpdate = currentTimeMillis;
            return;
        }
        float n = (float)(currentTimeMillis - lastUpdate) / 20.0f;
        if (n == 0.0f) {
            n = 50.0f;
        }
        float n2 = 1000.0f / n;
        if (f > 20.0f) {
            n2 = 20.0f;
        }
        System.arraycopy(tpsCounts, 0, tpsCounts, 1, tpsCounts.length - 1);
        ServerUtil.tpsCounts[0] = n2;
        double n3 = 0.0;
        float[] tpsCounts = ServerUtil.tpsCounts;
        int length = tpsCounts.length;
        for (int i = 0; i < length; ++i) {
            n3 += (double)tpsCounts[i];
        }
        double number = n3 / (double)tpsCounts.length;
        if (d > 20.0) {
            number = 20.0;
        }
        TPS = Float.parseFloat(String.format(format, number));
        lastUpdate = currentTimeMillis;
    }

    public static int getPing() {
        if (ServerUtil.mc.field_71441_e == null || ServerUtil.mc.field_71439_g == null) {
            return 0;
        }
        try {
            return Objects.requireNonNull(mc.func_147114_u()).func_175102_a(mc.func_147114_u().func_175105_e().getId()).func_178853_c();
        }
        catch (Exception ex) {
            return 0;
        }
    }

    public static float getTPS() {
        return TPS;
    }

    public void setServerBrand(String serverBrand) {
    }

    public static void reset() {
        Arrays.fill(tpsCounts, 20.0f);
        TPS = 20.0f;
    }

    public static void onPacketReceived() {
        timer.reset();
    }

    static {
        format = "%.3f";
    }
}
