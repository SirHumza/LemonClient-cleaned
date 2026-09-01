/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.util.chat;

import com.lemonclient.api.util.chat.Subscriber;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.zero.alpine.listener.Listener;

public class SubscriberImpl
implements Subscriber {
    protected final List<Listener<?>> listeners = new ArrayList();

    @Override
    public Collection<Listener<?>> getListeners() {
        return this.listeners;
    }
}
