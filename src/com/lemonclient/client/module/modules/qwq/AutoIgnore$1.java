/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.module.modules.qwq;

import java.util.TimerTask;

class AutoIgnore.1
extends TimerTask {
    final /* synthetic */ String val$string;

    AutoIgnore.1(String string) {
        this.val$string = string;
    }

    @Override
    public void run() {
        AutoIgnore.this.messageTimes.put(this.val$string, AutoIgnore.this.messageTimes.get(this.val$string) - 1);
    }
}
