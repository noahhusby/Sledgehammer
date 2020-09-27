/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerListLocationFragment.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.commands.fragments.admin.server;

import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

import java.util.List;

public class ServerListLocationFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        List<Location> locations = ServerConfig.getInstance().getLocationsFromServer(args[0]);
        if(locations == null) {
            sender.sendMessage(ChatHelper.getInstance().makeAdminTextComponent(new TextElement("No locations were found on that server!", ChatColor.GRAY)));
            return;
        }

        if(locations.isEmpty()) {
            sender.sendMessage(ChatHelper.getInstance().makeAdminTextComponent(new TextElement("No locations were found on that server!", ChatColor.GRAY)));
            return;
        }

        sender.sendMessage(ChatHelper.getInstance().makeAdminTextComponent(new TextElement("Locations for ", ChatColor.GRAY),
                new TextElement(args[0].toLowerCase(), ChatColor.BLUE), new TextElement(":", ChatColor.GRAY)));
        for(Location l : locations) {
            String x = "";
            if(!l.city.equals("")) x+= ChatHelper.capitalize(l.city)+", ";
            if(!l.county.equals("")) x+= ChatHelper.capitalize(l.county)+", ";
            if(!l.state.equals("")) x+= ChatHelper.capitalize(l.state)+", ";
            if(!l.country.equals("")) x+= ChatHelper.capitalize(l.country);
            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement(ChatHelper.capitalize(l.detailType.name())+" - ", ChatColor.RED),
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
