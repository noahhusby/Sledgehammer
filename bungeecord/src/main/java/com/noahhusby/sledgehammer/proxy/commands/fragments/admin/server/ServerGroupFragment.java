/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerSetFriendlyFragment.java
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

package com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.config.ServerGroup;
import com.noahhusby.sledgehammer.proxy.config.ServerHandler;
import com.noahhusby.sledgehammer.proxy.config.SledgehammerServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class ServerGroupFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (ServerHandler.getInstance().getServer(args[0]) == null) {
            sender.sendMessage(ChatUtil.notSledgehammerServer);
            return;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatUtil.combine(ChatColor.RED, "Usage: /sha server <server name> setgroup [clear|<group id>]"));
            return;
        }

        SledgehammerServer s = ServerHandler.getInstance().getServer(args[0]);
        if (args[2].equalsIgnoreCase("clear")) {
            if (s.getGroup().getID().equals(s.getName())) {
                sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "This server has no groups!"));
                return;
            }

            for (ServerGroup sg : ServerHandler.getInstance().getGroups()) {
                sg.getServers().remove(s.getName());
            }

            ServerHandler.getInstance().getGroups().save(true);
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.GREEN, "Server has been removed from all groups!"));
            return;
        }

        for (ServerGroup sg : ServerHandler.getInstance().getGroups()) {
            if (sg.getServers().contains(s.getName())) {
                sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "This server is assigned to another group!"));
                sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.GRAY, "Use ", ChatColor.BLUE,
                        "/sha server <server name> setgroup clear ", ChatColor.GRAY, "to remove it from all groups!"));
                return;
            }
        }

        String id = args[2];
        ServerGroup group = null;

        for (ServerGroup sg : ServerHandler.getInstance().getGroups()) {
            if (sg.getID().equals(id)) {
                group = sg;
            }
        }

        if (group == null) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "That group doesn't exist!"));
            return;
        }

        group.getServers().add(s.getName());
        ServerHandler.getInstance().getGroups().save(true);

        sender.sendMessage(ChatUtil.getValueMessage("group", group.getID(), s.getName()));
    }

    @Override
    public String getName() {
        return "setgroup";
    }

    @Override
    public String getPurpose() {
        return "Set group for the server";
    }

    @Override
    public String[] getArguments() {
        return new String[]{ "[clear|<group id>]" };
    }
}
