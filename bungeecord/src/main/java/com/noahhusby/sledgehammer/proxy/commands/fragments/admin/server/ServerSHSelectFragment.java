/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerSHSelectFragment.java
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
import com.noahhusby.sledgehammer.proxy.config.ServerHandler;
import com.noahhusby.sledgehammer.proxy.config.SledgehammerServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

import java.util.Locale;

public class ServerSHSelectFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Usage: /sha server <server name> setsledgehammer <true/false>"));
        } else {
            String arg = args[2].toLowerCase();
            if (arg.equals("true") || arg.equals("false")) {

                if (!Boolean.parseBoolean(arg)) {
                    ServerHandler.getInstance().getServers().remove(args[0]);
                } else {
                    if (!ServerHandler.getInstance().getServers().containsKey(args[0])) {
                        ServerHandler.getInstance().getServers().put(args[0], new SledgehammerServer(args[0]));
                        ServerHandler.getInstance().getServers().saveAsync();
                    }
                }

                sender.sendMessage(ChatUtil.getValueMessage("runs_sledgehammer", arg, args[0].toLowerCase(Locale.ROOT)));
            } else {
                sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Usage: /sha server <server name> setsledgehammer <true/false>"));
            }
        }
    }

    @Override
    public String getName() {
        return "setsledgehammer";
    }

    @Override
    public String getPurpose() {
        return "Set whether the server is running sledgehammer or not";
    }

    @Override
    public String[] getArguments() {
        return new String[]{ "<true/false>" };
    }
}
