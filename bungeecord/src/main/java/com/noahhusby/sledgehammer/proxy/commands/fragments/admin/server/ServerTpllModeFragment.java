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

import com.noahhusby.sledgehammer.common.TpllMode;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import com.noahhusby.sledgehammer.proxy.servers.SledgehammerServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

import java.util.Locale;

public class ServerTpllModeFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (ServerHandler.getInstance().getServer(args[0]) == null) {
            sender.sendMessage(ChatUtil.notSledgehammerServer);
            return;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Usage: /sha server <server name> settpllmode <normal/passthrough>"));
            return;
        }

        String arg = args[2].toUpperCase(Locale.ROOT);
        if (arg.equalsIgnoreCase("normal") || arg.equalsIgnoreCase("passthrough")) {
            SledgehammerServer s = ServerHandler.getInstance().getServer(args[0]);
            s.setTpllMode(TpllMode.valueOf(arg));
            ServerHandler.getInstance().getServers().saveAsync();
            sender.sendMessage(ChatUtil.getValueMessage("tpll_mode", arg, s.getName()));
        } else {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, "Usage: /sha server <server name> settpllmode <normal/passthrough>"));
        }

    }

    @Override
    public String getName() {
        return "settpllmode";
    }

    @Override
    public String getPurpose() {
        return "Set the tpll mode";
    }

    @Override
    public String[] getArguments() {
        return new String[]{ "<normal/passthrough>" };
    }
}
