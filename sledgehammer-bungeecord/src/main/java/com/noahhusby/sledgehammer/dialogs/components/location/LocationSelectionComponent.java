/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - LocationSelectionComponent.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 *
 */

package com.noahhusby.sledgehammer.dialogs.components.location;

import com.noahhusby.sledgehammer.dialogs.components.DialogComponent;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;

public class LocationSelectionComponent extends DialogComponent {
    @Override
    public String getKey() {
        return "locationmode";
    }

    @Override
    public String getPrompt() {
        return "What type of location?";
    }

    @Override
    public TextElement[] getExplanation() {
        return new TextElement[]{new TextElement("Enter one of the following modes: ", ChatColor.GRAY),
        new TextElement("City", ChatColor.BLUE), new TextElement(", ", ChatColor.GRAY), new TextElement("County", ChatColor.BLUE),
        new TextElement(", ", ChatColor.GRAY), new TextElement("State", ChatColor.BLUE), new TextElement(", or ", ChatColor.GRAY),
        new TextElement("Country", ChatColor.BLUE)};
    }

    @Override
    public String[] getAcceptableResponses() {
        return new String[]{"*"};
    }

    @Override
    public boolean validateResponse(String v) {
        String vm = v.toLowerCase().trim();
        return vm.equals("city") || vm.equals("county") || vm.equals("state") || vm.equals("country");
    }
}
