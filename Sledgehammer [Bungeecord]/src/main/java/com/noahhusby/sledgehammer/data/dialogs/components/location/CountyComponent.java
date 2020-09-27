/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - CountyComponent.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.data.dialogs.components.location;

import com.noahhusby.sledgehammer.data.dialogs.components.DialogComponent;
import com.noahhusby.sledgehammer.util.TextElement;

public class CountyComponent extends DialogComponent {
    @Override
    public String getKey() {
        return "county";
    }

    @Override
    public String getPrompt() {
        return "What county?";
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
