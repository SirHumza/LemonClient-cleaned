/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.render;

public class FadeUtils {
    protected long start;
    protected long length;

    public FadeUtils(long ms) {
        this.length = ms;
        this.reset();
    }

    public void reset() {
        this.start = System.currentTimeMillis();
    }

    public boolean isEnd() {
        return this.getTime() >= this.length;
    }

    public FadeUtils end() {
        this.start = System.currentTimeMillis() - this.length;
        return this;
    }

    protected long getTime() {
        return System.currentTimeMillis() - this.start;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getLength() {
        return this.length;
    }

    public double getFadeOne() {
        return this.isEnd() ? 1.0 : (double)this.getTime() / (double)this.length;
    }

    public double toDelta() {
        double value = (double)this.toDelta(this.start) / (double)this.length;
        if (value > 1.0) {
            value = 1.0;
        }
        if (value < 0.0) {
            value = 0.0;
        }
        return value;
    }

    public long toDelta(long start) {
        return System.currentTimeMillis() - start;
    }

    public double getFade(String fadeMode) {
        return FadeUtils.getFade(fadeMode, this.getFadeOne());
    }

    public static double getFade(String fadeMode, double current) {
        switch (fadeMode) {
            case "FADE_IN": {
                return FadeUtils.getFadeInDefault(current);
            }
            case "FADE_OUT": {
                return FadeUtils.getFadeOutDefault(current);
            }
            case "FADE_EPS_IN": {
                return FadeUtils.getEpsEzFadeIn(current);
            }
            case "FADE_EPS_OUT": {
                return FadeUtils.getEpsEzFadeOut(current);
            }
            case "FADE_EASE_IN_QUAD": {
                return FadeUtils.easeInQuad(current);
            }
            case "FADE_EASE_OUT_QUAD": {
                return FadeUtils.easeOutQuad(current);
            }
        }
        return current;
    }

    public static double getFadeType(String fadeType, boolean FadeIn, double current) {
        switch (fadeType) {
            case "FADE_DEFAULT": {
                return FadeIn ? FadeUtils.getFadeInDefault(current) : FadeUtils.getFadeOutDefault(current);
            }
            case "FADE_EPS": {
                return FadeIn ? FadeUtils.getEpsEzFadeIn(current) : FadeUtils.getEpsEzFadeOut(current);
            }
            case "FADE_EASE_QUAD": {
                return FadeIn ? FadeUtils.easeInQuad(current) : FadeUtils.easeOutQuad(current);
            }
        }
        return FadeIn ? current : 1.0 - current;
    }

    private static double checkOne(double one) {
        return Math.max(0.0, Math.min(1.0, one));
    }

    public static double getFadeInDefault(double current) {
        return Math.tanh(FadeUtils.checkOne(current) * 3.0);
    }

    public static double getFadeOutDefault(double current) {
        return 1.0 - FadeUtils.getFadeInDefault(current);
    }

    public static double getEpsEzFadeIn(double current) {
        return 1.0 - FadeUtils.getEpsEzFadeOut(current);
    }

    public static double getEpsEzFadeOut(double current) {
        return Math.cos(1.5707963267948966 * FadeUtils.checkOne(current)) * Math.cos(2.5132741228718345 * FadeUtils.checkOne(current));
    }

    public static double easeOutQuad(double current) {
        return 1.0 - FadeUtils.easeInQuad(current);
    }

    public static double easeInQuad(double current) {
        return FadeUtils.checkOne(current) * FadeUtils.checkOne(current);
    }

    public double getEpsEzFadeInGUI() {
        if (this.isEnd()) {
            return 1.0;
        }
        return Math.sin(this.getFadeOne());
    }
}
