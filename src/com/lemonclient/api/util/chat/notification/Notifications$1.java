/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.chat.notification;

import com.lemonclient.api.util.chat.notification.Notifications;

static class Notifications.1 {
    static final /* synthetic */ int[] $SwitchMap$com$lemonclient$api$util$chat$notification$Notifications$Type;

    static {
        $SwitchMap$com$lemonclient$api$util$chat$notification$Notifications$Type = new int[Notifications.Type.values().length];
        try {
            Notifications.1.$SwitchMap$com$lemonclient$api$util$chat$notification$Notifications$Type[Notifications.Type.ERROR.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Notifications.1.$SwitchMap$com$lemonclient$api$util$chat$notification$Notifications$Type[Notifications.Type.INFO.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Notifications.1.$SwitchMap$com$lemonclient$api$util$chat$notification$Notifications$Type[Notifications.Type.SUCCESS.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Notifications.1.$SwitchMap$com$lemonclient$api$util$chat$notification$Notifications$Type[Notifications.Type.WARNING.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Notifications.1.$SwitchMap$com$lemonclient$api$util$chat$notification$Notifications$Type[Notifications.Type.DISABLE.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
