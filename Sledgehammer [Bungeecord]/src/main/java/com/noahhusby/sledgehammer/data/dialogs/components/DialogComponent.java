package com.noahhusby.sledgehammer.data.dialogs.components;

import com.noahhusby.sledgehammer.data.dialogs.IDialogScene;

public abstract class DialogComponent implements IDialogComponent {
    private String value = "";
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String v) {
        this.value = v;
    }
}
