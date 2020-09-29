/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerListLocationFragment.java
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

package com.noahhusby.sledgehammer.commands.fragments.admin.server;

import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

import java.util.List;

public class ServerListLocationFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {

        if(ServerConfig.getInstance().getServer(args[0]) == null) {
            sender.sendMessage(ChatConstants.notSledgehammerServer);
            return;
        }

        if(!ServerConfig.getInstance().getServer(args[0]).earthServer) {
            sender.sendMessage(ChatConstants.notEarthServer);
            return;
        }

        List<Location> locations = ServerConfig.getInstance().getLocationsFromServer(args[0]);
        if(locations == null) {
            sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("No locations were found on that server!", ChatColor.GRAY)));
            return;
        }

        if(locations.isEmpty()) {
            sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("No locations were found on that server!", ChatColor.GRAY)));
            return;
        }

        sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Locations for ", ChatColor.GRAY),
                new TextElement(args[0].toLowerCase(), ChatColor.BLUE), new TextElement(":", ChatColor.GRAY)));
        for(Location l : locations) {
            String x = "";
            if(!l.city.equals("")) x+= ChatHelper.capitalize(l.city)+", ";
            if(!l.county.equals("")) x+= ChatHelper.capitalize(l.county)+", ";
            if(!l.state.equals("")) x+= ChatHelper.capitalize(l.state)+", ";
            if(!l.country.equals("")) x+= ChatHelper.capitalize(l.country);
            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement(ChatHelper.capitalize(l.detailType.name())+" - ", ChatColor.RED),
                    new TextElement(x, ChatColor.GOLD)));
        }
    }

    @Override
    public String getName() {
        return "listlocations";
    }

    @Override
    public String getPurpose() {
        return "List all locations on the server";
    }

    @Override
    public String[] getArguments() {
        return null;
    }
}
