/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - LocationRemovalComponent.java
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

package com.noahhusby.sledgehammer.proxy.dialogs.components.location;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.datasets.Location;
import com.noahhusby.sledgehammer.proxy.dialogs.components.DialogComponent;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.List;

public class LocationRemovalComponent extends DialogComponent {

    ServerInfo server;
    List<Location> locations;

    public LocationRemovalComponent(ServerInfo server) {
        this.server = server;
    }

    @Override
    public String getKey() {
        return "locationremove";
    }

    @Override
    public String getPrompt() {
        return "Which location would you like to delete?";
    }

    @Override
    public TextComponent getExplanation() {
        TextComponent explanation = ChatUtil.combine(ChatColor.GRAY, "Enter the # of the location to delete:");
        locations = ServerHandler.getInstance().getLocationsFromServer(server.getName());
        for (int i = 0; i < locations.size(); i++) {
            explanation.addExtra(ChatUtil.combine(ChatColor.RED, "\n" + i + ". ", ChatColor.GOLD, ChatUtil.capitalize(locations.get(i).detailType.name()), " - ", ChatColor.RED, locations.get(i)));
        }
        return explanation;
    }

    @Override
    public String[] getAcceptableResponses() {
        return new String[]{ "*" };
    }

    @Override
    public boolean validateResponse(String v) {
        try {
            int vm = Integer.parseInt(v.toLowerCase().trim());
            return vm > -1 && vm < locations.size();
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
