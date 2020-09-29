/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - CityComponent.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.dialogs.components.location;

import com.noahhusby.sledgehammer.dialogs.components.DialogComponent;
import com.noahhusby.sledgehammer.chat.TextElement;

public class CityComponent extends DialogComponent {
    @Override
    public String getKey() {
        return "city";
    }

    @Override
    public String getPrompt() {
        return "What city?";
    }

    @Override
    public TextElement[] getExplanation() {
        return null;
    }

    @Override
    public String[] getAcceptableResponses() {
        return new String[]{"*"};
    }

    @Override
    public boolean validateResponse(String v) {
        return true;
    }
}
