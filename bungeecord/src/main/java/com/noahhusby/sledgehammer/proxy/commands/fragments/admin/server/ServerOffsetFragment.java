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
import com.noahhusby.sledgehammer.proxy.config.ServerHandler;
import com.noahhusby.sledgehammer.proxy.config.SledgehammerServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class ServerOffsetFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (ServerHandler.getInstance().getServer(args[0]) == null) {
            sender.sendMessage(ChatUtil.notSledgehammerServer);
            return;
        }

        if (args.length < 4) {
            sender.sendMessage(ChatUtil.combine(ChatColor.RED, "Usage: /sha server <server name> setoffset <x/z> offset"));
            return;
        }

        SledgehammerServer s = ServerHandler.getInstance().getServer(args[0]);
        String axis = args[2];
        if (!(axis.equalsIgnoreCase("x") || axis.equalsIgnoreCase("z"))) {
            sender.sendMessage(ChatUtil.combine(ChatColor.RED, "Usage: /sha server <server name> setoffset <x/z> offset"));
            return;
        }

        String valString = args[3];
        int value;
        try {
            value = Integer.parseInt(valString);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatUtil.combine(ChatColor.RED, "Usage: /sha server <server name> setoffset <x/z> offset"));
            return;
        }

        if (axis.equalsIgnoreCase("z")) {
            s.setZOffset(value);
            sender.sendMessage(ChatUtil.getValueMessage("zOffset", valString, s.getName()));
        } else {
            s.setXOffset(value);
            sender.sendMessage(ChatUtil.getValueMessage("xOffset", valString, s.getName()));
        }

        ServerHandler.getInstance().getServers().save(true);
    }

    @Override
    public String getName() {
        return "setoffset";
    }

    @Override
    public String getPurpose() {
        return "Set the tpll offset";
    }

    @Override
    public String[] getArguments() {
        return new String[]{ "<x|z> <offset>" };
    }
}
