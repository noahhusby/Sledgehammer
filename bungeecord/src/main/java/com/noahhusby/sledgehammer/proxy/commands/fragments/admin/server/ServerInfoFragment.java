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

package com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import com.noahhusby.sledgehammer.proxy.servers.SledgehammerServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public class ServerInfoFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        SledgehammerServer server = ServerHandler.getInstance().getServer(args[0]);
        ServerInfo info = ProxyServer.getInstance().getServerInfo(args[0]);
        ChatUtil.sendMessageBox(sender, ChatColor.YELLOW + "Server Report", () -> {
            sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Name: ", ChatColor.WHITE, info.getName()));
            if (server == null) {
                sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Status: ", ChatColor.RED, "Unconfigured"));
            } else if (!server.isInitialized()) {
                sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Status: ", ChatColor.GREEN, "Configured",
                        ChatColor.RED, " (Not Initialized)"));
                sender.sendMessage();
                sender.sendMessage(ChatUtil.combine(ChatColor.BLUE, "Earth: ", (server.isEarthServer() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No")));
                sender.sendMessage(ChatUtil.combine(ChatColor.BLUE, "Friendly Name: ", ChatColor.WHITE, server.getFriendlyName()));
                sender.sendMessage(ChatUtil.combine(ChatColor.BLUE, "X Offset: ", ChatColor.WHITE, server.getXOffset()));
                sender.sendMessage(ChatUtil.combine(ChatColor.BLUE, "Z Offset: ", ChatColor.WHITE, server.getZOffset()));
                sender.sendMessage(ChatUtil.combine(ChatColor.BLUE, "Tpll Mode: ", ChatColor.WHITE, server.getTpllMode().name()));
            } else {
                sender.sendMessage(ChatUtil.combine(ChatColor.YELLOW, "Status: ", ChatColor.GREEN, "Configured"));
                sender.sendMessage();
                sender.sendMessage(ChatUtil.combine(ChatColor.BLUE, "Earth: ", (server.isEarthServer() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No")));
                sender.sendMessage(ChatUtil.combine(ChatColor.BLUE, "Friendly Name: ", ChatColor.WHITE, server.getFriendlyName()));
                sender.sendMessage(ChatUtil.combine(ChatColor.BLUE, "X Offset: ", ChatColor.WHITE, server.getXOffset()));
                sender.sendMessage(ChatUtil.combine(ChatColor.BLUE, "Z Offset: ", ChatColor.WHITE, server.getZOffset()));
                sender.sendMessage(ChatUtil.combine(ChatColor.BLUE, "Tpll Mode: ", ChatColor.WHITE, server.getTpllMode().name()));
                sender.sendMessage();
                sender.sendMessage(ChatUtil.combine(ChatColor.BLUE, "SH Version: ", ChatColor.BLUE, server.getSledgehammerVersion()));
            }
        });

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
