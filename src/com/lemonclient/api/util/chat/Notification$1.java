/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.chat;

import com.lemonclient.api.util.chat.Notification;

static class Notification.1 {
    static final /* synthetic */ int[] $SwitchMap$com$lemonclient$api$util$chat$Notification$Type;

    static {
        $SwitchMap$com$lemonclient$api$util$chat$Notification$Type = new int[Notification.Type.values().length];
        try {
            Notification.1.$SwitchMap$com$lemonclient$api$util$chat$Notification$Type[Notification.Type.ERROR.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Notification.1.$SwitchMap$com$lemonclient$api$util$chat$Notification$Type[Notification.Type.INFO.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Notification.1.$SwitchMap$com$lemonclient$api$util$chat$Notification$Type[Notification.Type.SUCCESS.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Notification.1.$SwitchMap$com$lemonclient$api$util$chat$Notification$Type[Notification.Type.WARNING.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Notification.1.$SwitchMap$com$lemonclient$api$util$chat$Notification$Type[Notification.Type.DISABLE.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
