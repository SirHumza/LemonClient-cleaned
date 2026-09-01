/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.render;

public interface Easings {
    public static final String[] easings = new String[]{"none", "cubic", "quint", "quad", "quart", "expo", "sine", "circ"};

    public static double toOutEasing(String easing, double value) {
        switch (easing) {
            case "cubic": {
                return Easings.cubicOut(value);
            }
            case "quint": {
                return Easings.quintOut(value);
            }
            case "quad": {
                return Easings.quadOut(value);
            }
            case "quart": {
                return Easings.quartOut(value);
            }
            case "expo": {
                return Easings.expoOut(value);
            }
            case "sine": {
                return Easings.sineOut(value);
            }
            case "circ": {
                return Easings.circOut(value);
            }
        }
        return value;
    }

    public static double toInEasing(String easing, double value) {
        switch (easing) {
            case "cubic": {
                return Easings.cubicIn(value);
            }
            case "quint": {
                return Easings.quintIn(value);
            }
            case "quad": {
                return Easings.quadIn(value);
            }
            case "quart": {
                return Easings.quartIn(value);
            }
            case "expo": {
                return Easings.expoIn(value);
            }
            case "sine": {
                return Easings.sineIn(value);
            }
            case "circ": {
                return Easings.circIn(value);
            }
        }
        return value;
    }

    public static double inOutEasing(String easing, double value) {
        switch (easing) {
            case "cubic": {
                return Easings.cubicInOut(value);
            }
            case "quint": {
                return Easings.quintInOut(value);
            }
            case "quad": {
                return Easings.quadInOut(value);
            }
            case "quart": {
                return Easings.quartInOut(value);
            }
            case "expo": {
                return Easings.expoInOut(value);
            }
            case "sine": {
                return Easings.sineInOut(value);
            }
            case "circ": {
                return Easings.circInOut(value);
            }
        }
        return value;
    }

    public static double cubicIn(double value) {
        return value * value * value;
    }

    public static double cubicOut(double value) {
        return 1.0 - Math.pow(1.0 - value, 3.0);
    }

    public static double cubicInOut(double value) {
        return value < 0.5 ? 4.0 * value * value * value : 1.0 - Math.pow(-2.0 * value + 2.0, 3.0) / 2.0;
    }

    public static double quintIn(double value) {
        return value * value * value * value * value;
    }

    public static double quintOut(double value) {
        return 1.0 - Math.pow(1.0 - value, 5.0);
    }

    public static double quintInOut(double value) {
        return value < 0.5 ? 16.0 * value * value * value * value * value : 1.0 - Math.pow(-2.0 * value + 2.0, 5.0) / 2.0;
    }

    public static double quadIn(double value) {
        return value * value;
    }

    public static double quadOut(double value) {
        return 1.0 - (1.0 - value) * (1.0 - value);
    }

    public static double quadInOut(double value) {
        return value < 0.5 ? 2.0 * value * value : 1.0 - Math.pow(-2.0 * value + 2.0, 2.0) / 2.0;
    }

    public static double quartIn(double value) {
        return value * value * value * value;
    }

    public static double quartOut(double value) {
        return 1.0 - Math.pow(1.0 - value, 4.0);
    }

    public static double quartInOut(double value) {
        return value < 0.5 ? 8.0 * value * value * value * value : 1.0 - Math.pow(-2.0 * value + 2.0, 4.0) / 2.0;
    }

    public static double expoIn(double value) {
        return value == 0.0 ? 0.0 : Math.pow(2.0, 10.0 * value - 10.0);
    }

    public static double expoOut(double value) {
        return value == 1.0 ? 1.0 : 1.0 - Math.pow(2.0, -10.0 * value);
    }

    public static double expoInOut(double value) {
        return value == 0.0 ? 0.0 : (value == 1.0 ? 1.0 : (value < 0.5 ? Math.pow(2.0, 20.0 * value - 10.0) / 2.0 : (2.0 - Math.pow(2.0, -20.0 * value + 10.0)) / 2.0));
    }

    public static double sineIn(double value) {
        return 1.0 - Math.cos(value * Math.PI / 2.0);
    }

    public static double sineOut(double value) {
        return Math.sin(value * Math.PI / 2.0);
    }

    public static double sineInOut(double value) {
        return -(Math.cos(Math.PI * value) - 1.0) / 2.0;
    }

    public static double circIn(double value) {
        return 1.0 - Math.sqrt(1.0 - Math.pow(value, 2.0));
    }

    public static double circOut(double value) {
        return Math.sqrt(1.0 - Math.pow(value - 1.0, 2.0));
    }

    public static double circInOut(double value) {
        return value < 0.5 ? (1.0 - Math.sqrt(1.0 - Math.pow(2.0 * value, 2.0))) / 2.0 : (Math.sqrt(1.0 - Math.pow(-2.0 * value + 2.0, 2.0)) + 1.0) / 2.0;
    }
}
