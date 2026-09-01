/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package com.lemonclient.api.util.misc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MathUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();
    public static Random rnd = new Random();

    public static int getRandom(int min, int max) {
        return rnd.nextInt(max - min + 1) + min;
    }

    public static float[] calcAngle(Vec3d from, Vec3d to) {
        double difX = to.field_72450_a - from.field_72450_a;
        double difY = (to.field_72448_b - from.field_72448_b) * -1.0;
        double difZ = to.field_72449_c - from.field_72449_c;
        double dist = MathHelper.func_76133_a((double)(difX * difX + difZ * difZ));
        return new float[]{(float)MathHelper.func_76138_g((double)(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0)), (float)MathHelper.func_76138_g((double)Math.toDegrees(Math.atan2(difY, dist)))};
    }

    public static double calculateAngle(double x1, double y1, double x2, double y2) {
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        angle += Math.ceil(-angle / 360.0) * 360.0;
        return angle;
    }

    public static int clamp(int num, int min, int max) {
        return num < min ? min : Math.min(num, max);
    }

    public static float clamp(float num, float min, float max) {
        return num < min ? min : Math.min(num, max);
    }

    public static double clamp(double num, double min, double max) {
        return num < min ? min : Math.min(num, max);
    }

    public static long clamp(long num, long min, long max) {
        return num < min ? min : Math.min(num, max);
    }

    public static BigDecimal clamp(BigDecimal num, BigDecimal min, BigDecimal max) {
        return MathUtil.smallerThan(num, min) ? min : (MathUtil.biggerThan(num, max) ? max : num);
    }

    public static Vec3d roundVec(Vec3d vec3d, int places) {
        return new Vec3d(MathUtil.round(vec3d.field_72450_a, places), MathUtil.round(vec3d.field_72448_b, places), MathUtil.round(vec3d.field_72449_c, places));
    }

    public static boolean biggerThan(BigDecimal bigger, BigDecimal than) {
        return bigger.compareTo(than) > 0;
    }

    public static boolean smallerThan(BigDecimal smaller, BigDecimal than) {
        return smaller.compareTo(than) < 0;
    }

    public static double round(double value, int places) {
        return places < 0 ? value : new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }

    public static float round(float value, int places) {
        return places < 0 ? value : new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).floatValue();
    }

    public static float round(float value, int places, float min, float max) {
        return MathHelper.func_76131_a((float)(places < 0 ? value : new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).floatValue()), (float)min, (float)max);
    }

    public static Vec3d interpolateEntity(Entity entity, float time) {
        return new Vec3d(entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)time, entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)time, entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)time);
    }

    public static float rad(float angle) {
        return (float)((double)angle * Math.PI / 180.0);
    }

    public static float[] calcAngleNoY(Vec3d from, Vec3d to) {
        double difX = to.field_72450_a - from.field_72450_a;
        double difZ = to.field_72449_c - from.field_72449_c;
        return new float[]{(float)MathHelper.func_76138_g((double)(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0))};
    }

    public static Double calculateDoubleChange(double oldDouble, double newDouble, int step, int currentStep) {
        return oldDouble + (newDouble - oldDouble) * (double)Math.max(0, Math.min(step, currentStep)) / (double)step;
    }

    public static double square(double input) {
        return input * input;
    }

    public static double[] directionSpeed(double speed) {
        Minecraft mc = Minecraft.func_71410_x();
        float forward = mc.field_71439_g.field_71158_b.field_192832_b;
        float side = mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = mc.field_71439_g.field_70126_B + (mc.field_71439_g.field_70177_z - mc.field_71439_g.field_70126_B) * mc.func_184121_ak();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static float square(float v1) {
        return v1 * v1;
    }

    public static double square(Double v1) {
        return v1 * v1;
    }

    public static double calculateDistanceWithPartialTicks(double n, double n2, float renderPartialTicks) {
        return n2 + (n - n2) * (double)mc.func_184121_ak();
    }

    public static Vec3d interpolateEntityClose(Entity entity, float renderPartialTicks) {
        return new Vec3d(MathUtil.calculateDistanceWithPartialTicks(entity.field_70165_t, entity.field_70142_S, renderPartialTicks) - MathUtil.mc.func_175598_ae().field_78725_b, MathUtil.calculateDistanceWithPartialTicks(entity.field_70163_u, entity.field_70137_T, renderPartialTicks) - MathUtil.mc.func_175598_ae().field_78726_c, MathUtil.calculateDistanceWithPartialTicks(entity.field_70161_v, entity.field_70136_U, renderPartialTicks) - MathUtil.mc.func_175598_ae().field_78723_d);
    }

    public static double radToDeg(double rad) {
        return rad * (double)57.29578f;
    }

    public static double degToRad(double deg) {
        return deg * 0.01745329238474369;
    }

    public static Vec3d direction(float yaw) {
        return new Vec3d(Math.cos(MathUtil.degToRad(yaw + 90.0f)), 0.0, Math.sin(MathUtil.degToRad(yaw + 90.0f)));
    }

    public static float wrap(float val) {
        if ((val %= 360.0f) >= 180.0f) {
            val -= 360.0f;
        }
        if (val < -180.0f) {
            val += 360.0f;
        }
        return val;
    }

    public static double map(double value, double a, double b, double c, double d) {
        value = (value - a) / (b - a);
        return c + value * (d - c);
    }

    public static double linear(double from, double to, double incline) {
        return from < to - incline ? from + incline : (from > to + incline ? from - incline : to);
    }

    public static double parabolic(double from, double to, double incline) {
        return from + (to - from) / incline;
    }

    public static double getDistance(Vec3d pos, double x, double y, double z) {
        double deltaX = pos.field_72450_a - x;
        double deltaY = pos.field_72448_b - y;
        double deltaZ = pos.field_72449_c - z;
        return MathHelper.func_76133_a((double)(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ));
    }

    public static double[] calcIntersection(double[] line, double[] line2) {
        double a1 = line[3] - line[1];
        double b1 = line[0] - line[2];
        double c1 = a1 * line[0] + b1 * line[1];
        double a2 = line2[3] - line2[1];
        double b2 = line2[0] - line2[2];
        double c2 = a2 * line2[0] + b2 * line2[1];
        double delta = a1 * b2 - a2 * b1;
        return new double[]{(b2 * c1 - b1 * c2) / delta, (a1 * c2 - a2 * c1) / delta};
    }

    public static boolean isIntersect(AxisAlignedBB a, AxisAlignedBB b) {
        if (a.field_72336_d <= b.field_72340_a || a.field_72340_a >= b.field_72336_d) {
            return false;
        }
        if (a.field_72337_e <= b.field_72338_b || a.field_72338_b >= b.field_72337_e) {
            return false;
        }
        return !(a.field_72334_f <= b.field_72339_c) && !(a.field_72339_c >= b.field_72334_f);
    }
}
