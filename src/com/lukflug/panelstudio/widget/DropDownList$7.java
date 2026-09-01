/*
 * Decompiled with CFR 0.152.
 */
package com.lukflug.panelstudio.widget;

import com.lukflug.panelstudio.base.Context;
import com.lukflug.panelstudio.component.IFixedComponent;
import com.lukflug.panelstudio.popup.IPopupPositioner;
import com.lukflug.panelstudio.setting.ILabeled;
import com.lukflug.panelstudio.theme.IButtonRenderer;
import com.lukflug.panelstudio.widget.Button;
import com.lukflug.panelstudio.widget.DropDownList;
import com.lukflug.panelstudio.widget.TextField;
import java.awt.Rectangle;
import java.util.function.Supplier;

class DropDownList.7
extends Button<Void> {
    final /* synthetic */ IFixedComponent val$popup;
    final /* synthetic */ IPopupPositioner val$positioner;
    final /* synthetic */ TextField val$textField;

    DropDownList.7(ILabeled x0, Supplier x1, IButtonRenderer x2, IFixedComponent iFixedComponent, IPopupPositioner iPopupPositioner, TextField textField) {
        this.val$popup = iFixedComponent;
        this.val$positioner = iPopupPositioner;
        this.val$textField = textField;
        super(x0, x1, x2);
    }

    @Override
    public void handleButton(Context context, int button) {
        super.handleButton(context, button);
        DropDownList.this.rect = new Rectangle(((DropDownList)DropDownList.this).rect.x, context.getPos().y, context.getPos().x + context.getSize().width - ((DropDownList)DropDownList.this).rect.x, context.getSize().height);
        if (button == 0 && context.isClicked(button) || DropDownList.this.transferFocus) {
            context.getPopupDisplayer().displayPopup(this.val$popup, DropDownList.this.rect, DropDownList.this.toggle, this.val$positioner);
            DropDownList.this.transferFocus = false;
        }
    }

    @Override
    public int getHeight() {
        return this.val$textField.getHeight();
    }
}
