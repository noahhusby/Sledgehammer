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
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.commands.fragments.admin.server;

import com.noahhusby.sledgehammer.ChatUtil;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ServerHandler;
import com.noahhusby.sledgehammer.datasets.Location;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

import java.util.List;
import java.util.Locale;

public class ServerListLocationFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {

        if(ServerHandler.getInstance().getServer(args[0]) == null) {
            sender.sendMessage(ChatUtil.notSledgehammerServer);
            return;
        }

        if(!ServerHandler.getInstance().getServer(args[0]).isEarthServer()) {
            sender.sendMessage(ChatUtil.notEarthServer);
            return;
        }

        List<Location> locations = ServerHandler.getInstance().getLocationsFromServer(args[0]);
        if(locations == null) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.GRAY, "No locations were found on that server!"));
            return;
        }

        if(locations.isEmpty()) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.GRAY, "No locations were found on that server!"));
            return;
        }

        sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.GRAY, "Locations for ",
                ChatColor.BLUE, args[0].toLowerCase(Locale.ROOT), ChatColor.GRAY, ":"));
        for(Location l : locations) {
            String x = "";
            if(!l.city.equals("")) x+= ChatUtil.capitalize(l.city)+", ";
            if(!l.county.equals("")) x+= ChatUtil.capitalize(l.county)+", ";
            if(!l.state.equals("")) x+= ChatUtil.capitalize(l.state)+", ";
            if(!l.country.equals("")) x+= ChatUtil.capitalize(l.country);
            sender.sendMessage(ChatUtil.combine(ChatColor.RED, ChatUtil.capitalize(l.detailType.name()) + " - ",
                    ChatColor.GOLD, x));
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
