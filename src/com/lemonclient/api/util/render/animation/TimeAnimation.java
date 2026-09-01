/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package com.lemonclient.api.util.render.animation;

import com.lemonclient.api.util.render.animation.AnimationMode;
import net.minecraft.util.math.MathHelper;

public class TimeAnimation {
    private final long length;
    private final double start;
    private final double end;
    private double current;
    private double progress;
    private boolean playing;
    private boolean backwards;
    private boolean reverseOnEnd;
    private long lastTime;
    private double per;
    private AnimationMode mode;

    public TimeAnimation(long length, double start, double end, boolean backwards, AnimationMode mode) {
        this.length = length;
        this.start = start;
        this.current = start;
        this.end = end;
        this.mode = mode;
        this.backwards = backwards;
        this.playing = true;
        switch (mode) {
            case LINEAR: {
                this.per = (end - start) / (double)length;
                break;
            }
            case EXPONENTIAL: {
                boolean flag;
                double dif = end - start;
                boolean bl = flag = dif < 0.0;
                if (flag) {
                    dif *= -1.0;
                }
                int i = 0;
                while ((long)i < length) {
                    dif = Math.sqrt(dif);
                    ++i;
                }
                this.per = dif;
            }
        }
        this.lastTime = System.currentTimeMillis();
    }

    public void add() {
        if (this.playing) {
            if (this.mode == AnimationMode.LINEAR) {
                this.current = this.start + this.progress;
                this.progress += this.per * (double)(System.currentTimeMillis() - this.lastTime);
            }
            this.current = MathHelper.func_151237_a((double)this.current, (double)this.start, (double)this.end);
            if (this.current >= this.end || this.backwards && this.current <= this.start) {
                if (this.reverseOnEnd) {
                    this.reverse();
                    this.reverseOnEnd = false;
                } else {
                    this.playing = false;
                }
            }
        }
        this.lastTime = System.currentTimeMillis();
    }

    public long getLength() {
        return this.length;
    }

    public double getStart() {
        return this.start;
    }

    public double getEnd() {
        return this.end;
    }

    public double getCurrent() {
        return this.current;
    }

    public AnimationMode getMode() {
        return this.mode;
    }

    public void setMode(AnimationMode mode) {
        this.mode = mode;
    }

    public void play() {
        this.playing = true;
    }

    public void stop() {
        this.playing = false;
    }

    public void reverse() {
        this.backwards = !this.backwards;
        this.per *= -1.0;
    }
}
