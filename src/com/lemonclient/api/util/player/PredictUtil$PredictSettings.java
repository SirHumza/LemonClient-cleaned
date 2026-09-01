/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.player;

public static class PredictUtil.PredictSettings {
    final int tick;
    final boolean calculateY;
    final int startDecrease;
    final int exponentStartDecrease;
    final int decreaseY;
    final int exponentDecreaseY;
    final boolean splitXZ;
    final boolean manualOutHole;
    final boolean aboveHoleManual;
    final boolean stairPredict;
    final int nStairs;
    final double speedActivationStairs;

    public PredictUtil.PredictSettings(int tick, boolean calculateY, int startDecrease, int exponentStartDecrease, int decreaseY, int exponentDecreaseY, boolean splitXZ, boolean manualOutHole, boolean aboveHoleManual, boolean stairPredict, int nStairs, double speedActivationStairs) {
        this.tick = tick;
        this.calculateY = calculateY;
        this.startDecrease = startDecrease;
        this.exponentStartDecrease = exponentStartDecrease;
        this.decreaseY = decreaseY;
        this.exponentDecreaseY = exponentDecreaseY;
        this.splitXZ = splitXZ;
        this.manualOutHole = manualOutHole;
        this.aboveHoleManual = aboveHoleManual;
        this.stairPredict = stairPredict;
        this.nStairs = nStairs;
        this.speedActivationStairs = speedActivationStairs;
    }
}
