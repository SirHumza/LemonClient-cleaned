/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.render;

private static class RenderUtil.Points {
    double[][] point = new double[10][2];
    private int count = 0;
    private final double xCenter;
    private final double zCenter;
    public final double yMin;
    public final double yMax;
    private final float rotation;

    public RenderUtil.Points(double yMin, double yMax, double xCenter, double zCenter, float rotation) {
        this.yMin = yMin;
        this.yMax = yMax;
        this.xCenter = xCenter;
        this.zCenter = zCenter;
        this.rotation = rotation;
    }

    public void addPoints(double x, double z) {
        double rotateX = (x -= this.xCenter) * Math.cos(this.rotation) - (z -= this.zCenter) * Math.sin(this.rotation);
        double rotateZ = x * Math.sin(this.rotation) + z * Math.cos(this.rotation);
        this.point[this.count++] = new double[]{rotateX += this.xCenter, rotateZ += this.zCenter};
    }

    public double[] getPoint(int index) {
        return this.point[index];
    }
}
