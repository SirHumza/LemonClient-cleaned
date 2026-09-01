/*
 * Decompiled with CFR 0.152.
 */
package com.lemonclient.api.event;

import com.lemonclient.api.event.LemonClientEvent;
import com.lemonclient.api.event.Phase;

public interface MultiPhase<T extends LemonClientEvent> {
    public Phase getPhase();

    public T nextPhase();
}
