/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 */
package com.lemonclient.api.util.chat.notification;

import com.lemonclient.api.util.chat.notification.Notification;
import com.lemonclient.api.util.chat.notification.Notifications;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class NotificationsManager {
    public static final LinkedBlockingQueue<Notification> pendingNotifications;
    private static Notification currentNotification;
    public static ArrayList<Notifications> notifications;

    public static void show(Notification notification) {
        pendingNotifications.add(notification);
    }

    public static void show(Notifications notification) {
        notifications.add(notification);
    }

    public static void update() {
        if (currentNotification != null && !currentNotification.isShown()) {
            currentNotification = null;
        }
        if (currentNotification == null && !pendingNotifications.isEmpty()) {
            currentNotification = pendingNotifications.poll();
            currentNotification.show();
        }
    }

    public static void render() {
        try {
            int divider = Minecraft.func_71410_x().field_71474_y.field_74335_Z;
            int width = Minecraft.func_71410_x().field_71443_c / divider;
            int height = Minecraft.func_71410_x().field_71440_d / divider;
            NotificationsManager.update();
            if (currentNotification != null) {
                currentNotification.render(width, height);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void drawNotifications() {
        try {
            double lastY;
            ScaledResolution res = new ScaledResolution(Minecraft.func_71410_x());
            double startY = lastY = (double)(res.func_78328_b() - 25);
            for (int i = 0; i < notifications.size(); ++i) {
                Notifications not = notifications.get(i);
                if (not.shouldDelete()) {
                    notifications.remove(not);
                    int cao = 0;
                    while ((double)cao > not.width) {
                        not.animationX = (double)cao - not.width;
                        --cao;
                    }
                    startY += not.getHeight() + 3.0;
                }
                not.draw(startY, lastY);
                int number = 0;
                while ((double)number < not.width) {
                    not.animationX = (double)number + not.width;
                    ++number;
                }
                startY -= not.getHeight() + 2.0;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    static {
        notifications = new ArrayList();
        pendingNotifications = new LinkedBlockingQueue();
        currentNotification = null;
    }
}
