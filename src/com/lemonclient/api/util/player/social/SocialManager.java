/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.player.social;

import com.lemonclient.api.util.player.social.Enemy;
import com.lemonclient.api.util.player.social.Friend;
import com.lemonclient.api.util.player.social.Ignore;
import com.lemonclient.client.module.ModuleManager;
import com.lemonclient.client.module.modules.qwq.Friends;
import java.util.ArrayList;

public class SocialManager {
    private static final ArrayList<Friend> friends = new ArrayList();
    private static final ArrayList<Enemy> enemies = new ArrayList();
    private static final ArrayList<Ignore> ignores = new ArrayList();

    public static ArrayList<Friend> getFriends() {
        return friends;
    }

    public static ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public static ArrayList<Ignore> getIgnores() {
        return ignores;
    }

    public static ArrayList<String> getFriendsByName() {
        ArrayList<String> friendNames = new ArrayList<String>();
        SocialManager.getFriends().forEach(friend -> friendNames.add(friend.getName()));
        return friendNames;
    }

    public static ArrayList<String> getEnemiesByName() {
        ArrayList<String> enemyNames = new ArrayList<String>();
        SocialManager.getEnemies().forEach(enemy -> enemyNames.add(enemy.getName()));
        return enemyNames;
    }

    public static ArrayList<String> getIgnoresByName() {
        ArrayList<String> ignoreNames = new ArrayList<String>();
        SocialManager.getIgnores().forEach(ignore -> ignoreNames.add(ignore.getName()));
        return ignoreNames;
    }

    public static boolean isFriend(String name) {
        for (Friend friend : SocialManager.getFriends()) {
            if (!friend.getName().equalsIgnoreCase(name) || !ModuleManager.isModuleEnabled(Friends.class)) continue;
            return true;
        }
        return false;
    }

    public static boolean isOnFriendList(String name) {
        boolean value = false;
        for (Friend friend : SocialManager.getFriends()) {
            if (!friend.getName().equalsIgnoreCase(name)) continue;
            value = true;
            break;
        }
        return value;
    }

    public static boolean isEnemy(String name) {
        for (Enemy enemy : SocialManager.getEnemies()) {
            if (!enemy.getName().equalsIgnoreCase(name)) continue;
            return true;
        }
        return false;
    }

    public static boolean isOnEnemyList(String name) {
        boolean value = false;
        for (Enemy enemy : SocialManager.getEnemies()) {
            if (!enemy.getName().equalsIgnoreCase(name)) continue;
            value = true;
            break;
        }
        return value;
    }

    public static boolean isIgnore(String name) {
        for (Ignore ignore : SocialManager.getIgnores()) {
            if (!ignore.getName().equalsIgnoreCase(name)) continue;
            return true;
        }
        return false;
    }

    public static boolean isOnIgnoreList(String name) {
        boolean value = false;
        for (Ignore ignore : SocialManager.getIgnores()) {
            if (!ignore.getName().equalsIgnoreCase(name)) continue;
            value = true;
            break;
        }
        return value;
    }

    public static Friend getFriend(String name) {
        for (Friend friend : SocialManager.getFriends()) {
            if (!friend.getName().equalsIgnoreCase(name)) continue;
            return friend;
        }
        return null;
    }

    public static Enemy getEnemy(String name) {
        for (Enemy enemy : SocialManager.getEnemies()) {
            if (!enemy.getName().equalsIgnoreCase(name)) continue;
            return enemy;
        }
        return null;
    }

    public static Ignore getIgnore(String name) {
        for (Ignore ignore : SocialManager.getIgnores()) {
            if (!ignore.getName().equalsIgnoreCase(name)) continue;
            return ignore;
        }
        return null;
    }

    public static void addFriend(String name) {
        if (!SocialManager.isOnFriendList(name)) {
            SocialManager.getFriends().add(new Friend(name));
        }
    }

    public static void delFriend(String name) {
        SocialManager.getFriends().remove(SocialManager.getFriend(name));
    }

    public static void addEnemy(String name) {
        if (!SocialManager.isOnEnemyList(name)) {
            SocialManager.getEnemies().add(new Enemy(name));
        }
    }

    public static void delEnemy(String name) {
        SocialManager.getEnemies().remove(SocialManager.getEnemy(name));
    }

    public static void addIgnore(String name) {
        if (!SocialManager.isOnIgnoreList(name)) {
            SocialManager.getIgnores().add(new Ignore(name));
        }
    }

    public static void delIgnore(String name) {
        SocialManager.getIgnores().remove(SocialManager.getIgnore(name));
    }

    public static void clearIgnoreList() {
        SocialManager.getIgnores().clear();
    }
}
