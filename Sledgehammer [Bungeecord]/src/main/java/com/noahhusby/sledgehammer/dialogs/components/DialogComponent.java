/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - DialogComponent.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.dialogs.components;

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

    @Override
    public boolean isManual() {
        return false;
    }
}
