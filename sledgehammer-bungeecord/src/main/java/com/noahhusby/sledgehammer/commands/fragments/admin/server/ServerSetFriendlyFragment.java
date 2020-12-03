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

package com.noahhusby.sledgehammer.commands.fragments.admin.server;

import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.SledgehammerServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class ServerSetFriendlyFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 3) {
            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Usage: /sha server <server name> setname <name>", ChatColor.RED)));
            return;
        }

        StringBuilder name = new StringBuilder(args[2]);
        for(int i = 3; i < args.length; i++) {
            name.append(" ").append(args[i]);
        }

        SledgehammerServer s = ServerConfig.getInstance().getServer(args[0]);
        s.friendly_name = name.toString();

        ServerConfig.getInstance().pushServer(s);

        sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Changed name of ", ChatColor.GRAY),
                new TextElement(s.name, ChatColor.BLUE), new TextElement(" to ", ChatColor.GRAY),
                new TextElement(name.toString(), ChatColor.YELLOW)));
    }

    @Override
    public String getName() {
        return "setname";
    }

    @Override
    public String getPurpose() {
        return "Set nickname for server";
    }

    @Override
    public String[] getArguments() {
        return new String[]{"<name>"};
    }
}
