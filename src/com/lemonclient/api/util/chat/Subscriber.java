/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.chat;

import java.util.Collection;
import me.zero.alpine.listener.Listener;

public interface Subscriber {
    public Collection<Listener<?>> getListeners();
}
