/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.component;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.component.ComponentProxy;
import com.lukflug.panelstudio.component.IComponent;
import com.lukflug.panelstudio.component.IFixedComponent;
import java.awt.Point;

class DraggableComponent.1
extends ComponentProxy<S> {
    DraggableComponent.1(IComponent x0) {
        super(x0);
    }

    @Override
    public void handleButton(Context context, int button) {
        super.handleButton(context, button);
        if (context.isClicked(button) && button == 0) {
            DraggableComponent.this.dragging = true;
            DraggableComponent.this.attachPoint = context.getInterface().getMouse();
        } else if (!context.getInterface().getButton(0) && DraggableComponent.this.dragging) {
            Point mouse = context.getInterface().getMouse();
            DraggableComponent.this.dragging = false;
            Point p = ((IFixedComponent)DraggableComponent.this.getComponent()).getPosition(context.getInterface());
            p.translate(mouse.x - DraggableComponent.this.attachPoint.x, mouse.y - DraggableComponent.this.attachPoint.y);
            ((IFixedComponent)DraggableComponent.this.getComponent()).setPosition(context.getInterface(), p);
        }
    }

    @Override
    public void exit() {
        DraggableComponent.this.dragging = false;
        super.exit();
    }
}
