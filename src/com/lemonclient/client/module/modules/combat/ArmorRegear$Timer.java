/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.module.modules.combat;

private static class ArmorRegear.Timer {
    private long time = -1L;

    private ArmorRegear.Timer() {
    }

    public boolean passedMs(long ms) {
        return this.passedNS(this.convertToNS(ms));
    }

    public boolean passedNS(long ns) {
        return System.nanoTime() - this.time >= ns;
    }

    public void reset() {
        this.time = System.nanoTime();
    }

    public long convertToNS(long time) {
        return time * 1000000L;
    }
}
