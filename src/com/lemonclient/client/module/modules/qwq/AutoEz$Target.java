/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.module.modules.qwq;

static class AutoEz.Target {
    String name;
    int time;

    public AutoEz.Target(String name) {
        this.name = name;
        this.time = 20;
    }

    void updateTime() {
        --this.time;
    }

    void update() {
        this.time = 20;
    }
}
