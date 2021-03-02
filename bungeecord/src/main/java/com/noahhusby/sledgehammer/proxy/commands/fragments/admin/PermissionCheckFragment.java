/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - PermissionCheckFragment.java
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

package com.noahhusby.sledgehammer.proxy.commands.fragments.admin;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PermissionCheckFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer p;
        if (args.length == 0) {
            p = ProxyServer.getInstance().getPlayer(sender.getName());
        } else {
            p = ProxyServer.getInstance().getPlayer(args[0]);
        }

        if (p == null) {
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.RED, args[0], ChatColor.DARK_RED, " could not be found on the network!"));
            return;
        }

        sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.GRAY, "Permissions for ", ChatColor.BLUE, p.getName()));

        for (String s : p.getPermissions()) {
            if (s.contains("sledgehammer")) {
                sender.sendMessage(ChatUtil.combine(ChatColor.GOLD, s));
            }
        }
    }

    @Override
    public String getName() {
        return "pcheck";
    }

    @Override
    public String getPurpose() {
        return "List all sledgehammer permissions for a given player";
    }

    @Override
    public String[] getArguments() {
        return new String[]{ "[player]" };
    }
}
