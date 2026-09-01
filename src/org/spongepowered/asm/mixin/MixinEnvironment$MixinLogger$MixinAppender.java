/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.Level
 *  org.apache.logging.log4j.core.Filter
 *  org.apache.logging.log4j.core.Layout
 *  org.apache.logging.log4j.core.LogEvent
 *  org.apache.logging.log4j.core.appender.AbstractAppender
 */
package org.spongepowered.asm.mixin;

import java.io.Serializable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.spongepowered.asm.mixin.MixinEnvironment;

static class MixinEnvironment.MixinLogger.MixinAppender
extends AbstractAppender {
    protected MixinEnvironment.MixinLogger.MixinAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
        super(name, filter, layout);
    }

    public void append(LogEvent event) {
        if (event.getLevel() == Level.DEBUG && "Validating minecraft".equals(event.getMessage().getFormat())) {
            MixinEnvironment.gotoPhase(MixinEnvironment.Phase.INIT);
        }
    }
}
