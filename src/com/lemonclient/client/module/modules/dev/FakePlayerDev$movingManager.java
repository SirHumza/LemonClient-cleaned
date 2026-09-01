/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.module.modules.dev;

import com.lemonclient.client.module.modules.dev.FakePlayerDev;
import java.util.ArrayList;

static class FakePlayerDev.movingManager {
    private final ArrayList<FakePlayerDev.movingPlayer> players = new ArrayList();

    FakePlayerDev.movingManager() {
    }

    void addPlayer(int id, String type, double speed, int direction, double range, boolean follow) {
        this.players.add(new FakePlayerDev.movingPlayer(id, type, speed, direction, range, follow));
    }

    void update() {
        this.players.forEach(FakePlayerDev.movingPlayer::move);
    }

    void remove() {
        this.players.clear();
    }
}
