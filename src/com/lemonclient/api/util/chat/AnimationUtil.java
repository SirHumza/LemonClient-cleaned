/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.chat;

import com.lemonclient.api.util.misc.Timing;

public class AnimationUtil {
    private static final float defaultSpeed = 0.125f;
    private final Timing timerUtil = new Timing();

    public static float moveTowards(float current, float end, float smoothSpeed, float minSpeed, boolean back) {
        float movement = (end - current) * smoothSpeed;
        if (movement > 0.0f) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        } else if (movement < 0.0f) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }
        if (back) {
            return movement - current;
        }
        return current + movement;
    }

    public static double moveTowards(double target, double current, double speed) {
        boolean larger = target > current;
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        current = larger ? (current += factor) : (current -= factor);
        return current;
    }

    public static double expand(double target, double current, double speed) {
        if (current > target) {
            current = target;
        }
        if (current < -target) {
            current = -target;
        }
        return current += speed;
    }

    public static float calculateCompensation(float target, float current, long delta, double speed) {
        float diff = current - target;
        if (delta < 1L) {
            delta = 1L;
        }
        if (delta > 1000L) {
            delta = 16L;
        }
        if ((double)diff > speed) {
            float f;
            double xD = Math.max(speed * (double)delta / 16.0, 0.5);
            current = (float)((double)current - xD);
            if (f < target) {
                current = target;
            }
        } else if ((double)diff < -speed) {
            float f;
            double xD = Math.max(speed * (double)delta / 16.0, 0.5);
            current = (float)((double)current + xD);
            if (f > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }

    public float moveUD(float current, float end, float minSpeed) {
        return this.moveUD(current, end, 0.125f, minSpeed);
    }

    public double animate(double target, double current, double speed) {
        if (this.timerUtil.passedMs(4L)) {
            boolean larger;
            boolean bl = larger = target > current;
            if (speed < 0.0) {
                speed = 0.0;
            } else if (speed > 1.0) {
                speed = 1.0;
            }
            double dif = Math.max(target, current) - Math.min(target, current);
            double factor = dif * speed;
            if (factor < 0.1) {
                factor = 0.1;
            }
            current = larger ? current + factor : current - factor;
            this.timerUtil.reset();
        }
        return current;
    }

    public double animates(double target, double current, double speed) {
        if (this.timerUtil.passedMs(1L)) {
            boolean larger = target > current;
            boolean bl = larger;
            if (speed < 0.0) {
                speed = 0.0;
            } else if (speed > 1.0) {
                speed = 1.0;
            }
            double dif = Math.max(target, current) - Math.min(target, current);
            double factor = dif * speed;
            if (factor < 0.1) {
                factor = 0.1;
            }
            current = larger ? (current = current + factor) : (current = current - factor);
            this.timerUtil.reset();
        }
        return current;
    }

    public float animate(float target, float current, float speed) {
        if (this.timerUtil.passedMs(4L)) {
            boolean larger;
            boolean bl = larger = target > current;
            if (speed < 0.0f) {
                speed = 0.0f;
            } else if ((double)speed > 1.0) {
                speed = 1.0f;
            }
            float dif = Math.max(target, current) - Math.min(target, current);
            float factor = dif * speed;
            if (factor < 0.01f) {
                factor = 0.01f;
            }
            current = larger ? current + factor : current - factor;
            this.timerUtil.reset();
        }
        if ((double)Math.abs(current - target) < 0.2) {
            return target;
        }
        return current;
    }

    public float moveUD(float current, float end, float smoothSpeed, float minSpeed) {
        float movement = 0.0f;
        if (this.timerUtil.passedMs(4L)) {
            movement = (end - current) * smoothSpeed;
            if (movement > 0.0f) {
                movement = Math.max(minSpeed, movement);
                movement = Math.min(end - current, movement);
            } else if (movement < 0.0f) {
                movement = Math.min(-minSpeed, movement);
                movement = Math.max(end - current, movement);
            }
            this.timerUtil.reset();
        }
        return current + movement;
    }
}
