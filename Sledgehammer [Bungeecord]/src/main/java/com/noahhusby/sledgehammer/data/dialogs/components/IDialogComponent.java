/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - IDialogComponent.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.data.dialogs.components;

import com.noahhusby.sledgehammer.util.TextElement;

public interface IDialogComponent {
    String getKey();

    String getValue();

    void setValue(String v);

    String getPrompt();

    TextElement[] getExplanation();

    String[] getAcceptableResponses();

    boolean validateResponse(String v);

    boolean isManual();
}
