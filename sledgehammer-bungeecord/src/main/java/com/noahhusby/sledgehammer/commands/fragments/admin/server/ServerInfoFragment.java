/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerInfoFragment.java
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
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.SledgehammerServer;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.config.ServerInfo;

public class ServerInfoFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        SledgehammerServer server = ServerConfig.getInstance().getServer(args[0]);
        ServerInfo info = ProxyServer.getInstance().getServerInfo(args[0]);
        sender.sendMessage();
        sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.GRAY, "Server Info - ", ChatColor.BLUE, info.getName()));
        sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Name: ", ChatColor.BLUE, info.getName()));
        if(server == null) {
            sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Status: ", ChatColor.RED, "Unconfigured"));
        } else if (!server.isInitialized()) {
            sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Status: ", ChatColor.GREEN, "Configured",
                    ChatColor.RED, " (Not Initialized)"));
            sender.sendMessage();
            sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Earth: ", (server.isEarthServer() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No")));
            sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Friendly Name: ", ChatColor.BLUE, server.getFriendlyName()));
            sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "X Offset: ", ChatColor.BLUE, server.getXOffset()));
            sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Z Offset: ", ChatColor.BLUE, server.getFriendlyName()));
        } else {
            sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Status: ", ChatColor.GREEN, "Configured"));
            sender.sendMessage();
            sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Earth: ", (server.isEarthServer() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No")));
            sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Friendly Name: ", ChatColor.BLUE, server.getFriendlyName()));
            sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "X Offset: ", ChatColor.BLUE, server.getXOffset()));
            sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Z Offset: ", ChatColor.BLUE, server.getFriendlyName()));
            sender.sendMessage();
            sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "SH Version: ", ChatColor.BLUE, server.getSledgehammerVersion()));
        }
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getPurpose() {
        return "List info about server";
    }

    @Override
    public String[] getArguments() {
        return null;
    }
}
