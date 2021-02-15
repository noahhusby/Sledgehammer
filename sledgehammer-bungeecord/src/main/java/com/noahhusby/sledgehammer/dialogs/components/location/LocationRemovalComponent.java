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

package com.noahhusby.sledgehammer.dialogs.components.location;

import com.noahhusby.sledgehammer.ChatUtil;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.dialogs.components.DialogComponent;
import com.noahhusby.sledgehammer.datasets.Location;
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

        locations = ServerConfig.getInstance().getLocationsFromServer(server.getName());
        int v = 0;
        for(Location l : locations) {
            String x = "";
            if(!l.city.equals("")) x+= ChatUtil.capitalize(l.city)+", ";
            if(!l.county.equals("")) x+= ChatUtil.capitalize(l.county)+", ";
            if(!l.state.equals("")) x+= ChatUtil.capitalize(l.state)+", ";
            if(!l.country.equals("")) x+= ChatUtil.capitalize(l.country);
            explanation.addExtra(ChatUtil.combine(ChatColor.RED, "\n" + v + ". ", ChatColor.GOLD,
                    ChatUtil.capitalize(l.detailType.name()), " - ", ChatColor.RED, x));
            v++;
        }

        return explanation;
    }

    @Override
    public String[] getAcceptableResponses() {
        return new String[]{"*"};
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
