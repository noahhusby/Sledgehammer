/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - EarthServerComponent.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.dialogs.components.setup;

import com.noahhusby.sledgehammer.dialogs.components.DialogComponent;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;

public class EarthServerComponent extends DialogComponent {
    @Override
    public String getKey() {
        return "earth";
    }

    @Override
    public String getPrompt() {
        return "Is this a build server (for BTE, not just general building)?";
    }

    @Override
    public TextElement[] getExplanation() {
        return new TextElement[]{new TextElement("Enter ", ChatColor.GRAY),
                new TextElement("Yes [Y]", ChatColor.BLUE), new TextElement(" or ", ChatColor.GRAY),
                new TextElement("No [N]", ChatColor.BLUE)};
    }

    @Override
    public String[] getAcceptableResponses() {
        return new String[]{"*"};
    }

    @Override
    public boolean validateResponse(String v) {
        String vm = v.toLowerCase().trim();
        return vm.equals("yes") || vm.equals("no") || vm.equals("y") || vm.equals("n");
    }

    @Override
    public boolean isManual() {
        return true;
    }
}
