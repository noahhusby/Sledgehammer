/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - EditComponent.java
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
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.dialogs.components.setup;

import com.noahhusby.sledgehammer.dialogs.components.DialogComponent;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;

public class EditComponent extends DialogComponent {
    @Override
    public String getKey() {
        return "edit";
    }

    @Override
    public String getPrompt() {
        return "Do you want to edit this server?";
    }

    @Override
    public TextElement[] getExplanation() {
        return new TextElement[]{new TextElement("Enter ", ChatColor.GRAY),
                new TextElement("Yes [Y]", ChatColor.BLUE), new TextElement(" or ", ChatColor.GRAY),
        new TextElement("No [N]", ChatColor.BLUE)};
    }

    @Override
    public String[] getAcceptableResponses() {
        return new String[]{"yes", "no", "y", "n"};
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
