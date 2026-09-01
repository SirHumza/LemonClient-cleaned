/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.module.modules.dev;

class FakePlayerDev.playerInfo {
    final String name;
    int tickPop = -1;
    int tickRegen = 0;

    public FakePlayerDev.playerInfo(String name) {
        this.name = name;
    }

    boolean update() {
        if (this.tickPop != -1 && ++this.tickPop >= (Integer)FakePlayerDev.this.vulnerabilityTick.getValue()) {
            this.tickPop = -1;
        }
        if (++this.tickRegen >= (Integer)FakePlayerDev.this.tickRegenVal.getValue()) {
            this.tickRegen = 0;
            return true;
        }
        return false;
    }

    boolean canPop() {
        return this.tickPop == -1;
    }
}
