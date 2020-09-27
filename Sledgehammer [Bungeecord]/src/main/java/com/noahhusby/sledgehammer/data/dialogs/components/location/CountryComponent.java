/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - CountryComponent.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.data.dialogs.components.location;

import com.noahhusby.sledgehammer.data.dialogs.components.DialogComponent;
import com.noahhusby.sledgehammer.util.TextElement;

public class CountryComponent extends DialogComponent {
    @Override
    public String getKey() {
        return "country";
    }

    @Override
    public String getPrompt() {
        return "What country?";
    }

    @Override
    public TextElement[] getExplanation() {
        return new TextElement[0];
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
