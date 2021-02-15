/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - GroupInfoFragment.java
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

package com.noahhusby.sledgehammer.commands.fragments.admin.groups;

import com.noahhusby.sledgehammer.ChatUtil;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.ServerGroup;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class GroupInfoFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 1) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Usage: /sha group info <id>"));
            return;
        }

        String gn = args[0];
        ServerGroup group = null;

        for(ServerGroup g : ServerConfig.getInstance().getGroups())
            if(g.getID().equalsIgnoreCase(gn)) group = g;

        if(group == null) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Group doesn't exist!"));
            return;
        }

        sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.GRAY, "Group Information:"));
        sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "ID: ", ChatColor.BLUE, group.getID()));
        sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Name: ", ChatColor.BLUE, group.getName()));
        sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Servers: ", ChatColor.BLUE, String.join(", ", group.getServers())));
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getPurpose() {
        return "See info about a group";
    }

    @Override
    public String[] getArguments() {
        return new String[]{"<id>"};
    }
}
