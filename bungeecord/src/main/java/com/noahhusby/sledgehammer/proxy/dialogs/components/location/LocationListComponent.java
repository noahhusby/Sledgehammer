/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - LocationListComponent.java
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

public class LocationListComponent extends DialogComponent {

    private final ServerInfo server;
    List<Location> locations;

    public LocationListComponent(ServerInfo server) {
        this.server = server;
    }

    @Override
    public String getKey() {
        return "";
    }

    @Override
    public String getPrompt() {
        return "Locations - " + server.getName();
    }

    @Override
    public TextComponent getExplanation() {
        TextComponent explanation = ChatUtil.combine(ChatColor.GRAY, "Type anything to continue");
        locations = ServerHandler.getInstance().getLocationsFromServer(server.getName());
        int v = 0;
        for (Location l : locations) {
            explanation.addExtra(ChatUtil.combine(ChatColor.RED, "\n" + v + ". ", ChatColor.GOLD, ChatUtil.capitalize(l.detailType.name()), " - ", ChatColor.RED, l));
            v++;
        }
        return explanation;
    }

    @Override
    public String[] getAcceptableResponses() {
        return new String[]{ "*" };
    }

    @Override
    public boolean validateResponse(String v) {
        return true;
    }
}
