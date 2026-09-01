/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.client.module.modules.misc;

import com.lemonclient.api.util.player.social.SocialManager;
import java.util.TimerTask;

class AntiSpam.1
extends TimerTask {
    final /* synthetic */ String val$finalUsername;

    AntiSpam.1(String string) {
        this.val$finalUsername = string;
    }

    @Override
    public void run() {
        AntiSpam.this.ignoredList.remove(this.val$finalUsername);
        if (!SocialManager.isIgnore(this.val$finalUsername)) {
            return;
        }
        SocialManager.delIgnore(this.val$finalUsername);
    }
}
