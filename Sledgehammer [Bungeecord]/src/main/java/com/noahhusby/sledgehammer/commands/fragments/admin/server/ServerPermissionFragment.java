/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerPermissionFragment.java
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
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.commands.fragments.admin.server;

import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.types.SledgehammerServer;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class ServerPermissionFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(ServerConfig.getInstance().getServer(args[0]) == null) {
            sender.sendMessage(ChatConstants.notSledgehammerServer);
            return;
        }

        if(args.length < 3) {
            sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Usage: /sha server <server name> permission_type <global/local>", ChatColor.RED)));
        } else {
            String arg = args[2].toLowerCase();
            if(arg.equals("global") || arg.equals("local")) {
                SledgehammerServer s = ServerConfig.getInstance().getServer(args[0]);

                s.permission_type = arg;
                ServerConfig.getInstance().pushServer(s);

                sender.sendMessage(ChatConstants.getValueMessage("permission_type", arg, s.name));
            } else {
                sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Usage: /sha server <server name> permission_type <global/local>", ChatColor.RED)));
            }
        }
    }

    @Override
    public String getName() {
        return "setpermission";
    }

    @Override
    public String getPurpose() {
        return "Set the permission type of the server";
    }

    @Override
    public String[] getArguments() {
        return new String[]{"<global/local>"};
    }
}
