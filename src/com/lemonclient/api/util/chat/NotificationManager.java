/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.chat;

import com.lemonclient.api.util.chat.Notification;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.hud.Notifications;
import java.util.ArrayList;

public class NotificationManager {
    public static ArrayList<Notification> notifications = new ArrayList();

    public static void add(Notification notify) {
        Notifications notification = ModuleManager.getModule(Notifications.class);
        int max = (Integer)notification.max.getValue();
        if (max != 0 && notifications.size() >= max) {
            switch ((String)notification.mode.getValue()) {
                case "Remove": {
                    notifications.remove(notifications.get(0));
                    break;
                }
                case "Cancel": {
                    return;
                }
            }
        }
        notify.y = notifications.size() * 25;
        notifications.add(notify);
    }

    public static void draw() {
        if (notifications.isEmpty()) {
            return;
        }
        Notification remove = null;
        for (Notification notify : notifications) {
            if (notify.x == 0.0f) {
                boolean bl = notify.in = !notify.in;
            }
            if (Math.abs((double)notify.x - notify.width) < 0.1 && !notify.in) {
                remove = notify;
            }
            Notifications notifications = ModuleManager.getModule(Notifications.class);
            notify.x = notify.in ? notify.animationUtils.animate(0.0f, notify.x, ((Double)notifications.xSpeed.getValue()).floatValue()) : (float)notify.animationUtils.animate(notify.width, (double)notify.x, (double)((Double)notifications.xSpeed.getValue()).floatValue());
            notify.onRender();
        }
        if (remove != null) {
            notifications.remove(remove);
        }
    }
}
