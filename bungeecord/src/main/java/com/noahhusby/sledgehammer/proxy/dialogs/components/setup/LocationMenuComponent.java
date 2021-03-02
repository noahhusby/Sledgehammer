/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - LocationMenuComponent.java
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
 */

package com.noahhusby.sledgehammer.proxy.dialogs.components.setup;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.dialogs.components.DialogComponent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

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
    public TextComponent getExplanation() {
        return ChatUtil.combine(ChatColor.GRAY, "Type ", ChatColor.RED, "list", ChatColor.GRAY, " to view current locations, ",
                ChatColor.RED, "add", ChatColor.GRAY, "to add a new location, ", ChatColor.RED, "remove",
                ChatColor.GRAY, " to remove an existing location, or ", ChatColor.RED, "finish",
                ChatColor.GRAY, " to finish setting up this server.");
    }

    @Override
    public String[] getAcceptableResponses() {
        return new String[]{ "*" };
    }

    @Override
    public boolean validateResponse(String v) {
        String vm = v.toLowerCase().trim();
        return vm.equals("list") || vm.equals("add") || vm.equals("remove") || vm.equals("finish");
    }
}
