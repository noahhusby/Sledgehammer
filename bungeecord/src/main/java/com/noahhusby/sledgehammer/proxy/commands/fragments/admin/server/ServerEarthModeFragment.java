/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerEarthModeFragment.java
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

public class ServerEarthModeFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(ServerHandler.getInstance().getServer(args[0]) == null) {
            sender.sendMessage(ChatUtil.notSledgehammerServer);
            return;
        }

        if(args.length < 3) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Usage: /sha server <server name> setearth <true/false>"));
            return;
        }

        String arg = args[2].toLowerCase();
        if(arg.equals("true") || arg.equals("false")) {
            SledgehammerServer s = ServerHandler.getInstance().getServer(args[0]);
            s.setEarthServer(Boolean.parseBoolean(arg));
            ServerHandler.getInstance().pushServer(s);
            sender.sendMessage(ChatUtil.getValueMessage("earth", arg, s.getName()));
        } else {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Usage: /sha server <server name> setearth <true/false>"));
        }

    }

    @Override
    public String getName() {
        return "setearth";
    }

    @Override
    public String getPurpose() {
        return "Set whether the server is a build server";
    }

    @Override
    public String[] getArguments() {
        return new String[]{"<true/false>"};
    }
}
