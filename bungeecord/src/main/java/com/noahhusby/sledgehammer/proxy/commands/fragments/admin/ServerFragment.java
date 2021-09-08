/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerFragment.java
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
import com.noahhusby.sledgehammer.proxy.commands.fragments.FragmentManager;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerAddLocationFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerEarthModeFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerGroupFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerInfoFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerListLocationFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerOffsetFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerRemoveLocationFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerSHSelectFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerSetFriendlyFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerStealthModeFragment;
import com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server.ServerTpllModeFragment;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.Map;

import static net.md_5.bungee.api.ProxyServer.*;

public class ServerFragment implements ICommandFragment {

    private final FragmentManager manager = new FragmentManager("/sha server <server name>", ChatColor.YELLOW + "Servers");

    public ServerFragment() {
        manager.register(new ServerInfoFragment());
        manager.register(new ServerAddLocationFragment());
        manager.register(new ServerRemoveLocationFragment());
        manager.register(new ServerListLocationFragment());
        manager.register(new ServerEarthModeFragment());
        manager.register(new ServerSHSelectFragment());
        manager.register(new ServerStealthModeFragment());
        manager.register(new ServerGroupFragment());
        manager.register(new ServerSetFriendlyFragment());
        manager.register(new ServerOffsetFragment());
        manager.register(new ServerTpllModeFragment());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 0) {
            for (Map.Entry<String, ServerInfo> s : getInstance().getServers().entrySet()) {
                if (s.getValue().getName().equalsIgnoreCase(args[0])) {
                    manager.setBase(String.format("/sha server %s", args[0]));
                    manager.execute(sender, args, 1);
                    return;
                }
            }
            sender.sendMessage(ChatUtil.adminAndCombine(ChatColor.DARK_RED, args[0], ChatColor.RED, " is not a bungeecord server!"));
            return;
        }
        manager.setBase("/sha server <server name>");
        manager.execute(sender, args, 1);
    }

    @Override
    public String getName() {
        return "server";
    }

    @Override
    public String getPurpose() {
        return "Configure settings of sledgehammer server";
    }

    @Override
    public String[] getArguments() {
        return new String[]{ "<server name>" };
    }
}
