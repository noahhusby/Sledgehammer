/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - LocationMenuComponent.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.dialogs.components.setup;

import com.noahhusby.sledgehammer.dialogs.components.DialogComponent;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;

public class LocationMenuComponent extends DialogComponent {
    @Override
    public String getKey() {
        return "location";
    }

    @Override
    public String getPrompt() {
        return "Edit the server locations?";
    }

    @Override
    public TextElement[] getExplanation() {
        return new TextElement[]{new TextElement("Type ", ChatColor.GRAY),
                new TextElement("list", ChatColor.RED), new TextElement(" to view current locations, ", ChatColor.GRAY),
                new TextElement("add", ChatColor.RED), new TextElement(" to add a new location, ", ChatColor.GRAY),
                new TextElement("remove", ChatColor.RED), new TextElement(" to remove an existing location, or ", ChatColor.GRAY),
                new TextElement("finish", ChatColor.RED), new TextElement(" to finish setting up this server.", ChatColor.GRAY)};
    }

    @Override
    public String[] getAcceptableResponses() {
        return new String[]{"*"};
    }

    @Override
    public boolean validateResponse(String v) {
        String vm = v.toLowerCase().trim();
        return vm.equals("list") || vm.equals("add") || vm.equals("remove") || vm.equals("finish");
    }
}
