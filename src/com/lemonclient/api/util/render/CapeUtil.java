/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.render;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CapeUtil {
    private static final List<UUID> uuids = new ArrayList<UUID>();

    public static void init() {
        try {
            String inputLine;
            URL capesList = new URL("https://raw.githubusercontent.com/OaDwH/CapeUUID/main/list.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(capesList.openStream()));
            while ((inputLine = in.readLine()) != null) {
                uuids.add(UUID.fromString(inputLine));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean hasCape(UUID id) {
        return uuids.contains(id);
    }
}
